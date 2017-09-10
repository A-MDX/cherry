package com.madx.cherry.core.line.web;

import com.madx.cherry.core.line.service.LineService;
import com.madx.cherry.core.wechat.bean.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * Created by a-mdx on 2017/7/18.
 */
@RestController
@RequestMapping("line")
public class LineController {

    @Autowired
    private LineService lineService;

    /**
     * 新增
     * @param data
     * @return
     */
    @RequestMapping(method = RequestMethod.POST)
    public Result create(@RequestBody Map<String, Object> data){
        return lineService.create(data);
    }



}
