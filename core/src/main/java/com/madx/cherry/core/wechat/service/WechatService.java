package com.madx.cherry.core.wechat.service;

import com.madx.cherry.core.wechat.bean.XmlMsg;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


/**
 * 
 * Created by A-mdx on 2017/6/24.
 */
@Service
public class WechatService {

    private static Logger logger = LoggerFactory.getLogger(WechatService.class);

    @Autowired
    private WechatMessageService messageService;
    
    public String distributeMsg(XmlMsg msg) {
        switch (msg.getMsgType()){
            case "text":
                return commandFind(msg);
            case "image":
                return messageService.analysisImage(msg);
//            case "voice":
//            case "video":
//                return messageService.analysisDataMsg(msg);
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

    /**
     * 寻找是否为命令，暂定命令为 0-9， 若为命令，会启动菜单赋予模式
     * @param msg
     * @return
     */
    public String commandFind(XmlMsg msg){
        String content = msg.getContent();
        if (content.length() == 1){
            try {
                return commandRun(Integer.parseInt(content));
            } catch (NumberFormatException e) {
//                e.printStackTrace();
            }
        }
        return messageService.analysisDataMsg(msg);
    }

    private String commandRun(int command) {
        switch (command){
            case 0:
            case 1:
            case 2:
            case 3:
            case 4:
            case 5:
            case 6:
            case 7:
            case 8:
            case 9:

        }
        new Thread(() -> {
            // 发 一个命令列表链接， 外加一个 直接的控制命令
            logger.info("something is send.....1");
            logger.info("something is send.....2");
        }).start();

        return "success";
    }

}
