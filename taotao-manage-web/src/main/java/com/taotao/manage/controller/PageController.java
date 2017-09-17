package com.taotao.manage.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 转发到具体的页面
 */
@RequestMapping("/page")
@Controller
public class PageController {

	@RequestMapping("/{pageName}")
	public String toPage(@PathVariable String pageName) {
		return pageName;
	}
}
