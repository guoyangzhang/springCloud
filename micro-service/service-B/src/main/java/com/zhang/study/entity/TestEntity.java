package com.zhang.study.entity;

import lombok.Data;

import java.io.Serializable;

/**
 * @Author: Mr.ZHANG
 * @Date: 2018/12/13 0013 上午 9:46
 */
@Data
public class TestEntity implements Serializable {

    private static final long serialVersionUID = -7076820073976669086L;
    private String id;

    private String name;

    private String sex;
}
