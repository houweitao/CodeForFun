package hou.CrawlerHupuByRedis;

import org.junit.Test;

import redis.clients.jedis.Jedis;
import redis.util.JedisFactory;

/**
 * @author houweitao
 * @date 2016年1月14日 下午3:33:45
 */

public class JedisFactoryTest {

	@Test
	public void connect() {
		JedisFactory factory = new JedisFactory();
		Jedis jedis = factory.getInstance();
	}
}
