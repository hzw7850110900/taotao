package com.taotao.manage.service;

import com.taotao.common.vo.DataGridResult;
import com.taotao.manage.pojo.Item;

public interface ItemService extends BaseService<Item> {

	/**
	 * 保存商品基本信息和描述信息
	 * @param item 基本信息
	 * @param desc 描述信息
	 * @return
	 */
	Long saveItem(Item item, String desc);

	/**
	 * 更新商品基本信息和描述信息
	 * @param item 基本信息
	 * @param desc 描述信息
	 * @return
	 */
	void updateItem(Item item, String desc);

	/**
	 * 根据商品标题分页查询商品数据
	 * @param title 查询关键字
	 * @param page 页号
	 * @param rows 页大小
	 * @return
	 */
	DataGridResult queryItemListByTitleInPage(String title, Integer page, Integer rows);

}
