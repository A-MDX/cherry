package com.madx.wechat.common;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * Created by A-mdx on 2017/2/24.
 */
@Component
public class RedisDao {
    
    public static final String prefix_name = "com.madx.cherry.";
    
    @Autowired
    private StringRedisTemplate template;
    
    public String getVal(String key){
        return template.opsForValue().get(prefix_name+key); 
    }
    
    public void deleteVal(String key){
        template.delete(prefix_name+key);
    }
    
    public void addVal(String key, String val){
        template.opsForValue().set(prefix_name+key, val);
    }
    
    public void addSet(String key, Set<String> list, int time, TimeUnit timeType){
        template.opsForSet().add(prefix_name+key, genStrArray(list));
        template.expire(prefix_name+key, time, timeType);
    }
    
    public Set<String> getSet(String key){
        return template.opsForSet().members(prefix_name+key);
    }
    
    public void addMap(String key, Map<String, String> map, int time, TimeUnit timeType){
        template.opsForHash().putAll(prefix_name+key, map);
        template.expire(prefix_name+key, time, timeType);
    }
    
    public Map<Object, Object> getMap(String key){
        return template.opsForHash().entries(prefix_name+key);
    }
    
    public void addVal(String key, String val, int time, TimeUnit timeType){
        template.opsForValue().set(prefix_name+key, val, time, timeType);
    }
    
    public Set<String> getKeys(String name){
        String prefixStr = prefix_name+name; 
        Set<String> set = new HashSet<>();
        template.keys(prefixStr+"*").forEach(s -> {
            s = s.replace(prefixStr,"");
            set.add(s);
        });
        return set; 
        
    }
    
    private static String[] genStrArray(Set<String> set){
        int size = set.size();
        String[] arr = new String[size];
        int i = 0;
        for (String str : set){
            arr[i] = str;
            i++;
        }
        return arr;
    }
    
}
