package com.xl.example.redis;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Map;

@Component
public class RedisUtil {

    @Autowired
    private StringRedisTemplate redisTemplate;


    public String get(String key) {

        return redisTemplate.opsForValue().get(key);
    }

    public void put(String key, String value) {

        redisTemplate.opsForValue().set(key, value);
    }

    public Map getHash(String key) {
        if(key==null) {
            return null;
        }
        return redisTemplate.opsForHash().entries(key);
    }

    public void putHash(String key, Map<?, ?> map) {
        if (key == null || map == null) {
            return;
        }
        redisTemplate.opsForHash().putAll(key, map);
    }

    public void remove(Collection<String> keys) {
        redisTemplate.delete(keys);

    }

}
