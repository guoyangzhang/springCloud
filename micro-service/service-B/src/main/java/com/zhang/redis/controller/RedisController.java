package com.zhang.redis.controller;

import com.zhang.redis.service.RedisService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @Author: Mr.ZHANG
 * @Date: 2018/12/18 0018 上午 9:36
 */

@RestController
@RequestMapping("redis")
public class RedisController {

    @Resource
    private RedisService redisService;

    @RequestMapping(value = "/set", method = RequestMethod.POST)
    public void add() {
        redisService.setValue();
    }

    @RequestMapping(value = "/setList", method = RequestMethod.POST)
    public void setList() {
        redisService.setList();
    }
    @RequestMapping(value = "/redisList", method = RequestMethod.POST)
    public void redisList() {
        redisService.redisList();
    }

    @RequestMapping(value = "/redisListKey", method = RequestMethod.POST)
    public void redisListKey() {
        redisService.redisListKey();
    }
}
