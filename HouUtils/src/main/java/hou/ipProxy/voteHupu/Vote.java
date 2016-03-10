package hou.ipProxy.voteHupu;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

/**
 * @author houweitao
 * @date 2016年1月13日 上午11:54:18
 * 投票程序。单线程
 */

public class Vote {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Vote vote = new Vote();
		vote.testKuaiDaili();
	}

	void testKuaiDaili() {
		String vote[] = {
				"http://snsvote.hupu.com/InsertVote_new.php?enews=InsertVote&voteid=279518&vote=1&jsoncallback=jsonp",
				"http://snsvote.hupu.com/InsertVote_new.php?enews=InsertVote&voteid=279519&vote=1&jsoncallback=jsonp",
				"http://snsvote.hupu.com/InsertVote_new.php?enews=InsertVote&voteid=275991&vote=7&jsoncallback=jsonp" };

		List<Proxy> ret = new LinkedList<>();
		ret.addAll(new GetKuaiDaili().get());
		int index = 1;

		System.setProperty("http.maxRedirects", "50");
		System.getProperties().setProperty("proxySet", "true");
		for (Proxy ip : ret) {
			System.getProperties().setProperty("http.proxyHost", ip.getHost());
			System.getProperties().setProperty("http.proxyPort", ip.getPort());

			try {

				long time = System.currentTimeMillis();
				Document doc = Jsoup.connect(vote[0] + time + "&_=" + time).followRedirects(true).timeout(1000)
						.userAgent("Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.0)").get();
				System.out.println(doc.select("body").text());

				time = System.currentTimeMillis();
				doc = Jsoup.connect(vote[1] + time + "&_=" + time).followRedirects(true).timeout(1000)
						.userAgent("Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.0)").get();
				System.out.println(doc.select("body").text());

				time = System.currentTimeMillis();
				doc = Jsoup.connect(vote[2] + time + "&_=" + time).followRedirects(true).timeout(1000)
						.userAgent("Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.0)").get();
				System.out.println(doc.select("body").text());

			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			System.out.println("第" + index++ + "个");
			System.out.println(ip.getHost());
		}

	}

	void voteMcGrady() {
		//http://bbs.hupu.com/15224702.html
		//http://bbs.hupu.com/14838067.html

		String vote[] = {
				"http://snsvote.hupu.com/InsertVote_new.php?enews=InsertVote&voteid=279621&vote=2&jsoncallback=jsonp" };//http://bbs.hupu.com/15233463.html
//				"http://snsvote.hupu.com/InsertVote_new.php?enews=InsertVote&voteid=279518&vote=1&jsoncallback=jsonp",
//				"http://snsvote.hupu.com/InsertVote_new.php?enews=InsertVote&voteid=279519&vote=1&jsoncallback=jsonp",
//				"http://snsvote.hupu.com/InsertVote_new.php?enews=InsertVote&voteid=275991&vote=7&jsoncallback=jsonp" };

		System.setProperty("http.maxRedirects", "50");
		System.getProperties().setProperty("proxySet", "true");

		GetProxy proxy = new GetProxy();
		List<Proxy> ret = proxy.getAllProxy();

		int index = 1;
		for (Proxy ip : ret) {
			System.getProperties().setProperty("http.proxyHost", ip.getHost());
			System.getProperties().setProperty("http.proxyPort", ip.getPort());

			try {
				for (int i = 0; i < vote.length; i++) {
					long time = System.currentTimeMillis();
					Document doc = Jsoup.connect(vote[i] + time + "&_=" + time).followRedirects(true).timeout(1500)
							.userAgent("Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.0)").get();
					System.out.println(doc.select("body").text());
				}

			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			System.out.println("第" + index++ + "个");
			System.out.println(ip.getHost());
		}
	}
}
