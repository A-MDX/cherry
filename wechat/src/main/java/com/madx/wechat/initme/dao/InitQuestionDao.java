package com.madx.wechat.initme.dao;

import com.madx.wechat.initme.entity.InitQuestionPo;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Create by A-mdx at 2018/3/17 0017 13:14
 */
public interface InitQuestionDao extends JpaRepository<InitQuestionPo, String> {
    
}
