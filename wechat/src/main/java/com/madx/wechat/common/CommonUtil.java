package com.madx.wechat.common;

import com.google.gson.Gson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by a-mdx on 2017/7/5.
 */
public class CommonUtil {

    private static final Logger logger = LoggerFactory.getLogger(CommonUtil.class);
    
    public static Gson gson = new Gson();
    
    // 默认时间格式
    private static SimpleDateFormat defaultSdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");

    /**
     * 转换时间 -> str
     * @param date
     * @return
     */
    public static String transDate2Str(Date date){
        return defaultSdf.format(date);
    }
    
    public static Date transStr2Date(String str){
        if (StringUtils.isEmpty(str)){
            return new Date();
        }
        try {
            return defaultSdf.parse(str);
        } catch (ParseException e) {
            e.printStackTrace();
            logger.error("时间转换失败", e);
        }
        return null;
    }
    
    /**
     * 验证数据是否为空
     * @param obj
     * @param msg
     */
    public static void validArgumentEmpty(Object obj, String msg){
        if (obj == null || "".equals(obj) || "null".equals(obj)){
            throw new ArgumentsException(msg);
        }
    }

    /**
     * 将实体类或者对象全部转换为 json 形式的，便于观看
     * @param obj
     * @return
     */
    public static String toString(Object obj){
        return gson.toJson(obj);
    }
}
