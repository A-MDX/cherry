package com.madx.wechat.initme.web;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * Create by A-mdx at 2018-03-17 12:02
 */
@RestController
public class InitMeController {
    
    @RequestMapping("hi")
    public Map<String, Object> hello(){
        Map<String, Object> result = new HashMap<>();
        result.put("hi", "你好。");
        result.put("ai", "你知道什么是绝望吗？");
        result.put("who", "你喜欢在雨天里打滚吗？");
        return result;
    }
    
}
