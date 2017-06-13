package com.madx.cherry.javaline.bean;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;


@Entity
@Table(name="fix_code")
public class FixCodePO implements Serializable{ 

private static final long serialVersionUID = 1L;

	@Id
	@Column(name="code")
	private Integer code;

	@Column(name="code_type")
	private Integer codeType;

	@Column(name="code_name")
	private String codeName;

	@Column(name="creation_time")
	private Date creationTime;

	@Column(name="creator")
	private Integer creator;

	@Column(name="remark")
	private String remark;

	public void setCode(Integer code){
    	this.code = code;
 	}

	public Integer getCode(){
    	return this.code;
 	}

	public void setCodeType(Integer codeType){
    	this.codeType = codeType;
 	}

	public Integer getCodeType(){
    	return this.codeType;
 	}

	public void setCodeName(String codeName){
    	this.codeName = codeName;
 	}

	public String getCodeName(){
    	return this.codeName;
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

	public void setRemark(String remark){
    	this.remark = remark;
 	}

	public String getRemark(){
    	return this.remark;
 	}

} 

