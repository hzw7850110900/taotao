package com.taotao.manage.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.taotao.manage.service.TestService;

@RequestMapping("/test")
@Controller
public class TestController {

	@Autowired
	private TestService testService;
	
	@RequestMapping("/date")
	@ResponseBody
	public String queryCurrentDate() {
		return testService.queryCurrentDate();
	}
}
