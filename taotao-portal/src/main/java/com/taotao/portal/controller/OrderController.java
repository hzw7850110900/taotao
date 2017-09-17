package com.taotao.portal.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.taotao.cart.pojo.Cart;
import com.taotao.cart.service.CartService;
import com.taotao.order.pojo.Order;
import com.taotao.order.service.OrderService;
import com.taotao.sso.pojo.User;
import com.taotao.sso.service.UserService;

@RequestMapping("/order")
@Controller
public class OrderController {
	
	@Autowired
	private CartService cartService;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private OrderService orderService;
	
	/**
	 * 跳转到订单成功页面
	 * @param orderId 订单号
	 * @return
	 */
	@RequestMapping("/success")
	public ModelAndView toSuccessPage(@RequestParam("id")String orderId) {
		ModelAndView mv = new ModelAndView("success");
		
		try {
			//订单信息
			Order order = orderService.queryOrderByOrderId(orderId);
			mv.addObject("order", order);
			
			//送达时间；在订单创建时间的两天之后
			mv.addObject("date", DateTime.now().plusDays(2).toString("MM月dd日"));
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return mv;
	}
	
	/**
	 * 将订单结算页中确定的信息（订单基本信息、物流信息、订单商品信息）保存到订单系统
	 * @param order 订单信息
	 * @return
	 */
	@RequestMapping(value="/submit", method = RequestMethod.POST)
	public ResponseEntity<Map<String, Object>> submitOrder(Order order, HttpServletRequest request){
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("status", 500);
		
		try {
			User user = (User)request.getAttribute("user");
			order.setUserId(user.getId());
			order.setBuyerNick(user.getUsername());
			//订单号
			String orderId = orderService.saveOrder(order);
			if(StringUtils.isNotBlank(orderId)) {
				result.put("status", 200);
				result.put("data", orderId);
			}
			return ResponseEntity.ok(result);
		} catch (Exception e) {
			e.printStackTrace();
		}
		//返回500
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
	}

	/**
	 * 跳转到订单确定页面
	 * @return
	 */
	@RequestMapping("/create")
	public ModelAndView toOrderCartPage(HttpServletRequest request) {
		ModelAndView mv = new ModelAndView("order-cart");
		
		try {
			//获取cookie中的ticket
			/*String ticket = CookieUtils.getCookieValue(request, UserController.COOKIE_TICKET);
			User user = userService.queryUserByTicket(ticket);*/
			User user = (User)request.getAttribute("user");
			//加载当前登录用户的购物车数据
			List<Cart> carts = cartService.getCarListByUserId(user.getId());
			
			mv.addObject("carts", carts);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return mv;
	}
}
