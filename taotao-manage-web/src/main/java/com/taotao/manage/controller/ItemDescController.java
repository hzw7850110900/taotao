package com.taotao.manage.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.taotao.common.vo.DataGridResult;
import com.taotao.manage.pojo.ItemDesc;
import com.taotao.manage.service.ItemDescService;

@RequestMapping("/item/desc")
@Controller
public class ItemDescController {

	@Autowired
	private ItemDescService itemDescService;

	/**
	 * 根据商品id查询商品描述信息
	 * @param itemId 商品id
	 * @return
	 */
	@RequestMapping(value="/{itemId}", method = RequestMethod.GET)
	public ResponseEntity<ItemDesc> queryItemDescByItemId(@PathVariable Long itemId) {
		
		try {
			ItemDesc desc = itemDescService.queryById(itemId);
			
			return ResponseEntity.ok(desc);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		// 响应状态为500
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
	}

	
}
