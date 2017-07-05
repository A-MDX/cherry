package com.madx.cherry.core.common;

import com.google.gson.Gson;

/**
 * Created by a-mdx on 2017/7/5.
 */
public class CommonUtil {

    public static Gson gson = new Gson();
    
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
