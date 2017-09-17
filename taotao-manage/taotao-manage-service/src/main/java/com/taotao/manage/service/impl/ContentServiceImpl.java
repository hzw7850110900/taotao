package com.taotao.manage.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.taotao.common.service.redis.RedisService;
import com.taotao.common.vo.DataGridResult;
import com.taotao.manage.mapper.ContentMapper;
import com.taotao.manage.pojo.Content;
import com.taotao.manage.service.ContentService;

import tk.mybatis.mapper.entity.Example;

@Service
public class ContentServiceImpl extends BaseServiceImpl<Content> implements ContentService {
	
	@Value("${CONTENT_CATEGORY_BIG_AD_ID}")
	private Long CONTENT_CATEGORY_BIG_AD_ID;
	@Value("${TAOTAO_PORTAL_INDEX_BIG_AD_NUMBER}")
	private Integer TAOTAO_PORTAL_INDEX_BIG_AD_NUMBER;
	
	@Autowired
	private ContentMapper contentMapper;
	
	@Autowired
	private RedisService redisService;
	
	private static final ObjectMapper MAPPER = new ObjectMapper();
	
	//存放在redis中的key名称
	private static final String REDIS_BIG_AD_KEY = "TT_PORTAL_BIG_AD_DATA";
	//大广告的缓存生存时间；1周
	private static final int REDIS_BIG_AD_EXPIRE_TIME = 60*60*24*7;

	@Override
	public DataGridResult queryContentListByCategoryId(Long categoryId, Integer page, Integer rows) {
		//根据内容分类id分页查询该分类下的内容列表；并且更新时间降序排序
		Example example = new Example(Content.class);
		
		//设置查询
		example.createCriteria().andEqualTo("categoryId", categoryId);
		
		//更新时间降序排序
		example.orderBy("updated").desc();
		
		//设置分页
		PageHelper.startPage(page, rows);
		
		List<Content> list = contentMapper.selectByExample(example);
		
		//转换为分页信息对象
		PageInfo<Content> pageInfo = new PageInfo<>(list);
		return new DataGridResult(pageInfo.getTotal(), pageInfo.getList());
	}

	@Override
	public String getPortalBigAdData() throws Exception {
		
		try {
			//如果缓存中存在数据；则直接获取并返回
			String bigAdDataJsonStr = redisService.get(REDIS_BIG_AD_KEY);
			if(StringUtils.isNotBlank(bigAdDataJsonStr)) {
				return bigAdDataJsonStr;
			}
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		
		//1、根据内容分类id（大广告分类）查询分页6条数据
		DataGridResult dataGridResult = queryContentListByCategoryId(CONTENT_CATEGORY_BIG_AD_ID, 1, TAOTAO_PORTAL_INDEX_BIG_AD_NUMBER);
		List<Content> contentList = (List<Content>)dataGridResult.getRows();
		//2、转换数据为大广告数据结构
		List<Map<String, Object>> resultList = new ArrayList<>();
		if(contentList != null && contentList.size() >0) {
			Map<String, Object> map = null;
			for(Content c: contentList) {
				map = new HashMap<>();
				map.put("alt", c.getTitle());
				map.put("height", 240);
				map.put("heightB", 240);
				map.put("href", c.getUrl());
				map.put("src", c.getPic());
				map.put("srcB", c.getPic());
				map.put("width", 670);
				map.put("widthB", 550);
				resultList.add(map);
			}
		}
		//3、返回大广告json格式数据
		String resultJsonStr = MAPPER.writeValueAsString(resultList);
		
		try {
			//将返回的数据先存放到redis缓存中并且设置缓存的过期时间，1周
			redisService.setex(REDIS_BIG_AD_KEY, REDIS_BIG_AD_EXPIRE_TIME, resultJsonStr);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return resultJsonStr;
	}

}
