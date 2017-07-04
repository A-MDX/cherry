package com.madx.cherry.core.wechat.bean;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;


/**
 * CREATE TABLE `wechat_msg` (
 `id` bigint(20) NOT NULL AUTO_INCREMENT,
 `type` int(11) DEFAULT NULL COMMENT '数据类型，若为文本，则data为文本，其他则为id',
 `msg_type` char(15) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '数据的类型，文字表示',
 `data` varchar(1000) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '根据type的不同会产生不同的数据，如果是文本，则会是文字，若是其他，则会传输的id',
 `time` datetime DEFAULT NULL,
 `user` char(15) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '来源',
 `status` int(11) DEFAULT NULL COMMENT '状态，一般为有效无效',
 `modifier` char(15) COLLATE utf8_unicode_ci DEFAULT NULL,
 `modify_time` datetime DEFAULT NULL,
 PRIMARY KEY (`id`)
 ) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci

 */

@Entity
@Table(name="wechat_msg")
public class WechatMsgPO implements Serializable{ 

private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue
	@Column(name="id")
	private Long id;

	@Column(name="type")
	private Integer type;

	@Column(name = "msg_type")
	private String msgType;
	
	@Column(name="data")
	private String data;

	@Column(name="time")
	private Date time;

	@Column(name="user")
	private String user;

	@Column(name="status")
	private Integer status;

	@Column(name="modifier")
	private String modifier;

	@Column(name="modify_time")
	private Date modifyTime;

	public String getMsgType() {
		return msgType;
	}

	public void setMsgType(String msgType) {
		this.msgType = msgType;
	}

	public void setId(Long id){
    	this.id = id;
 	}

	public Long getId(){
    	return this.id;
 	}

	public void setType(Integer type){
    	this.type = type;
 	}

	public Integer getType(){
    	return this.type;
 	}

	public void setData(String data){
    	this.data = data;
 	}

	public String getData(){
    	return this.data;
 	}

	public void setTime(Date time){
    	this.time = time;
 	}

	public Date getTime(){
    	return this.time;
 	}

	public void setUser(String user){
    	this.user = user;
 	}

	public String getUser(){
    	return this.user;
 	}

	public void setStatus(Integer status){
    	this.status = status;
 	}

	public Integer getStatus(){
    	return this.status;
 	}

	public void setModifier(String modifier){
    	this.modifier = modifier;
 	}

	public String getModifier(){
    	return this.modifier;
 	}

	public void setModifyTime(Date modifyTime){
    	this.modifyTime = modifyTime;
 	}

	public Date getModifyTime(){
    	return this.modifyTime;
 	}

} 

