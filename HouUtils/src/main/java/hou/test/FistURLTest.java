package hou.test;

/**
 * @author houweitao
 * @date 2015年12月4日 下午1:12:29
 */

public class FistURLTest {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		FistURLTest test = new FistURLTest();
		String url = "http://bbs.hupu.com/3712591.html";
		url = test.changeUrl(url);
		System.out.println(url);
	}

	String changeUrl(String url) {
		if (!url.contains("-")) {
			url = url.replace(".html", "-1.html");
		}
		System.out.println(url);
		return url;
	}
}
