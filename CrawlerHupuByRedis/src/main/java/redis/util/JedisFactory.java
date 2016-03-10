package redis.util;

import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Properties;

import redis.clients.jedis.Jedis;

/**
 * @author houweitao
 * @date 2016年1月14日 下午3:24:02
 */

public class JedisFactory {
	private static Properties redisConf = new Properties();
	private static Jedis jedis;

	static {
		try {
			InputStream in = JedisFactory.class.getClassLoader().getResourceAsStream("redis.properties");
//			InputStream in = ConnectionFactory.class.getClassLoader().getResourceAsStream("localRedis.properties");
			redisConf.load(in);

			jedis = new Jedis(redisConf.getProperty("redis.host"), Integer.valueOf(redisConf.getProperty("redis.port")),
					10000);

		} catch (Exception e) {
			System.out.println("=====无配置文件======");
		}
	}

	public Jedis getInstance() {
		return jedis;
	}
}
