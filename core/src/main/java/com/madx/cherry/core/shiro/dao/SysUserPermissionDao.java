package com.madx.cherry.core.shiro.dao;


import com.madx.cherry.core.shiro.bean.SysUserPermissionPO;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by a-mdx on 2017/8/15.
 */
public interface SysUserPermissionDao extends JpaRepository<SysUserPermissionPO, Long> {
}
