package com.madx.cherry.core.wechat.service.command;

import com.madx.cherry.core.common.CommonCode;
import com.madx.cherry.core.common.entity.SysUserPO;
import com.madx.cherry.core.wechat.bean.XmlMsg;
import com.mongodb.BasicDBObject;
import com.mongodb.client.MongoCollection;
import org.bson.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * 发送 给当前用户，发送消息
 * Created by a-mdx on 2017/7/4.
 */
@Component
public class SendDailyArticleCommand implements CommandExecute {

    private static Logger logger = LoggerFactory.getLogger(SendDailyArticleCommand.class);

    public static final String WECHAT_DAILY_PROFIX = "wechat.daily.";

    @Autowired
    private CommandExecuteUtil executeUtil;
    @Value("${wechat.bindUrl}")
    private String bindUrl;
    

    @Async
    @Override
    public void execute(XmlMsg msg, SysUserPO userPO) {
        
        logger.info("-----------------------------------------------------------------");
        logger.info("  执行命令： 发送昨天的日记  ");

        String loginName = userPO.getLoginName();
        BasicDBObject json = new BasicDBObject();
        json.append("touser", userPO.getOpenId())
                .append("msgtype", "news");
        BasicDBObject article = new BasicDBObject();

        
        // 寻找昨天的数据
        LocalDate yesterday = LocalDate.now().minusDays(1);
        MongoCollection<Document> collection = executeUtil.getMongoClient()
                .getDatabase(executeUtil.getDatabase()).getCollection(executeUtil.getCollectionDaily());
        Document query = new Document("name", yesterday.format(DateTimeFormatter.ISO_DATE))
                .append("user", loginName).append("status", CommonCode.STATUS_YES_OR_VALID);
        Document yesterdayDaily = collection.find(query).first();
        // ？没有的话，现在建立？算了，麻烦
        if (yesterdayDaily == null || yesterdayDaily.isEmpty()){
            // 发空消息
            json.append("msgtype", "text");
            json.append("text", new Document("content", "不知为何，咩有生成数据"));
            executeUtil.getWechatConfigUtil().sendMessage(json);
            return;
        }


        article.append("title", yesterday.format(DateTimeFormatter.ofPattern("yyyy年 MM月 dd日")) );
        article.append("description", yesterdayDaily.get("description"));
        article.append("picurl", yesterdayDaily.get("picUrl"));

        // 生成 url + token
        String token = UUID.randomUUID().toString();
        token = token.replace("-", "");
        token = token.substring(0, 12);

        executeUtil.getRedisDao().addVal(WECHAT_DAILY_PROFIX+token, yesterdayDaily.toJson(), 22, TimeUnit.HOURS);

        // 使用hash
        /*
        window.location.hash这个属性可读可写。读取时，可以用来判断网页状态是否改变；写入时，则会在不重载网页的前提下，创造一条访问历史记录。
        #代表网页中的一个位置。其右面的字符，就是该位置的标识符。比如，http://www.example.com/index.html#print就代表网页index.html的print位置。浏览器读取这个URL后，会自动将print位置滚动至可视区域。
　　为网页位置指定标识符，有两个方法。一是使用锚点，比如<a name="print"></a>，二是使用id属性，比如<div id="print">。
         */
        
        // todo 域名问题
        String url = bindUrl + "wechat/daily.html#"+token;
        article.append("url", url);
        
        json.append("news", new BasicDBObject("articles", Collections.singletonList(article)));

        System.out.println("-------***-----");
        System.out.println(json.toJson());

        Optional<BasicDBObject> reOption = executeUtil.getWechatConfigUtil().sendMessage(json);
        reOption.ifPresent(basicDBObject -> logger.info(basicDBObject.toJson()));

    }
    public static void main(String... args){
        LocalDate localDate = LocalDate.now();
        String str = localDate.format(DateTimeFormatter.ofPattern("yyyy年 MM月 dd日"));
        System.out.println(str);

        String token = UUID.randomUUID().toString();
        token = token.replace("-", "");
        token = token.substring(0, 12);
        System.out.println(token);
    }
}
