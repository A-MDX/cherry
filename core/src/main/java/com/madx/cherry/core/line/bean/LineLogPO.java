package com.madx.cherry.core.line.bean;
import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;




@Entity
@Table(name="line_log")
public class LineLogPO implements Serializable{ 

	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue
	@Column(name="id")
	private Long id;

	@Column(name="project_id")
	private Integer projectId;

	@Column(name="now_line")
	private Integer nowLine;

	@Column(name="now_file")
	private Integer nowFile;

	@Column(name="than_last_line")
	private Integer thanLastLine;

	@Column(name="than_last_file")
	private Integer thanLastFile;

	@Column(name="creation_time")
	private Date creationTime;

	public void setId(Long id){
    	this.id = id;
 	}

	public Long getId(){
    	return this.id;
 	}

	public void setProjectId(Integer projectId){
    	this.projectId = projectId;
 	}

	public Integer getProjectId(){
    	return this.projectId;
 	}

	public void setNowLine(Integer nowLine){
    	this.nowLine = nowLine;
 	}

	public Integer getNowLine(){
    	return this.nowLine;
 	}

	public void setNowFile(Integer nowFile){
    	this.nowFile = nowFile;
 	}

	public Integer getNowFile(){
    	return this.nowFile;
 	}

	public void setThanLastLine(Integer thanLastLine){
    	this.thanLastLine = thanLastLine;
 	}

	public Integer getThanLastLine(){
    	return this.thanLastLine;
 	}

	public void setThanLastFile(Integer thanLastFile){
    	this.thanLastFile = thanLastFile;
 	}

	public Integer getThanLastFile(){
    	return this.thanLastFile;
 	}

	public void setCreationTime(Date creationTime){
    	this.creationTime = creationTime;
 	}

	public Date getCreationTime(){
    	return this.creationTime;
 	}

} 

