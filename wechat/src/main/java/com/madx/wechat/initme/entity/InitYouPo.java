package com.madx.wechat.initme.entity;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

/**
 * Create by A-mdx at 2018-03-21 10:35
 */
@Entity
@Table(name = "init_you")
public class InitYouPo {

    @Id
    private String openId;
    
    private String name;

    private Date createTime = new Date();

    public String getOpenId() {
        return openId;
    }

    public void setOpenId(String openId) {
        this.openId = openId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}
