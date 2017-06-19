package com.madx.cherry.common.util;

import org.apache.commons.lang3.StringUtils;

import java.util.List;
import java.util.Map;

/**
 * Created by A-mdx on 2017/6/11.
 */
public class SqlUtil {
    /**
     *  转换 int集合 --> 数据
     *  一般用于jdbc中
     * @param intList
     * @return
     */
    public static int[] convertIntArr(List<Integer> intList){
        int size = intList.size();
        int[] arr = new int[size];
        for (int i = 0; i < size;i++){
            arr[i] = intList.get(i);
        }
        return arr;
    }

    /**
     * 判断是否为空，然后不为空，则顺便添加元素等
     * 一般用于 jdbc 中
     * @param param
     * @param colum
     * @param objList
     * @return
     */
    public static boolean isNotNull(Map<String,Object> param, String colum, List<Object> objList){
        Object obj = param.get(colum);
        if (obj != null && StringUtils.isNotBlank(obj.toString())){
            objList.add(obj);
            return true;
        }
        return false;
    }
}
