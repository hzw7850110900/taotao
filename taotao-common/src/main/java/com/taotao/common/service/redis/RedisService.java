package com.taotao.common.service.redis;

import java.util.List;

public interface RedisService {

	/**
	 * 设置字符串类型的key
	 * @param key 键
	 * @param value 值
	 * @return
	 */
	public String set(String key, String value);
	
	/**
	 * 设置字符串类型的key并且设置该键在redis中的生存时间为seconds对应的秒钟
	 * @param key 键
	 * @param seconds 生存时间，单位为秒
	 * @param value 值
	 * @return
	 */
	public String setex(String key, int seconds, String value);
	
	/**
	 * 设置已经存在的键在redis中的生存时间为seconds对应的秒钟
	 * @param key 键
	 * @param seconds 生存时间秒
	 * @return
	 */
	public Long expire(String key, int seconds);
	
	/**
	 * 获取键对应的值
	 * @param key 键
	 * @return
	 */
	public String get(String key);
	
	/**
	 * 删除redis中的键
	 * @param key 键名
	 * @return
	 */
	public Long del(String key);
	
	/**
	 * 递增，默认步长为1
	 * @param key 键
	 * @return
	 */
	public Long incr(String key);
	
	//设置散列类型数据
	public Long hset(String key, String field, String value);
	
	//获取散列类型数据
	public String hget(String key, String field);
	
	//获取散列的所有值集合
	public List<String> hvals(String key);
	
	//删除散列的某个域
	public Long hdel(String key, String field);
}
