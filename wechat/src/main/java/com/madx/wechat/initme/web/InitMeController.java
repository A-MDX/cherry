package com.madx.wechat.initme.web;

import com.madx.wechat.common.Result;
import com.madx.wechat.common.YuanZu;
import com.madx.wechat.initme.dao.InitAnswerDao;
import com.madx.wechat.initme.dao.InitQuestionDao;
import com.madx.wechat.initme.dao.InitYouDao;
import com.madx.wechat.initme.entity.InitQuestionPo;
import com.madx.wechat.initme.service.InitService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
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
    public Map<String, Object> hello() {
        Map<String, Object> result = new HashMap<>();
        result.put("hi", "你好。");
        result.put("ai", "你知道什么是绝望吗？");
        result.put("who", "你喜欢在雨天里打滚吗？");
        return result;
    }

    @Autowired
    private InitQuestionDao initQuestionDao;
    @Autowired
    private InitYouDao initYouDao;
    @Autowired
    private InitAnswerDao initAnswerDao;
    @Autowired
    private InitService initService;

    @GetMapping("findAnswerByIndex")
    public Result findAnswerByIndex(@RequestParam int index, @RequestParam String uuid) {
        initService.checkUuid(uuid);
        return Result.instance().data(initAnswerDao.findAnswerByIndex(index));
    }

    @GetMapping("findByIndex")
    public Object findByIndex(@RequestParam int index, @RequestParam String uuid) {
        initService.checkUuid(uuid);
        return Result.instance().data(initQuestionDao.findByStatusAndIndex(1, index));
    }

    // 设置昵称与传递第一题
    @GetMapping("bindYou")
    public Object bindYou(@RequestParam String name) {
        String uuid = initService.bindYou(name);
        InitQuestionPo questionPo = initQuestionDao.findByStatusAndIndex(1, 1);
        return YuanZu.init(uuid, questionPo);
    }

    // 以往的答案
    @GetMapping("queryYouAnswer")
    public Result queryYouAnswer(@RequestParam String uuid, @RequestParam int index) {
        return initService.queryYouAnswer(uuid, index);
    }

    // 统计
    @GetMapping("queryQuesNum")
    public Result queryQuesNum() {
        return Result.instance().data(initQuestionDao.findCount());
    }

    // 自动下一题
    @PostMapping("answer")
    public Result answerMe(@RequestBody Map<String, Object> answer, @RequestParam String uuid) {
        return Result.instance().data(initService.answerMe(uuid, answer));
    }

    // 寻找下一个最大的下标
    @GetMapping("nextIndex")
    public Object nextIndex() {
        return Result.instance().data(initQuestionDao.findMaxIndex() + 1);
    }

    @RequestMapping(value = "{pageNum}/{pageSize}", method = RequestMethod.GET)
    public Object queryPage(HttpServletResponse response, @PathVariable int pageNum, @PathVariable int pageSize
            , @RequestParam String uuid) {
        initService.checkUuid(uuid);
        // 根据 index 排序
        return initQuestionDao.findAll(PageRequest.of(pageNum, pageSize, Sort.by("index")));
    }

    @GetMapping("{questionId}")
    public Object queryOne(HttpServletResponse response, @PathVariable String questionId) {
        return initQuestionDao.findById(questionId);
    }

    @PutMapping("{questionId}")
    public Result updateOne(HttpServletResponse response, @PathVariable String questionId
            , @RequestBody InitQuestionPo po, @RequestParam String uuid) {
        initService.checkUuid(uuid);
        initQuestionDao.save(po);
        return Result.instance();
    }

    @PostMapping()
    public Result insertOne(HttpServletResponse response, @RequestBody InitQuestionPo po) {
        InitQuestionPo questionPo = initQuestionDao.save(po);
        Result result = Result.instance();
        result.setData(questionPo.getId());
        return result;
    }

}
