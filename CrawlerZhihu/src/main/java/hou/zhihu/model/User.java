package hou.zhihu.model;

/**
 * @author houweitao
 * @date 2016年1月21日 下午3:20:36
 */

public class User {
	String id;
	String url;
	String userName;
	int followNum;
	int followerNum;
	String address;
	String job;
	String weibo;

	int searchNum;
	int depth;

	public User() {

	}

	public User(String url) {
		this.url = url;
		this.depth = 1;
	}

	public User(String url, String newName, int searchNum, int depth) {
		this.url = url;
		this.userName = newName;
		this.searchNum = searchNum;
		this.depth = depth;
	}

	public User(String url, String newName, int searchNum, int depth, String address, String job, String weibo,
			int followNum, int followerNum) {
		this.url = url;
		this.userName = newName;
		this.searchNum = searchNum;
		this.depth = depth;
		this.address = address;
		this.job = job;
		this.weibo = weibo;
		this.followerNum = followerNum;
		this.followNum = followNum;
	}

	public User addInfomation() {
		if (address == null)
			setAddress("null");
		if (job == null)
			setJob("null");
		if (weibo == null)
			setWeibo("null");

		return this;
	}

	public String toString() {
		return "用户名： " + userName + " , url: " + url + " , 深度： " + depth + " , 地址： " + address + " , 工作： " + job
				+ " , 微博：" + weibo + " , 粉丝数量： " + followerNum + " , 关注人数： " + followNum;
	}

	public int getSearchNum() {
		return searchNum;
	}

	public void setSearchNum(int searchNum) {
		this.searchNum = searchNum;
	}

	public int getDepth() {
		return depth;
	}

	public void setDepth(int depth) {
		this.depth = depth;
	}

	public String getId() {
		return id;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public int getFollowNum() {
		return followNum;
	}

	public void setFollowNum(int followNum) {
		this.followNum = followNum;
	}

	public int getFollowerNum() {
		return followerNum;
	}

	public void setFollowerNum(int followerNum) {
		this.followerNum = followerNum;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getJob() {
		return job;
	}

	public void setJob(String job) {
		this.job = job;
	}

	public String getWeibo() {
		return weibo;
	}

	public void setWeibo(String weibo) {
		this.weibo = weibo;
	}

}
