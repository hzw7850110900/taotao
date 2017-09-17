package com.taotao.portal.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.taotao.cart.pojo.Cart;
import com.taotao.manage.pojo.Item;
import com.taotao.manage.service.ItemService;
import com.taotao.portal.util.CookieUtils;

@Service
public class CookieCartService {

	// 购物车数据存放在cookie中的名称
	private static final String COOKIE_CART_NAME = "TT_CART";

	private static final ObjectMapper MAPPER = new ObjectMapper();
	// 购物车数据存放在cookie中的最大过期时间
	private static final int COOKIE_CART_MAX_AGE = 60 * 60 * 24 * 7;

	@Autowired
	private ItemService itemService;

	public void addCart(Long itemId, Integer num, HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		// 获取cookie中的购物车列表
		List<Cart> cartList = getCartList(request);
		// 是否处理了 加入购物车
		boolean isDeal = false;
		// 如果该商品已经存在则数据量叠加
		if (cartList != null && cartList.size() > 0) {
			for (Cart cart : cartList) {
				if (cart.getItemId().equals(itemId)) {
					cart.setNum(cart.getNum() + num);
					cart.setUpdated(new Date());
					isDeal = true;
					break;
				}
			}
		}
		// 如果商品不存在则新增
		if (!isDeal) {
			if (cartList == null) {
				cartList = new ArrayList<Cart>();
			}
			// 获取商品
			Item item = itemService.queryById(itemId);
			Cart cart = new Cart();
			cart.setItemId(item.getId());
			cart.setItemTitle(item.getTitle());
			cart.setItemPrice(item.getPrice());
			cart.setNum(num);
			if (item.getImages() != null) {
				cart.setItemImage(item.getImages()[0]);
			}
			cart.setCreated(new Date());
			cart.setUpdated(cart.getCreated());

			cartList.add(cart);
		}

		// 将购物车列表写回cookie
		CookieUtils.setCookie(request, response, COOKIE_CART_NAME, MAPPER.writeValueAsString(cartList),
				COOKIE_CART_MAX_AGE, true);
	}

	public List<Cart> getCartList(HttpServletRequest request) throws Exception {
		String cartListStr = CookieUtils.getCookieValue(request, COOKIE_CART_NAME, true);
		
		if (StringUtils.isNotBlank(cartListStr)) {
			return MAPPER.readValue(cartListStr,
					MAPPER.getTypeFactory().constructCollectionType(List.class, Cart.class));
		}
		return null;
	}

	public void updateCartNumByItemId(Integer num, Long itemId, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		// 获取cookie中的购物车列表
		List<Cart> cartList = getCartList(request);
		if (cartList != null && cartList.size() > 0) {
			for (Cart cart : cartList) {
				if (cart.getItemId().equals(itemId)) {
					cart.setNum(num);
					cart.setUpdated(new Date());
					break;
				}
			}
			// 将购物车列表写回cookie
			CookieUtils.setCookie(request, response, COOKIE_CART_NAME, MAPPER.writeValueAsString(cartList),
					COOKIE_CART_MAX_AGE, true);
		}
	}

	public void deleteCartByItemId(Long itemId, HttpServletRequest request, HttpServletResponse response) throws Exception {
		// 获取cookie中的购物车列表
		List<Cart> cartList = getCartList(request);
		if (cartList != null && cartList.size() > 0) {
			Iterator<Cart> iterator = cartList.iterator();
			while(iterator.hasNext()) {
				Cart cart = iterator.next();
				if(cart.getItemId().equals(itemId)) {
					cartList.remove(cart);
					break;
				}
			}
		}
		
		if(cartList != null && cartList.size() > 0) {
			// 将购物车列表写回cookie
			CookieUtils.setCookie(request, response, COOKIE_CART_NAME, MAPPER.writeValueAsString(cartList),
					COOKIE_CART_MAX_AGE, true);
		} else {
			CookieUtils.deleteCookie(request, response, COOKIE_CART_NAME);
		}
		
	}

}
