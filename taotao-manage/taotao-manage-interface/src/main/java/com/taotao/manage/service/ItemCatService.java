package com.taotao.manage.service;

import java.util.List;

import com.taotao.manage.pojo.ItemCat;

public interface ItemCatService extends BaseService<ItemCat> {

	/**
	 * 分页查询商品类目列表
	 * @param page 页号
	 * @param rows 页大小
	 * @return
	 */
	@Deprecated
	List<ItemCat> queryItemCatListByPage(Integer page, Integer rows);
	
}
