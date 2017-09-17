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
import org.springframework.web.bind.annotation.ResponseBody;

import com.taotao.manage.pojo.ItemCat;
import com.taotao.manage.pojo.ItemDesc;
import com.taotao.manage.service.ItemCatService;

@RequestMapping("/item/cat")
@Controller
public class ItemCatController {
	
	@Autowired
	private ItemCatService itemCatService;
	
	/**
	 * 根据商品类目id查询商品类目
	 * @param itemCatId 商品类目id
	 * @return
	 */
	@RequestMapping(value="/{itemCatId}", method = RequestMethod.GET)
	public ResponseEntity<ItemCat> queryItemCatByItemCatId(@PathVariable Long itemCatId) {
		
		try {
			ItemCat itemCat = itemCatService.queryById(itemCatId);
			
			return ResponseEntity.ok(itemCat);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		// 响应状态为500
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
	}

	
	@RequestMapping("/query/{page}")
	@ResponseBody
	public List<ItemCat> queryItemCatListByPage(@PathVariable Integer page, @RequestParam(value = "rows", defaultValue = "10")Integer rows){
		//return itemCatService.queryItemCatListByPage(page, rows);
		return itemCatService.queryByPage(page, rows);
	}
	
	/**
	 * 根据父节点id查询该节点的所有子节点
	 * @param parentId 父节点
	 * @return
	 */
	@RequestMapping(method = RequestMethod.GET)
	public ResponseEntity<List<ItemCat>> queryItemCatListByParentId(@RequestParam(value = "id", defaultValue="0")Long parentId){
		try {
			ItemCat itemCat = new ItemCat();
			itemCat.setParentId(parentId);
			
			List<ItemCat> list = itemCatService.queryByWhere(itemCat);
			
			//返回响应状态码为200，并输出的内容为list
			return ResponseEntity.ok(list);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		//如果查询失败，返回500，输出内容为空
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
	}

}
