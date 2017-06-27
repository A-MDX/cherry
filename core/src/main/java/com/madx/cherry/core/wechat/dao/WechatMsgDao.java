package com.madx.cherry.core.wechat.dao;


import com.madx.cherry.core.wechat.bean.WechatMsgPO;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by a-mdx on 2017/6/21.
 */
public interface WechatMsgDao extends JpaRepository<WechatMsgPO, Long> {

}
