package com.taotao.manage.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.taotao.common.vo.DataGridResult;
import com.taotao.manage.pojo.Content;
import com.taotao.manage.service.ContentService;

@RequestMapping("/content")
@Controller
public class ContentController {

	@Autowired
	private ContentService contentService;

	/**
	 * 根据内容分类id查询该分类下的分页内容
	 * 
	 * @param categoryId
	 *            内容分类id
	 * @return
	 */
	@RequestMapping(method = RequestMethod.GET)
	public ResponseEntity<DataGridResult> queryContentListByCategoryId(
			@RequestParam(value = "categoryId", defaultValue = "0") Long categoryId,
			@RequestParam(value = "page", defaultValue = "1") Integer page,
			@RequestParam(value = "rows", defaultValue = "20") Integer rows) {

		try {
			DataGridResult dataGridResult = contentService.queryContentListByCategoryId(categoryId, page, rows);

			return ResponseEntity.ok(dataGridResult);
		} catch (Exception e) {
			e.printStackTrace();
		}

		// 响应状态为500
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
	}

	/**
	 * 新增内容
	 * @param content 内容
	 * @return
	 */
	@RequestMapping(method = RequestMethod.POST)
	public ResponseEntity<Void> saveContent(Content content){
		try {
			contentService.saveSelective(content);
			
			return ResponseEntity.ok(null);
		} catch (Exception e) {
			e.printStackTrace();
		}
		// 响应状态为500
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
	}
	
	/**
	 * 更新内容
	 * @param content 内容
	 * @return
	 */
	@RequestMapping(value="/edit", method = RequestMethod.POST)
	public ResponseEntity<Void> updateContent(Content content){
		try {
			contentService.updateSelective(content);
			
			return ResponseEntity.ok(null);
		} catch (Exception e) {
			e.printStackTrace();
		}
		// 响应状态为500
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
	}
	
	/**
	 * 批量删除内容
	 * @param ids 内容id集合
	 * @return
	 */
	@RequestMapping(value="/delete", method = RequestMethod.POST)
	public ResponseEntity<Map<String, Object>> deleteContentByIds(@RequestParam(value="ids", required = false)Long[] ids){
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("status", 500);
		try {
			if(ids != null) {
				contentService.deleteByIds(ids);
			}
			result.put("status", 200);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		// 响应状态为500
		return ResponseEntity.ok(result);
	}
}
