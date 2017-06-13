package com.madx.cherry.javaline.bean;
import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;


@Entity
@Table(name="eat_type")
public class EatTypePO implements Serializable{ 

private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue
	@Column(name="id")
	private Integer id;

	@Column(name="name")
	private String name;

	@Column(name="status")
	private Integer status;

	@Column(name="max_dian")
	private Integer maxDian;

	@Column(name="now_dian")
	private Integer nowDian;

	@Column(name="remark")
	private String remark;

	@Column(name="creation_time")
	private Date creationTime;

	@Column(name="creator")
	private Integer creator;

	@Column(name="modify_time")
	private Date modifyTime;

	@Column(name="modifier")
	private Integer modifier;

	@Column(name = "picture")
	private String picture;

	public String getPicture() {
		return picture;
	}

	public void setPicture(String picture) {
		this.picture = picture;
	}

	public void setId(Integer id){
    	this.id = id;
 	}

	public Integer getId(){
    	return this.id;
 	}

	public void setName(String name){
    	this.name = name;
 	}

	public String getName(){
    	return this.name;
 	}

	public void setStatus(Integer status){
    	this.status = status;
 	}

	public Integer getStatus(){
    	return this.status;
 	}

	public void setMaxDian(Integer maxDian){
    	this.maxDian = maxDian;
 	}

	public Integer getMaxDian(){
    	return this.maxDian;
 	}

	public void setNowDian(Integer nowDian){
    	this.nowDian = nowDian;
 	}

	public Integer getNowDian(){
    	return this.nowDian;
 	}

	public void setRemark(String remark){
    	this.remark = remark;
 	}

	public String getRemark(){
    	return this.remark;
 	}

	public void setCreationTime(Date creationTime){
    	this.creationTime = creationTime;
 	}

	public Date getCreationTime(){
    	return this.creationTime;
 	}

	public void setCreator(Integer creator){
    	this.creator = creator;
 	}

	public Integer getCreator(){
    	return this.creator;
 	}

	public void setModifyTime(Date modifyTime){
    	this.modifyTime = modifyTime;
 	}

	public Date getModifyTime(){
    	return this.modifyTime;
 	}

	public void setModifier(Integer modifier){
    	this.modifier = modifier;
 	}

	public Integer getModifier(){
    	return this.modifier;
 	}

} 

