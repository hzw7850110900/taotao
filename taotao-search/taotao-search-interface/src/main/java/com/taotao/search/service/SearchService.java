package com.taotao.search.service;

import java.util.List;

import com.taotao.common.vo.DataGridResult;
import com.taotao.search.pojo.SolrItem;

public interface SearchService {

	/**
	 * 批量新增或者更新solr中数据
	 * @param solrItemList
	 * @throws Exception 
	 */
	void addOrUpdateSolrItemList(List<SolrItem> solrItemList) throws Exception;

	/**
	 * 根据搜索关键字到solr中分页查询商品数据
	 * @param keyWords 搜索关键字
	 * @param page 页号
	 * @param rows 页大小
	 * @return
	 * @throws Exception 
	 */
	DataGridResult search(String keyWords, Integer page, Integer rows) throws Exception;

	/**
	 * 新增或者更新solr中数据
	 * @param solrItem
	 * @throws Exception 
	 */
	void addOrUpdateSolrItem(SolrItem solrItem) throws Exception;

	/**
	 * 根据商品id将solr的商品索引数据删除
	 * @param itemId
	 * @throws Exception 
	 */
	void deleteSolrItemByItemId(Long itemId) throws Exception;

}
