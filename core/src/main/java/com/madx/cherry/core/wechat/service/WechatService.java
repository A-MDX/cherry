package com.madx.cherry.core.wechat.service;

import com.madx.cherry.core.common.CommonCode;
import com.madx.cherry.core.common.CommonUtil;
import com.madx.cherry.core.common.dao.RedisDao;
import com.madx.cherry.core.common.dao.SysUserDao;
import com.madx.cherry.core.common.entity.SysUserPO;
import com.madx.cherry.core.common.util.SpringContextUtil;
import com.madx.cherry.core.wechat.bean.XmlMsg;
import com.madx.cherry.core.wechat.common.WechatException;
import com.madx.cherry.core.wechat.common.WechatUtil;
import com.madx.cherry.core.wechat.service.command.CommandExecute;
import com.madx.cherry.core.wechat.service.command.CommandExecuteUtil;
import com.madx.cherry.core.wechat.service.command.SendDailyArticleCommand;
import com.mongodb.BasicDBObject;
import com.mongodb.client.MongoCollection;
import org.bson.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.TimeUnit;


/**
 * 
 * Created by A-mdx on 2017/6/24.
 */
@Service
public class WechatService {

    private static Logger logger = LoggerFactory.getLogger(WechatService.class);

    @Autowired
    private WechatMessageService messageService;
    @Autowired
    private RedisDao redisDao;
    @Autowired
    private SysUserDao userDao;
    @Autowired
    private SimpleAsyncTaskExecutor taskExecutor;
    @Autowired
    private CommandExecuteUtil executeUtil;

    public static final String WECHAT_DAILY_PROFIX = "wechat.daily.";
    @Value("${wechat.bindUrl}")
    private String bindUrl;


    /**
     * 注册一个简单的 任务 线程池
     * @return
     */
    @Bean
    public SimpleAsyncTaskExecutor getTaskExecutor(){
        return new SimpleAsyncTaskExecutor();
    }
    
    public String distributeMsg(XmlMsg msg) {

        // 先在这一层验证当前user 信息的合法性
        String openId = msg.getFromUserName();
        SysUserPO userPO = userDao.findByOpenIdAndStatus(openId, CommonCode.VALID_TRUE);
        WechatUtil.isNullForMsg(userPO, msg, "没找到你的用户信息，或者你被注销了，要不你找管理员注册一个？");

        switch (msg.getMsgType()){
            case "text":
                return commandFind(msg, userPO);
            case "image":
                return messageService.analysisImage(msg);
//            case "voice":
//            case "video":
//                return messageService.analysisDataMsg(msg);
//            case "link":
//                return messageService.analysisLink(msg);
//            case "location":
//                return messageService.analysisLocation(msg);
//            case "event":
//                return messageService.analysisEvent(msg);
//            default:
//                return messageService.analysisOther(msg);
        }
        return null;
    }

    /**
     * 寻找是否为命令，暂定命令为 0-9， 若为命令，会启动菜单赋予模式
     * @param msg
     * @param userPO
     * @return
     */
    public String commandFind(XmlMsg msg, SysUserPO userPO){
        String content = msg.getContent();
        if (content.length() == 1){
            try {
                return commandRun(Integer.parseInt(content), msg, userPO);
            } catch (NumberFormatException e) {
//                e.printStackTrace();
            }
        }
        return messageService.analysisText(msg);
    }

    private String commandRun(int command, XmlMsg msg, SysUserPO userPO) {
        
        String beanName = null;
        switch (command){
            case 0:
                break;
            case 1:
                // 执行一个发送文章的请求
                beanName = "sendDailyArticleCommand";
                break;
            case 2:
            case 3:
            case 4:
            case 5:
            case 6:
            case 7:
            case 8:
            case 9:

        }

        // 不要异步了，直接 exception 处理出去
        genTextHtml(msg, userPO, command);
        // 下面的基本走不到了。

        if (beanName != null){
            String finalBeanName = beanName;


            taskExecutor.execute(() -> {
                CommandExecute execute = SpringContextUtil.getBean(finalBeanName);
                execute.execute(msg, userPO);
            });
        }
        

        return "success";
    }

    public void genTextHtml(XmlMsg msg, SysUserPO userPO, int day){
        logger.info("-----------------------------------------------------------------");
        logger.info("  执行命令： 发送 那些天 的日记   ");

        String loginName = userPO.getLoginName();

        // 寻找昨天的数据
        LocalDate yesterday = LocalDate.now().minusDays(day);
        MongoCollection<Document> collection = executeUtil.getMongoClient()
                .getDatabase(executeUtil.getDatabase()).getCollection(executeUtil.getCollectionDaily());
        Document query = new Document("name", yesterday.format(DateTimeFormatter.ISO_DATE))
                .append("user", loginName).append("status", CommonCode.VALID_TRUE);
        Document daily = collection.find(query).first();
        // ？没有的话，现在建立？算了，麻烦
        if (daily == null || daily.isEmpty()){
            // 发空消息
            msg.setMsgType("text");
            msg.setContent("不知为何，咩有生成数据");

            throw new WechatException("不知为何，咩有生成数据", msg);
        }

        msg.setMsgType("news");
        msg.setTitle(yesterday.format(DateTimeFormatter.ofPattern("yyyy年 MM月 dd日")));
        msg.setPicUrl(daily.get("picUrl").toString());
        msg.setDescription(daily.get("description").toString());

        // 生成 url + token
        String token = UUID.randomUUID().toString();
        token = token.replace("-", "");
        token = token.substring(0, 12);

        executeUtil.getRedisDao().addVal(WECHAT_DAILY_PROFIX+token, daily.toJson(), 22, TimeUnit.HOURS);

        // 使用hash
        /*
        window.location.hash这个属性可读可写。读取时，可以用来判断网页状态是否改变；写入时，则会在不重载网页的前提下，创造一条访问历史记录。
        #代表网页中的一个位置。其右面的字符，就是该位置的标识符。比如，http://www.example.com/index.html#print就代表网页index.html的print位置。浏览器读取这个URL后，会自动将print位置滚动至可视区域。
　　为网页位置指定标识符，有两个方法。一是使用锚点，比如<a name="print"></a>，二是使用id属性，比如<div id="print">。
         */

        // todo 域名问题
        String url = bindUrl + "wechat/daily.html#"+token;

        msg.setUrl(url);

        logger.info("ã aaa啊啊啊啊啊啊啊啊啊啊啊啊啊  啊啊啊啊啊啊啊啊啊啊");
        logger.info("闪开，我要抛异常了。");

        throw new WechatException("", msg);
    }

    /**
     * 获取前一天的消息
     * @return
     */
    public String getDailyMessage(String token) {
        String str = redisDao.getVal(SendDailyArticleCommand.WECHAT_DAILY_PROFIX + token);
        CommonUtil.validArgumentEmpty(str, "额，似乎因为过期，你的这个信息丢失了。");
        
        return str;
    }
}
