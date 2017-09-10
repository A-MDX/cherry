package com.madx.cherry.core.shiro.bean;
import javax.persistence.*;
import java.io.Serializable;




@Entity
@Table(name="sys_role_permission")
public class SysRolePermissionPO implements Serializable{ 

private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue
	@Column(name="id")
	private Integer id;

	@Column(name="role_id")
	private Integer roleId;

	@Column(name="permission_id")
	private Integer permissionId;

	public void setId(Integer id){
    	this.id = id;
 	}

	public Integer getId(){
    	return this.id;
 	}

	public void setRoleId(Integer roleId){
    	this.roleId = roleId;
 	}

	public Integer getRoleId(){
    	return this.roleId;
 	}

	public void setPermissionId(Integer permissionId){
    	this.permissionId = permissionId;
 	}

	public Integer getPermissionId(){
    	return this.permissionId;
 	}

} 

