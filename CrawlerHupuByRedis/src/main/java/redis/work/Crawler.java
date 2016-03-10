package redis.work;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.model.User;
import redis.util.JedisPoolFactory;

/**
 * @author houweitao
 * @date 2016年1月14日 下午3:49:11
 * 边爬用户，边爬收藏。2：45~6：15。三个半小时。不爬收藏十几分钟。最深层次28。共47429个用户形成一个闭环。235080个收藏。
 */

public class Crawler {
//	private Jedis jedis;
	private static final Logger log = LoggerFactory.getLogger(Crawler.class);
	JedisPool pool;
	private int threadCount = 20;
	private int depth = Integer.MAX_VALUE;
	public final Object signal = new Object(); // 线程间通信变量
	private boolean killThread = false;
	public int count = 0; // 表示有多少个线程处于wait状态
	private String crawerList = "HUPU$CRAWLERLIST";
	private String userList = "HUPU$USER";
	private String markList = "HUPU$MARK";
	private String score = "HUPU$SCORE";
	private Map<String, String> cookies;

	public void init() {
		cookies = getHupuCookies();
		pool = new JedisPoolFactory().getInstance();
		Jedis jedis = pool.getResource();
		System.out.println(jedis.del(crawerList));
		System.out.println(jedis.del(userList));
//		System.out.println(jedis.del(markList));
//		System.out.println(jedis.del(score));
		User first = new User("http://my.hupu.com/zhangjiawei", "张佳玮", 1, 1);
		jedis.rpush(crawerList, first.getUrl());
		jedis.hset(userList, first.getUrl(), JSON.toJSONString(first));
		System.out.println("当前：" + jedis.llen(crawerList));
		pool.returnResource(jedis);
//		System.out.println(jedis.lpop(crawerList));
		begin();
	}

	public void begin() {
		for (int i = 0; i < threadCount; i++) {
			new Thread(new Runnable() {
				Jedis jedis = pool.getResource();

				public void run() {
					log.info("run: " + Thread.currentThread().getName());
					while (!killThread) {
						try {
							log.info("当前进入" + Thread.currentThread().getName());
							log.info("待爬取链接数量：" + jedis.llen(crawerList));
							log.info("已爬取链接数量：" + jedis.hlen(userList));
							if (jedis.llen(crawerList) == 0) {
								killThread = true;
								log.info(Thread.currentThread().getName() + " 爬取队列为空！！break！！");
								break;
							}

							String url = jedis.lpop(crawerList);
							if (url != null && url.length() != 0) {
								try {
									User father = JSON.parseObject(jedis.hget(userList, url), User.class);
									if (father.getDepth() < depth)
										crawler(father, jedis);
									else
										log.info("超出最大深度！");
								} catch (Exception e) {
									e.printStackTrace();
								}
							} else {

							}
						} catch (Exception e) {
							log.info("ssssssssssssssss");
							pool.returnBrokenResource(jedis);
							jedis = pool.getResource();
						}
					}
					log.info("end: " + Thread.currentThread().getName());
					pool.returnResource(jedis);
				}
			}, "thread-" + i).start();

			try {
				Thread.sleep(3000);
			} catch (InterruptedException e1) {
				e1.printStackTrace();
			}
		}
	}

	protected void crawler(User father, Jedis jedis) {
		String url = father.getUrl();

		try {
			Document doc = Jsoup.connect(url).followRedirects(true).timeout(10000)
					.userAgent("Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.0)").get();
			Elements visters = doc.select("#visitor").select(".peoplelist").select("a.u").select("a[href]");

			for (Element e : visters) {
//				System.out.println(e.text());
				String newUrl = e.attr("abs:href");
				String newName = e.text();

				if (!jedis.hexists(userList, newUrl)) {
					jedis.rpush(crawerList, newUrl);
					System.out.println("新用户： " + e.text());
					User user = new User(newUrl, newName, 1, father.getDepth() + 1);

					Document docDetil = Jsoup.connect(url + "/profile").followRedirects(true).timeout(10000)
							.userAgent("Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.0)").cookies(cookies).get();
					Element profile = docDetil.select(".profile_table").first();
					user.addInfo(profile.text());
//					System.out.println(profile.text());
					jedis.hset(userList, newUrl, JSON.toJSONString(user));
					log.info(user.toString());

//					dealMarkList(newUrl, jedis);

				} else {
					System.out.println("已经存在： " + e.text());
					Object ret = jedis.hget(userList, newUrl);
					User retUser = JSON.parseObject(ret.toString(), User.class);
					retUser.setSearchNum(retUser.getSearchNum() + 1);
					retUser.setDepth(father.getDepth() + 1);
					jedis.hset(userList, url, JSON.toJSONString(retUser));

					log.info("深度： " + retUser.getDepth());
				}
			}

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void dealMarkList(String url, Jedis jedis) {

		url = url + "/topic-fav-main-";
		int pageNum = 0;
		try {
			Document doc = Jsoup.connect(url + "1").followRedirects(true).timeout(10000)
					.userAgent("Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.0)").cookies(cookies).get();
			Element page = doc.select("div.page").first();
//			System.out.println(page.text());
			String[] dealText = page.text().split("»");
			pageNum = Integer.valueOf(dealText[dealText.length - 1].replace("共", "").replace("篇帖子", ""));
			System.out.println("num: " + pageNum);
			if (pageNum > 0)
				pageNum = pageNum / 40 + 1;

			for (int i = 1; i < pageNum; i++) {
//				System.out.println("toooooooooooooooooooo: "+(url+i)+","+i);
				Document docMark = Jsoup.connect(url + i).followRedirects(true).timeout(10000)
						.userAgent("Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.0)").cookies(cookies).get();
				Elements marks = docMark.select("td.p_title");
				for (Element e : marks) {
//					System.out.println(e);
					//这里很奇怪。网页上没有"a"，但是上一行输出的e有。所以只好这么来获取url了
					String tar = e.select("a").first().attr("abs:href");
//					System.out.println("tar: "+tar);
					String time = jedis.hget(markList, tar);
//					System.out.println("收藏次数： "+time);
					if (time == null) {
						jedis.hset(markList, tar, "1");
						log.info("新发现：" + tar);
					} else {
						jedis.hset(markList, tar, Integer.valueOf(time) + 1 + "");
						log.info("已收藏：" + tar + " : " + (Integer.valueOf(time) + 1) + " 次");
					}
				}
			}

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public Map<String, String> getHupuCookies() {
		Map<String, String> map = new HashMap<>();

		//ctrl+shift+j，然后输入document.cookie回车
		map.put("_dacevid3", "c848bf5d.52a5.fd37.a1e5.a15ed1eaa721");
		map.put("HUPUSSOID", "b830e5f2-4646-4b7c-8342-c0661775fd60");
		map.put("PHPSESSID", "u4jr5vkcbtnh1vsppp72jna257");
		map.put("u", "14535515|5oiR5Y+q6IO95ZG15ZG1|afe8|28240ffa8e8e7aa3a87a55f19ea272e3|8e8e7aa3a87a55f1");
		map.put("ua", "89815856");
		map.put("dacevst", "f4d58eab.52d5fcc6|1437055566615");
		map.put("CNZZDATA30020080", "cnzz_eid%3D174751465-1437052208-%26ntime%3D1437052208");
		map.put("_cnzz_CV30020080", "buzi_cookie%7Cc848bf5d.52a5.fd37.a1e5.a15ed1eaa721%7C-1");

		return map;
	}
}
