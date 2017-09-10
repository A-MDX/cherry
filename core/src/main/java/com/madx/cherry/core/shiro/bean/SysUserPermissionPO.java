package com.madx.cherry.core.shiro.bean;
import javax.persistence.*;
import java.io.Serializable;




@Entity
@Table(name="sys_user_permission")
public class SysUserPermissionPO implements Serializable{ 

private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue
	@Column(name="id")
	private Long id;

	@Column(name="user_id")
	private Integer userId;

	@Column(name="permission_id")
	private Integer permissionId;

	public void setId(Long id){
    	this.id = id;
 	}

	public Long getId(){
    	return this.id;
 	}

	public void setUserId(Integer userId){
    	this.userId = userId;
 	}

	public Integer getUserId(){
    	return this.userId;
 	}

	public void setPermissionId(Integer permissionId){
    	this.permissionId = permissionId;
 	}

	public Integer getPermissionId(){
    	return this.permissionId;
 	}

} 

