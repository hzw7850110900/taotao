package com.taotao.sso.controller;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.taotao.sso.service.UserService;

@RequestMapping("/user")
@Controller
public class UserController {

	@Autowired
	private UserService userService;

	/**
	 * 检查 param 对应的数据是否可用；需要支持jsop
	 * 
	 * @param param
	 *            要校验的数据
	 * @param type
	 *            数据类型；其值为1、2、3分别代表username、phone、email
	 * @return
	 */
	@RequestMapping(value = "/check/{param}/{type}", method = RequestMethod.GET)
	public ResponseEntity<String> check(@PathVariable String param, @PathVariable Integer type,
			@RequestParam(value="callback", required=false)String callback) {
		try {
			if (0 < type && type < 4) {
				Boolean bool = userService.check(param, type);
				String result = bool.toString();
				
				if(StringUtils.isNotBlank(callback)) {
					//需要支持jsonp
					result = callback + "(" + result + ");";
				}

				// 如果操作成功的话，返回200
				return ResponseEntity.ok(result);
			} else {
				// 如果数据类型不合法，返回400
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		// 如果失败返回500
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
	}

	/**
	 * 根据ticket到redis查询用户json格式字符串并返回，需要支持jsonp
	 * @param ticket 用户登录标识符
	 * @return
	 */
	@RequestMapping("/{ticket}")
	public ResponseEntity<String> queryUserByTicket(@PathVariable String ticket, @RequestParam(value="callback", required=false)String callback) {
		try {
			String userJsonStr = "";
			if (StringUtils.isNotBlank(ticket)) {
				userJsonStr = userService.queryUserStrByTicket(ticket);
				
				if(StringUtils.isNotBlank(callback)) {
					//需要支持jsonp
					userJsonStr = callback + "(" + userJsonStr + ");";
				}
			}
			// 如果操作成功的话，返回200
			return ResponseEntity.ok(userJsonStr);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		// 如果失败返回500
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
	}
}
