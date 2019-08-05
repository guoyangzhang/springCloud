package com.zhang.login.entity;

import lombok.Data;

import java.io.Serializable;

/**
 * @Author: Mr.ZHANG
 * @Date: 2019/2/20 0020 下午 2:49
 */
@Data
public class LoginEntity implements Serializable {
    private static final long serialVersionUID = 7054059053732479433L;

    private String account;

    private String userName;

    private String password;

}
