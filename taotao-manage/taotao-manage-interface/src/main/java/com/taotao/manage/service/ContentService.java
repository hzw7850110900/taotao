package com.taotao.manage.service;

import com.taotao.common.vo.DataGridResult;
import com.taotao.manage.pojo.Content;

public interface ContentService extends BaseService<Content> {

	/**
	 * 根据内容分类id查询该分类下的分页内容
	 * 
	 * @param categoryId
	 *            内容分类id
	 * @param page 页号
	 * @param rows 页大小
	 * @return
	 */
	DataGridResult queryContentListByCategoryId(Long categoryId, Integer page, Integer rows);

	/**
	 * 获取门户系统需要的6条大广告数据
	 * @return
	 * @throws Exception 
	 */
	String getPortalBigAdData() throws Exception;

	
}
