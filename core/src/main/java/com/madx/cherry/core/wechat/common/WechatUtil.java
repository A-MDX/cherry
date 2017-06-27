package com.madx.cherry.core.wechat.common;

import com.madx.cherry.core.wechat.bean.XmlMsg;

/**
 * Created by A-mdx on 2017/6/26.
 */
public class WechatUtil {
    /**
     * 验证是否为空
     * @param obj 验证对象
     * @param msg 主要给返回数据使用
     * @param message 错误信息
     */
    public static void isNullForMsg(Object obj, XmlMsg msg, String message){
        if (obj == null){
            throw new WechatException(message, msg);
        }
        
    }
}
