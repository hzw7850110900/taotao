package com.taotao.item.activemq;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.jms.JMSException;
import javax.jms.MapMessage;
import javax.jms.Message;
import javax.jms.Session;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;

import com.taotao.manage.service.ItemDescService;
import com.taotao.manage.service.ItemService;

import freemarker.core.ParseException;
import freemarker.template.Configuration;
import freemarker.template.MalformedTemplateNameException;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import freemarker.template.TemplateNotFoundException;
/**
 * 
 * 如果是新增或者更新的话，根据商品id查询商品基本信息和描述信息然后与freemarker的商品模版进行结合输出html页面到指定路径，
 * 然后在浏览器中访问的时候可以结合nginx跳转到具体的html页面；
 * 如果是删除的话，那么将商品id对应的静态页面html从指定路径删除。
 *
 */
@Component
public class ItemMessageListener {
	
	@Value("${TAOTAO_ITEM_HTML_PATH}")
	private String TAOTAO_ITEM_HTML_PATH;
	@Autowired
	private FreeMarkerConfigurer freeMarkerConfigurer;
	@Autowired
	private ItemService itemService;
	@Autowired
	private ItemDescService itemDescService;

	@JmsListener(destination="topic.item")
	public void onMessage(Message message, Session session) throws JMSException {
		try {
			if(message instanceof MapMessage) {
				//1、获取消息
				MapMessage mapMessage = (MapMessage)message;
				String type = mapMessage.getString("type");//操作类型
				Long itemId = mapMessage.getLong("itemId");//商品id
				
				//2、处理消息
				
				if(!"delete".equals(type)) {
					//2.1、新增或更新商品；生成或更新静态页面
					Configuration configuration = freeMarkerConfigurer.getConfiguration();
					//获取模版
					Template template = configuration.getTemplate("item.ftl");
					
					//数据
					Map<String, Object> dataModel = new HashMap<String, Object>();
					dataModel.put("item", itemService.queryById(itemId));
					dataModel.put("itemDesc", itemDescService.queryById(itemId));
					
					//静态页面存放的地址
					String fileName = TAOTAO_ITEM_HTML_PATH + File.separator + itemId + ".html";
					//输出的地方
					FileWriter fileWriter = new FileWriter(new File(fileName));
					
					//数据+模版
					template.process(dataModel, fileWriter);
					
				}else {
					//2.2、删除商品
					//静态页面存放的地址
					String fileName = TAOTAO_ITEM_HTML_PATH + File.separator + itemId + ".html";
					File file = new File(fileName);
					if(file.exists()) {
						file.delete();
					}
				}
				
			}
		} catch (TemplateNotFoundException e) {
			e.printStackTrace();
		} catch (MalformedTemplateNameException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (TemplateException e) {
			e.printStackTrace();
		}
	}

}
