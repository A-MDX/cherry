package com.madx.wechat.initme.service;

import com.madx.wechat.common.ArgumentsException;
import com.madx.wechat.common.CommonUtil;
import com.madx.wechat.common.RedisDao;
import com.madx.wechat.initme.dao.InitAnswerDao;
import com.madx.wechat.initme.dao.InitQuestionDao;
import com.madx.wechat.initme.dao.InitYouDao;
import com.madx.wechat.initme.entity.InitAnswerPo;
import com.madx.wechat.initme.entity.InitQuestionPo;
import com.madx.wechat.initme.entity.InitYouPo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.UUID;
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

    public String checkUuid(String uuid){
        String youId = redisDao.getVal(REDIS_PREFIX_YOU+uuid);
        if (youId == null){
            throw new ArgumentsException("登录会话过期.");
        }
        return youId;
    }
    
    // 绑定当前
    public String bindYou(String name) {
        name = name.trim();
        InitYouPo youPo = initYouDao.findByName(name);
        if (youPo == null){
            youPo = new InitYouPo();
            youPo.setName(name);
            youPo = initYouDao.save(youPo);
        }
        String uuid = UUID.randomUUID().toString();
        redisDao.addVal(REDIS_PREFIX_YOU + uuid, youPo.getId(), 2, TimeUnit.HOURS);
        return uuid;
    }
    
    // 新建回答，并创建下一题
    public InitQuestionPo answerMe(String uuid, Map<String, Object> data) {
        String youId = redisDao.getVal(REDIS_PREFIX_YOU + uuid);
        if (youId == null){
            throw new ArgumentsException("登录会话过期.");
        }
        
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
}
