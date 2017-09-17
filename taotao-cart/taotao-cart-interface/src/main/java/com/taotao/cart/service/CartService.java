package com.taotao.cart.service;

import java.util.List;

import com.taotao.cart.pojo.Cart;
import com.taotao.manage.pojo.Item;

public interface CartService {

	/**
	 * 将商品加入到redis中
	 * @param item 商品
	 * @param num 购买数量
	 * @param userId 购买用户id
	 * @throws Exception 
	 */
	void addCart(Item item, Integer num, Long userId) throws Exception;

	
	/**
	 * 获取redis中的用户对应的购物车数据
	 * @param userId 用户id
	 * @return
	 * @throws Exception 
	 */
	List<Cart> getCarListByUserId(Long userId) throws Exception;

	/**
	 * 修改购买商品的购买数量
	 * @param num 购买数量
	 * @param itemId 购买商品id
	 * @param userId 用户id
	 * @throws Exception 
	 */
	void updateCartNumByItemIdAndUserId(Integer num, Long itemId, Long userId) throws Exception;

	/**
	 * 删除用户对应的购物车商品数据
	 * @param itemId 商品id
	 * @param userId 用户id
	 */
	void deleteCartByItemIdAndUserId(Long itemId, Long userId);

}
