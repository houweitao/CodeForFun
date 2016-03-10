package hou.test;

import java.io.IOException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 * @author houweitao
 * @date 2015年11月25日 下午3:54:13
 */

public class ClawerWeiXin {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		String url="http://mp.weixin.qq.com/s?__biz=MzA3MzE3MTcxMQ==&mid=400600543&idx=3&sn=1c1558b51b0844f327a279e0f123f439&3rd=MzA3MDU4NTYzMw==&scene=6#rd";
//		try {
//			String text = Jsoup.connect(url).followRedirects(true).timeout(10000)
//					.userAgent("Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.0)").get()
//					.select("div.rich_media_content ")
//					.text();
//			System.out.println(text);
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		
		
		url="http://weixin.sogou.com/share?ie=utf-8&query=%E6%AD%A6%E5%A4%A7";
		try {
			Document doc= Jsoup.connect(url).followRedirects(true).timeout(10000)
					.userAgent("Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.0)").get();
			System.out.println(doc.title());
			
			Elements titles=doc.select("div.txt-box");
			
			for(Element title:titles){
				System.out.println(title.text());
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
