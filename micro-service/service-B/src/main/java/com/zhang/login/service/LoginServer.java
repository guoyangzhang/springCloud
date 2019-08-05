package com.zhang.login.service;

import com.zhang.login.entity.LoginEntity;

import java.util.List;

/**
 * @Author: Mr.ZHANG
 * @Date: 2019/2/20 0020 下午 2:57
 */
public interface LoginServer {

    List<LoginEntity> search (LoginEntity loginEntity);
}
