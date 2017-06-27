package com.madx.cherry.core.common.dao;

import com.madx.cherry.core.common.entity.SysUserPO;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by A-mdx on 2017/6/19.
 */
public interface SysUserDao extends JpaRepository<SysUserPO, Integer>{

    SysUserPO findByOpenId(String openId);
}
