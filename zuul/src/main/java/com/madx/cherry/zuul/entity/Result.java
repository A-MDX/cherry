package com.madx.cherry.zuul.entity;

import com.mongodb.BasicDBObject;

import java.util.Arrays;

/**
 * Created by A-mdx on 2017/6/11.
 */
public class Result {
    private int code;
    private String msg;
    private Object data;

    
    public static final int SUCCESS = 50;
    public static final int PARAM_ERROR = 0;
    public static final int SYSTEM_ERROR = 5;
    
    public static final String DEFAULT_MSG = "OK";
    
    public static Result getInstance(){
        Result result = new Result();
        result.setCode(SUCCESS);
        result.setMsg(DEFAULT_MSG);
        return result;
    }
    
    public String toJson(){
        BasicDBObject json = new BasicDBObject();
        json.append("code", code);
        json.append("msg", msg);
        json.append("data", data);
        return json.toJson(); 
    }

    @Override
    public String toString() {
        return "Result{" +
                "code=" + code +
                ", msg='" + msg + '\'' +
                ", data=" + data +
                '}';
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public static void main(String[] args) {
        Result result = Result.getInstance();
        result.setData(Arrays.asList(1,2,3,4,5));
        String json = result.toJson();
        System.out.println(json);
    }
}
