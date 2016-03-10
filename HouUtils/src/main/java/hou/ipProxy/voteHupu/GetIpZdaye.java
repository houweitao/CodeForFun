package hou.ipProxy.voteHupu;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

/**
 * @author houweitao
 * @date 2016年1月15日 下午11:19:08
 * to do: http://ip.zdaye.com/ http://blog.csdn.net/problc/article/details/5794460
 */

public class GetIpZdaye {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		GetIpZdaye get=new GetIpZdaye();
		get.get("http://ip.zdaye.com/");
	}

	private List<Proxy> get(String url) {
		List<Proxy> ret = new LinkedList<>();

		Document doc;
		try {
			doc = Jsoup.connect(url).followRedirects(true).timeout(10000)
					.userAgent("Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.0)").get();
			Elements links = doc.select("tbody").select("tr");

			for (int j = 1; j < links.size(); j++) {
				System.out.println(links.get(j).select("td").first().text());
//				String[] after = links.get(j).text().split(" ");
//				Proxy proxy = new Proxy(after[0], after[1]);
//				ret.add(proxy);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return ret;
	}
}
