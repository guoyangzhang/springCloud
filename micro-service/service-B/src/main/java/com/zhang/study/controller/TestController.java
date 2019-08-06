package com.zhang.study.controller;

import com.zhang.study.service.TestService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @Author: Mr.ZHANG
 * @Date: 2018/12/12 0012 下午 4:45
 */
@RestController
@RequestMapping("study")
@Api(value = "用户controller", tags = {"用户操作接口"})
public class TestController {
    @Resource
    private TestService service;

    @RequestMapping(value = "/add", method = RequestMethod.POST)
    @ApiOperation(value = "测试新增", notes = "新增操作", httpMethod = "POST", response = void.class)
    public void add() {
        service.add("范");
    }

}
