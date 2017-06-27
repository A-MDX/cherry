package com.madx.cherry.javaline.dao;


import com.madx.cherry.javaline.bean.EatMemuPO;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Created by A-mdx on 2016/12/26.
 */
public interface EatMemuDao extends JpaRepository<EatMemuPO,Integer> {
    
    List<EatMemuPO> findByStatus(Integer status);

    List<EatMemuPO> findByStatusAndType(Integer status, Integer type);
    
}
