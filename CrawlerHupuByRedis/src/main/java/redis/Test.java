package redis;

import redis.clients.jedis.Jedis;
import redis.util.JedisFactory;

/**
 * @author houweitao
 * @date 2016年1月14日 下午5:10:32
 */

public class Test {
	private static String crawerList = "HUPU$CRAWLERLIST";
	private static String userList = "HUPU$USER";

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Jedis jedis = new JedisFactory().getInstance();
		while (true)
			System.out.println(jedis.llen(crawerList));
	}

}
