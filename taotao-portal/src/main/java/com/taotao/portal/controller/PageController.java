package com.taotao.portal.controller;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

@RequestMapping("/page")
@Controller
public class PageController {

	@RequestMapping("/{pageName}")
	public ModelAndView toPage(@PathVariable String pageName, @RequestParam(value="redirectUrl", required = false)String redirectUrl) {
		ModelAndView mv = new ModelAndView(pageName);
		if(StringUtils.isNotBlank(redirectUrl)) {
			mv.addObject("redirectUrl", redirectUrl);
		}
		return mv;
	}
}
