package hou.ipProxy.voteHupu;

/**
 * @author houweitao
 * @date 2016年1月13日 下午3:29:48
 */

public class ReplaceTest {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		String[] url = { "http://www.youdaili.net/Daili/http/4075.html", "http://www.youdaili.net/Daili/QQ/4083.html",
				"http://www.youdaili.net/Daili/Socks/4079.html", "http://www.youdaili.net/Daili/http/4080.html" };
		for (int k = 0; k < url.length; k++) {
			for (int i = 1; i <= 6; i++) {
				String toRead = url[k];
				if (i != 1) {
					toRead = toRead.replace(".html", "_");
					toRead = toRead + i + ".html";
				}
				System.out.println(toRead);
			}
		}
	}

}
