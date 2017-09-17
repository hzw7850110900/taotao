package com.taotao.manage.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.taotao.manage.mapper.TestMapper;
import com.taotao.manage.service.TestService;

@Service
public class TestServiceImpl implements TestService {
	
	@Autowired
	private TestMapper testMapper;

	@Override
	public String queryCurrentDate() {
		return testMapper.queryCurrentDate();
	}

}
