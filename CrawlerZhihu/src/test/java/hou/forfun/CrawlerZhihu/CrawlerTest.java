package hou.forfun.CrawlerZhihu;

import java.io.IOException;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.Connection.Response;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.junit.Test;

import hou.zhihu.model.User;
import hou.zhihu.util.JedisPoolFactory;
import hou.zhihu.work.Crawler;
import redis.clients.jedis.Jedis;

/**
 * @author houweitao
 * @date 2016年1月21日 下午3:38:30
 */

public class CrawlerTest {
//	public static void main(String[] args) {
//		crawlerOne();
//	}

	@Test
	public void crawlerOne() {
		Crawler c = new Crawler();
		try {
			Document doc = Jsoup.connect("https://www.zhihu.com/people/kaifulee/followees").followRedirects(true)
					.timeout(10000).cookies(c.getZhiHuCookies())
					.userAgent(
							"Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/47.0.2526.80 Safari/537.36")
					.get();

			System.out.println(doc.title());
			System.out.println(doc.toString());

			Elements follower = doc.select(".zm-list-content-medium");
			for (Element e : follower) {
				System.out.println(e.text());
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	//oh yeah!
	//http://caidongrong.blog.163.com/blog/static/21424025220139292525874/
	//设置headers
	@Test
	public void testHeaderLogin() {
		Crawler c = new Crawler();
		Connection con = Jsoup.connect("https://www.zhihu.com/people/kaifulee/followees");//获取连接  
//		con.header("User-Agent",
//				"Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/47.0.2526.80 Safari/537.36");//配置模拟浏览器  
//		con.header("Host", "www.zhihu.com");
//		con.header("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8");
		con.cookies(c.getZhiHuCookies());
		con.timeout(10000);
		Response rs;
		try {
			rs = con.execute();
			Document d1 = Jsoup.parse(rs.body());//转换为Dom树  
			Elements visters2 = d1.select("span.name");

//			System.out.println(d1.toString());
			System.out.println(d1.title());

			System.out.println(visters2.size());
			System.out.println(visters2.first().text());

			Elements follower = d1.select(".zm-list-content-medium");
			for (Element e : follower) {
				System.out.println(e.text());
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} //获取响应  

	}
}
