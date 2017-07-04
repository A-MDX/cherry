package com.madx.cherry.core.wechat.service.command;

import com.madx.cherry.core.common.dao.RedisDao;
import com.madx.cherry.core.common.dao.SysUserDao;
import com.madx.cherry.core.wechat.common.WechatConfig;
import com.mongodb.MongoClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * Created by a-mdx on 2017/7/4.
 */
@Component
public abstract class AbstractCommandExecute implements CommandExecute{

    @Autowired
    protected RedisDao redisDao;
    @Autowired
    protected SysUserDao userDao;
    @Autowired
    protected WechatConfig wechatConfig;
    @Autowired
    protected MongoClient mongoClient;

    @Value("${msg.mongo.database}")
    protected String database;

    @Value("${msg.mongo.collection.daily}")
    protected String collectionDaily;

}
