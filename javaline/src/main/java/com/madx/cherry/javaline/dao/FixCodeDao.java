package com.madx.cherry.javaline.dao;

import madx.entity.FixCodePO;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Created by A-mdx on 2016/12/1.
 */
public interface FixCodeDao extends JpaRepository<FixCodePO,Integer> {
    
    List<FixCodePO> findByCodeType(Integer codeType);
    
}
