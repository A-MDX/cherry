package com.madx.wechat.initme.web;

import com.madx.wechat.common.Result;
import com.madx.wechat.initme.dao.InitQuestionDao;
import com.madx.wechat.initme.entity.InitQuestionPo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

/**
 * Create by A-mdx at 2018-03-17 12:02
 */
@RestController
@RequestMapping("initMe")
public class InitMeController {
    
    @RequestMapping("hi")
    public Map<String, Object> hello(){
        Map<String, Object> result = new HashMap<>();
        result.put("hi", "你好。");
        result.put("ai", "你知道什么是绝望吗？");
        result.put("who", "你喜欢在雨天里打滚吗？");
        return result;
    }

    @Autowired
    private InitQuestionDao initQuestionDao;
    
    @RequestMapping(value = "{pageNum}/{pageSize}", method = RequestMethod.GET)
    public Object queryPage(HttpServletResponse response, @PathVariable int pageNum, @PathVariable int pageSize
            , String uuid){
        return initQuestionDao.findAll(PageRequest.of(pageNum, pageSize));
    }
    
    @GetMapping("{questionId}")
    public Object queryOne(HttpServletResponse response, @PathVariable String questionId){
        return initQuestionDao.findById(questionId);
    }
    
    @PutMapping("{questionId}")
    public Result updateOne(HttpServletResponse response, @PathVariable String questionId, @RequestBody InitQuestionPo po){
        initQuestionDao.save(po);
        return Result.instance();
    }

    @PostMapping()
    public Result insertOne(HttpServletResponse response, @RequestBody InitQuestionPo po){
        InitQuestionPo questionPo = initQuestionDao.save(po);
        Result result = Result.instance();
        result.setData(questionPo.getId());
        return result;
    }
    
}
