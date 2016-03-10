package redis;

import java.util.Map;
import java.util.Map.Entry;

import com.alibaba.fastjson.JSON;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.model.User;
import redis.util.JedisPoolFactory;

/**
 * @author houweitao
 * @date 2016年1月19日 上午9:50:01
 */

public class ShowData {
	private JedisPool pool = new JedisPoolFactory().getInstance();
	private String userList = "HUPU$USER";

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		ShowData show = new ShowData();
		show.showUser();
	}

	void showUser() {
		Jedis jedis = pool.getResource();
		Map<String, String> userMap = jedis.hgetAll(userList);
		for (Entry<String, String> entry : userMap.entrySet()) {
			User user = JSON.parseObject(entry.getValue(), User.class);
			System.out.println(user.userInfo());
		}
	}
}
