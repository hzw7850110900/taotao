package com.taotao.portal.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.taotao.cart.pojo.Cart;
import com.taotao.cart.service.CartService;
import com.taotao.manage.pojo.Item;
import com.taotao.manage.service.ItemService;
import com.taotao.portal.service.CookieCartService;
import com.taotao.portal.util.CookieUtils;
import com.taotao.sso.pojo.User;
import com.taotao.sso.service.UserService;

@RequestMapping("/cart")
@Controller
public class CartController {
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private CartService cartService;
	
	@Autowired
	private CookieCartService cookieCartService;
	
	@Autowired
	private ItemService itemService;

	/**
	 * 将商品加入到购物车中，如果登录加入redis，如果未登录加入cookie
	 * @param itemId 商品id
	 * @param num 购买数量
	 * @return
	 */
	@RequestMapping(value = "/{itemId}/{num}", method = RequestMethod.GET)
	public String addCart(@PathVariable Long itemId, @PathVariable Integer num,
			HttpServletRequest request, HttpServletResponse response) {
		try {
			//判断是否已经登录：从cookie获取ticket，然后根据ticket到单点登录系统获取用户信息，如果存在用户信息说明已经登录
			String ticket = CookieUtils.getCookieValue(request, UserController.COOKIE_TICKET);
			User user = userService.queryUserByTicket(ticket);
			if(user != null) {//已登录；将数据存储到redis中
				Item item = itemService.queryById(itemId);
				cartService.addCart(item, num, user.getId());
			} else {//未登录；将数据存储到cookie
				cookieCartService.addCart(itemId, num, request, response);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		//重定向到购物车列表页面
		return "redirect:/cart/show.html";
	}
	
	/**
	 * 获取购物车数据
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/show", method = RequestMethod.GET)
	public ModelAndView toCartPage(HttpServletRequest request) {
		ModelAndView mv = new ModelAndView("cart");
		try {
			//判断是否已经登录：从cookie获取ticket，然后根据ticket到单点登录系统获取用户信息，如果存在用户信息说明已经登录
			String ticket = CookieUtils.getCookieValue(request, UserController.COOKIE_TICKET);
			User user = userService.queryUserByTicket(ticket);
			List<Cart> cartList = null;
			if(user != null) {//已登录；从redis中获取购物车列表数据
				cartList = cartService.getCarListByUserId(user.getId());
			} else {//未登录；从cookie中获取购物车数据
				cartList = cookieCartService.getCartList(request);
			}
			mv.addObject("cartList", cartList);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return mv;
	}
	
	/**
	 * 更新购买商品的购买数量
	 * @param itemId 商品id
	 * @param num 最新的购买数量
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/update/num/{itemId}/{num}", method = RequestMethod.POST)
	public ResponseEntity<Void> updateCartNum(@PathVariable Long itemId, @PathVariable Integer num,
			HttpServletRequest request, HttpServletResponse response) {
		try {
			//判断是否已经登录：从cookie获取ticket，然后根据ticket到单点登录系统获取用户信息，如果存在用户信息说明已经登录
			String ticket = CookieUtils.getCookieValue(request, UserController.COOKIE_TICKET);
			User user = userService.queryUserByTicket(ticket);
			if(user != null) {//已登录；修改redis中购买数量
				cartService.updateCartNumByItemIdAndUserId(num, itemId, user.getId());
			} else {//未登录；修改cookie中购买数量
				cookieCartService.updateCartNumByItemId(num, itemId, request, response);
			}
			return ResponseEntity.ok(null);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		//返回500
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
	}

	@RequestMapping(value = "/delete/{itemId}", method = RequestMethod.GET)
	public String delCart(@PathVariable Long itemId,
			HttpServletRequest request, HttpServletResponse response) {
		try {
			//判断是否已经登录：从cookie获取ticket，然后根据ticket到单点登录系统获取用户信息，如果存在用户信息说明已经登录
			String ticket = CookieUtils.getCookieValue(request, UserController.COOKIE_TICKET);
			User user = userService.queryUserByTicket(ticket);
			if(user != null) {//已登录；将数据存储到redis中
				cartService.deleteCartByItemIdAndUserId(itemId, user.getId());
			} else {//未登录；将数据存储到cookie
				cookieCartService.deleteCartByItemId(itemId, request, response);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		//重定向到购物车列表页面
		return "redirect:/cart/show.html";
	}
}
