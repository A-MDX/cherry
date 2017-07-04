package com.madx.cherry.core.common.dao;

import com.madx.cherry.core.common.entity.SysUserPO;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Created by A-mdx on 2017/6/19.
 */
public interface SysUserDao extends JpaRepository<SysUserPO, Integer>{

    SysUserPO findByOpenIdAndStatus(String openId, Integer status);

    List<SysUserPO> findByStatus(Integer status);
}
