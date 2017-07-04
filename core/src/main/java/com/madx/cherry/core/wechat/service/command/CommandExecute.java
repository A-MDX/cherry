package com.madx.cherry.core.wechat.service.command;

import com.madx.cherry.core.common.entity.SysUserPO;
import com.madx.cherry.core.wechat.bean.XmlMsg;

/**
 * 命令执行，主要面对输入情况为0-9 问题
 * Created by a-mdx on 2017/7/4.
 */
@FunctionalInterface
public interface CommandExecute {
    /**
     * 命令直接执行
     * @param msg
     */
    void execute(XmlMsg msg, SysUserPO userPO);
}
