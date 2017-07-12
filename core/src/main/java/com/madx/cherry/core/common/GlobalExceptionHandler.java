package com.madx.cherry.core.common;

import com.madx.cherry.core.wechat.bean.Result;
import com.madx.cherry.core.wechat.bean.XmlMsg;
import com.madx.cherry.core.wechat.common.WechatException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;

/**
 * Created by A-mdx on 2017/6/24.
 */
@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(value = ArgumentsException.class)
    @ResponseBody
    public Result defaultExceptionHandle(HttpServletRequest request, ArgumentsException exception, HttpServletResponse response){
        Result result = Result.instance();
        result.setCode(0);
        result.setMsg(exception.getMessage());
        response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        return result;
    }
    
    @ExceptionHandler(value = WechatException.class)
    @ResponseBody
    public String wechatExceptionHandle(HttpServletRequest request, WechatException e, HttpServletResponse response){
        XmlMsg msg = e.getXmlMsg();

        StringBuilder returnStr = new StringBuilder("<xml>\n");
        returnStr.append("<ToUserName><![CDATA["+msg.getFromUserName()+"]]></ToUserName>\n");
        returnStr.append("<FromUserName><![CDATA["+msg.getToUserName()+"]]></FromUserName>\n");
        returnStr.append("<CreateTime>"+(new Date().getTime())/1000+"</CreateTime>\n");
        if ("news".equals(msg.getMsgType())){
            returnStr.append("<MsgType><![CDATA[news]]></MsgType>");
            returnStr.append("<ArticleCount>1</ArticleCount>");
            returnStr.append("<Articles>");
            returnStr.append("<item>");
            returnStr.append("<Title><![CDATA["+msg.getTitle()+"]]></Title> ");
            returnStr.append("<Description><![CDATA["+msg.getDescription()+"]]></Description>");
            returnStr.append("<PicUrl><![CDATA["+msg.getPicUrl()+"]]></PicUrl>");
            returnStr.append("<Url><![CDATA["+msg.getUrl()+"]]></Url>");
            returnStr.append("</item>\n" +
                    "</Articles>");
        }else {
            returnStr.append("<MsgType><![CDATA[text]]></MsgType>\n");
            returnStr.append("<Content><![CDATA[服务器可能发生了点错误？哎 "+e.getMessage()+"]]></Content>");
        }


        returnStr.append("</xml>");
        
        response.setContentType("text/xml");
        response.setCharacterEncoding("utf-8");
//        response.getWriter().write(returnStr.toString());
        
        return returnStr.toString();
    }
    
    
}
