package com.taotao.search.activemq;

import javax.jms.JMSException;
import javax.jms.MapMessage;
import javax.jms.Message;
import javax.jms.Session;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.listener.adapter.AbstractAdaptableMessageListener;

import com.taotao.manage.pojo.Item;
import com.taotao.manage.service.ItemService;
import com.taotao.search.pojo.SolrItem;
import com.taotao.search.service.SearchService;

/**
 *如果是新增或者更新的话，根据商品id查询商品的具体信息，然后将商品转为solr可以接收的对象并更新到solr;
 *如果是删除则根据商品id将solr的商品索引数据删除
 */
public class ItemMessageListener extends AbstractAdaptableMessageListener {
	
	@Autowired
	private ItemService itemService;
	@Autowired
	private SearchService searchService;

	@Override
	public void onMessage(Message message, Session session) throws JMSException {
		try {
			if(message instanceof MapMessage) {
				//1、获取信息
				MapMessage mapMessage = (MapMessage)message;
				//操作类型
				String type = mapMessage.getString("type");
				//商品id
				Long itemId = mapMessage.getLong("itemId");
				
				//2、判断类型进行
				if(!"delete".equals(type)) {
					//2.1、如果是新增或者更新的话，根据商品id查询商品的具体信息，然后将商品转为solr可以接收的对象并更新到solr
					//2.1.1、根据商品id查询商品
					Item item = itemService.queryById(itemId);
					
					//2.1.2、转换商品为solrItem
					SolrItem solrItem = new SolrItem();
					solrItem.setId(item.getId());
					solrItem.setTitle(item.getTitle());
					solrItem.setImage(item.getImage());
					solrItem.setPrice(item.getPrice());
					solrItem.setSellPoint(item.getSellPoint());
					solrItem.setStatus(item.getStatus());
					
					//2.1.3、更新
					searchService.addOrUpdateSolrItem(solrItem);
				} else {
					//2.2、如果是删除则根据商品id将solr的商品索引数据删除
					searchService.deleteSolrItemByItemId(itemId);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
