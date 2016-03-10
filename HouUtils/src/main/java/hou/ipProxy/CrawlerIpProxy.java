package hou.ipProxy;

import java.io.IOException;

import org.apache.commons.httpclient.Credentials;
import org.apache.commons.httpclient.DefaultHttpMethodRetryHandler;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.UsernamePasswordCredentials;
import org.apache.commons.httpclient.auth.AuthScope;
import org.apache.commons.httpclient.cookie.CookiePolicy;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.params.HttpMethodParams;

/**
 * @author houweitao
 * @date 2016年1月13日 上午9:45:39
 */

public class CrawlerIpProxy {
	HttpClient httpClient = new HttpClient();
	// 设置 Http 连接超时 5s
	String Hosturl = "www.baidu.com";
	int proxyPort = proxyServer.proxyPort[0];

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		CrawlerIpProxy test = new CrawlerIpProxy();
		test.work();
	}

	void work() {
		httpClient.getHttpConnectionManager().getParams().setConnectionTimeout(5000);
		int ret = testProxyServer(Hosturl, "111.40.195.11", 8080);
		System.out.println(ret);
	}

	/**
	* 设置代理
	*/
	private void setProxy(String proxyIP, int hostPort) {
		System.out.println("正在设置代理：" + proxyIP + ":" + hostPort);
		// TODO Auto-generated method stub
		httpClient.getHostConfiguration().setHost(Hosturl, hostPort, "http");
		httpClient.getParams().setCookiePolicy(CookiePolicy.BROWSER_COMPATIBILITY);
		httpClient.getHostConfiguration().setProxy(proxyIP, proxyPort);
		Credentials defaultcreds = new UsernamePasswordCredentials("", "");
		httpClient.getState().setProxyCredentials(new AuthScope(proxyIP, proxyPort, null), defaultcreds);
	}

	/**
	* @param string
	* @param i
	*
	* 测试代理服务器的可用性
	*
	* 只有返回HttpStatus.SC_OK才说明代理服务器有效
	* 其他的都是不行的
	*/
	private int testProxyServer(String url, String proxyIp, int proxyPort) {
		// TODO Auto-generated method stub
		setProxy(proxyIp, proxyPort);
		GetMethod getMethod = setGetMethod(url);
		if (getMethod == null) {
			System.out.println("请求协议设置都搞错了，所以我无法完成您的请求");
			System.exit(1);
		}

		System.out.println("??");
		try {
			int statusCode = httpClient.executeMethod(getMethod);
			System.out.println(statusCode);
			if (statusCode == HttpStatus.SC_OK) { //2XX状态码
				return HttpStatus.SC_OK;
			} else if (statusCode == HttpStatus.SC_FORBIDDEN) { //代理还是不行
				return HttpStatus.SC_FORBIDDEN;
			} else { //	其他的错误
				return 0;
			}
		} catch (HttpException e) {
			// 发生致命的异常，可能是协议不对或者返回的内容有问题
			System.out.println("Please check your provided http address!");
			System.exit(1);
		} catch (IOException e) {
			// 发生网络异常
			System.out.println("???????");
			System.exit(1);
		} finally {
			// 释放连接
			getMethod.releaseConnection();
		}
		System.out.println("??");
		return 0;
	}

	private GetMethod setGetMethod(String url) {
		// TODO Auto-generated method stub
		/* 2.��� GetMethod �������ò��� */
		GetMethod getMethod = null;
		try {
			//���ܻ��ڲ�ѯ��ʱ������쳣�����Ǽ򵥵Ķ�ȥ
			getMethod = new GetMethod(url);
		} catch (IllegalArgumentException e) {
			return null;
		}

		// ���� get ����ʱ 5s
		getMethod.getParams().setParameter(HttpMethodParams.SO_TIMEOUT, 5000);
		// �����������Դ���
		getMethod.getParams().setParameter(HttpMethodParams.RETRY_HANDLER, new DefaultHttpMethodRetryHandler());
		//Mozilla/5.0 (Windows; U; Windows NT 5.2) Gecko/2008070208 Firefox/3.0.1
		//Mozilla/5.0 (Windows; U; Windows NT 5.1) Gecko/20070309 Firefox/2.0.0.3
		//Mozilla/5.0 (Windows; U; Windows NT 5.1) Gecko/20070803 Firefox/1.5.0.12
		//Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.0; WOW64; Trident/4.0; SLCC1)
		//Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.2; Trident/4.0; .NET CLR 1.1.4322; .NET CLR 2.0.50727)
		//Mozilla/5.0 (Windows; U; Windows NT 5.2) AppleWebKit/525.13 (KHTML, like Gecko) Chrome/0.2.149.27 Safari/525.13
		//����USER_AGENT
		getMethod.getParams().setParameter(HttpMethodParams.USER_AGENT,
				"Mozilla/5.0 (Windows; U; Windows NT 5.1) Gecko/20070803 Firefox/1.5.0.12");
		return getMethod;
	}
}

class proxyServer {
	public static String proxyIP[] = { "ec2-23-22-95-3.compute-1.amazonaws.com", "211.68.70.169", "202.203.132.29",
			"218.192.175.84", "ec2-50-16-197-120.compute-1.amazonaws.com",
			"50.22.206.184-static.reverse.softlayer.com", };
	public static int proxyPort[] = { 8000, 3128, 3128, 3128, 8001, 8080 };
}