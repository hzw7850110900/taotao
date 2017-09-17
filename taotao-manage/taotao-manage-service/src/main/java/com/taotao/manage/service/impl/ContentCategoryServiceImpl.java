package com.taotao.manage.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.taotao.manage.mapper.ContentCategoryMapper;
import com.taotao.manage.pojo.ContentCategory;
import com.taotao.manage.service.ContentCategoryService;

@Service
public class ContentCategoryServiceImpl extends BaseServiceImpl<ContentCategory> implements ContentCategoryService {
	
	@Autowired
	private ContentCategoryMapper contentCategoryMapper;

	@Override
	public ContentCategory saveContentCategory(ContentCategory contentCategory) {
		//1、保存节点
		contentCategory.setSortOrder(100);
		contentCategory.setIsParent(false);
		saveSelective(contentCategory);
		
		//2、更新父节点为父节点（更新isParent为true）
		ContentCategory parent = new ContentCategory();
		parent.setId(contentCategory.getParentId());
		parent.setIsParent(true);
		updateSelective(parent);
		
		return contentCategory;
	}

	@Override
	public void deleteContentCategory(ContentCategory contentCategory) {
		//1、删除该节点及其子孙节点
		List<Long> ids = new ArrayList<>();
		ids.add(contentCategory.getId());
		
		//获取该节点的子孙节点
		getCategoryIds(contentCategory, ids);
		
		deleteByIds(ids.toArray(new Long[]{}));
		
		//2、更新该节点父节点：如果父节点没有其它任何子节点了那么需要将该父节点更新为一个叶子节点
		ContentCategory param = new ContentCategory();
		param.setParentId(contentCategory.getParentId());
		int count = queryCountByWhere(param);
		if(count == 0) {
			//该节点已经没有任何兄弟节点，也就是父节点已经没有任何子节点，所以需要更新父节点为叶子节点
			ContentCategory parent = new ContentCategory();
			parent.setId(contentCategory.getParentId());
			parent.setIsParent(false);
			updateSelective(parent);
		}
		
	}

	/**
	 * 获取contentCategory对应的所有子孙节点
	 * @param contentCategory 内容分类节点
	 * @param ids 内容分类集合
	 */
	private void getCategoryIds(ContentCategory contentCategory, List<Long> ids) {
		
		ContentCategory param = new ContentCategory();
		param.setParentId(contentCategory.getId());
		//查询所有直接子节点
		List<ContentCategory> list = queryByWhere(param);
		if(list != null && list.size() > 0) {
			for (ContentCategory cc : list) {
				ids.add(cc.getId());
				getCategoryIds(cc, ids);
			}
		}
	}

}
