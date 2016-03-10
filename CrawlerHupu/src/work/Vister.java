package work;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import Dao.MainDao;
import model.Main;
import util.ConnectionFactory;

/**
 * @author houweitao 2015年8月5日 上午9:48:15
 */

public class Vister {
	Main site;

	public ArrayList<Main> allSiteSet = new ArrayList<Main>();// 所有的网页url，需要更高效的去重可以考虑HashSet
	public ArrayList<Main> notCrawlSiteSet = new ArrayList<Main>();// 未爬过的网页url
	HashMap<String, Integer> depth = new HashMap<String, Integer>();// 所有网页的url深度
	HashMap<String, Integer> num = new HashMap<String, Integer>();// 所有网页的url次数

	private HashMap<String, Boolean> hm = new HashMap<String, Boolean>();
	int userNum = 0;
	int visterNum = 0;
	int existNum = 0;

	public ArrayList<Main> tmpSet = new ArrayList<Main>();

	public long pageNumber = 0;

	int crawDepth = 9; // 爬虫深度
	public int threadCount = 50; // 线程数量
	public int count = 0; // 表示有多少个线程处于wait状态
	public final Object signal = new Object(); // 线程间通信变量
	// MainDao minDao = new MainDao();
	// DetailDao detailDao = new DetailDaoImpl();

	MainDao mainDao = new MainDao();

	public Connection conn = ConnectionFactory.getInstance().makeConnection();

	boolean killThread = false;

	File f;
	int countInfo = 0;

