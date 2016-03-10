package hou.ipProxy.voteHupu;

import java.io.IOException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import com.alibaba.fastjson.JSON;

/**
 * @author houweitao
 * @date 2016年1月13日 下午4:38:08
 */

public class DealJson {

	public static void main(String[] args) throws IOException {
		String url = "http://snsvote.hupu.com/InsertVote_new.php?enews=InsertVote&voteid=279621&vote=2&jsoncallback=jsonp1452668502846&_=1452668591792";
		Document doc = Jsoup.connect(url).followRedirects(true).timeout(1500)
				.userAgent("Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.0)").get();

		String response = doc.select("body").text();
		System.out.println(response);

		System.out.println(response.substring(19).replace(")", ""));
		String text=response.substring(19).replace(")", "");
		Response re=JSON.parseObject(text, Response.class);
		System.out.println(re.toString());

		if(re.getCode().equals("-10"))
			System.out.println("失败");
	}

}
