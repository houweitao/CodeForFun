package test;

import java.io.IOException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 * @author houweitao 2015年8月4日 上午10:29:39
 */

public class SelectTest {

	public static void main(String[] args) {
		// TODO Auto-generated method stub

		String url = "http://my.hupu.com/BelieveMyself";

		SelectTest st = new SelectTest();
		// st.select(url);
		st.zhudui(url);

	}

	void zhudui(String url) {
		Document doc;
		try {
			doc = Jsoup.connect(url).followRedirects(true).timeout(10000)
					.userAgent("Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.0)").get();

			String zhudui = null;
			Elements links = doc.select(".personalinfo");
			System.out.println(links);

			for (Element link : links) {
				System.out.println(link.text());

				if (link.toString().contains("NBA主队")) {
					zhudui = link.text();
					System.out.println(zhudui);

					int i = 0;
					while (i < 20) {
						System.out.print(zhudui.charAt(i));
						i++;
						if(String.valueOf(zhudui.charAt(i)).contains("C"))
							i=20;
							
					}

				}
			}

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	void select(String url) {
		url = url + "/following";

		Document doc;
		try {
			doc = Jsoup.connect(url).followRedirects(true).timeout(10000)
					.userAgent("Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.0)").get();

			Elements links = doc.select("div.contact_item_main").select("a[href]");

			for (Element link : links) {
				String str = link.attr("abs:href");
				System.out.println(str);
				// System.out.println(link.text());
			}

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
