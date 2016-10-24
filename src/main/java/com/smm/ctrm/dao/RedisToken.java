package com.smm.ctrm.dao;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.stereotype.Repository;
import org.springframework.util.Assert;

import com.smm.ctrm.domain.Token;


/**
 * 
 * @author zengshihua
 *
 */
public class RedisToken extends RedisTokenDao<String, Token> {

	private Logger logger=Logger.getLogger(this.getClass());
	
	/**
	 * 添加对象
	 * 
	 */
	public boolean add(final Token token) {
		logger.info("准备将token缓存志redis:"+token.toString());
		boolean result = redisTemplate.execute(new RedisCallback<Boolean>() {
			public Boolean doInRedis(RedisConnection connection)
					throws DataAccessException {
				RedisSerializer<String> serializer = getRedisSerializer();
				byte[] key = serializer.serialize(token.getKey());
				byte[] name = serializer.serialize(token.getToken());
				connection.setNX(key, name);//缓存Token到redis
				return connection.expire(key, token.getExpire());//设置失效时间
			}
		});
		logger.info("缓存结果："+result);
		return result;
	}
	
	/**
	 * 单独设置超时间
	 */
	public boolean expire(final String keyId,final long time) {
		logger.info("单独设置超时间 key:"+keyId+" time"+time);
		boolean result = redisTemplate.execute(new RedisCallback<Boolean>() {
			public Boolean doInRedis(RedisConnection connection)
					throws DataAccessException {
				RedisSerializer<String> serializer = getRedisSerializer();
				byte[] key = serializer.serialize(keyId);
				return connection.expire(key,time);//设置失效时间
			}
		});
		logger.info("单独设置超时间结果："+result);
		return result;
	}

	/**
	 * 添加集合
	 */
	public boolean add(final List<Token> list) {
		Assert.notEmpty(list);
		boolean result = redisTemplate.execute(new RedisCallback<Boolean>() {
			public Boolean doInRedis(RedisConnection connection)
					throws DataAccessException {
				RedisSerializer<String> serializer = getRedisSerializer();
				for (Token token : list) {
					byte[] key = serializer.serialize(token.getKey());
					byte[] name = serializer.serialize(token.getToken());
					connection.setNX(key, name);
				}
				return true;
			}
		}, false, true);
		return result;
	}

	/**
	 * 删除对象 ,依赖key
	 */
	public void delete(String key) {
		List<String> list = new ArrayList<String>();
		list.add(key);
		delete(list);
	}

	/**
	 * 删除集合 ,依赖key集合
	 */
	public void delete(List<String> keys) {
		redisTemplate.delete(keys);
	}

	/**
	 * 修改对象
	 */
	public boolean update(final Token token) {
		String key = token.getKey();
		if (get(key) == null) {
			throw new NullPointerException("数据行不存在, key = " + key);
		}
		boolean result = redisTemplate.execute(new RedisCallback<Boolean>() {
			public Boolean doInRedis(RedisConnection connection)
					throws DataAccessException {
				RedisSerializer<String> serializer = getRedisSerializer();
				byte[] key = serializer.serialize(token.getKey());
				byte[] tokenVal = serializer.serialize(token.getToken());
				connection.set(key, tokenVal);
				return true;
			}
		});
		return result;
	}

	/**
	 * 根据key获取对象
	 */
	public Token get(final String keyId) {
		logger.info("根据key获取对象:"+keyId);
		Token result = redisTemplate.execute(new RedisCallback<Token>() {
			public Token doInRedis(RedisConnection connection)
					throws DataAccessException {
				RedisSerializer<String> serializer = getRedisSerializer();
				byte[] key = serializer.serialize(keyId);
				byte[] value = connection.get(key);
				if (value == null) {
					logger.info("返回结果:"+null);
					return null;
				}
				
				String token = serializer.deserialize(value);
				logger.info("返回结果token:"+token);
				return new Token(keyId, token,connection.ttl(key));
			}
		});
		
		return result;
	}

}