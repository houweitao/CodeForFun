package hou.zhihu.util;

import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

/**
 * @author houweitao
 * @date 2016年1月21日 下午3:19:45
 */

public class JedisPoolFactory {
	JedisPool pool;

	public JedisPoolFactory() {
		JedisPoolConfig config = new JedisPoolConfig();
		config.setMaxTotal(40);
		pool = new JedisPool(config, "localhost", 6379, 100000);//最后一个参数是超时时间。读取的时候。默认是2秒。
	}

	public JedisPool getInstance() {
		return pool;
	}
}
