package hou.CrawlerHupuByRedis;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import redis.util.JedisClient;

/**
 * @author houweitao
 * @date 2016年1月15日 上午10:55:51
 */

public class JedisClientTest {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		JedisPool pool = new JedisPool(new JedisPoolConfig(), "localhost", 6379);

		Jedis jedis = pool.getResource();
		System.out.println(jedis.keys("*"));
		
	}

}
