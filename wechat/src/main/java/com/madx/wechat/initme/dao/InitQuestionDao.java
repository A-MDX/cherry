package com.madx.wechat.initme.dao;

import com.madx.wechat.initme.entity.InitQuestionPo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

/**
 * Create by A-mdx at 2018/3/17 0017 13:14
 */
public interface InitQuestionDao extends JpaRepository<InitQuestionPo, String> {
    
    @Query("select max(p.index) from InitQuestionPo p where p.status = 1")
    Integer findMaxIndex();
    
    Page<InitQuestionPo> findByStatus(Integer status, Pageable pageable);
    
    InitQuestionPo findByStatusAndIndex(Integer status, Integer index);
    
    @Query("select count(*) from InitQuestionPo p where p.status = 1")
    Integer findCount();
}
