package com.madx.cherry.core.wechat.bean;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;




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

