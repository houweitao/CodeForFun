package hou.ipProxy.voteHupu;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

/**
 * @author houweitao
 * @date 2016年1月15日 下午10:58:47
 * @end 2016年1月15日23:12:47
 * http://www.xicidaili.com/
 */

public class GetXiciDaili {

	public List<Proxy> getAll() {
		List<Proxy> ret = new LinkedList<>();
		ArrayList<String> source = new ArrayList<>();
		source.add("http://www.xicidaili.com/nn/");
		source.add("http://www.xicidaili.com/nt/");
		source.add("http://www.xicidaili.com/wn/");
		source.add("http://www.xicidaili.com/wt/");

		for (int i = 0; i < source.size(); i++) {
			ret.addAll(get(source.get(i)));
		}

		return ret;
	}

	private List<Proxy> get(String url) {
		List<Proxy> ret = new LinkedList<>();

		for (int i = 1; i <= 10; i++) {
			Document doc;
			try {
				doc = Jsoup.connect(url + i).followRedirects(true).timeout(10000)
						.userAgent("Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.0)").get();
				Elements links = doc.select("#ip_list").select("tr");

				for (int j = 1; j < links.size(); j++) {
					String[] after = links.get(j).text().split(" ");
					Proxy proxy = new Proxy(after[0], after[1]);
					ret.add(proxy);
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return ret;
	}
}
