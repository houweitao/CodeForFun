package hou.ipProxy.voteHupu;

/**
 * @author houweitao
 * @date 2016年1月13日 下午4:11:37
 * 获取返回的json：例子，jsonp1452677173408({"code":-10,"info":"\u4f60\u5df2\u7ecf\u6295\u8fc7"})
 */

public class Response {
	String code;
	String info;

	public Response() {

	}

	public Response(String code, String info) {
		this.code = code;
		this.info = info;
	}

	public String toString() {
		return code + " , " + info;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getInfo() {
		return info;
	}

	public void setInfo(String info) {
		this.info = info;
	}
}
