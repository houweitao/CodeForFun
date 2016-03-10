package hou.zhihu.work;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;

import hou.zhihu.model.User;
import hou.zhihu.util.JedisPoolFactory;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

/**
 * @author houweitao
 * @date 2016年1月21日 下午3:12:35
 */

public class Crawler {
	private static final Logger log = LoggerFactory.getLogger(Crawler.class);
	JedisPool pool;
	private int threadCount = 5;
	private int depth = Integer.MAX_VALUE;
	public final Object signal = new Object(); // 线程间通信变量
	private boolean killThread = false;
	public int count = 0; // 表示有多少个线程处于wait状态
	private String crawerList = "ZHIHU$CRAWLERLIST";
	private String userList = "ZHIHU$USER";
	private String markList = "ZHIHU$MARK";
	private String score = "ZHIHU$SCORE";
	private Map<String, String> cookies;
	Vector<Thread> threadList = new Vector<Thread>();

	int num = 1;

	@SuppressWarnings("deprecation")
	public void init() {
		cookies = getZhiHuCookies();
		pool = new JedisPoolFactory().getInstance();
		Jedis jedis = pool.getResource();
		System.out.println(jedis.del(crawerList));
		System.out.println(jedis.del(userList));
//		System.out.println(jedis.del(markList));
//		System.out.println(jedis.del(score));
		User first = new User("https://www.zhihu.com/people/warfalcon");
		jedis.rpush(crawerList, first.getUrl());
		jedis.hset(userList, first.getUrl(), JSON.toJSONString(first));
		System.out.println("当前：" + jedis.llen(crawerList));
		pool.returnResource(jedis);
//		System.out.println(jedis.lpop(crawerList));
		begin();

		System.out.println(num + " 次");
	}

