package com.zhang.utils;


import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.List;
import java.util.Set;

/**
 * @Author: Mr.ZHANG
 * @Date: 2018/12/17 0017 下午 5:10
 *
 * 未设置响应返回值,true,false
 */
@Component
public class RedisUntils<K, V> {
    @Resource
    private RedisTemplate<K, V> redisTemplate;

    /**
     * 删除redis
     *
     * @param key
     */
    @SuppressWarnings("unchecked")
    public void del(K... key) {
        if (key != null && key.length > 0) {
            if (key.length == 1) {
                redisTemplate.delete(key[0]);
            } else {
                redisTemplate.delete(CollectionUtils.arrayToList(key));
            }
        }
    }

    /**
     * 获取redis
     *
     * @param key
     * @return
     */
    public Object get(K key) {
        return key == null ? null : redisTemplate.opsForValue().get(key);
    }

    /**
     * @param key
     * @param value
     * @return
     */
    public boolean set(K key, V value) {
        try {
            redisTemplate.opsForValue().set(key, value);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    //    放入list集合
    public boolean setList(K key, List<V> value) {
        try {
            redisTemplate.opsForList().rightPushAll(key, value);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    //获取所有的key
    public Set<K> getKeys(K value){
        return redisTemplate.keys(value);
    }


}
