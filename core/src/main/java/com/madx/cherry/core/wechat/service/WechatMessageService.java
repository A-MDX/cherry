package com.madx.cherry.core.wechat.service;


import com.madx.cherry.core.common.CommonCode;
import com.madx.cherry.core.common.dao.SysUserDao;
import com.madx.cherry.core.common.entity.SysUserPO;
import com.madx.cherry.core.wechat.bean.MongoDataPO;
import com.madx.cherry.core.wechat.bean.WechatMsgPO;
import com.madx.cherry.core.wechat.bean.XmlMsg;
import com.madx.cherry.core.wechat.common.WechatException;
import com.madx.cherry.core.wechat.common.WechatUtil;
import com.madx.cherry.core.wechat.dao.MongoDataDao;
import com.madx.cherry.core.wechat.dao.WechatMsgDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

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
    private MongoDataDao mongoDataDaol;
    
    public String analysisText(XmlMsg msg) {
        WechatMsgPO msgPO = initPO(msg);
        msgPO.setType(CommonCode.WECHAT_MSG_TYPE_TEXT);
        msgPO.setData(msg.getContent());
        wechatMsgDao.save(msgPO);
        
        if (msg.getContent().equals("hehe") ){
            throw new WechatException("hehe 个篮子。", msg);
        }
        return "success";
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
