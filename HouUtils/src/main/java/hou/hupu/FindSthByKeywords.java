package hou.hupu;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 * @author houweitao
 * @date 2015年11月19日 下午4:00:30
 * @end 2015年11月19日17:22:12
 * @to 从虎扑指定帖子找指定内容
 */

public class FindSthByKeywords {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		FindSthByKeywords find = new FindSthByKeywords();

		Properties prop = new Properties();
		InputStream in = Object.class.getResourceAsStream("/FindSthByKeyword.properties");
		try {
			prop.load(in);
			String url = prop.getProperty("url").trim();
			String keyword = prop.getProperty("keyword").trim();

			System.out.println("find.." + keyword + " from: " + url);
			find.Find(url, keyword);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	void Find(String url, String keyword) throws IOException {
		if (!url.contains("-")) {
			url = url.replace(".html", "-1.html");
		}
//		System.out.println(url);

		String pre = null;
		boolean end = false;

		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH,mm,ss");// 设置日期格式

		File fileName = new File("doc//"+keyword + df.format(new Date()) + ".txt");
		if (!fileName.exists()) {
			fileName.createNewFile();
		}

		while (!end) {
			BufferedWriter bw = new BufferedWriter(new FileWriter(fileName, true));
			try {
				Document doc = Jsoup.connect(url).followRedirects(true).timeout(10000)
						.userAgent("Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.0)").get();
				Elements links = doc.select("div.floor").select(".floor_box");
				Elements names = doc.select("div.floor").select("uname");

//				System.out.println(url);

				String judge = links.get(0).text();
				// 判断是否终止
				if (judge.equals(pre))
					end = true;
				else
					pre = judge;

				for (Element link : links) {
//					System.out.println(link.text());
					if (link.text().contains(keyword)) {
						System.out.println(link.select(".case").text());
						bw.write(link.select("a.u").first().text() + "  " + link.select(".stime").text() + "\r\n"
								+ link.select(".case").text() + ", From: " + url + "\r\n" + "\r\n");
					}
				}

				url = getNext(url);
//				try {
//					Thread.sleep(1500);
//				} catch (InterruptedException e) {
//					// TODO Auto-generated catch block
//					bw.write("休眠失败.." + " @: " + url + "\r\n");
//					e.printStackTrace();
//				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			bw.close();
		}
	}

	String getNext(String url) {
		String s = url.split("-")[1].split("\\.")[0];
		String n = String.valueOf(Integer.valueOf(s) + 1);

		url = url.replace("-" + s, "-" + n);
//		System.out.println(url);
		return url;
	}
}
