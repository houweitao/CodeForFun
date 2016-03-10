package hou.forfun.CrawlerZhihu;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import hou.zhihu.util.JedisPoolFactory;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.ScanParams;
import redis.clients.jedis.ScanResult;
import redis.clients.jedis.ShardedJedis;
import redis.clients.jedis.ShardedJedisPool;

/**
 * @author houweitao
 * @date 2016年1月29日 上午1:24:21
 */

public class RedisScanTest {
	private static String userList = "ZHIHU$USER";

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Jedis jedis = new JedisPoolFactory().getInstance().getResource();

//		ScanResult<Entry<String, String>> s = jedis.hscan(userList, 0);
		Set<String> list = jedis.hkeys(userList);

		System.out.println(list.size());
		for (String str : list) {
			System.out.println(str);
		}
	}

}
