package com.madx.cherry.core.shiro.bean;
import javax.persistence.*;
import java.io.Serializable;




@Entity
@Table(name="sys_user_role")
public class SysUserRolePO implements Serializable{ 

private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue
	@Column(name="id")
	private Integer id;

	@Column(name="user_id")
	private Integer userId;

	@Column(name="role_id")
	private Integer roleId;

	public void setId(Integer id){
    	this.id = id;
 	}

	public Integer getId(){
    	return this.id;
 	}

	public void setUserId(Integer userId){
    	this.userId = userId;
 	}

	public Integer getUserId(){
    	return this.userId;
 	}

	public void setRoleId(Integer roleId){
    	this.roleId = roleId;
 	}

	public Integer getRoleId(){
    	return this.roleId;
 	}

} 

