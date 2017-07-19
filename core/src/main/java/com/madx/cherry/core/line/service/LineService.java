package com.madx.cherry.core.line.service;

import com.madx.cherry.core.common.CommonCode;
import com.madx.cherry.core.common.CommonUtil;
import com.madx.cherry.core.common.dao.SysUserDao;
import com.madx.cherry.core.common.entity.SysUserPO;
import com.madx.cherry.core.line.bean.LineLogPO;
import com.madx.cherry.core.line.bean.LineProjectPO;
import com.madx.cherry.core.line.dao.LineLogDao;
import com.madx.cherry.core.line.dao.LineProjectDao;
import com.madx.cherry.core.wechat.bean.Result;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * {
 * "cherry": {
 * "javaFile": "49",
 * "javaLine": "4074"
 * },
 * "hehe": {
 * "javaFile": "802",
 * "javaLine": "118765"
 * },
 * "java_learn": {
 * "javaFile": "188",
 * "javaLine": "10775"
 * },
 * "user": "alexmdx"
 * }
 * Created by a-mdx on 2017/7/18.
 */
@Service
public class LineService {

    @Autowired
    private LineLogDao logDao;
    @Autowired
    private LineProjectDao projectDao;
    @Autowired
    private SysUserDao userDao;

    @Transactional
    public Result create(Map<String, Object> data) {
        Result result = Result.instance();
        String userName = (String) data.get("user");
        CommonUtil.validArgumentEmpty(userName, "用户名不可为空.");

        SysUserPO user = userDao.findByLoginNameAndStatus(userName, CommonCode.VALID_TRUE);
        CommonUtil.validArgumentEmpty(user, "根据这个用户名查不到用户："+userName);

        data.remove("user");

        data.forEach((k, v) -> {
            if (v instanceof Map){
                // 处理数据
                LineProjectPO projectPO = projectDao.findByProject(k);

                Map<String, String> map = (Map<String, String>) v;
                Integer line = Integer.valueOf(map.get("javaLine"));
                Integer file = Integer.valueOf(map.get("javaFile"));
                if (projectPO == null){
                    // 则 新建
                    projectPO = new LineProjectPO();
                    projectPO.setCreationTime(new Date());
                    projectPO.setUser(userName);
                    projectPO.setProject(k);

                    projectPO.setFile(file);
                    projectPO.setLine(line);

                    projectPO.setStatus(CommonCode.VALID_TRUE);

                    projectPO = projectDao.save(projectPO);

                    // 新建日志
                    LineLogPO logPO = new LineLogPO();
                    logPO.setCreationTime(new Date());
                    logPO.setNowFile(file);
                    logPO.setNowLine(line);
                    logPO.setProjectId(projectPO.getId());
                    logPO.setThanLastFile(file);
                    logPO.setThanLastLine(line);

                    logDao.save(logPO);

                }else {
                    // 先新增日志
                    LineLogPO logPO = new LineLogPO();
                    logPO.setProjectId(projectPO.getId());
                    logPO.setNowLine(line);
                    logPO.setNowFile(file);
                    logPO.setCreationTime(new Date());

                    logPO.setThanLastLine(line - projectPO.getLine());
                    logPO.setThanLastFile(file - projectPO.getFile());

                    logDao.save(logPO);

                    // 更新
                    projectPO.setModifyTime(new Date());
                    projectPO.setLine(line);
                    projectPO.setFile(file);

                    projectDao.save(projectPO);

                }
            }
        });

        return result;
    }

    /**
     * 获取这个用户的所有 java 信息， 哎，还这个用户，基本就我了。
     * @param userName
     * @return
     */
    public Document getUserInfo(String userName){
        Document info = new Document();
        info.append("userName", userName);
        List<LineProjectPO> projects = projectDao.findByUserAndStatus(userName, CommonCode.VALID_TRUE);

        List<Document> list = new ArrayList<>();
        int file = 0;
        int line = 0;
        for (LineProjectPO p : projects) {
            file += p.getFile();
            line += p.getLine();
            // todo .......
        }

        return info;
    }
}
