package com.madx.cherry.core.shiro.web;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by a-mdx on 2017/8/1.
 */
@RestController
public class PermissionController {

    @RequiresRoles("admin")
    @RequestMapping("eat")
    public Map<String, Object> eat(){
        Map<String, Object> map = new HashMap<>();
        map.put("eat", "apple");
        map.put("some", "hhe");
        map.put("shiro", "good");
        return map;
    }

    @RequiresPermissions("drink")
    @RequestMapping("drink")
    public Map<String, Object> drink(){
        Map<String, Object> map = new HashMap<>();
        map.put("eat", "banana");
        map.put("some", "fuck");
        map.put("shiro", "nice");
        return map;
    }

}
