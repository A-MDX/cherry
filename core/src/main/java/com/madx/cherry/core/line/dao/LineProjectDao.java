package com.madx.cherry.core.line.dao;

import com.madx.cherry.core.line.bean.LineProjectPO;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Created by a-mdx on 2017/7/18.
 */
public interface LineProjectDao extends JpaRepository<LineProjectPO, Integer> {

    LineProjectPO findByProject(String project);

    List<LineProjectPO> findByUserAndStatus(String user, Integer status);

}
