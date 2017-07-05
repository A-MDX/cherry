package com.madx.cherry.core.wechat.dao;


import com.madx.cherry.core.wechat.bean.WechatMsgPO;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Date;
import java.util.List;

/**
 * Created by a-mdx on 2017/6/21.
 */
public interface WechatMsgDao extends JpaRepository<WechatMsgPO, Long> {

    List<WechatMsgPO> findByUserAndStatusAndType(String user, Integer status, Integer type, Pageable pageable);

    List<WechatMsgPO> findByUserAndTimeBetween(String user, Date time1, Date time2);
}
