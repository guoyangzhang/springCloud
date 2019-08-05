package com.zhang.login.mapper;

import com.zhang.login.entity.LoginEntity;

import java.util.List;

/**
 * @Author: Mr.ZHANG
 * @Date: 2019/2/20 0020 下午 3:09
 */
public interface LoginMapper {

    List<LoginEntity> search(LoginEntity loginEntity);
}
