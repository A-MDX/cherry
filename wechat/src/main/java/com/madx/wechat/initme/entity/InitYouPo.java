package com.madx.wechat.initme.entity;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Date;

/**
 * Create by A-mdx at 2018-03-21 10:35
 */
@Entity
@Table(name = "init_you")
public class InitYouPo {

    @Id
    @GeneratedValue(generator = "systemUUID")
    @GenericGenerator(name="systemUUID",strategy = "uuid")
    private String id;
    
    // 唯一
    @Column(unique = true)
    private String name;

    private Date createTime = new Date();

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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
