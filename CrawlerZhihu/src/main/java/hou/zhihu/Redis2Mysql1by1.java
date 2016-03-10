package hou.zhihu;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import com.alibaba.fastjson.JSON;

import hou.zhihu.dao.UserDao;
import hou.zhihu.model.User;
import hou.zhihu.util.JedisPoolFactory;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

/**
 * @author houweitao
 * @date 2016年1月29日 上午1:44:25
 */

public class Redis2Mysql1by1 {
	private static JedisPool pool;
	private static Jedis jedis;
	private static String userList = "ZHIHU$USER";

	@SuppressWarnings("deprecation")
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		pool = new JedisPoolFactory().getInstance();
		jedis = pool.getResource();

		Set<String> userSet = jedis.hkeys(userList);

		UserDao userDao = new UserDao();
		int i = 1, all = userSet.size();
		for (String str : userSet) {
			System.out.println(i++ + " , " + all);
			try {
				User user = JSON.parseObject(jedis.hget(userList, str), User.class).addInfomation();
				userDao.insertUser(user);
			} catch (Exception e) {
				pool.returnBrokenResource(jedis);
				jedis = pool.getResource();
			}
		}
	}

}
