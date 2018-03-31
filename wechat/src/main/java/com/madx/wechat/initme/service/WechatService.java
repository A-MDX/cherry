package com.madx.wechat.initme.service;

import com.madx.wechat.common.CommonUtil;
import com.madx.wechat.common.RedisDao;
import com.madx.wechat.common.WechatConfigUtil;
import com.madx.wechat.common.XmlMsg;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.core.task.AsyncTaskExecutor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Create by A-mdx at 2018-03-31 15:18
 */
@Service
public class WechatService {

    public static final String REDIS_PREFIX_INIT = InitService.REDIS_PREFIX_YOU;
    
    @Autowired
    private WechatConfigUtil wechatConfigUtil;
    @Autowired
    private RedisDao redisDao;

    public String distributeMsg(XmlMsg msg) {
        String content = msg.getContent();

        String adminOpenid = wechatConfigUtil.getAdminOpenid();
        String openid = msg.getFromUserName();

        if (!adminOpenid.equals(openid)) {
            return "success";
        }

        if (content.length() > 1){
            return "xxx";
        }
        
        int command = Integer.parseInt(content);
        String result = null;
        switch (command) {
            case 0:

            case 1:

            case 2:
            case 3:
            case 4:
            case 5:
            case 6:
            case 7:
            case 8:
            case 9:
                // 进入 initQuestion 管理页
                sendQuesMsg();
        }
        return result;
    }

    @Value("${msg.dailyImage.default}")
    private String defaultImage;

    // 发送管理链接
    @Async
    public void sendQuesMsg() {
        String adminOpenid = wechatConfigUtil.getAdminOpenid();
        String url = wechatConfigUtil.getBindUrl();
        String uuid = UUID.randomUUID().toString();
        redisDao.addVal(REDIS_PREFIX_INIT + uuid, adminOpenid, 30, TimeUnit.MINUTES);
        url += "init/list.html#" + uuid;

        Map<String, Object> data = new HashMap<>();
        data.put("touser", adminOpenid);
        data.put("msgtype", "news");

        Map<String, Object> news = new HashMap<>();
        Map<String, Object> article = new HashMap<>();
        article.put("title", "点我进入管理页面");
        article.put("description", "这个项目怎么写的这么难。");
        System.out.println("defaultImage:" + defaultImage);
        article.put("url", url);
        article.put("picurl", defaultImage);
        news.put("articles", Collections.singleton(article));
        data.put("news", news);

        String result = CommonUtil.toString(data);
        result = wechatConfigUtil.sendWechatMsg(result);
        System.out.println("result:"+result);
    }

    @Bean
    public AsyncTaskExecutor taskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setThreadNamePrefix("a_mdx-Executor");
        executor.setMaxPoolSize(10);

        // 设置拒绝策略
        executor.setRejectedExecutionHandler(new RejectedExecutionHandler() {
            @Override
            public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {
                // .....
            }
        });
        // 使用预定义的异常处理类
        // executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());

        return executor;
    }

}
