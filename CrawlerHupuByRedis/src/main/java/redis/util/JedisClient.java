package redis.util;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

/**
 * @author houweitao
 * @date 2016年1月15日 上午10:51:18
 */

public class JedisClient {
	private JedisPool pool = new JedisPool(new JedisPoolConfig(), "localhost", 6379);

	public Jedis getInstance() {
		return pool.getResource();
	}
}
