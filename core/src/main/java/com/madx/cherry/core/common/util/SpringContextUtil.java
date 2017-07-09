package com.madx.cherry.core.common.util;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import java.util.Locale;

/**
 * Created by A-mdx on 2017/7/6.
 */
@Component
public class SpringContextUtil implements ApplicationContextAware {

    private static ApplicationContext context = null;
    
    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.context = applicationContext;
    }

    public static <T> T getBean(String beanName){
        return (T) context.getBean(beanName);
    }

    public static String getMessage(String key){
        return context.getMessage(key, null, Locale.getDefault());
    }
    
}
