package com.madx.cherry.core.wechat.service;


import com.madx.cherry.core.common.CommonCode;
import com.madx.cherry.core.common.dao.RedisDao;
import com.madx.cherry.core.common.dao.SysUserDao;
import com.madx.cherry.core.common.entity.SysUserPO;
import com.madx.cherry.core.wechat.bean.MongoDataPO;
import com.madx.cherry.core.wechat.bean.WechatMsgPO;
import com.madx.cherry.core.wechat.bean.XmlMsg;
import com.madx.cherry.core.wechat.common.WechatException;
import com.madx.cherry.core.wechat.common.WechatUtil;
import com.madx.cherry.core.wechat.dao.MongoDataDao;
import com.madx.cherry.core.wechat.dao.WechatMsgDao;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.annotation.Transient;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * Created by A-mdx on 2017/6/24.
 */
@Service
public class WechatMessageService {

    @Autowired
    private WechatMsgDao wechatMsgDao;
    @Autowired
    private SysUserDao sysUserDao;
    @Autowired
    private MongoDataDao mongoDataDao;
    @Autowired
    private RedisDao redisDao;

    @Value("${file.save.path}")
    private String fileSavePath;

    public static final String WECHAT_TEXT_MSG_PREFIX = "wechat.textMsg.";


    /**
     * 准备做一个机制，消息融合
      * @param msg
     * @return
     */
    public String analysisText(XmlMsg msg) {

        WechatMsgPO msgPO = initPO(msg);

        String content = msg.getContent();
        String user = msgPO.getUser();
        String value = redisDao.getVal(wechatMsgDao+user);
        if (value == null){
            // 说明还没有融合过，定义3分钟呢吧
            msgPO.setType(CommonCode.WECHAT_MSG_TYPE_TEXT);
            msgPO.setData(content);
            msgPO.setMsgType("text");
            wechatMsgDao.save(msgPO);

            redisDao.addVal(WECHAT_TEXT_MSG_PREFIX+user, 1+"", 3, TimeUnit.MINUTES);
        }else {
            // 需要启动融合机制了
            msgPO = wechatMsgDao.findByUserAndStatusAndType(user, CommonCode.WECHAT_MSG_TYPE_TEXT
                    , CommonCode.STATUS_YES
                    , new PageRequest(0, 1, new Sort(Sort.Direction.DESC, "time"))).get(0);
            msgPO.setData(msgPO.getData()+"\n"+msg.getContent());
            msgPO.setModifier(user);
            msgPO.setModifyTime(new Date());
            wechatMsgDao.save(msgPO);
            int val = Integer.parseInt(value);
            if (val >= 10){
                redisDao.deleteVal(WECHAT_TEXT_MSG_PREFIX+user);
            }else {
                val++;
                redisDao.addVal(WECHAT_TEXT_MSG_PREFIX+user, val+"", 3, TimeUnit.MINUTES);
            }
        }

        
        if (content.equals("hehe") || content.equals("呵呵")){
            throw new WechatException("呵呵 个篮子。", msg);
        }
        return "success";
    }

    /**
     * 准备 mysql 一个， mongo一份， 然后存到 本机中
     * @param msg
     * @return
     */
    @Transient
    public String analysisImage(XmlMsg msg) {

        WechatMsgPO msgPO = initPO(msg);

        Document json = new Document("type", msg.getMsgType());
        json.append("url", msg.getPicUrl());
        json.append("mediaId", msg.getMediaId());

        msgPO.setType(CommonCode.WECHAT_MSG_TYPE_IMAGE);
        msgPO.setData(json.toJson());
        msgPO.setMsgType("image");
        msgPO = wechatMsgDao.save(msgPO);

        // init mongo bean
        MongoDataPO mongoDataPO = new MongoDataPO();

        LocalDateTime ldtime = LocalDateTime.now();

        mongoDataPO.setCreationTime(new Date());
        mongoDataPO.setCreator(msgPO.getUser());
        mongoDataPO.setDataId(msg.getMediaId());
        mongoDataPO.setType("image");
        mongoDataPO.setPath(fileSavePath + "/image/"+ldtime.format(DateTimeFormatter.ofPattern("yyyyMM")));
        mongoDataPO.setMysqId(msgPO.getId());
        mongoDataPO.setName(ldtime.format(DateTimeFormatter.ofPattern("dd-HH:mm:ss.SSS"))+"+"+genRandomNum()+".jpg"); // todo...
        mongoDataPO.setDataUrl(msg.getPicUrl());

        mongoDataPO.setStatus(CommonCode.VALID_TRUE);
        mongoDataPO.setSaveLocal(false);

        mongoDataPO = mongoDataDao.save(mongoDataPO);

        System.out.println(mongoDataPO.toString());

        return "success";
    }

    /**
     * 随机给个int 数字 100以内的
     * @return
     */
    private static int genRandomNum(){
        double temp1 = Math.random()*100;
        return (int) temp1;
    }

    public static void main(String[] args) {
        LocalDateTime ldtime = LocalDateTime.now();
        String time = ldtime.format(DateTimeFormatter.ofPattern("dd-HH:mm:ss.SSS"));
        System.out.println(time);
    }

    public String analysisDataMsg(XmlMsg msg) {
        WechatMsgPO msgPO = initPO(msg);
        
        msgPO.setType(CommonCode.WECHAT_MSG_TYPE_DATA);
        
        // init mongo bean
        MongoDataPO mongoDataPO = new MongoDataPO();
        mongoDataPO.setCreationTime(new Date());
        mongoDataPO.setCreator(msgPO.getUser());
        mongoDataPO.setDataId(msg.getMediaId());
        mongoDataPO.setType(msg.getMsgType());
        mongoDataPO.setStatus(CommonCode.VALID_TRUE);
        mongoDataPO.setName("目前还没那么多新花样。"+msg.getMsgType());
        
        // download data?
        

        return null;
    }
    
    private WechatMsgPO initPO(final XmlMsg xmlMsg){
        WechatMsgPO msgPO = new WechatMsgPO();
        // init user 
        SysUserPO userPO = sysUserDao.findByOpenId(xmlMsg.getFromUserName());
        System.out.println("user:"+userPO);

        WechatUtil.isNullForMsg(userPO, xmlMsg, "查不到当前用户了。或许得先绑定一下。");

        msgPO.setUser(userPO.getLoginName());
        
        // init time
        msgPO.setTime(new Date(Long.parseLong(xmlMsg.getCreateTime()+"000")));
        
        msgPO.setStatus(CommonCode.VALID_TRUE);
        
        return msgPO;
    }


}
