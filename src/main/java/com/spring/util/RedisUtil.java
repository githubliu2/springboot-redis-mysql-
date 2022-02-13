package com.spring.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

@Service
public class RedisUtil {

	@Autowired
	private JedisPool jedisPool;
	
    public Jedis initRedis(int dbIndex){
        String methodId = "initRedis";
		 try {  
			if (jedisPool == null) {
				 throw new RuntimeException("redis init error");
			}
			// 从池里面获取一个连接
			Jedis jedis = jedisPool.getResource();
			if(dbIndex >=0){
				jedis.select(dbIndex);
			}
			return jedis;
		 }catch(Exception e){
			   throw new RuntimeException("redis init error");
		 }
    }
}
