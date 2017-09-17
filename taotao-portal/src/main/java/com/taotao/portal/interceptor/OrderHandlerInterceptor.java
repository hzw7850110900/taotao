package com.taotao.portal.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.taotao.portal.controller.UserController;
import com.taotao.portal.util.CookieUtils;
import com.taotao.sso.pojo.User;
import com.taotao.sso.service.UserService;

/**
 * 
 * 在操作任何订单相关的业务之前判断用户是否已经登录；
 * 根据ticket获取用户对象，如果存在则说明已经登录放行，
 * 如果不存在用户对象则说明没有登录则跳转到门户系统登录页面。
 *
 */
public class OrderHandlerInterceptor implements HandlerInterceptor {
	
	@Autowired
	private UserService userService;
	
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object obj) throws Exception {
		//获取cookie中的ticket
		String ticket = CookieUtils.getCookieValue(request, UserController.COOKIE_TICKET);
		User user = userService.queryUserByTicket(ticket);
		if(user != null) {
			//设置用户信息
			request.setAttribute("user", user);
			//已经登录；放行
			return true;
		}
		
		//获取访问地址
		String redirectUrl = request.getRequestURL().toString();
		
		//重定向到登录页面
		response.sendRedirect(request.getContextPath() + "/page/login.html?redirectUrl="+redirectUrl);
		
		return false;
	}


	@Override
	public void afterCompletion(HttpServletRequest arg0, HttpServletResponse arg1, Object arg2, Exception arg3)
			throws Exception {
	}

	@Override
	public void postHandle(HttpServletRequest arg0, HttpServletResponse arg1, Object arg2, ModelAndView arg3)
			throws Exception {
	}

	
}
