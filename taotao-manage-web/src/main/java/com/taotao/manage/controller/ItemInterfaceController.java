package com.taotao.manage.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.taotao.manage.pojo.Item;
import com.taotao.manage.service.ItemService;

@RequestMapping("/item/interface")
@Controller
public class ItemInterfaceController {

	@Autowired
	private ItemService itemService;

	/**
	 * 新增商品
	 * 
	 * @param item
	 *            商品
	 * @return
	 */
	@RequestMapping(method = RequestMethod.POST)
	public ResponseEntity<Void> saveItem(Item item) {
		try {
			itemService.saveSelective(item);

			// 成功；返回对象创建成功，201
			return ResponseEntity.status(HttpStatus.CREATED).build();
		} catch (Exception e) {
			e.printStackTrace();
		}

		// 失败；返回状态为500
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
	}

	/**
	 * 根据商品id查询商品
	 * @param itemId 商品id
	 * @return
	 */
	@RequestMapping(value="/{itemId}", method = RequestMethod.GET)
	public ResponseEntity<Item> queryItemByItemId(@PathVariable Long itemId) {
		try {
			if(itemId > 0) {
				Item item = itemService.queryById(itemId);
				// 成功；返回200
				return ResponseEntity.ok(item);
			} else {
				// 资源没有找到；返回404
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		// 失败；返回状态为500
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
	}
	

	/**
	 * 更新商品
	 * 
	 * @param item
	 *            商品
	 * @return
	 */
	@RequestMapping(method = RequestMethod.PUT)
	public ResponseEntity<Void> updateItem(Item item) {
		try {
			itemService.updateSelective(item);

			// 成功；返回204
			return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
		} catch (Exception e) {
			e.printStackTrace();
		}

		// 失败；返回状态为500
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
	}
	
	
	/**
	 * 批量删除商品
	 * 
	 * @param ids
	 *            商品id 集合
	 * @return
	 */
	@RequestMapping(method = RequestMethod.DELETE)
	public ResponseEntity<Void> deleteItemByIds(@RequestParam(value="ids", required = false)Long[] ids) {
		try {
			if(ids != null && ids.length >0) {
				itemService.deleteByIds(ids);
			}
			// 成功；返回204
			return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		// 失败；返回状态为500
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
	}

}