	public static void main(String[] args) {
		// TODO Auto-generated method stub

		Vister ch = new Vister();

		Main hp = new Main();
		hp.setHomePage("http://my.hupu.com/BelieveMyself");
		hp.setDepth(1);

		try {
			ch.doWork(hp);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public void doWork(Main site) throws SQLException, IOException {
		addSite(site, 1);
		// site = site;

		long start = System.currentTimeMillis();
		System.out.println("开始爬虫.........................................");
		// minDao.changeStatus(conn, site, 3);
		// detailDao.Insert(conn, site);

		mainDao.insert(site);

		begin();

		while (true) {
			if (notCrawlSiteSet.isEmpty() && Thread.activeCount() == 1 || count == threadCount) {
				// || count == threadCount - exception的数量) {
				long end = System.currentTimeMillis();

				System.out.println("当前网站总共爬了" + userNum + "个网页,共" + visterNum + "个来访者");

				System.out.println("减少了 " + existNum + " 个冗余");
				mainDao.loadInFile("F:/hou/test/" + "hupuVister" + ".txt");

				// for (int i = 0; i < num.size(); i++) {
				// System.out.println(num.);
				// System.out.println(x);
				// }

				f = new File("F:/hou/test/" + "hupuVister2" + ".txt");
				BufferedWriter bw = new BufferedWriter(new FileWriter(f, true));

				int name = 0;
				int co = 0;
				for (Entry<String, Integer> entry : num.entrySet()) {
					// System.out.println(entry.getKey()+" "+entry.getValue());
					name++;
					co = co + entry.getValue();

					bw.write(entry.getKey() + "\t" + entry.getValue() + "\r\n");
//					mainDao.insertNum(entry.getKey(), entry.getValue());
					
//					if (num.get(entry.getKey()) > 5)
//						System.out.println(entry.getKey() + ":" + entry.getValue());
				}

				String fName = "F:/hou/test/" + "hupuVister2" + ".txt";
				mainDao.loadInFile2(fName);
				System.out.println(name + ":" + co);

				System.out.println("当前网站总共耗时" + (end - start) / 1000 + "秒");
				// System.exit(1);
				// return;
				count = 0;
				killThread = true;
				bw.close();
				System.exit(0);
			}

		}
	}

	// public void spiderRoot() {
	// System.out.println("爬取根网页..");
	// Main tmp = getASite();
	// crawler(tmp);
	// }

	public void begin() {
		for (int i = 0; i < threadCount; i++) {
			new Thread(new Runnable() {

				// while (!notCrawlurlSet.isEmpty()) {

				public void run() {
					// while (true) {
					while (!killThread) {

						System.out.println("当前进入" + Thread.currentThread().getName());
						System.out.println("待爬取链接数量：" + notCrawlSiteSet.size());
						Main tmp = getASite();
						// String tmp = getAUrl();
						if (tmp != null) {
							try {
								crawler(tmp);
							} catch (Exception e) {
								e.printStackTrace();
								e.printStackTrace();
							}
						} else {
							synchronized (signal) { // ------------------（2）
								try {
									count++;
									System.out.println("当前有" + count + "个线程在等待");
									signal.wait();
								} catch (InterruptedException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
							}

						}
					}
				}
			}, "thread-" + i).start();
		}
	}

	public synchronized void addSite(Main site, int d) {
		notCrawlSiteSet.add(site);
		allSiteSet.add(site);
		depth.put(site.getHomePage(), d);
		userNum++;
	}

	// private synchronized void addSite2(String str, int dep, String keyword) {
	// // TODO Auto-generated method stub
	// Main tmp = new Main();
	// tmp.setKeyword(keyword);
	// tmp.setHomePage(str);
	//
	// if (dep < 3) {
	// notCrawlSiteSet.add(tmp);
	// depth.put(tmp, dep);
	// }
	//
	// }

	public synchronized Main getASite() {
		while (!notCrawlSiteSet.isEmpty()) {
			if (depth.get(notCrawlSiteSet.get(0).getHomePage()) < crawDepth) {

				Main tmpAUrl;

				tmpAUrl = notCrawlSiteSet.get(0);
				notCrawlSiteSet.remove(0);
				return tmpAUrl;
			} else
				notCrawlSiteSet.remove(0);

		}
		return null;
	}

	public void write(File f, String s, String name) throws IOException {
		BufferedWriter bw = new BufferedWriter(new FileWriter(f, true));
		// for(int i=0;i<al.size();i++){
		// bw.write(al.get(i));
		// bw.newLine();
		bw.write(s + " " + name + "\n");
		// }
		bw.close();

	}

	public void writeHtml(File f, Document doc) throws IOException {
		BufferedWriter bw = new BufferedWriter(new FileWriter(f, true));
		bw.write(doc.html() + "\n");
		// }
		bw.close();

	}

	// 爬网页sUrl
	public void crawler(Main site) {
		try {
			String url = site.getHomePage();
			// url = url + "/following";

			System.out.println("########Fetching:" + url + "     还剩" + notCrawlSiteSet.size() + "个链接");
			Document doc = Jsoup.connect(url).followRedirects(true).timeout(10000)
					.userAgent("Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.0)").get();

			Elements links = doc.select("#visitor").select("a.u").select("a[href]");

			int d = depth.get(site.getHomePage());
			System.out.println("爬网页" + site.getHomePage() + " " + site.getName() + "成功，深度为" + d + " 是由线程"
					+ Thread.currentThread().getName() + "来爬");
			System.out.println("临时有数量：" + allSiteSet.size());
			System.out.println();

			if (d < crawDepth) {
				parseContext2(links, d + 1, site);
			}
			// System.out.println(sb.toString());

		} catch (IOException e) {
			if (depth.get(site.getHomePage()) == 1)
				allSiteSet.clear();
			e.printStackTrace();
		}
	}

	// 从context提取url地址
	public void parseContext2(Elements links, int dep, Main parseSite) throws IOException {
		ArrayList<Main> mainSet = new ArrayList<Main>();
		for (Element link : links) {
			String str = link.attr("abs:href");

			Main tmp = new Main();
			tmp.setHomePage(str);
			tmp.setName(link.text());
			tmp.setDepth(dep);
			// tmp.setFollowNum(links.size());

			// if (!allSiteSet.contains(tmp)) {
			if (!depth.containsKey(tmp.getHomePage())) {
				if (dep < crawDepth)
					addSite(tmp, dep);
				mainSet.add(tmp);
				num.put(tmp.getName(), 1);
				visterNum++;
			} else {
				existNum++;
				System.out.println(tmp.getName() + "已存在！");

				
				if (dep < crawDepth)
					addSite(tmp, dep);
				
				System.out.println(num.get(tmp.getName()));
				int n = num.get(tmp.getName()) + 1;
				num.put(tmp.getName(), n);
			}

			if (count > 0) { // 如果有等待的线程，则唤醒
				synchronized (signal) { // ---------------------（2）
					count--;
					signal.notify();
				}
			}

		}

		f = new File("F:/hou/test/" + "hupuVister" + ".txt");
		writeFile(f, mainSet);
	}

	private void writeFile(File f, ArrayList<Main> mainSet) throws IOException {
		BufferedWriter bw = new BufferedWriter(new FileWriter(f, true));

		for (Main mainToSave : mainSet) {
			bw.write(mainToSave.getHomePage() + "\t" + mainToSave.getName() + "\t" + mainToSave.getDepth() + "\r\n");
		}
		bw.close();

	}

}
