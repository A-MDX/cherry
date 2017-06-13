package com.madx.cherry.javaline.dao;

import com.madx.cherry.common.util.SqlUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.sql.Types;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by A-mdx on 2016/12/26.
 */
@Component
public class EatJdbcDao {
    
    @Autowired
    private JdbcTemplate jdbcTemplate;

    public List<Map<String,Object>> queryMemuList(Map<String,Object> param,boolean isPage){
        List<Object> objList = new ArrayList<>();
        List<Integer> intList = new ArrayList<>();

        StringBuilder sql = new StringBuilder("SELECT\n");

        if (isPage){
            sql.append("  m.`id`,\n" +
                    "  m.`name`,\n" +
                    "  m.`address`,\n" +
                    "  m.`max_dian`,\n" +
                    "  m.`now_dian`,\n" +
                    "  m.`picture`,\n" +
                    "  t.`name` type,\n" +
                    "  c.`code_name` status_str,\n" +
                    "  m.`remark`,\n" +
                    "  m.`status`,\n" +
                    "  DATE_FORMAT(\n" +
                    "    m.`creation_time`,\n" +
                    "    '%Y-%m-%d %H:%i:%s'\n" +
                    "  ) creation_time,\n" +
                    "  DATE_FORMAT(\n" +
                    "    m.`modify_time`,\n" +
                    "    '%Y-%m-%d %H:%i:%s'\n" +
                    "  ) modify_time \n");
        }else{
            sql.append("  COUNT(1) count\n");
        }

        sql.append("FROM\n" +
                "  eat_memu m \n" +
                "  LEFT JOIN eat_type t \n" +
                "    ON m.`type` = t.`id` \n" +
                "  LEFT JOIN fix_code c \n" +
                "    ON m.`status` = c.`code` \n" +
                "WHERE 1 = 1  \n");

        // max_dian_than_big
        if (SqlUtil.isNotNull(param,"max_dian_than_big",objList)){
            sql.append("  AND m.`max_dian` >= ? \n");
            intList.add(Types.INTEGER);
        }

        // max_dian_than_small
        if (SqlUtil.isNotNull(param,"max_dian_than_small",objList)){
            sql.append("  AND m.`max_dian` <= ? \n");
            intList.add(Types.INTEGER);
        }

        // now_dian_than_big
        if (SqlUtil.isNotNull(param,"now_dian_than_big",objList)){
            sql.append("  AND m.`now_dian` >= ? \n");
            intList.add(Types.INTEGER);
        }

        // now_dian_than_small
        if (SqlUtil.isNotNull(param,"now_dian_than_small",objList)){
            sql.append("  AND m.`now_dian` <= ? \n");
            intList.add(Types.INTEGER);
        }

        // status
        if (SqlUtil.isNotNull(param,"status",objList)){
            sql.append("  AND m.`status` = ? \n");
            intList.add(Types.INTEGER);
        }

        // name
        Object name = param.get("name");
        if (name != null && StringUtils.isNotBlank(name.toString())){
            sql.append("  AND m.`name` LIKE ?\n");
            objList.add("%"+name.toString().trim()+"%");
            intList.add(Types.VARCHAR);
        }
        
        // 

        if (isPage){
            sql.append("ORDER BY m.`creation_time` DESC \n" +
                    "LIMIT ?, ?");
            intList.add(Types.INTEGER);
            objList.add(param.get("pageNumber"));
            intList.add(Types.INTEGER);
            objList.add(param.get("pageSize"));
        }

        System.out.println("EatJdbcDao -> queryMemuList : \n"+sql);

        return jdbcTemplate.queryForList(sql.toString(),objList.toArray(),SqlUtil.convertIntArr(intList));
    }
    
    public List<Map<String,Object>> queryTypeList(Map<String,Object> param,boolean isPage){
        List<Object> objList = new ArrayList<>();
        List<Integer> intList = new ArrayList<>();

        StringBuilder sql = new StringBuilder("SELECT\n");
        if (isPage){
            sql.append("  e.`id`,\n" +
                    "  e.`name`,\n" +
                    "  f.`code_name`,\n" +
                    "  e.`max_dian`,\n" +
                    "  e.`now_dian`,\n" +
                    "  e.`picture`,\n" +
                    "  e.`remark`,\n" +
                    "  DATE_FORMAT(\n" +
                    "    e.`creation_time`,\n" +
                    "    '%Y-%m-%d %H:%i:%s'\n" +
                    "  ) creation_time,\n" +
                    "  DATE_FORMAT(\n" +
                    "    e.`modify_time`,\n" +
                    "    '%Y-%m-%d %H:%i:%s'\n" +
                    "  ) modify_time \n");
        }else{
            sql.append("  COUNT(1) count\n");
        }

        sql.append("FROM\n" +
                "  eat_type e \n" +
                "  LEFT JOIN fix_code f \n" +
                "    ON f.`code` = e.`status` \n" +
                "WHERE 1 = 1 \n");
        
        // max_dian_than_big
        if (SqlUtil.isNotNull(param,"max_dian_than_big",objList)){
            sql.append("  AND e.`max_dian` >= ? \n");
            intList.add(Types.INTEGER);
        }
        
        // max_dian_than_small
        if (SqlUtil.isNotNull(param,"max_dian_than_small",objList)){
            sql.append("  AND e.`max_dian` <= ? \n");
            intList.add(Types.INTEGER);
        }
        
        // now_dian_than_big
        if (SqlUtil.isNotNull(param,"now_dian_than_big",objList)){
            sql.append("  AND e.`now_dian` >= ? \n");
            intList.add(Types.INTEGER);
        }
        
        // now_dian_than_small
        if (SqlUtil.isNotNull(param,"now_dian_than_small",objList)){
            sql.append("  AND e.`now_dian` <= ? \n");
            intList.add(Types.INTEGER);
        }
        
        // status
        if (SqlUtil.isNotNull(param,"status",objList)){
            sql.append("  AND e.`status` = ? \n");
            intList.add(Types.INTEGER);
        }

        // name
        Object name = param.get("name");
        if (name != null && StringUtils.isNotBlank(name.toString())){
            sql.append("  AND m.`name` LIKE ?\n");
            objList.add("%"+name.toString().trim()+"%");
            intList.add(Types.VARCHAR);
        }
        
        
        if (isPage){
            sql.append("ORDER BY e.`creation_time` \n" +
                    "LIMIT ?, ?");
            intList.add(Types.INTEGER);
            objList.add(param.get("pageNumber"));
            intList.add(Types.INTEGER);
            objList.add(param.get("pageSize"));
        }

        System.out.println("EatJdbcDao -> queryTypeList : \n"+sql);
        
        return jdbcTemplate.queryForList(sql.toString(),objList.toArray(),SqlUtil.convertIntArr(intList));
    }
    
}
