package com.taotao.search.service.impl;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.CloudSolrServer;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.taotao.common.vo.DataGridResult;
import com.taotao.search.pojo.SolrItem;
import com.taotao.search.service.SearchService;

@Service
public class SearchServiceImpl implements SearchService {
	
	@Autowired
	private CloudSolrServer cloudSolrServer;

	@Override
	public void addOrUpdateSolrItemList(List<SolrItem> solrItemList) throws Exception {
		cloudSolrServer.addBeans(solrItemList);
		cloudSolrServer.commit();
	}

	@Override
	public DataGridResult search(String keyWords, Integer page, Integer rows) throws Exception {
		if(StringUtils.isBlank(keyWords)) {
			keyWords = "*";
		}
		//创建查询对象
		SolrQuery solrQuery = new SolrQuery();
		solrQuery.setQuery("title:" + keyWords + " AND status:1");
		
		//设置分页
		solrQuery.setStart((page - 1)* rows);//起始索引号
		solrQuery.setRows(rows);//页大小
		
		//设置高亮
		boolean isHighlight = !"*".equals(keyWords);
		if(isHighlight) {
			solrQuery.setHighlight(true);
			solrQuery.addHighlightField("title");//高亮的域
			solrQuery.setHighlightSimplePre("<em>");//高亮的起始标签
			solrQuery.setHighlightSimplePost("</em>");//高亮的结束标签
		}
		
		//查询
		QueryResponse queryResponse = cloudSolrServer.query(solrQuery);
		
		//总记录数
		long total = queryResponse.getResults().getNumFound();
		
		//获取查询列表
		List<SolrItem> list = queryResponse.getBeans(SolrItem.class);
		
		if(isHighlight && list != null && list.size() > 0) {
			//获取高亮标题集合
			Map<String, Map<String, List<String>>> highlighting = queryResponse.getHighlighting();
			for (SolrItem solrItem : list) {
				solrItem.setTitle(highlighting.get(solrItem.getId().toString()).get("title").get(0));
			}
		}
		return new DataGridResult(total, list);
	}

	@Override
	public void addOrUpdateSolrItem(SolrItem solrItem) throws Exception {
		cloudSolrServer.addBean(solrItem);
		cloudSolrServer.commit();
	}

	@Override
	public void deleteSolrItemByItemId(Long itemId) throws Exception {
		cloudSolrServer.deleteById(itemId.toString());
		cloudSolrServer.commit();
	}

}
