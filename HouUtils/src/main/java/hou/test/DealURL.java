package hou.test;

import java.io.IOException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 * @author houweitao
 * @date 2015年11月19日 下午11:52:46
 */

public class DealURL {

	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		String url = "http://bbs.hupu.com/14402999-1.html";
		DealURL du = new DealURL();
		du.dealUrl(url);
		du.getNext(url);
		
		
		Document doc = Jsoup.connect(url).followRedirects(true).timeout(10000)
				.userAgent("Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.0)").get();
		Elements links = doc.select("div.floor").select(".floor_box").select(".case");
		Elements names = doc.select("div.floor").select(".user").select("div.j_u");
//		System.out.println(names);
		if(names.size()==0)
			System.out.println("null");
		
		for(Element name:names)
			System.out.println(name.attr("uname"));
	}

	void dealUrl(String url) {
		if (!url.contains("-")) {
			System.out.println("not contains..");
			url = url.replace(".html", "-1.html");
		}
		System.out.println(url);
	}

	String getNext(String url) {
		String s = url.split("-")[1].split("\\.")[0];
		String n = String.valueOf(Integer.valueOf(s) + 1);
		
		url = url.replace("-"+s, "-"+n);
		System.out.println(url);
		return url;
	}
}
