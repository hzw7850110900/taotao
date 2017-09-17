package com.taotao.sso.service.impl;

import java.io.IOException;
import java.util.Date;
import java.util.List;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.taotao.common.service.redis.RedisService;
import com.taotao.sso.mapper.UserMapper;
import com.taotao.sso.pojo.User;
import com.taotao.sso.service.UserService;

@Service
public class UserServiceImpl implements UserService {

	// 用户的登录标识符在redis中前缀
	private static final String REDIS_TICKET_PREFIX = "TT_TICKET_";

	@Autowired
	private UserMapper userMapper;
	@Autowired
	private RedisService redisService;

	private static final ObjectMapper MAPPER = new ObjectMapper();

	@Override
	public Boolean check(String param, Integer type) {
		// 如果该数据存在在数据库中，那么返回false，否则返回true

		User user = new User();
		switch (type) {
		case 1:
			user.setUsername(param);
			break;
		case 2:
			user.setPhone(param);
			break;

		default:
			user.setEmail(param);
			break;
		}

		int count = userMapper.selectCount(user);

		return count == 0;
	}

	@Override
	public String queryUserStrByTicket(String ticket) {
		// 根据ticket查询用户信息
		String key = REDIS_TICKET_PREFIX + ticket;
		String userJsonStr = redisService.get(key);
		// 每一次请求用户信息表示当前用户处于活跃状态，用户存储在redis中的信息的过期时间应该重置回为1小时
		if (StringUtils.isNoneBlank(userJsonStr)) {
			redisService.expire(key, 3600);
		}
		return userJsonStr;
	}

	@Override
	public void register(User user) {
		user.setCreated(new Date());
		user.setUpdated(user.getCreated());
		// 使用md5加密
		user.setPassword(DigestUtils.md5Hex(user.getPassword()));
		userMapper.insertSelective(user);
	}

	@Override
	public String login(User user) throws Exception {
		String ticket = "";
		// 1、根据用户名和密码查询用户
		user.setPassword(DigestUtils.md5Hex(user.getPassword()));
		// 如果不是主键就不能百分百保证唯一
		List<User> list = userMapper.select(user);
		// 2、获取用户
		if (list != null && list.size() > 0) {
			User tmp = list.get(0);
			// ticket应该要唯一
			ticket = DigestUtils.md5Hex(user.getUsername() + "" + System.currentTimeMillis());

			String key = REDIS_TICKET_PREFIX + ticket;
			// 3、将用户转为json字符串存储到redis中
			redisService.setex(key, 3600, MAPPER.writeValueAsString(tmp));

		}
		// 4、返回ticket
		return ticket;
	}

	@Override
	public User queryUserByTicket(String ticket) throws Exception {
		// 根据ticket查询用户信息
		String key = REDIS_TICKET_PREFIX + ticket;
		String userJsonStr = redisService.get(key);
		// 每一次请求用户信息表示当前用户处于活跃状态，用户存储在redis中的信息的过期时间应该重置回为1小时
		if (StringUtils.isNoneBlank(userJsonStr)) {
			redisService.expire(key, 3600);
			return MAPPER.readValue(userJsonStr, User.class);
		}
		return null;
	}

}
