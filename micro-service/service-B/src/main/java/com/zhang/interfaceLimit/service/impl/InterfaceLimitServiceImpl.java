package com.zhang.interfaceLimit.service.impl;

import com.zhang.interfaceLimit.entity.InterfaceLimit;
import com.zhang.interfaceLimit.mapper.InterfaceLimitMapper;
import com.zhang.interfaceLimit.service.InterfaceLimitService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class InterfaceLimitServiceImpl implements InterfaceLimitService {
	
	@Autowired
	private InterfaceLimitMapper mapper;

	@Override
	public InterfaceLimit getEntityByPri(Integer id) {
		return mapper.selectByPrimaryKey(id);
	}

}
