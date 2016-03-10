package redis;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.util.Set;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.util.JedisPoolFactory;
import redis.work.Crawler;

/**
 * @author houweitao
 * @date 2016年1月18日 下午2:27:05
 */

public class MostPopularMark {
	private String userList = "HUPU$USER";
	private String markList = "HUPU$MARK";
	private String score = "HUPU$SCORE";
	private JedisPool pool = new JedisPoolFactory().getInstance();

	public static void main(String[] args) {
		MostPopularMark getPop = new MostPopularMark();
//		getPop.putIn(getPop.getData());
		getPop.showMostMark();
	}

	Map<String, Double> getData() {
		Jedis jedis = pool.getResource();
		Map<String, Double> ret = new HashMap<>();
		Map<String, String> tmp = jedis.hgetAll(markList);

		for (Entry<String, String> entry : tmp.entrySet()) {
			ret.put(entry.getKey(), Double.valueOf(entry.getValue()));
			System.out.println(entry.getKey() + "," + Double.valueOf(entry.getValue()));
		}
//		pool.returnResource(jedis);
		return ret;
	}

	void putIn(Map<String, Double> map) {
		Jedis jedis = new JedisPoolFactory().getInstance().getResource();
		jedis.zadd(score, map);
//		pool.returnResource(jedis);
	}

	void showMostMark() {
		Jedis jedis = new JedisPoolFactory().getInstance().getResource();
		Set<String> result = jedis.zrevrange(score, 0, -1);
		System.out.println(result.size());
		Iterator<String> i = result.iterator();
		while (i.hasNext()) {
			String title = "";
			String author = "";
			String time = "";
			Double count = jedis.zscore(score, i.next());
			if (count > 98) {
				try {
					Document doc = Jsoup.connect(i.next()).followRedirects(true).timeout(10000)
							.userAgent("Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.0)").get();
					title = doc.select(".bbs-hd-h1").select("#j_data").first().text();
					author = doc.select(".left").select(".u").first().text();
					time = doc.select(".left").select(".stime").first().text();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				System.out.println(title + "  " + i.next() + " : " + count + "次  " + "[" + author + "@" + time + "]");
			}
		}
	}

	//============================

	//直接用user数据
	void writeMarkList() {
		Jedis jedis = pool.getResource();
		Map<String, String> users = jedis.hgetAll(userList);
		Crawler c = new Crawler();
		for (Entry<String, String> entry : users.entrySet()) {
			try {
				c.dealMarkList(entry.getKey(), jedis);
			} catch (Exception e) {
				pool.returnBrokenResource(jedis);
				jedis = pool.getResource();
			}
		}
		pool.returnResource(jedis);
	}
}
