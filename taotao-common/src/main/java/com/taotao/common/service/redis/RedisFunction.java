package com.taotao.common.service.redis;

public interface RedisFunction<T, E> {

	public T callback(E jedis);
}
