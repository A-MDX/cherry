package com.madx.wechat.initme.entity;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Date;

/**
 * Create by A-mdx at 2018-03-21 10:24
 */
@Entity
@Table(name = "init_answer")
public class InitAnswerPo {

    @Id
    @GeneratedValue(generator = "systemUUID")
    @GenericGenerator(name = "systemUUID", strategy = "uuid")
    private String id;

    private String answer;

    //    type = 1, 表示单选， 其他0，多选
    private Integer type = 1;

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "question_id")
    private InitQuestionPo initQuestionPo;

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "you_id")
    private InitYouPo initYouPo;

    private Date createTime = new Date();

    public InitYouPo getInitYouPo() {
        return initYouPo;
    }

    public void setInitYouPo(InitYouPo initYouPo) {
        this.initYouPo = initYouPo;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public InitQuestionPo getInitQuestionPo() {
        return initQuestionPo;
    }

    public void setInitQuestionPo(InitQuestionPo initQuestionPo) {
        this.initQuestionPo = initQuestionPo;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}
