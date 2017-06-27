package com.madx.cherry.core.wechat.web;

import com.madx.cherry.core.wechat.common.WechatConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by A-mdx on 2017/6/22.
 */
@RestController
public class HiController {
    
    @Autowired
    private WechatConfig wechatConfig;
    
    @RequestMapping(value = "hi", method = RequestMethod.GET)
    public String hi(){
        return "Hi, Mr.Ma.";
    }

    @RequestMapping(value = "he", method = RequestMethod.GET)
    public String he(){
        
        return "Hi, Mr.Ma."+"\n "+wechatConfig;
    }

}
