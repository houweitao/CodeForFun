package hou.ipProxy.voteHupu;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 * @author houweitao
 * @date 2016年1月13日 下午1:10:00
 * http://www.kuaidaili.com/
 */

public class GetKuaiDaili {

	List<Proxy> get() {
		String url = "http://www.kuaidaili.com/proxylist/";
		List<Proxy> ret = new LinkedList<>();
		Document doc;

		for (int i = 1; i <= 10; i++) {
			try {
				doc = Jsoup.connect(url + i).followRedirects(true).timeout(10000)
						.userAgent("Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.0)").get();
				Elements links = doc.select("tbody").select("tr");

				for (Element e : links) {
					String[] text = e.text().split(" ");
					ret.add(new Proxy(text[0], text[1]));
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return ret;
	}
}
