package com.madx.cherry.javaline.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * Created by A-mdx on 2017/6/11.
 */
@RestController
public class HiController {

    private static Logger logger = LoggerFactory.getLogger(HiController.class);

    @Value("${server.port}")
    private Integer port;
    
    @Value("${spring.application.name}")
    private String appName;
    
    @RequestMapping(value = "hi", method = RequestMethod.GET)
    public String hi(@RequestParam String name){
        logger.error("Hi, someone come here.");
        logger.error("Hello "+name+", I am "+appName+" from "+port+".");
        return "Hello "+name+", I am "+appName+" from "+port+".";
    }

    @RequestMapping(value = "api",method = RequestMethod.POST, consumes = "application/json")
    public Map<String, Object> receive(@RequestParam(name = "token", required = false) String token
            , @RequestBody Map<String, Object> json){
        System.out.println("token:"+token);
        json.forEach((k,v) -> System.out.println(k+" : "+v));
        return json;
    }
    
}