	public void begin() {
		for (int i = 0; i < threadCount; i++) {
			Thread thread = new Thread(new Runnable() {
				Jedis jedis = pool.getResource();

				@SuppressWarnings("deprecation")
				public void run() {
					log.info("run: " + Thread.currentThread().getName());
					while (!killThread) {
						try {
							log.info("当前进入" + Thread.currentThread().getName());
							log.info("待爬取链接数量：" + jedis.llen(crawerList));
							log.info("已爬取链接数量：" + jedis.hlen(userList));
							if (jedis.llen(crawerList) == 0) {
								Thread.sleep(10000);
								if (jedis.llen(crawerList) == 0) {
									killThread = true;
									log.info(Thread.currentThread().getName() + " 爬取队列为空！！break！！");
									break;
								}
							}

							String url = jedis.lpop(crawerList);
							if (url != null && url.length() != 0) {
								User father = JSON.parseObject(jedis.hget(userList, url), User.class);
								crawler(father, jedis);
							}
						} catch (Exception e) {
							pool.returnBrokenResource(jedis);
							jedis = pool.getResource();
						}
					}
					log.info("end: " + Thread.currentThread().getName());
					pool.returnResource(jedis);
				}
			}, "thread-" + i);
			thread.start();
			threadList.add(thread);
		}

		for (Thread iThread : threadList) {
			try {
				// 等待所有线程执行完毕
				iThread.join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	public void crawler(User father, Jedis jedis) {
		String url = father.getUrl();

		try {

//			Connection con = Jsoup.connect(url + "/followers");//获取连接  
//			con.header("User-Agent",
//					"Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/47.0.2526.80 Safari/537.36");//配置模拟浏览器  
//			con.header("Host", "www.zhihu.com");
//			con.header("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8");
//			Response rs = con.execute();//获取响应  
//			Document d1 = Jsoup.parse(rs.body());//转换为Dom树  
//			Elements visters2 = d1.select("#zh-profile-follows-list").select(".zm-list-content-medium").select("a");
//			System.out.println(visters2.size());

			String[] appendUrl = { "/followees", "/followers" };
			for (int i = 0; i < appendUrl.length; i++) {
				Document doc = Jsoup.connect(url + appendUrl[i]).cookies(cookies).followRedirects(true).timeout(10000)
						.userAgent(
								"Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/47.0.2526.80 Safari/537.36")
						.get();

				if (i == 0) {
					String address = "";
					String job = "";
					String weibo = "";

					try {
						address = doc.select(".location.item").first().text();
					} catch (Exception e) {
						log.warn("no address");
					}
					try {
						job = doc.select(".business.item").first().text();
					} catch (Exception e) {
						log.warn("no job");
					}
					try {
						weibo = doc.select(".weibo-wrap").first().select("a").first().attr("abs:href");
					} catch (Exception e) {
						log.warn("no weibo");
					}

					Elements social = doc.select(".zm-profile-side-following.zg-clear").select(".item")
							.select("strong");
					int followNum = Integer.valueOf(social.get(0).text());
					int followerNum = Integer.valueOf(social.get(1).text());

					address = check(address);
					job = check(job);
					weibo = check(weibo);

					User retUser = JSON.parseObject(jedis.hget(userList, url).toString(), User.class);

					retUser.setAddress(address);
					retUser.setJob(job);
					retUser.setWeibo(weibo);
					retUser.setFollowerNum(followerNum);
					retUser.setFollowNum(followNum);

					log.info(followerNum + " " + followNum + " " + address + " " + job + " " + weibo);

					log.info("fix: " + retUser.toString());
					jedis.hset(userList, url, JSON.toJSONString(retUser));
					System.out.println(num++);
				}

				if (father.getDepth() < depth) {
					Elements users = doc.select("#zh-profile-follows-list").select(".zm-list-content-medium");

					for (Element e : users) {
//						System.out.println(e.text());
						Element ee = e.select("a").first();
						String newUrl = ee.attr("abs:href");
						String newName = ee.text();

						System.out.println(newUrl + "  " + newName);

						if (!jedis.hexists(userList, newUrl)) {
							jedis.rpush(crawerList, newUrl);
							System.out.println("新用户： " + ee.text());

							User user = new User(newUrl, newName, 1, father.getDepth() + 1);
							jedis.hset(userList, newUrl, JSON.toJSONString(user));
							log.info(user.toString());
						} else {
							System.out.println("已经存在： " + ee.text());
							Object ret = jedis.hget(userList, newUrl);
							User retU = JSON.parseObject(ret.toString(), User.class);
							retU.setSearchNum(retU.getSearchNum() + 1);
							retU.setDepth(father.getDepth() + 1);
							jedis.hset(userList, url, JSON.toJSONString(retU));

							log.info("深度： " + retU.getDepth());
						}
					}
				}
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private String check(String str) {
		if (str == null || str.length() == 0)
			return "null";
		else
			return str;
	}

	public Map<String, String> getZhiHuCookies() {
		Map<String, String> map = new HashMap<String, String>();

		// ctrl+shift+j，然后输入document.cookie回车

		map.put("_za", "3c0123a2-e8cc-4798-9eb7-6a18e06a8325");
		map.put("_ga", "GA1.2.2113311030.1433748124");
		map.put("q_c1", "c2aa1be9dba94866b0e8d2e44accd04a|1450947928000|1430899083000");
		map.put("cap_id",
				"ZDQ0MGRhMmYwZTU1NDU1M2JmZWM1YjA1MWRjMDhhN2Y=|1452574597|64ea48a2821893872633bc1f41697355611395e4");
		map.put("aliyungf_tc", "AQAAAPUfgD4WRwQAJe4xOhIxJKecTRkx");
		map.put("_xsrf", "1375ccde0dbdab35e4e2dab24920c15d");
		map.put("__utmt", "1");
		map.put("__utma", "155987696.2113311030.1433748124.1453773243.1453773243.1");
		map.put("__utmb", "155987696.2.10.1453773243");
		map.put("__utmc", "155987696");
		map.put("__utmz", "155987696.1453773243.1.1.utmcsr=zhihu.com|utmccn=(referral)|utmcmd=referral|utmcct=/search");
		map.put("__utmv", "155987696.100-1|2=registration_date=20130101=1^3=entry_date=20130101=1");

		map.put("n_c", "");
		map.put("unlock_ticket",
				"QUFBQWNRa2FBQUFYQUFBQVlRSlZUVnlib0ZiTTRFdnJ3azZBN2I5YkVzNkt5cmRTN3BDclJRPT0=|1453364308|5b292f3b1b0971967f2f56f46f1c58b411cdfb79");
		map.put("z_c0",
				"QUFBQWNRa2FBQUFYQUFBQVlRSlZUYmdVdkZaZzhNa3pYZkVyYW02ZDJDcUpyaUFHNVhTYXF3PT0=|1452574648|39ff67b235a24d6b2d292f8e5d3507b5d73c6366");
		return map;
	}
}
