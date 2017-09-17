package com.taotao.portal.controller;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.taotao.portal.util.CookieUtils;
import com.taotao.sso.pojo.User;
import com.taotao.sso.service.UserService;

@RequestMapping("/user")
@Controller
public class UserController {

	//登录标识符ticket存放在cookie中的名称
	public static final String COOKIE_TICKET = "TT_TICKET";
	//登录标识符ticket存放在cookie中的最大生存时间
	private static final int COOKIE_TICKET_MAX_AGE = 3600;
	@Autowired
	private UserService userService;
	
	@RequestMapping(value="/logout", method = RequestMethod.GET)
	public String logout(HttpServletRequest request, HttpServletResponse response) {
		//TODO 1、根据ticket删除单点登录系统中存储的用户信息
		//2、删除cookie中的ticket
		CookieUtils.deleteCookie(request, response, COOKIE_TICKET);
		//3、重定向到门户系统首页
		return "redirect:/index.html";
	}
	
	/**
	 * 将用户注册信息保存到单点登录系统
	 * @param user 用户信息
	 * @return
	 */
	@RequestMapping(value="/doRegister", method = RequestMethod.POST)
	public ResponseEntity<Map<String, Object>> register(User user){
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("status", 500);//200表示成功
		
		try {
			userService.register(user);
			result.put("status", 200);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return ResponseEntity.ok(result);
	}
	
	/**
	 * 根据用户名和密码到单点登录系统查询用户，如果登录成功则返回一个登录成功的标识符ticket，将ticket存入cookie中
	 * @param user 用户信息
	 * @return
	 */
	@RequestMapping(value="/doLogin", method = RequestMethod.POST)
	public ResponseEntity<Map<String, Object>> login(User user,
			HttpServletRequest request, HttpServletResponse response){
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("status", 500);//200表示成功
		
		try {
			String ticket = userService.login(user);
			if(StringUtils.isNotBlank(ticket)) {
				//登录成功；将ticket存入cookie
				CookieUtils.setCookie(request, response, COOKIE_TICKET, ticket, COOKIE_TICKET_MAX_AGE);
				result.put("status", 200);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return ResponseEntity.ok(result);
	}
	
}
