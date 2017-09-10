package com.madx.cherry.core.shiro.config;


import com.madx.cherry.core.common.entity.SysUserPO;
import com.madx.cherry.core.shiro.service.SysUserService;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Created by a-mdx on 2017/8/15.
 */
@Component
public class MyRealm extends AuthorizingRealm {

    @Autowired
    private SysUserService userService;

    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        String currentUser = (String) principalCollection.getPrimaryPrincipal();
        List<String> roles = userService.getRolesByLoginName(currentUser);

        List<String> permissions = userService.getPermissionByLoginName(currentUser);

        // 设置角色权限
        SimpleAuthorizationInfo authorizationInfo = new SimpleAuthorizationInfo();
        authorizationInfo.addRoles(roles);

        authorizationInfo.addStringPermissions(permissions);

        return authorizationInfo;
    }

    /**
     * 验证 当前的 subject
     * @param authenticationToken
     * @return
     * @throws AuthenticationException
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        System.out.println("###【开始认证[i]】"+ SecurityUtils.getSubject().getSession().getId());
        String loginName = (String) authenticationToken.getPrincipal();
        SysUserPO userPO = userService.findByLoginName(loginName);

        if (userPO == null){
            throw new UnknownAccountException();
        }
        SimpleAuthenticationInfo authenticationInfo = new SimpleAuthenticationInfo(
                userPO.getLoginName(),  //
                userPO.getPassword(),  //
                ByteSource.Util.bytes(userPO.getCredentialsSalt()), //salt=username+salt
                getName()
        );

        return authenticationInfo;
    }
}
