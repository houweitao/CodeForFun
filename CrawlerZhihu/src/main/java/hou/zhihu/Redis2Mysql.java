package hou.zhihu;

import java.sql.Connection;
import java.util.HashMap;
import java.util.Map;

import hou.zhihu.dao.UserDao;
import hou.zhihu.util.JedisPoolFactory;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

/**
 * @author houweitao
 * @date 2016年1月27日 下午9:57:33
 */

public class Redis2Mysql {
	private static JedisPool pool;
	private static Jedis jedis;
	private static String userList = "ZHIHU$USER";

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		pool = new JedisPoolFactory().getInstance();
		jedis = pool.getResource();

		Map<String, String> users = new HashMap<String, String>();
		users = jedis.hgetAll(userList);
		System.out.println(users.size());
		UserDao userDao = new UserDao();
		userDao.insertList(users);
	}

}
