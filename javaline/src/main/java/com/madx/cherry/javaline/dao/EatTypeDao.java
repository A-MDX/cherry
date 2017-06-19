package com.madx.cherry.javaline.dao;


import com.madx.cherry.javaline.bean.EatTypePO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * Created by A-mdx on 2016/12/26.
 */
public interface EatTypeDao extends JpaRepository<EatTypePO,Integer> {

    @Query("select t.id,t.name from EatTypePO t")
    List<Object[]> findIdAndName();
    
    List<EatTypePO> findByStatus(Integer status);
    
}
