package hou.ipProxy.voteHupu;

/**
 * @author houweitao
 * @date 2016年1月13日 上午11:31:18
 * IP的地址和端口的类
 */

public class Proxy {
	String host;
	String port;

	Proxy(String host, String port) {
		this.host = host;
		this.port = port;
	}

	public String toString() {
		return host + ":" + port;
	}

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public String getPort() {
		return port;
	}

	public void setPort(String port) {
		this.port = port;
	}
}
