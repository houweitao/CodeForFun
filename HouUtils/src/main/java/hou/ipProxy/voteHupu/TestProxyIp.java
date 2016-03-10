package hou.ipProxy.voteHupu;

import java.io.IOException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.BufferedInputStream;
import java.net.URL;
import java.net.URLConnection;

/**
 * @author houweitao
 * @date 2016年1月13日 上午10:44:31
 * 测试设置ip和端口。http://www.iteblog.com/archives/118
 */

public class TestProxyIp {
	private static final Logger log = LoggerFactory.getLogger(TestProxyIp.class);

	public static void main(String[] args) throws IOException {
		System.setProperty("http.maxRedirects", "50");
		System.getProperties().setProperty("proxySet", "true");
		// 如果不设置，只要代理IP和代理端口正确,此项不设置也可以
		String ip = "93.91.200.146";
		ip = "221.130.18.5";
		ip = "221.130.23.135";
		ip = "221.130.18.78";
		ip = "221.130.23.134";
		ip = "221.130.18.49";
		ip = "111.1.32.36";
		ip = "221.130.18.49";
		ip = "221.130.18.49";
		ip = "58.252.8.25";
		System.getProperties().setProperty("http.proxyHost", ip);
		System.getProperties().setProperty("http.proxyPort", "8000");

		//确定代理是否设置成功
		log.info(getHtml("http://www.ip138.com/ip2city.asp"));

		getIP();
		testVote();
//		vote();
	}

	static void getIP() {
		String url = "http://www.mayidaili.com/free/2";
		try {
			Document doc = Jsoup.connect(url).followRedirects(true).timeout(10000)
					.userAgent("Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.0)").get();

			Elements links = doc.select("tbody").select("tr");
			
			for (Element element : links) {
				System.out.println(element.select("td").get(0));
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private static void testVote() throws IOException {
		// TODO Auto-generated method stub
		Document doc = Jsoup
				.connect(
						"http://snsvote.hupu.com/InsertVote_new.php?enews=InsertVote&voteid=279524&vote=1&jsoncallback=jsonp1452654306177&_=1452654390177")
				.followRedirects(true).timeout(10000).userAgent("Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.0)")
				.get();

		System.out.println("--------------------");
		System.out.println(doc.toString());
	}

	private static void vote() {
		// TODO Auto-generated method stub
		String str = "http://www.ip138.com/ip2city.asp";

		try {
			URL url = new URL(str);
			URLConnection conn = url.openConnection();
			conn.setRequestProperty("User-Agent",
					"Mozilla/4.0 (compatible; MSIE 7.0; NT 5.1; GTB5; .NET CLR 2.0.50727; CIBA)");
			BufferedInputStream in = new BufferedInputStream(conn.getInputStream());
			System.out.println(in.toString());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private static String getHtml(String address) {
		StringBuffer html = new StringBuffer();
		String result = null;
		try {
			URL url = new URL(address);
			URLConnection conn = url.openConnection();
			conn.setRequestProperty("User-Agent",
					"Mozilla/4.0 (compatible; MSIE 7.0; NT 5.1; GTB5; .NET CLR 2.0.50727; CIBA)");
			BufferedInputStream in = new BufferedInputStream(conn.getInputStream());
			System.out.println("in");
			System.out.println(in.toString());
			try {
				String inputLine;
				byte[] buf = new byte[4096];
				int bytesRead = 0;
				while (bytesRead >= 0) {
					inputLine = new String(buf, 0, bytesRead, "ISO-8859-1");
					html.append(inputLine);
					bytesRead = in.read(buf);
					inputLine = null;
				}
				buf = null;
			} finally {
				in.close();
				conn = null;
				url = null;
			}
			result = new String(html.toString().trim().getBytes("ISO-8859-1"), "gb2312").toLowerCase();

		} catch (Exception e) {
			e.printStackTrace();
			return null;
		} finally {
			html = null;
		}
		return result;
	}
}
