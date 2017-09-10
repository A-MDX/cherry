package com.madx.cherry.core.shiro.bean;
import javax.persistence.*;
import java.io.Serializable;




@Entity
@Table(name="sys_permission")
public class SysPermissionPO implements Serializable{ 

private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue
	@Column(name="id")
	private Integer id;

	@Column(name="code")
	private String code;

	@Column(name="name")
	private String name;

	@Column(name="remark")
	private String remark;

	@Column(name="status")
	private Integer status;

	public void setId(Integer id){
    	this.id = id;
 	}

	public Integer getId(){
    	return this.id;
 	}

	public void setCode(String code){
    	this.code = code;
 	}

	public String getCode(){
    	return this.code;
 	}

	public void setName(String name){
    	this.name = name;
 	}

	public String getName(){
    	return this.name;
 	}

	public void setRemark(String remark){
    	this.remark = remark;
 	}

	public String getRemark(){
    	return this.remark;
 	}

	public void setStatus(Integer status){
    	this.status = status;
 	}

	public Integer getStatus(){
    	return this.status;
 	}

} 

