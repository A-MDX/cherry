package com.madx.wechat.initme.entity;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

/**
 * Create by A-mdx at 2018-03-17 12:53
 */
@Entity
@Table(name = "init_question")
public class InitQuestionPo {
    
    @Id
    @GeneratedValue(generator = "systemUUID")
    @GenericGenerator(name="systemUUID",strategy = "uuid")
    private String id;
    
    // 主题
    private String theme;
    
    // 类型，多选还是多选 1 为单选，其他多选
    @Column
    private int type = 1;
    
    private String text;
    
    // 1: 有效， 0：无效
    private Integer status = 1;
    
    @JoinColumn(name = "question_id")
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    /* 过滤条件
    @BatchSize(size=10)
@Where(clause="DEL_FLAG=1")
@OrderBy(clause="CREATED_DATE asc")
     */
    private List<InitQuestionItemPo> optionItems;

    // 默认创建时间
    private Date createTime = new Date();


    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTheme() {
        return theme;
    }

    public void setTheme(String theme) {
        this.theme = theme;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public List<InitQuestionItemPo> getOptionItems() {
        return optionItems;
    }

    public void setOptionItems(List<InitQuestionItemPo> optionItems) {
        this.optionItems = optionItems;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}
