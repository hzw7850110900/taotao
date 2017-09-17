package com.taotao.manage.service;

import com.taotao.manage.pojo.ContentCategory;

public interface ContentCategoryService extends BaseService<ContentCategory> {

	/**
	 * 保存新增的内容分类，而且需要去更新父节点
	 * @param contentCategory 内容分类
	 * @return
	 */
	ContentCategory saveContentCategory(ContentCategory contentCategory);

	/**
	 * 删除内容分类节点及其子孙节点；更新其父节点
	 * @param contentCategory
	 */
	void deleteContentCategory(ContentCategory contentCategory);

}
