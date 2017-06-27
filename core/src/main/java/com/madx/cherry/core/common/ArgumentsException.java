package com.madx.cherry.core.common;

/**
 * 自定义异常，用于统一处理该类问题
 * 以前的代码总是不往外抛异常，现在则全面利用这种处理方式，直接往外抛
 * Created by A-mdx on 2017/6/24.
 */
public class ArgumentsException extends RuntimeException {
    
    public ArgumentsException(String msg){
        super(msg);
    }
    
}
