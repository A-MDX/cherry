package com.madx.cherry.core.wechat.dao;

import com.madx.cherry.core.wechat.bean.MongoDataPO;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * Created by A-mdx on 2017/6/26.
 */
public interface MongoDataDao extends MongoRepository<MongoDataPO, String> {
    MongoDataPO findByDataId(String dataId);
    
}
