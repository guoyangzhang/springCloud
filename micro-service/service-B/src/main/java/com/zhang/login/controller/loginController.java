package com.zhang.login.controller;

import com.zhang.login.entity.LoginEntity;
import com.zhang.login.service.LoginServer;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @Author: Mr.ZHANG
 * @Date: 2019/2/20 0020 下午 2:46
 */
@RestController
@RequestMapping("login")
public class loginController {

    @Resource
    private LoginServer loginServer;

    @RequestMapping(value = "/search", method = RequestMethod.POST)
    public List<LoginEntity> search(HttpServletRequest httpServletRequest, @RequestBody LoginEntity login) {
             return  loginServer.search(login);
    }
}
