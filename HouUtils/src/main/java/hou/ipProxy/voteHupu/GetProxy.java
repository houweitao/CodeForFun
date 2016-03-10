package hou.ipProxy.voteHupu;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 * @author houweitao
 * @date 2016年1月13日 上午11:30:58
 * 从： http://www.youdaili.net/Daili/ 获取代理
 */

public class GetProxy {
//	public static void main(String[] args) {
//		GetProxy proxy = new GetProxy();
//		List<Proxy> ret = new LinkedList<>();
//		String url = "http://www.youdaili.net/Daili/http/4080.html";
//		for (int i = 1; i <= 6; i++) {
//			if (i != 1)
//				url = "http://www.youdaili.net/Daili/http/4080_" + i + ".html";
////			ret = proxy.get();
//			ret.addAll(proxy.get(url));
//		}
//
//		for (Proxy p : ret)
//			System.out.println(p.toString());
//
//		System.out.println(ret.size());
//	}

	List<Proxy> getAllProxy() {
		List<Proxy> ret = new LinkedList<>();
		ArrayList<String> url = null;
		url = getSourcePage();

		for (int k = 0; k < url.size(); k++) {
			for (int i = 1; i <= 6; i++) {
				String toRead = url.get(k);
				if (i != 1) {
					toRead = toRead.replace(".html", "_");
					toRead = toRead + i + ".html";
				}
				ret.addAll(get(toRead));
			}
		}

		return ret;
	}

	public ArrayList<String> getSourcePage() {
		ArrayList<String> ret = new ArrayList<>();
		try {
			Document doc = Jsoup.connect("http://www.youdaili.net/Daili/").followRedirects(true).timeout(10000)
					.userAgent("Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.0)").get();

			Elements links = doc.select(".newslist_line").select("li");
			for (Element e : links)
				ret.add(e.select("a[href]").attr("abs:href"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return ret;
	}

	List<Proxy> get(String url) {
//		String url = "http://www.youdaili.net/Daili/http/4080.html";

		System.out.println("抓取： " + url);

		List<Proxy> ret = new LinkedList<>();
		Document doc;
		try {
			doc = Jsoup.connect(url).followRedirects(true).timeout(10000)
					.userAgent("Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.0)").get();
			Elements links = doc.select(".cont_font").select("p");

			System.out.println(links.toString());
			if (links != null)
				ret = make(links.toString());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return ret;
	}

	List<Proxy> make(String str) {
		List<Proxy> ret = new LinkedList<>();
		String[] after = str.split("<br /> ");
		for (int i = 0; i < after.length; i++) {
			if (true) {
				String m = after[i].split("@")[0];
				System.out.println(m);
				String[] r = m.split(":");

				if (r.length == 2)
					ret.add(new Proxy(r[0], r[1]));
			}
		}

		return ret;
	}
}
