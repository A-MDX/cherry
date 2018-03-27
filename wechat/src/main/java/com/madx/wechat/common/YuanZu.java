package com.madx.wechat.common;

/**
 * 返回两个类型数据
 * Create by A-mdx at 2018-03-27 14:04
 */
public class YuanZu <A, B>{
    public final A a;
    public final B b;
    
    public static <A, B> YuanZu init(A a, B b){
        return new YuanZu(a, b);
    }
    
    public YuanZu(A a, B b){
        this.a = a;
        this.b = b;
    }
}
