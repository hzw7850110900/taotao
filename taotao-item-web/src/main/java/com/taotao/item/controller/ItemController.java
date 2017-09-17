package com.taotao.item.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.taotao.manage.service.ItemDescService;
import com.taotao.manage.service.ItemService;

@RequestMapping("/item")
@Controller
public class ItemController {
	
	@Autowired
	private ItemService itemService;
	@Autowired
	private ItemDescService itemDescService;

	/**
	 * 根据商品id查询商品基本信息和描述信息并转发到商品详情页面
	 * @param itemId 商品id
	 * @return
	 */
	@RequestMapping(value="/{itemId}", method = RequestMethod.GET)
	public ModelAndView toItemPage(@PathVariable Long itemId) {
		ModelAndView mv = new ModelAndView("item");
		//加载商品基本信息
		mv.addObject("item", itemService.queryById(itemId));
		//加载商品描述信息
		mv.addObject("itemDesc", itemDescService.queryById(itemId));
		return mv;
	}
}
