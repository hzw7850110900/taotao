package com.taotao.sso.service;

import com.taotao.sso.pojo.User;

public interface UserService {

	/**
	 * 检查 param 对应的数据是否可用；
	 * 
	 * @param param 要校验的数据
	 * @param type 数据类型；其值为1、2、3分别代表username、phone、email
	 * @return
	 */
	Boolean check(String param, Integer type);

	/**
	 * 根据ticket到redis查询用户json格式字符串并返回
	 * @param ticket 用户登录标识符
	 * @return
	 */
	String queryUserStrByTicket(String ticket);

	/**
	 * 将用户注册信息保存到mysql数据库中
	 * @param user 用户信息
	 * @return
	 */
	void register(User user);

	/**
	 * 根据用户名和密码到mysql查询用户，如果登录成功则返回一个登录成功的标识符ticket
	 * @param user 用户信息
	 * @return 登录成功的标识符ticket
	 * @throws Exception 
	 */
	String login(User user) throws Exception;

	/**
	 * 根据ticket到redis查询用户
	 * @param ticket 用户登录标识符
	 * @return
	 * @throws Exception 
	 */
	User queryUserByTicket(String ticket) throws Exception;

}
