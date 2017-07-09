package com.madx.cherry.core.wechat.common;

import com.madx.cherry.core.common.CommonCode;
import com.madx.cherry.core.common.dao.SysUserDao;
import com.madx.cherry.core.common.entity.SysUserPO;
import com.madx.cherry.core.wechat.bean.MongoDataPO;
import com.madx.cherry.core.wechat.bean.WechatMsgPO;
import com.madx.cherry.core.wechat.dao.MongoDataDao;
import com.madx.cherry.core.wechat.dao.WechatMsgDao;
import com.mongodb.BasicDBObject;
import com.mongodb.MongoClient;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.util.JSON;
import org.bson.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * Created by A-mdx on 2017/6/29.
 */
@EnableScheduling
@Component
public class WechatJob {

    private static Logger logger = LoggerFactory.getLogger(WechatJob.class);

    @Autowired
    private MongoDataDao mongoDataDao;
    @Autowired
    private MongoClient mongoClient;
    @Autowired
    private WechatMsgDao wechatMsgDao;
    @Autowired
    private SysUserDao userDao;
    
    @Value("${msg.dailyImage.default}")
    private String dailyImageUrl;

    @Value("${msg.mongo.database}")
    private String mongoDb;

    @Value("${msg.mongo.collection.daily}")
    private String collectionDaily;



    /**
     * 下载文件至本地
     */
    @Scheduled(cron = "0 0/30 5 * * *")
    public void downloadFile(){
        logger.info("------------------------------------------------------------------------------");
        logger.info("------------------------------start downloadFile -----------------------------");
        List<MongoDataPO> list = mongoDataDao.findBySaveLocalAndStatus(false, CommonCode.VALID_TRUE);
        logger.info("downloadFile size : "+list.size());

        list.forEach(k -> {
            logger.info(k.toString());
            boolean ok = WechatUtil.saveFileFromUrl(k.getDataUrl(), k.getName(), k.getPath());
            if (ok){
                k.setSaveLocal(true);
                k.setModiftTime(new Date());
                mongoDataDao.save(k);
            }
        });

        logger.info("------------------------------end downloadFile--------------------------------");
        logger.info("------------------------------------------------------------------------------");
    }
    
    @Scheduled(cron = "0 0/30 6 * * *")
//    @Scheduled(cron = "0/30 * * * * *")
    public void genDailyMessage(){
        logger.info("------------------------------------------------------------------------------");
        logger.info("--------------------------start genDailyMessage ------------------------------");

        // 1.寻找存储的数据，若没有，则继续走下去
        MongoCollection<Document> collection = mongoClient.getDatabase(mongoDb).getCollection(collectionDaily);
        List<Document> list = new ArrayList<>();
        LocalDate localDate = LocalDate.now();
        localDate = localDate.minusDays(1);
        String yesterday = localDate.format(DateTimeFormatter.ISO_DATE);

        Document query = new Document("name", yesterday).append("status", CommonCode.VALID_TRUE);

        FindIterable<Document> iterable = collection.find(query);

        iterable.into(list);
        if (list.size() > 0){
            logger.info("已经存在了需要的数据，不需要在跑数据库加数据了。");
            return;
        }
        // 2.遍历用户数组
        List<SysUserPO> users = userDao.findByStatus(CommonCode.VALID_TRUE);

        // 3.查询数据列表，开始拼装
        
        Date date1 = Date.from(localDate.atTime(LocalTime.MIN).atZone(ZoneId.systemDefault()).toInstant());
        Date date2 = Date.from(localDate.atTime(LocalTime.MAX).atZone(ZoneId.systemDefault()).toInstant());
        
        Date date = new Date();
        users.forEach(u -> {
            List<WechatMsgPO> msgPOs = wechatMsgDao.findByUserAndTimeBetween(u.getLoginName(), date1, date2);
            Document dailyMsg = new Document();
            dailyMsg.append("name", yesterday).append("status", CommonCode.VALID_TRUE).append("user", u.getLoginName());
            dailyMsg.append("creationTime", date);
      
            final StringBuilder description = new StringBuilder();
            final String[] picUrl = {dailyImageUrl};
            
            List<Document> data = new ArrayList<>();

            msgPOs.forEach(m -> {
                Document item = new Document();
 
                // 4.生成时间
                Instant instant = m.getTime().toInstant();
                LocalDateTime time = instant.atZone(ZoneId.systemDefault()).toLocalDateTime();
                String timeStr = time.format(DateTimeFormatter.ofPattern("HH:mm"));
                item.append("time", timeStr);

                // 5.生成数据,条目
                switch (m.getType()){
                    case CommonCode.WECHAT_MSG_TYPE_TEXT:
                        item.append("type", "text");
                        // init data
                        List<String> msgList = new ArrayList<>();
                        String msgData = m.getData();
                        msgList.addAll(Arrays.asList(msgData.split("\n")));
                        item.append("message", msgList);
                        
                        // for description
                        description.append(m.getData());
                        
                        break;
                    case CommonCode.WECHAT_MSG_TYPE_IMAGE:
                        item.append("type", "image");
                        // init url
                        String jsonStr = m.getData();
                        if (jsonStr.contains("{")){
                            BasicDBObject json = (BasicDBObject) JSON.parse(jsonStr);
                            item.append("url", json.get("url"));
                            
                            // picUrl
                            picUrl[0] = json.getString("url");
                        }else {
                            item.append("url", jsonStr);

                            // picUrl
                            picUrl[0] = jsonStr;
                        }

                        break;
                    default:
                        return;
                }

                data.add(item);
            });
            dailyMsg.append("data", data);
            
            description.append("...讲真，昨天的我好像没写。");
            int length = description.length();
            String description_str = description.toString();
            if (length > 35){
                description_str = description.substring(0, 35);
                description_str += "...";
            }
            dailyMsg.append("description", description_str)
                    .append("picUrl", picUrl[0]);
            
            // 6.保存至数据库
            collection.insertOne(dailyMsg);

        });

        logger.info("-----------------------------end genDailyMessage------------------------------");
        logger.info("------------------------------------------------------------------------------");
    }
    

}
