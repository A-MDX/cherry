package com.madx.cherry.core.wechat.common;

import com.madx.cherry.core.common.dao.RedisDao;
import com.mongodb.BasicDBObject;
import com.mongodb.util.JSON;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

/**
 * Created by A-mdx on 2017/6/23.
 */
@Component
@ConfigurationProperties(prefix = "wechat")
public class WechatConfig {

    private static Logger logger = LoggerFactory.getLogger(WechatConfig.class);

    private String token;
    private String appID;
    private String appsecret;
    private String url;

    private static final String getAccessTokenUrl = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=APPID&secret=APPSECRET";
    private static final String redisPrefix = "wechat.";

    @Autowired
    private RedisDao redisDao;

    public Optional<BasicDBObject> sendMessage(BasicDBObject json){
        return WechatUtil.sendMessage(getAccessToken(), json.toJson());
    }

    /**
     * 生成 accessToken
     * @return
     */
    public BasicDBObject genAccessToken(){
        String url = getAccessTokenUrl;
        url = url.replace("APPID", appID).replace("APPSECRET", appsecret);
        try {
            URL realUrl = new URL(url);
            HttpURLConnection connection = (HttpURLConnection) realUrl.openConnection();
            connection.connect();

            if (connection.getResponseCode() != 200){
                return null;
            }
            // 获取链接数据
            StringBuilder result = new StringBuilder();
            try(BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()))){
                String temp;
                while ((temp = br.readLine()) != null){
                    result.append(temp);
                }
            }

            return (BasicDBObject) JSON.parse(result.toString());
        } catch (IOException e) {
            e.printStackTrace();
            logger.error("获取accessToken 失败", e);
        }

        return null;
    }

    /**
     * 获取 accessToken
     * @return
     */
    public String getAccessToken(){
        String accessToken = redisDao.getVal(redisPrefix+"accessToken");
        if (accessToken == null) {
            BasicDBObject json = genAccessToken();
            if (json == null){
                throw new RuntimeException("本次任务不得不终止，accessToken 获取失败。");
            }

            accessToken = json.getString("access_token");
            // 判断为空
            if (accessToken == null){
                logger.error("获取accessToken 失败了， 返回数据是：{}", json.toJson());
                throw new RuntimeException("本次任务不得不终止，accessToken 获取失败。");
            }
            // 设置过期等
            redisDao.addVal(redisPrefix+"access_token", accessToken, json.getInt("expires_in"), TimeUnit.SECONDS);
        }
        return accessToken;
    }

    public static void main(String... args){
        WechatConfig config = new WechatConfig();
        config.setAppID("wx55378b2a7eaa7a16");
        config.setAppsecret("7e21d2375084101072729953c84a08d7");
        BasicDBObject json = config.genAccessToken();
        System.out.println(json.toJson());
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getAppID() {
        return appID;
    }

    public void setAppID(String appID) {
        this.appID = appID;
    }

    public String getAppsecret() {
        return appsecret;
    }

    public void setAppsecret(String appsecret) {
        this.appsecret = appsecret;
    }

    @Override
    public String toString() {
        return "WechatConfig{" +
                "token='" + token + '\'' +
                ", appID='" + appID + '\'' +
                ", appsecret='" + appsecret + '\'' +
                '}';
    }

}
