package com.taotao.common.service.redis.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.taotao.common.service.redis.RedisService;

import redis.clients.jedis.JedisCluster;

public class RedisClusterService implements RedisService {
	
	//表示该对象不是必须的，如果在ioc容器存在则注入
	@Autowired(required = false)
	private JedisCluster jedisCluster;

	@Override
	public String set(String key, String value) {
		return jedisCluster.set(key, value);
	}

	@Override
	public String setex(String key, int seconds, String value) {
		return jedisCluster.setex(key, seconds, value);
	}

	@Override
	public Long expire(String key, int seconds) {
		return jedisCluster.expire(key, seconds);
	}

	@Override
	public String get(String key) {
		return jedisCluster.get(key);
	}

	@Override
	public Long del(String key) {
		return jedisCluster.del(key);
	}

	@Override
	public Long incr(String key) {
		return jedisCluster.incr(key);
	}

	@Override
	public Long hset(String key, String field, String value) {
		return jedisCluster.hset(key, field, value);
	}

	@Override
	public String hget(String key, String field) {
		return jedisCluster.hget(key, field);
	}

	@Override
	public List<String> hvals(String key) {
		return jedisCluster.hvals(key);
	}

	@Override
	public Long hdel(String key, String field) {
		return jedisCluster.hdel(key, field);
	}

}
