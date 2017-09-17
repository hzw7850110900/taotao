package com.taotao.manage.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.taotao.manage.pojo.ContentCategory;
import com.taotao.manage.pojo.ItemDesc;
import com.taotao.manage.service.ContentCategoryService;

@RequestMapping("/content/category")
@Controller
public class ContentCategoryController {

	@Autowired
	private ContentCategoryService contentCategoryService;

	/**
	 * 根据内容分类id查询该分类下的所有子分类
	 * 
	 * @param parentId
	 *            内容分类id
	 * @return
	 */
	@RequestMapping(method = RequestMethod.GET)
	public ResponseEntity<List<ContentCategory>> queryContentCategoryListByParentId(
			@RequestParam(value = "id", defaultValue = "0") Long parentId) {

		try {
			ContentCategory contentCategory = new ContentCategory();
			contentCategory.setParentId(parentId);

			List<ContentCategory> list = contentCategoryService.queryByWhere(contentCategory);

			return ResponseEntity.ok(list);
		} catch (Exception e) {
			e.printStackTrace();
		}

		// 响应状态为500
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
	}

	/**
	 * 将内容分类保存到数据库中
	 * @param contentCategory 内容分类
	 * @return
	 */
	@RequestMapping(value = "/add", method = RequestMethod.POST)
	public ResponseEntity<ContentCategory> saveContentCategory(ContentCategory contentCategory) {
		try {
			ContentCategory result = contentCategoryService.saveContentCategory(contentCategory);
			return ResponseEntity.ok(result);
		} catch (Exception e) {
			e.printStackTrace();
		}
		// 响应状态为500
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
	}
	
	/**
	 * 将内容分类id更加id重命名节点的名称
	 * @param contentCategory 内容分类
	 * @return
	 */
	@RequestMapping(value = "/update", method = RequestMethod.POST)
	public ResponseEntity<Void> updateContentCategory(ContentCategory contentCategory) {
		try {
			contentCategoryService.updateSelective(contentCategory);
			
			return ResponseEntity.ok(null);
		} catch (Exception e) {
			e.printStackTrace();
		}
		// 响应状态为500
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
	}
	
	/**
	 * 将内容分类删除
	 * @param contentCategory 内容分类
	 * @return
	 */
	@RequestMapping(value = "/delete", method = RequestMethod.POST)
	public ResponseEntity<Void> deleteContentCategory(ContentCategory contentCategory) {
		try {
			contentCategoryService.deleteContentCategory(contentCategory);
			
			return ResponseEntity.ok(null);
		} catch (Exception e) {
			e.printStackTrace();
		}
		// 响应状态为500
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
	}
}
