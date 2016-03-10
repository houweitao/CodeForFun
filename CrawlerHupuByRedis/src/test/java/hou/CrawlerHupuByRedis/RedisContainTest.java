package hou.CrawlerHupuByRedis;

import redis.clients.jedis.Jedis;
import redis.model.User;
import redis.util.JedisFactory;

/**
 * @author houweitao
 * @date 2016年1月14日 下午10:52:06
 */

public class RedisContainTest {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Jedis jedis = new JedisFactory().getInstance();
		User user = new User("hahah", "jajd", 2, 2);
		User test = new User("hahah", "jajds", 32, 32);
		jedis.hset("test", user.getUrl(), "ssss");
	}

}
