package hou.SthInteresting.CrawlerFanfou.gzallen;

/**
 * @author houweitao
 * @date 2016年1月31日 上午12:04:48
 */

public class Mark {
	private String author;
	private String content;
	private String time;

	Mark(String author, String content, String time) {
		this.author = author;
		this.content = content;
		this.time = time;
	}

	Mark(String author) {
		this.author = author;
		this.content = "此消息已删除或不公开";
		this.time = "hide";
	}

	@Override
	public String toString() {
		if (!time.equals("hide"))
			return author + " , " + content + " , " + time;
		else
			return author + " , " + content;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}
}
