package hou.SthInteresting.CrawlerFanfou.gzallen;

/**
 * @author houweitao
 * @date 2016年1月30日 下午11:01:19
 */

public class Message {
	private String message;
	private String time;
	private String method;

	Message(String message, String time, String method) {
		this.message = message;
		this.time = time;
		this.method = method;
	}

	@Override
	public String toString() {
		return message + " , " + time + " , " + method;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public String getMethod() {
		return method;
	}

	public void setMethod(String method) {
		this.method = method;
	}
}
