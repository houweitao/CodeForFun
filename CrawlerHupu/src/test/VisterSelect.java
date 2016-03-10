package test;

import java.io.IOException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 * @author houweitao 2015年8月5日 上午9:51:19
 */

public class VisterSelect {

	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		String url = "http://my.hupu.com/3157587";
		// url = url + "/following";

		Document doc = Jsoup.connect(url).followRedirects(true).timeout(10000)
				.userAgent("Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.0)").get();

		Elements links = doc.select("#visitor").select("a.u").select("a[href]");

		System.out.println(links);

		for (Element link : links) {
			String str = link.attr("abs:href");
			System.out.println(str);
			System.out.println(link.text());
		}

	}

}
