package com.madx.cherry.core.wechat.service.command;

import com.madx.cherry.core.common.dao.RedisDao;
import com.madx.cherry.core.common.dao.SysUserDao;
import com.madx.cherry.core.wechat.common.WechatConfig;
import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * Created by a-mdx on 2017/7/4.
 */
@Component
public class CommandExecuteUtil{

    @Autowired
    private RedisDao redisDao;
    @Autowired
    private SysUserDao userDao;
    @Autowired
    private WechatConfig wechatConfig;
    @Autowired
    private MongoClient mongoClient;

    @Value("${msg.mongo.database}")
    private String database;

    @Value("${msg.mongo.collection.daily}")
    private String collectionDaily;

    public MongoCollection<Document> getCollection(){
        return mongoClient.getDatabase(database).getCollection(collectionDaily);
    }

    public RedisDao getRedisDao() {
        return redisDao;
    }

    public void setRedisDao(RedisDao redisDao) {
        this.redisDao = redisDao;
    }

    public SysUserDao getUserDao() {
        return userDao;
    }

    public void setUserDao(SysUserDao userDao) {
        this.userDao = userDao;
    }

    public WechatConfig getWechatConfig() {
        return wechatConfig;
    }

    public void setWechatConfig(WechatConfig wechatConfig) {
        this.wechatConfig = wechatConfig;
    }

    public MongoClient getMongoClient() {
        return mongoClient;
    }

    public void setMongoClient(MongoClient mongoClient) {
        this.mongoClient = mongoClient;
    }

    public String getDatabase() {
        return database;
    }

    public void setDatabase(String database) {
        this.database = database;
    }

    public String getCollectionDaily() {
        return collectionDaily;
    }

    public void setCollectionDaily(String collectionDaily) {
        this.collectionDaily = collectionDaily;
    }
}
