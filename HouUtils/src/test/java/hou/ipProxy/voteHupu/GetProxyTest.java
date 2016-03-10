package hou.ipProxy.voteHupu;

import java.util.List;

import org.junit.Test;

/**
 * @author houweitao
 * @date 2016年1月13日 下午3:07:05
 */

public class GetProxyTest {

	public static void main(String[] args) {
		GetProxyTest test = new GetProxyTest();
		test.testGetAll();
	}

	void testGetPage() {
		GetProxy proxy = new GetProxy();
		proxy.getSourcePage();
	}

	public void testGetAll() {
		GetProxy proxy = new GetProxy();
		List<Proxy> list = proxy.getAllProxy();
		for (Proxy p : list)
			System.out.println(p.toString());

		System.out.println(list.size());
	}
}
