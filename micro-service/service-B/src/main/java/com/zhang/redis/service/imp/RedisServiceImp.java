package com.zhang.redis.service.imp;

import com.zhang.redis.service.RedisService;
import com.zhang.utils.RedisUntils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * @Author: Mr.ZHANG
 * @Date: 2018/12/18 0018 上午 9:40
 */
@Service
public class RedisServiceImp implements RedisService {

    @Resource
    private RedisUntils redisUntils;

    @Override
    public void setValue() {
        redisUntils.del("zhang");
        redisUntils.set("zhang", "123");
    }

    @Override
    public void setList() {
        redisUntils.del("name");
        redisUntils.setList("name", list());
    }

    @Override
    public void redisList() {
        for (int i = 0;i< list().size();i++) {
            redisUntils.set("name_"+i,list().get(i));
        }
    }

    @Override
    public void redisListKey() {
        Set<String> list = redisUntils.getKeys("name_*");
        System.out.println(list);
    }

    public List<String> list() {

        List<String> list = new ArrayList<>();
        list.add("zhang");
        list.add("wang");
        list.add("li");
        list.add("huang");
        return list;
    }


}
