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
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

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
    

    /**
     * 下载文件至本地
     */
    @Scheduled(cron = "0/30 * * * * *")
    public void downloadFile(){
        System.out.println("------------------------------------------------------------------------------");
        System.out.println("--------------------------------------start downloadFile -----------------------------------");
        List<MongoDataPO> list = mongoDataDao.findBySaveLocalAndStatus(false, CommonCode.VALID_TRUE);
        System.out.println("size : "+list.size());
        list.forEach(System.out::println);
        list.forEach(k -> {
            boolean ok = WechatUtil.saveFileFromUrl(k.getDataUrl(), k.getName(), k.getPath());
            if (ok){
                k.setSaveLocal(true);
                k.setModiftTime(new Date());
                mongoDataDao.save(k);
            }
        });


        System.out.println("----------------------------------------end downloadFile-----------------------------------");
        System.out.println("------------------------------------------------------------------------------");
    }
    
    
    public void gendailyMessage(){
        System.out.println("------------------------------------------------------------------------------");
        System.out.println("--------------------------------------start gendailyMessage -----------------------------------");

        // 1.寻找存储的数据，若没有，则继续走下去
        MongoCollection<Document> collection = mongoClient.getDatabase("test").getCollection("daily");
        List<Document> list = new ArrayList<>();
        LocalDate localDate = LocalDate.now();
        localDate = localDate.minusDays(1);
        String yesterday = localDate.format(DateTimeFormatter.ISO_DATE);

        FindIterable<Document> iterable = collection.find(new Document("date", yesterday).append("status", CommonCode.VALID_TRUE));
        iterable.into(list);
        if (list.size() > 0){
            logger.info("已经存在了需要的数据，不需要在跑数据库加数据了。");
            return;
        }
        // 2.遍历用户数组
        List<SysUserPO> users = userDao.findByStatus(CommonCode.VALID_TRUE);

        // 3.查询数据列表，开始拼装
        String time2 = yesterday+" 23:59:59";
        Date date = new Date();
        users.forEach(u -> {
            List<WechatMsgPO> msgPOs = wechatMsgDao.findByUserAndTimeBetween(u.getLoginName(), yesterday, time2);
            Document dailyMsg = new Document();
            dailyMsg.append("name", yesterday).append("status", CommonCode.VALID_TRUE).append("user", u.getLoginName());
            dailyMsg.append("creationTime", date);
            List<Document> data = new ArrayList<>();
            msgPOs.forEach(m -> {
                Document item = new Document();
                //todo 准备新增两个字段 看 msg.json

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
                        break;
                    case CommonCode.WECHAT_MSG_TYPE_IMAGE:
                        item.append("type", "image");
                        // init url
                        String jsonStr = m.getData();
                        if (jsonStr.contains("{")){
                            BasicDBObject json = (BasicDBObject) JSON.parse(jsonStr);
                            item.append("url", json.get("url"));
                        }else {
                            item.append("url", jsonStr);
                        }

                        break;
                    default:
                        return;
                }

                data.add(item);
            });
            dailyMsg.append("data", data);
            // 6.保存至数据库
            collection.insertOne(dailyMsg);

        });

        System.out.println("----------------------------------------end gendailyMessage-----------------------------------");
        System.out.println("------------------------------------------------------------------------------");
    }
    
    
    /*
    
    
    C:/Users/A-mdx/Desktop/image/201707
    
    java.io.FileNotFoundException: C:\Users\A-mdx\Desktop\image\201706\30-00:42:16.961+67.jpg (文件名、目录名或卷标语法不正确。)
	at java.io.FileOutputStream.open0(Native Method)
	at java.io.FileOutputStream.open(FileOutputStream.java:270)
	at java.io.FileOutputStream.<init>(FileOutputStream.java:213)
	at java.io.FileOutputStream.<init>(FileOutputStream.java:101)
	at com.madx.cherry.core.wechat.common.WechatUtil.saveFileFromUrl(WechatUtil.java:48)
	at com.madx.cherry.core.wechat.common.WechatJob.lambda$downloadFile$0(WechatJob.java:35)
	at java.util.ArrayList.forEach(ArrayList.java:1249)
	at com.madx.cherry.core.wechat.common.WechatJob.downloadFile(WechatJob.java:34)
	at sun.reflect.NativeMethodAccessorImpl.invoke0(Native Method)
	at sun.reflect.NativeMethodAccessorImpl.invoke(NativeMethodAccessorImpl.java:62)
	at sun.reflect.DelegatingMethodAccessorImpl.invoke(DelegatingMethodAccessorImpl.java:43)
	at java.lang.reflect.Method.invoke(Method.java:497)
	at org.springframework.scheduling.support.ScheduledMethodRunnable.run(ScheduledMethodRunnable.java:65)
	at org.springframework.scheduling.support.DelegatingErrorHandlingRunnable.run(DelegatingErrorHandlingRunnable.java:54)
	at org.springframework.scheduling.concurrent.ReschedulingRunnable.run(ReschedulingRunnable.java:81)
	at java.util.concurrent.Executors$RunnableAdapter.call(Executors.java:511)
	at java.util.concurrent.FutureTask.run(FutureTask.java:266)
	at java.util.concurrent.ScheduledThreadPoolExecutor$ScheduledFutureTask.access$201(ScheduledThreadPoolExecutor.java:180)
	at java.util.concurrent.ScheduledThreadPoolExecutor$ScheduledFutureTask.run(ScheduledThreadPoolExecutor.java:293)
	at java.util.concurrent.ThreadPoolExecutor.runWorker(ThreadPoolExecutor.java:1142)
	at java.util.concurrent.ThreadPoolExecutor$Worker.run(ThreadPoolExecutor.java:617)
	at java.lang.Thread.run(Thread.java:745)
     */

    public static void main(String... args){
        Instant instant = Instant.now();
        LocalDateTime time = instant.atZone(ZoneId.systemDefault()).toLocalDateTime();

        String timeStr = time.format(DateTimeFormatter.ofPattern("HH:mm"));
        System.out.println(timeStr);

    }

    // fC9Ne1AagOlgc2o7_21ADQvGJ4tH41HaQ5xF8PSPe8EK_OCQFufIdqAH46JRO0tJ_dPzo0M27BTuPFE6V8gyLzPUaI_drVW-zvX1rgnUZRc2mK0On3Iz6eMpjWJhq5zbSRUgADAVUX

}
