package com.madx.cherry.core.wechat.service;

import com.madx.cherry.core.common.ArgumentsException;
import com.madx.cherry.core.common.CommonCode;
import com.madx.cherry.core.common.CommonUtil;
import com.madx.cherry.core.common.dao.RedisDao;
import com.madx.cherry.core.common.dao.SysUserDao;
import com.madx.cherry.core.common.entity.SysUserPO;
import com.madx.cherry.core.wechat.bean.Result;
import com.madx.cherry.core.wechat.bean.XmlMsg;
import com.madx.cherry.core.wechat.common.WechatUtil;
import com.madx.cherry.core.wechat.service.command.CommandExecute;
import com.madx.cherry.core.wechat.service.command.SendDailyArticleCommand;
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
    @Autowired
    private RedisDao redisDao;
    @Autowired
    private SysUserDao userDao;
    
    public String distributeMsg(XmlMsg msg) {

        // 先在这一层验证当前user 信息的合法性
        String openId = msg.getFromUserName();
        SysUserPO userPO = userDao.findByOpenIdAndStatus(openId, CommonCode.VALID_TRUE);
        WechatUtil.isNullForMsg(userPO, msg, "没找到你的用户信息，或者你被注销了，要不你找管理员注册一个？");

        switch (msg.getMsgType()){
            case "text":
                return commandFind(msg, userPO);
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
     * @param userPO
     * @return
     */
    public String commandFind(XmlMsg msg, SysUserPO userPO){
        String content = msg.getContent();
        if (content.length() == 1){
            try {
                return commandRun(Integer.parseInt(content), msg, userPO);
            } catch (NumberFormatException e) {
//                e.printStackTrace();
            }
        }
        return messageService.analysisText(msg);
    }

    private String commandRun(int command, XmlMsg msg, SysUserPO userPO) {
        CommandExecute execute = null;
        switch (command){
            case 0:
                break;
            case 1:
                // 执行一个发送文章的请求
                execute = new SendDailyArticleCommand();
                break;
            case 2:
            case 3:
            case 4:
            case 5:
            case 6:
            case 7:
            case 8:
            case 9:

        }
        if (execute != null){
            CommandExecute finalExecute = execute;
            new Thread(() -> {
                // 发 一个命令列表链接， 外加一个 直接的控制命令
                logger.info("something is execute start");
                finalExecute.execute(msg, userPO);
                logger.info("something is execute end");
            }).start();
        }
        

        return "success";
    }

    /**
     * 获取前一天的消息
     * @param date
     * @return
     */
    public String getDailyMessage(String token) {
        String str = redisDao.getVal(SendDailyArticleCommand.WECHAT_DAILY_PROFIX + token);
        CommonUtil.validArgumentEmpty(str, "额，似乎因为过期，你的这个信息丢失了。");
        
        return str;
    }
}
