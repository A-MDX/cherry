package com.madx.cherry.core.wechat.common;

import com.madx.cherry.core.wechat.bean.XmlMsg;

/**
 * Created by A-mdx on 2017/6/25.
 */
public class WechatException extends RuntimeException {
    
    private XmlMsg xmlMsg;
    
    public WechatException(String msg, XmlMsg xmlMsg){
        super(msg);
        this.xmlMsg = xmlMsg;
    }

    public XmlMsg getXmlMsg() {
        return xmlMsg;
    }

    public void setXmlMsg(XmlMsg xmlMsg) {
        this.xmlMsg = xmlMsg;
    }
}
