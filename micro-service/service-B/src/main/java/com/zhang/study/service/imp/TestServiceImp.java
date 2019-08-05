package com.zhang.study.service.imp;

import com.zhang.study.common.Untils;
import com.zhang.study.entity.TestEntity;
import com.zhang.study.mapper.TestMapper;
import com.zhang.study.service.TestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Author: Mr.ZHANG
 * @Date: 2018/12/13 0013 上午 9:31
 */
@Service
public class TestServiceImp implements TestService {

    @Autowired
    private TestMapper mapper;
    @Override
    public void add(String value) {
        TestEntity testEntity = new TestEntity();
        testEntity.setId(Untils.getUUID());
        testEntity.setName(value);
        mapper.add(testEntity);
    }
}
