package com.madx.cherry.core.wechat.common;

import com.madx.cherry.core.common.CommonCode;
import com.madx.cherry.core.wechat.bean.MongoDataPO;
import com.madx.cherry.core.wechat.dao.MongoDataDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

/**
 * Created by A-mdx on 2017/6/29.
 */
@EnableScheduling
@Component
public class WechatJob {

    @Autowired
    private MongoDataDao mongoDataDao;

    /**
     * 下载文件至本地
     */
    @Scheduled(cron = "0/30 * * * * *")
    public void downloadFile(){
        System.out.println("------------------------------------------------------------------------------");
        System.out.println("--------------------------------------start-----------------------------------");
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
        
        
        System.out.println("----------------------------------------end-----------------------------------");
        System.out.println("------------------------------------------------------------------------------");
    }
    
    /*
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
        String path = "/Users/a-mdx/Desktop/image/201706";
        String name = "30-00:42:16.961+67.jpg";
        String httpUrl = "http://7xng4x.com1.z0.glb.clouddn.com/fakeUser_20170628_7";
        WechatUtil.saveFileFromUrl(httpUrl, name, path);
    }

    // fC9Ne1AagOlgc2o7_21ADQvGJ4tH41HaQ5xF8PSPe8EK_OCQFufIdqAH46JRO0tJ_dPzo0M27BTuPFE6V8gyLzPUaI_drVW-zvX1rgnUZRc2mK0On3Iz6eMpjWJhq5zbSRUgADAVUX

}
