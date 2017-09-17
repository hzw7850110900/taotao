package com.taotao.search.test;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.taotao.manage.pojo.Item;
import com.taotao.manage.service.ItemService;
import com.taotao.search.pojo.SolrItem;
import com.taotao.search.service.SearchService;

/**
 * 将后台管理系统的所有商品数据导入到solr中
 * @author admin
 *
 */
public class TaotaoItemImport2SolrTest {
	
	private SearchService searchService;
	private ItemService itemService;

	@Before
	public void setup() {
		ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("classpath:spring/*.xml");
		searchService = context.getBean(SearchService.class);
		itemService = context.getBean(ItemService.class);
	}
	
	@Test
	public void test() throws Exception {
		int page = 1;
		int pageSize = 500;
		List<Item> itemList = null;
		List<SolrItem> solrItemList = null;
		SolrItem solrItem = null;
		do {
			//1、获取后台管理系统的商品数据
			itemList = itemService.queryByPage(page, pageSize);
			System.out.println("----------正在导入第" + page +"页...");
			if(itemList != null && itemList.size() > 0) {
				//2、转换为搜索系统可以接收的数据并保存到solr中
				solrItemList = new ArrayList<>();
				for (Item item : itemList) {
					solrItem = new SolrItem();
					solrItem.setId(item.getId());
					solrItem.setTitle(item.getTitle());
					solrItem.setImage(item.getImage());
					solrItem.setPrice(item.getPrice());
					solrItem.setSellPoint(item.getSellPoint());
					solrItem.setStatus(item.getStatus());
					solrItemList.add(solrItem);
				}
				
				//将数据提到到搜索系统
				searchService.addOrUpdateSolrItemList(solrItemList);
			}
			System.out.println("----------导入第" + page +"页完成。");
			page++;
			pageSize = itemList.size();
		} while(pageSize==500);
	}
	
}
