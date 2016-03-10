package redis;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

/**
 * @author houweitao
 * @date 2016年1月15日 下午4:37:23
 */

public class SaveData {
	private String userList = "HUPU$USER";

	public static void main(String[] args) {
		SaveData sd = new SaveData();
		sd.saveData(sd.getDataFromRedis());
	}

	Map<String, String> getDataFromRedis() {
		JedisPool pool = new JedisPool(new JedisPoolConfig(), "localhost", 6379);
		Jedis jedis = pool.getResource();
		Map<String, String> userMap = new HashMap<String, String>();
		userMap = jedis.hgetAll(userList);

		return userMap;
	}

	void saveData(Map<String, String> map) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH时mm分");
		String name = sdf.format(new Date());
		String fileName = "doc/data" + name + ".txt";
		File file = new File(fileName);
		BufferedWriter bw;
		try {
			bw = new BufferedWriter(new FileWriter(file, true));
			for (Entry<String, String> entry : map.entrySet()) {
				System.out.println(entry.getValue());
				bw.write(entry.getValue() + "\r\n");
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	//testWrite
	void saveData() {
		String fileName = "doc/data.txt";
		File file = new File(fileName);
		BufferedWriter bw;
		try {
			bw = new BufferedWriter(new FileWriter(file, true));
			bw.write("sadadadas" + "\r\n");
			bw.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
