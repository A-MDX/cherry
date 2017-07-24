package com.madx.cherry.core.line.bean;
import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;




@Entity
@Table(name="line_project")
public class LineProjectPO implements Serializable{ 

	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue
	@Column(name="id")
	private Integer id;

	@Column(name="project")
	private String project;

	@Column(name="line")
	private Integer line;

	@Column(name="file")
	private Integer file;

	@Column(name="user")
	private String user;

	@Column(name="status")
	private Integer status;

	@Column(name="creation_time")
	private Date creationTime;

	@Column(name="modify_time")
	private Date modifyTime;

	public void setId(Integer id){
    	this.id = id;
 	}

	public Integer getId(){
    	return this.id;
 	}

	public void setProject(String project){
    	this.project = project;
 	}

	public String getProject(){
    	return this.project;
 	}

	public void setLine(Integer line){
    	this.line = line;
 	}

	public Integer getLine(){
    	return this.line;
 	}

	public void setFile(Integer file){
    	this.file = file;
 	}

	public Integer getFile(){
    	return this.file;
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

	public void setCreationTime(Date creationTime){
    	this.creationTime = creationTime;
 	}

	public Date getCreationTime(){
    	return this.creationTime;
 	}

	public void setModifyTime(Date modifyTime){
    	this.modifyTime = modifyTime;
 	}

	public Date getModifyTime(){
    	return this.modifyTime;
 	}

} 

