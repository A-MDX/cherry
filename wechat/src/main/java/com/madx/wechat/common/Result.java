package com.madx.wechat.common;

/**
 * Created by a-mdx on 2017/6/21.
 */
public class Result {

    public static final int SUCCESS = 50;
    public static final int PARAM_ERROR = 0;
    public static final int SYSTEM_ERROR = 5;


    private int code;
    private String msg;
    private Object data;

    public static Result instance(){
        Result result = new Result();
        result.setCode(SUCCESS);
        result.setMsg("ok");
        return result;
    }

    @Override
    public String toString() {
        return "{ Result : "+ CommonUtil.toString(this)+" }";
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
}
