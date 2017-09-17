package com.taotao.manage.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.taotao.manage.mapper.ItemDescMapper;
import com.taotao.manage.pojo.ItemDesc;
import com.taotao.manage.service.ItemDescService;

@Service
public class ItemDescServiceImpl extends BaseServiceImpl<ItemDesc> implements ItemDescService {
	
	@Autowired
	private ItemDescMapper itemDescMapper;

}
