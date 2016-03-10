package hou.SthInteresting.CrawlerFanfou.gzallen;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.jsoup.Connection;
import org.jsoup.Connection.Method;
import org.jsoup.Connection.Response;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 * @author houweitao
 * @date 2016年1月30日 下午10:42:42
 * @end 2016年1月30日23:49:29
 */

public class Crawler {

	public static void main(String[] args) throws Exception {
		Crawler c = new Crawler();
		String userPageUrl = "http://fanfou.com/~RLhcIDBjZAM";
		c.get(userPageUrl);
	}

	void get(String url) throws Exception {
		Map<String, String> cookies = getCookies("mail", "password");// 输入饭否账号和密码
		System.out.println(cookies.toString());

		String userName = getUserName(url, cookies);

		File file = new File("result/");
		if (!file.exists())
			file.mkdirs();

		List<Message> messageList = getMessage(cookies, url);
		saveStatus(messageList, userName);

		List<Mark> markList = getMark(cookies, url);
		saveMark(markList, userName);
	}

	private String getUserName(String url, Map<String, String> cookies) throws IOException {
		Document doc = Jsoup.connect(url).followRedirects(true).cookies(cookies).timeout(10000)
				.userAgent("Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.0)").get();
		String userName = doc.select("#info").select("#panel").select("h1").first().text();
		return userName;
	}

	private void saveStatus(List<Message> messageList, String userName) throws IOException {
		File file = new File("result/" + userName + " status.txt");
		if (!file.exists())
			file.createNewFile();
		else {
			file.delete();
			file.createNewFile();
		}

		int num = messageList.size();
		for (int i = num - 1; i >= 0; i--) {
			String conent = (num - i) + " : time@" + messageList.get(i).getTime() + "  "
					+ messageList.get(i).getMessage() + "\r\n";
			write(file.getAbsolutePath(), conent);
		}
	}

	private void saveMark(List<Mark> markList, String userName) throws IOException {
		File file = new File("result/" + userName + " mark.txt");
		if (!file.exists())
			file.createNewFile();
		else {
			file.delete();
			file.createNewFile();
		}

		int num = markList.size();
		for (int i = num - 1; i >= 0; i--) {
			String conent = (num - i) + " : " + markList.get(i).toString() + "\r\n";
			write(file.getPath(), conent);
		}
	}

	void write(String file, String conent) {
		BufferedWriter out = null;
		try {
			out = new BufferedWriter(new FileWriter(file, true));
			out.write(conent);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (out != null) {
					out.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	List<Message> getMessage(Map<String, String> cookies, String userPageUrl) {
		LinkedList<Message> list = new LinkedList<>();
		String status = userPageUrl + "/p.";
		int i = 1;
		boolean stop = false;
		while (!stop) {
			String url = status + i++;
			System.out.println(url);
			try {
				Document doc = Jsoup.connect(url).followRedirects(true).cookies(cookies).timeout(10000)
						.userAgent("Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.0)").get();

				Element exist = doc.select(".message").first();
				if (exist == null) {
					System.out.println("no more status.. end of crawler");
					stop = true;
				} else {
					Elements messages = exist.select("li");
					System.out.println(messages.size());
					for (Element e : messages) {
						String message = e.select(".content").first().text();
						String time = e.select(".time").first().text();
						String method = e.select(".method").first().text();

						Message mess = new Message(message, time, method);
						System.out.println(mess.toString());
						list.add(mess);
					}
				}

			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}

		return list;
	}

	List<Mark> getMark(Map<String, String> cookies, String userPageUrl) {
		LinkedList<Mark> list = new LinkedList<>();

//		http://fanfou.com/~RLhcIDBjZAM
//		http://fanfou.com/favorites/~RLhcIDBjZAM/p.2

		String status = userPageUrl.split("/~")[0] + "/favorites/~" + userPageUrl.split("/~")[1] + "/p.";
		int i = 1;
		boolean stop = false;
		while (!stop) {
			String url = status + i++;
			System.out.println(url);
			try {
				Document doc = Jsoup.connect(url).followRedirects(true).cookies(cookies).timeout(10000)
						.userAgent("Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.0)").get();

				Element exist = doc.select(".message").first();
				if (exist == null) {
					System.out.println("no more mark.. end of crawler");
					stop = true;
				} else {
					Elements messages = exist.select("li");
					System.out.println(messages.size());
					for (Element e : messages) {
						Mark m = null;
						String author = e.select(".author").first().text();
						String content = e.select(".content").first().text();
						if (content.contains("此消息已删除或不公开")) {
							m = new Mark(author);
						} else {
							String time = e.select(".time").first().text();
							m = new Mark(author, content, time);
						}
						System.out.println(m.toString());
						list.add(m);
					}
				}

			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}

		return list;
	}

	public Map<String, String> getCookies(String userName, String pwd) throws Exception {
		String loginPage = "http://fanfou.com/";

		// 第一次请求
		Connection con = Jsoup.connect(loginPage);// 获取连接
		con.header("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:29.0) Gecko/20100101 Firefox/29.0");// 配置模拟浏览器
		Response rs = con.execute();// 获取响应
		Document d1 = Jsoup.parse(rs.body());// 转换为Dom树

//		System.out.println(d1.select("body"));

		List<Element> et = d1.select("div.sect.first-sect");// 获取form表单，可以通过查看页面源码代码得知

		// 获取，cooking和表单属性，下面map存放post时的数据
		Map<String, String> datas = new HashMap<>();
		for (Element e : et.get(0).getAllElements()) {
			if (e.attr("name").equals("loginname")) {
				e.attr("value", userName);// 设置用户名
			}

			if (e.attr("name").equals("loginpass")) {
				e.attr("value", pwd); // 设置用户密码
			}

			if (e.attr("name").length() > 0) {// 排除空值表单属性
				datas.put(e.attr("name"), e.attr("value"));
			}
		}

		/**
		 * 第二次请求，post表单数据，以及cookie信息
		 * 
		 **/
		Connection con2 = Jsoup.connect(loginPage);
		con2.header("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:29.0) Gecko/20100101 Firefox/29.0");
		// 设置cookie和post上面的map数据
		Response login = con2.ignoreContentType(true).method(Method.POST).data(datas).cookies(rs.cookies())
				.timeout(100000).execute();
				// 打印，登陆成功后的信息
//		System.out.println(login.body());

		// 登陆成功后的cookie信息，可以保存到本地，以后登陆时，只需一次登陆即可
		Map<String, String> map = login.cookies();
//		for (String s : map.keySet()) {
//			System.out.println(s + "      " + map.get(s));
//		}

		return map;
	}
}
