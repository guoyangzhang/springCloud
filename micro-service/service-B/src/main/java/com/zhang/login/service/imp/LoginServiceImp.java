package com.zhang.login.service.imp;

import com.zhang.login.entity.LoginEntity;
import com.zhang.login.mapper.LoginMapper;
import com.zhang.login.service.LoginServer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Author: Mr.ZHANG
 * @Date: 2019/2/20 0020 下午 3:03
 */
@Service
public class LoginServiceImp implements LoginServer {

    @Autowired
    private LoginMapper mapper;
    @Override
    public List<LoginEntity> search(LoginEntity loginEntity) {
     return   mapper.search(loginEntity);
    }
}
