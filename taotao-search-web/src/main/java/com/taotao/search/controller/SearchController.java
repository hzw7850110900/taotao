package com.taotao.search.controller;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.taotao.common.vo.DataGridResult;
import com.taotao.search.service.SearchService;

@RequestMapping("/search")
@Controller
public class SearchController {
	
	//默认页大小
	private static final Integer DEFAULT_ROWS = 40;
	
	@Autowired
	private SearchService searchService;

	/**
	 * 根据搜索关键字到solr中分页查询商品数据
	 * @param keyWords 搜索关键字
	 * @param page 页号
	 * @return
	 */
	@RequestMapping(method = RequestMethod.GET)
	public ModelAndView search(@RequestParam(value="q", required = false)String keyWords, 
			@RequestParam(value="page", defaultValue="1")Integer page) {
		ModelAndView mv = new ModelAndView("search");
		try {
			//搜索关键字
			if(StringUtils.isNotBlank(keyWords)) {
				keyWords = new String(keyWords.getBytes("ISO-8859-1"), "utf-8");
			}
			mv.addObject("query", keyWords);
			
			//商品列表
			DataGridResult dataGridResult = searchService.search(keyWords, page, DEFAULT_ROWS);
			mv.addObject("itemList", dataGridResult.getRows());
			
			//页号
			mv.addObject("page", page);
			//总页数
			/**
			 *方法一：总页数 = （总记录数%页大小==0）？（总记录数/页大小）：（总记录数/页大小 + 1）
			 *方法二： 总页数 = (总记录数 + 页大小 - 1)/页大小
			 */
			long totalPages = (dataGridResult.getTotal() + DEFAULT_ROWS - 1)/DEFAULT_ROWS;
			
			mv.addObject("totalPages", totalPages);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return mv;
	}
}
