package com.madx.cherry.core.shiro.service;


import com.madx.cherry.core.common.CommonCode;
import com.madx.cherry.core.common.dao.SysUserDao;
import com.madx.cherry.core.common.entity.SysUserPO;
import com.madx.cherry.core.shiro.config.PasswordHelper;
import com.madx.cherry.core.shiro.dao.SysRolePermissionDao;
import com.madx.cherry.core.shiro.dao.SysUserRoleDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by a-mdx on 2017/7/31.
 */
@Service
public class SysUserService {

    @Autowired
    private SysUserDao sysUserDao;
    @Autowired
    private PasswordHelper passwordHelper;
    @Autowired
    private SysUserRoleDao userRoleDao;
    @Autowired
    private SysRolePermissionDao rolePermissionDao;

    public SysUserPO findByLoginName(String loginName){
        return sysUserDao.findByLoginNameAndStatus(loginName, CommonCode.STATUS_YES_OR_VALID);
    }

    public SysUserPO create(SysUserPO userPO){
        if (userPO == null){
            throw new RuntimeException("当前用户为空");
        }
        
        userPO.setStatus(CommonCode.STATUS_YES_OR_VALID);

        passwordHelper.entryptPassword(userPO);
        return sysUserDao.save(userPO);
    }

    /**
     * 根据登录名获取全部角色
     * @param loginName
     * @return
     */
    public List<String> getRolesByLoginName(String loginName){
        List<String> roles = new ArrayList<>();

        List<Object[]> roleMapList = userRoleDao.findVaildRoles(loginName);

        roleMapList.forEach(r -> roles.add(r[1].toString())); // r.id, r.code, r.name
        System.out.println(roles);
        return roles;
    }

    public List<String> getPermissionByLoginName(String loginName){
        List<String> permissions = new ArrayList<>();

        // p.id, p.code,  p.name
        List<Object[]> permissionArr = rolePermissionDao.queryValidPermissionByLoginName(loginName);
        permissionArr.forEach(p -> permissions.add(p[1].toString()));

        System.out.println(permissions);
        return permissions;
    }

}
