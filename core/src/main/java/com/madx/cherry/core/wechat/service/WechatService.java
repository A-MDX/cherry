package com.madx.cherry.core.wechat.service;

import com.madx.cherry.core.wechat.bean.XmlMsg;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 
 * Created by A-mdx on 2017/6/24.
 */
@Service
public class WechatService {

    @Autowired
    private WechatMessageService messageService;
    
    public String distributeMsg(XmlMsg msg) {
        switch (msg.getMsgType()){
            case "text":
                return messageService.analysisText(msg);
            case "image":
            case "voice":
            case "video":
                return messageService.analysisDataMsg(msg);
//            case "link":
//                return messageService.analysisLink(msg);
//            case "location":
//                return messageService.analysisLocation(msg);
//            case "event":
//                return messageService.analysisEvent(msg);
//            default:
//                return messageService.analysisOther(msg);
        }
        return null;
    }
}
