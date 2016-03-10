package hou.ipProxy.voteHupu;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Vector;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;

/**
 * @author houweitao
 * @date 2016年1月13日 下午3:45:35
 * @end 2016年1月13日16:02:39
 * @升级 改造成多线程投票。增加计数功能，失败的投票，成功的投票，异常的投票。2016年1月13日17:15:37
 */

public class MutithreadVote {
	private static final Logger log = LoggerFactory.getLogger(MutithreadVote.class);

	private int threadCount = 20;
	private ArrayList<String> voteAddress = new ArrayList<>();
	private List<Proxy> proxylist = new LinkedList<>();
	private boolean killThread = false;
	private int successNum = 0;
	private int failureNum = 0;
	private int exceptionNum = 0;
	private int proxyNum = 0;

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		MutithreadVote mv = new MutithreadVote();
		mv.init();
	}

	private void init() {
		// TODO Auto-generated method stub
//		voteAddress.add(
//				"http://snsvote.hupu.com/InsertVote_new.php?enews=InsertVote&voteid=279621&vote=2&jsoncallback=jsonp");
//		// http://bbs.hupu.com/15233463.html
//		voteAddress.add(
//				"http://snsvote.hupu.com/InsertVote_new.php?enews=InsertVote&voteid=279623&vote=2&jsoncallback=jsonp");
//		voteAddress.add(
//				"http://snsvote.hupu.com/InsertVote_new.php?enews=InsertVote&voteid=279624&vote=2&jsoncallback=jsonp");

		// http://bbs.hupu.com/15242591.html
//		voteAddress.add(
//				"http://snsvote.hupu.com/InsertVote_new.php?enews=InsertVote&voteid=279730&vote=2&jsoncallback=jsonp");
//		voteAddress.add(
//				"http://snsvote.hupu.com/InsertVote_new.php?enews=InsertVote&voteid=279731&vote=2&jsoncallback=jsonp");

		// http://bbs.hupu.com/15253510.html
		voteAddress.add(
				"http://snsvote.hupu.com/InsertVote_new.php?enews=InsertVote&voteid=279851&vote=2&jsoncallback=jsonp");

		long start = System.currentTimeMillis();
		proxylist.addAll(new GetXiciDaili().getAll());
		proxylist.addAll(new GetProxy().getAllProxy());
		proxylist.addAll(new GetKuaiDaili().get());
		begin();
		log.info("成功投票： " + successNum);
		log.info("失败投票： " + failureNum);
		log.info("异常投票： " + exceptionNum);
		log.info("代理数目： " + proxyNum);
		long end = System.currentTimeMillis();

		log.info("耗时： " + (end - start) / 100 + " 秒");
	}

	public void begin() {
		Vector<Thread> threads = new Vector<Thread>();
		for (int i = 0; i < threadCount; i++) {
			Thread thread = new Thread(new Runnable() {
				// while (!notCrawlurlSet.isEmpty()) {

				public void run() {
//					while (true) {
					while (!killThread) {

						log.info("当前进入" + Thread.currentThread().getName());
						log.info("待使用代理的数量：" + proxylist.size());

						Proxy tmp = getProxy();
						if (tmp != null) {
							try {
								vote(tmp, voteAddress);
							} catch (Exception e) {
								e.printStackTrace();
							}
						} else {

//							synchronized (signal) { // ------------------（2）
//								try {
//									count++;
//									System.out.println("当前有" + count + "个线程在等待");
//									signal.wait();
//								} catch (InterruptedException e) {
//									// TODO Auto-generated catch block
//									e.printStackTrace();
//								}
//							}

						}
					}
				}

			}, "thread-" + i);
			threads.add(thread);
			thread.start();

		}

		for (Thread iThread : threads) {
			try {
				// 等待所有线程执行完毕
				iThread.join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

		log.info("执完毕行！");
	}

	protected void vote(Proxy proxy, ArrayList<String> voteAddress) {
		// TODO Auto-generated method stub
		proxyNum++;

		System.setProperty("http.maxRedirects", "50");
		System.getProperties().setProperty("proxySet", "true");

		System.getProperties().setProperty("http.proxyHost", proxy.getHost());
		System.getProperties().setProperty("http.proxyPort", proxy.getPort());

		try {
			for (int i = 0; i < voteAddress.size(); i++) {
				long time = System.currentTimeMillis();
				Document doc = Jsoup.connect(voteAddress.get(i) + time + "&_=" + time).followRedirects(true)
						.timeout(1500).userAgent("Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.0)").get();

				String response = doc.select("body").text();
				log.info(response);
				response = response.substring(19).replace(")", "");
				Response re = JSON.parseObject(response, Response.class);
				if (re.getCode().equals("1")) {
					successNum++;
					log.info("成功: " + successNum);
				}
				if (re.getCode().equals("-10")) {
					failureNum++;
					log.info("失败: " + failureNum);
				}
			}
		} catch (IOException e) {
			exceptionNum++;
			log.info("异常: " + exceptionNum);
		}

		log.info(proxy.getHost());
	}

	private synchronized Proxy getProxy() {
		// TODO Auto-generated method stub
		if (proxylist.size() > 0) {
			Proxy ret = proxylist.get(0);
			proxylist.remove(0);
			return ret;
		} else {
			killThread = true;
			return null;
		}
	}
}
