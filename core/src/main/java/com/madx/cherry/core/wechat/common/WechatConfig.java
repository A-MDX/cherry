package com.madx.cherry.core.wechat.common;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * Created by A-mdx on 2017/6/23.
 */
@Component
@ConfigurationProperties(prefix = "wechat")
public class WechatConfig {
    
    private String token;
    private String appID;
    private String appsecret;
    private String url;

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
