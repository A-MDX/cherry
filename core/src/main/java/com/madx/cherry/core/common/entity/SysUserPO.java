package com.madx.cherry.core.common.entity;


import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name="sys_user")
public class SysUserPO implements Serializable{ 

private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue
	@Column(name="id")
	private Integer id;

	@Column(name="login_name")
	private String loginName;

	@Column(name="password")
	private String password;

	@Column(name="salt")
	private String salt;

	@Column(name="name")
	private String name;

	@Column(name="open_id")
	private String openId;

	@Column(name="remark")
	private String remark;

	@Column(name="creator")
	private Integer creator;

	@Column(name = "status")
	private Integer status;

	@Column(name="creation_time")
	private Date creationTime;

	@Column(name="modifier")
	private Integer modifier;

	@Column(name="mofify_time")
	private Date mofifyTime;

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public void setId(Integer id){
    	this.id = id;
 	}

	public Integer getId(){
    	return this.id;
 	}

	public void setLoginName(String loginName){
    	this.loginName = loginName;
 	}

	public String getLoginName(){
    	return this.loginName;
 	}

	public void setPassword(String password){
    	this.password = password;
 	}

	public String getPassword(){
    	return this.password;
 	}

	public void setSalt(String salt){
    	this.salt = salt;
 	}

	public String getSalt(){
    	return this.salt;
 	}

	public void setName(String name){
    	this.name = name;
 	}

	public String getName(){
    	return this.name;
 	}

	public void setOpenId(String openId){
    	this.openId = openId;
 	}

	public String getOpenId(){
    	return this.openId;
 	}

	public void setRemark(String remark){
    	this.remark = remark;
 	}

	public String getRemark(){
    	return this.remark;
 	}

	public void setCreator(Integer creator){
    	this.creator = creator;
 	}

	public Integer getCreator(){
    	return this.creator;
 	}

	public void setCreationTime(Date creationTime){
    	this.creationTime = creationTime;
 	}

	public Date getCreationTime(){
    	return this.creationTime;
 	}

	public void setModifier(Integer modifier){
    	this.modifier = modifier;
 	}

	public Integer getModifier(){
    	return this.modifier;
 	}

	public void setMofifyTime(Date mofifyTime){
    	this.mofifyTime = mofifyTime;
 	}

	public Date getMofifyTime(){
    	return this.mofifyTime;
 	}

} 

