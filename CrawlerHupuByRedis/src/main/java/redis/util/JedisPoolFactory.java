package redis.util;

import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

/**
 * @author houweitao
 * @date 2016年1月15日 下午5:54:07
 */

public class JedisPoolFactory {
	JedisPool pool;

	public JedisPoolFactory(){
		JedisPoolConfig config = new JedisPoolConfig();
		config.setMaxIdle(40);
		pool = new JedisPool(config, "localhost", 6379);
	}
	
	public JedisPool getInstance() {
		return pool;
	}
}
