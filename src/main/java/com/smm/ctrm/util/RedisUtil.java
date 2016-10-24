package com.smm.ctrm.util;

import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

import com.smm.ctrm.hibernate.cache.SerializeUtil;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

public class RedisUtil {

	protected static Logger logger = Logger.getLogger(RedisUtil.class);

	// Redis服务器IP
	private static String ADDR_ARRAY = PropertiesUtil.getDataFromPropertiseFile("redis.host");

	// Redis的端口号
	private static String PORT = PropertiesUtil.getDataFromPropertiseFile("redis.port");

	// 可用连接实例的最大数目，默认值为8；
	// 如果赋值为-1，则表示不限制；如果pool已经分配了maxActive个jedis实例，则此时pool的状态为exhausted(耗尽)。
	private static String MAX_ACTIVE = (String) PropertiesUtil.getDataFromPropertiseFile("redis.maxActive");

	// 控制一个pool最多有多少个状态为idle(空闲的)的jedis实例，默认值也是8。
	private static String MAX_IDLE = PropertiesUtil.getDataFromPropertiseFile("redis.maxIdle");

	// 等待可用连接的最大时间，单位毫秒，默认值为-1，表示永不超时。如果超过等待时间，则直接抛出JedisConnectionException；
	private static String MAX_WAIT = PropertiesUtil.getDataFromPropertiseFile("redis.maxWait");

	// 超时时间
	private static String TIMEOUT = PropertiesUtil.getDataFromPropertiseFile("redis.timeout");

	// 在borrow一个jedis实例时，是否提前进行validate操作；如果为true，则得到的jedis实例均是可用的；
	private static String TEST_ON_BORROW = PropertiesUtil.getDataFromPropertiseFile("redis.testOnBorrow");

	private static JedisPool jedisPool = null;

	/**
	 * redis过期时间,以秒为单位
	 */
	public final static int EXRP_HOUR = 60 * 60; // 一小时
	public final static int EXRP_DAY = 60 * 60 * 24; // 一天
	public final static int EXRP_MONTH = 60 * 60 * 24 * 30; // 一个月

	/**
	 * 初始化Redis连接池
	 */
	private static void initialPool() {
		try {
			JedisPoolConfig config = new JedisPoolConfig();
			config.setMaxTotal(Integer.valueOf(MAX_ACTIVE));
			config.setMaxIdle(Integer.valueOf(MAX_IDLE));
			config.setMaxWaitMillis(Integer.valueOf(MAX_WAIT));
			config.setTestOnBorrow(Boolean.valueOf(TEST_ON_BORROW));
			config.setLifo(true);
			jedisPool = new JedisPool(config, ADDR_ARRAY.split(",")[0], Integer.valueOf(PORT),
					Integer.valueOf(TIMEOUT));
		} catch (Exception e) {
			logger.error("First create JedisPool error : " + e);
		}
	}

	/**
	 * 在多线程环境同步初始化
	 */
	private static synchronized void poolInit() {
		if (jedisPool == null) {
			initialPool();
		}
	}

	/**
	 * 同步获取Jedis实例
	 * 
	 * @return Jedis
	 */
	public synchronized static Jedis getJedis() {
		if (jedisPool == null) {
			poolInit();
		}
		Jedis jedis = null;
		try {
			if (jedisPool != null) {
				jedis = jedisPool.getResource();
			}
		} catch (Exception e) {
			logger.error("Get jedis error : " + e);
			if (jedis != null) {
				jedis.close();
			}
			return null;
		}

		return jedis;
	}

	/**
	 * 释放jedis资源
	 * 
	 * @param jedis
	 */
	public static void returnResource(final Jedis jedis) {
		if (jedis != null && jedisPool != null) {
			jedisPool.returnResource(jedis);
		}
	}

	/**
	 * 释放jedis资源
	 * 
	 * @param jedis
	 */
	public static void returnBrokenResource(final Jedis jedis) {
		if (jedis != null) {
			jedisPool.returnBrokenResource(jedis);
		}
	}

	/**
	 * 设置 String
	 * 
	 * @param key
	 * @param value
	 */
	public static void set(String key, String value) {
		Jedis jedis = null;
		try {
			value = StringUtils.isEmpty(value) ? "" : value;
			jedis = getJedis();
			jedis.set(key, value);

		} catch (Exception e) {
			logger.error("Set key error : " + e);
		} finally {
			if (jedis != null) {
				jedis.close();
			}
		}
	}

	public static void putObject(Object key, Object value) {
		Jedis jedis = null;
		try {
			jedis = getJedis();
			jedis.set(SerializeUtil.serialize(key.toString()), SerializeUtil.serialize(value));
		} catch (Exception e) {
			logger.error("Set key error : " + e);
		} finally {
			if (jedis != null) {
				jedis.close();
			}
		}
	}

	public static Object getObject(Object key) {
		Jedis jedis = null;
		Object value = null;
		try {
			jedis = getJedis();
			value = SerializeUtil.unserialize(jedis.get(SerializeUtil.serialize(key.toString())));
		} catch (Exception e) {
			logger.error("Set key error : " + e);
		} finally {
			if (jedis != null) {
				jedis.close();
			}
		}
		return value;
	}

	/**
	 * 设置 过期时间
	 * 
	 * @param key
	 * @param seconds
	 *            以秒为单位
	 * @param value
	 */
	public static void set(String key, String value, int seconds) {
		Jedis jedis = null;
		try {
			value = StringUtils.isEmpty(value) ? "" : value;
			jedis = getJedis();
			String flag = jedis.setex(key, seconds, value);
		} catch (Exception e) {
			logger.error("Set keyex error : " + e);
		} finally {
			if (jedis != null) {
				jedis.close();
			}
		}
	}

	/**
	 * 获取String值
	 * 
	 * @param key
	 * @return value
	 */
	public static String get(String key) {
		Jedis jedis = null;
		String result = null;
		try {
			jedis = getJedis();
			if (jedis == null || !jedis.exists(key)) {
				return null;
			}
			result = jedis.get(key);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (jedis != null) {
				jedis.close();
			}
		}

		return result;
	}

	/**
	 * 删除 0失败 1成功
	 * 
	 * @param key
	 * @return value
	 */
	public static Long delete(String key) {
		Jedis jedis = null;
		Long result = null;
		try {
			jedis = getJedis();
			if (jedis != null && jedis.exists(key)) {
				result = jedis.del(key);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (jedis != null) {
				jedis.close();
			}
		}
		return result;
	}
	
	/**
	 * 删除 0失败 1成功
	 * 
	 * @param key
	 * @return value
	 */
	public static Set<String> keys(final String pattern) {
		Jedis jedis = null;
		Set<String> result = null;
		try {
			jedis = getJedis();
			if (jedis != null) {
				result = jedis.keys(pattern);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (jedis != null) {
				jedis.close();
			}
		}
		return result;
	}
	
	/**
	 * 清空缓存
	 * @return
	 */
	public static Long flushDB() {
		Jedis jedis = null;
		Long result = null;
		try {
			jedis = getJedis();
			if (jedis != null) {
				jedis.flushDB();
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (jedis != null) {
				jedis.close();
			}
		}
		return result;
	}

	public static void main(String[] args) throws InterruptedException {

		for (int i = 0; i < 10; i++) {
			// RedisUtil.set("123456", "123456",8640);
			// Thread.sleep(8640);
			System.out.println(RedisUtil.get("123456"));
		}
	}
}
