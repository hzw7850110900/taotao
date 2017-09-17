package com.taotao.cart.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.taotao.cart.pojo.Cart;
import com.taotao.cart.service.CartService;
import com.taotao.common.service.redis.RedisService;
import com.taotao.manage.pojo.Item;

@Service
public class CartServiceImpl implements CartService {
	//用户存储在redis中的购物车的key的前缀
	private static final String REDIS_CART_KEY = "TT_CART_";
	@Autowired
	private RedisService redisService;
	
	private static final ObjectMapper MAPPER = new ObjectMapper();
	
	@Override
	public void addCart(Item item, Integer num, Long userId) throws Exception {
		//将商品加入到redis中；如果该用户已经添加过该商品那么叠加购买数量；如果没有添加过该商品那么直接将该商品加入到redis中
		String key = REDIS_CART_KEY + userId;
		String field = item.getId().toString();
		String cartJsonStr = redisService.hget(key, field);
		Cart cart = null;//购物车商品
		if(StringUtils.isNotBlank(cartJsonStr)) {
			//如果该用户已经添加过该商品那么叠加购买数量
			cart = MAPPER.readValue(cartJsonStr, Cart.class);
			cart.setNum(cart.getNum() + num);
			cart.setUpdated(new Date());
		} else {
			//如果没有添加过该商品那么直接将该商品加入到redis中
			cart = new Cart();
			cart.setUserId(userId);
			cart.setItemId(item.getId());
			cart.setItemTitle(item.getTitle());
			cart.setItemPrice(item.getPrice());
			cart.setNum(num);
			if(item.getImages() != null) {
				cart.setItemImage(item.getImages()[0]);
			}
			cart.setCreated(new Date());
			cart.setUpdated(cart.getCreated());
		}
		
		//将最新的数据存回redis
		redisService.hset(key, field, MAPPER.writeValueAsString(cart));
	}

	@Override
	public List<Cart> getCarListByUserId(Long userId) throws Exception {
		String key = REDIS_CART_KEY + userId;
		List<Cart> cartList = new ArrayList<Cart>();
		List<String> cartJsonListStr = redisService.hvals(key);
		for (String cartJsonStr : cartJsonListStr) {
			cartList.add(MAPPER.readValue(cartJsonStr, Cart.class));
		}
		return cartList;
	}

	@Override
	public void updateCartNumByItemIdAndUserId(Integer num, Long itemId, Long userId) throws Exception {
		String key = REDIS_CART_KEY + userId;
		String field = itemId.toString();
		//查询商品
		String cartJsonStr = redisService.hget(key, field);
		
		if(StringUtils.isNotBlank(cartJsonStr)) {
			Cart cart = MAPPER.readValue(cartJsonStr, Cart.class);
			//修改商品的购买数量
			cart.setNum(num);
			cart.setUpdated(new Date());
			//保存回redis
			redisService.hset(key, field, MAPPER.writeValueAsString(cart));
		}
	}

	@Override
	public void deleteCartByItemIdAndUserId(Long itemId, Long userId) {
		//直接将redis中的商品删除
		String key = REDIS_CART_KEY + userId;
		String field = itemId.toString();
		redisService.hdel(key, field);
	}

}
