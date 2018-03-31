package com.madx.wechat.initme.service;

import com.madx.wechat.common.*;
import com.madx.wechat.initme.dao.InitAnswerDao;
import com.madx.wechat.initme.dao.InitQuestionDao;
import com.madx.wechat.initme.dao.InitYouDao;
import com.madx.wechat.initme.entity.InitAnswerPo;
import com.madx.wechat.initme.entity.InitQuestionItemPo;
import com.madx.wechat.initme.entity.InitQuestionPo;
import com.madx.wechat.initme.entity.InitYouPo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * Create by A-mdx at 2018-03-18 13:11
 */
@Service
public class InitService {

    private static final Logger logger = LoggerFactory.getLogger(InitService.class);

    // 前置
    public static final String REDIS_PREFIX_YOU = "wechat.you.";

    @Autowired
    private InitQuestionDao initQuestionDao;
    @Autowired
    private InitYouDao initYouDao;
    @Autowired
    private RedisDao redisDao;
    @Autowired
    private InitAnswerDao answerDao;
    @Autowired
    private WechatConfigUtil wechatConfigUtil;

    public String checkUuid(String uuid) {
        String id = redisDao.getVal(REDIS_PREFIX_YOU + uuid);
        if (id == null) {
            throw new ArgumentsException("登录会话过期.");
        }
        // 续时间 30 min
        redisDao.addVal(REDIS_PREFIX_YOU + uuid, id, 30, TimeUnit.MINUTES);
        return id;
    }

    // 绑定当前
    public String bindYou(String name) {
        name = name.trim();
        InitYouPo youPo = initYouDao.findByName(name);
        if (youPo == null) {
            youPo = new InitYouPo();
            youPo.setName(name);
            youPo = initYouDao.save(youPo);
        }
        String uuid = UUID.randomUUID().toString();
        redisDao.addVal(REDIS_PREFIX_YOU + uuid, youPo.getId(), 2, TimeUnit.HOURS);
        
        // 有人开始进行选择
        String msg = CommonUtil.transDate2Str(new Date())+" 此刻有人开始选择：";
        msg += "\n"+name;
        sendMsgToMe(msg);
        
        return uuid;
    }

    // 新建回答，并创建下一题
    public InitQuestionPo answerMe(String uuid, Map<String, Object> data) {
        String youId = checkUuid(uuid);

        InitAnswerPo answerPo = new InitAnswerPo();
        answerPo.setAnswer(data.get("answer").toString());

        InitQuestionPo questionPo = initQuestionDao.findById(data.get("id").toString()).get();
        answerPo.setInitQuestionPo(questionPo);

        int index = Integer.parseInt(data.get("index").toString());
        int type = Integer.parseInt(data.get("type").toString());
        answerPo.setType(type);

        InitYouPo youPo = initYouDao.findById(youId).get();
        answerPo.setInitYouPo(youPo);

        answerPo = answerDao.save(answerPo);

        logger.info("回答完成 {}", CommonUtil.toString(answerPo));

        // todo async 将答案发给我

        return initQuestionDao.findByStatusAndIndex(1, ++index);
    }
    
    @Async
    public void sendMsgToMe(String msg){
        String adminOpenid = wechatConfigUtil.getAdminOpenid();
        Map<String, Object> data = new HashMap<>();
        data.put("touser", adminOpenid);
        data.put("msgtype", "text");
        Map<String, Object> text = new HashMap<>();
        text.put("content", msg);
        data.put("text", text);
        String result = CommonUtil.toString(data);
        result = wechatConfigUtil.sendWechatMsg(result);
        System.out.println("result:"+result);
    }

    public static void main(String... args) {
        InitQuestionPo initQuestionPo = new InitQuestionPo();
        initQuestionPo.setText("你好吗？我不骗你");
        initQuestionPo.setIndex(1);
        initQuestionPo.setType(1);

        List<InitQuestionItemPo> items = new ArrayList<>();
        InitQuestionItemPo itemPo = new InitQuestionItemPo();
        itemPo.setTag("A");
        itemPo.setText("呵呵哒");
        items.add(itemPo);

        itemPo = new InitQuestionItemPo();
        itemPo.setText("哎呀吗.");
        itemPo.setTag("B");
        items.add(itemPo);

        initQuestionPo.setOptionItems(items);
        
        InitYouPo youPo = new InitYouPo();
        youPo.setName("小哥");
        youPo.setId("4654zxczxc");
        youPo.setCreateTime(new Date());
        
        InitAnswerPo answerPo = new InitAnswerPo();
        answerPo.setInitYouPo(youPo);
        answerPo.setType(1);
        answerPo.setAnswer("A");
        answerPo.setInitQuestionPo(initQuestionPo);
        answerPo.setCreateTime(new Date());
    }
    

    public Result queryYouAnswer(String uuid, int index) {
        String youId = checkUuid(uuid);
        return Result.instance().data(answerDao.findAnswerByIndexAndYouId(index, youId));
    }
}
