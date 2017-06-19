package com.madx.cherry.common.dao;

import com.madx.cherry.common.entity.SysUserPO;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by A-mdx on 2017/6/19.
 */
public interface SysUserDao extends JpaRepository<SysUserPO, Integer>{
    
}
