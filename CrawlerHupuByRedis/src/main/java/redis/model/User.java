package redis.model;

import org.springframework.core.style.ToStringCreator;

/**
 * @author houweitao
 * @date 2016年1月14日 下午3:16:02
 */

public class User {
	private String username;
	private String url;
	private int searchNum;
	private int depth;

	private int id;
	private String gender;
	private String birthdate;
	private int level;
	private String group;
	private String cash;
	private String onlineTime;
	private String registerTime;
	private String lastLoginTime;
	private String selfIntroduce;
	private String address;

	public User() {

	}

	public User(String url, String username, int searchNum, int depth) {
		this.url = url;
		this.searchNum = searchNum;
		this.username = username;
		this.depth = depth;
	}

	public String toString() {
		return "用户名： " + username + " , url：" + url + " , 深度： " + depth + " , 搜索数量： " + searchNum;
	}

	public String userInfo() {
		return "用户名： " + username + " , url：" + url + " , ID： " + id + " , 级别： " + level + " , 性别： " + gender
				+ " , 生日 ：" + birthdate + " , 地址： " + address + " , 注册时间： " + registerTime + " , 自我介绍： " + selfIntroduce
				+ " , +在线时间: " + onlineTime;
	}

//	public int hashCode() {
//		return usename;
//	}

	@Override
	public boolean equals(Object obj) {
		boolean bres = false;
		if (obj instanceof User) {
			User o = (User) obj;
			bres = (this.url.equals(o.url));
		}
		return bres;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
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

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public String getBirthdate() {
		return birthdate;
	}

	public void setBirthdate(String birthdate) {
		this.birthdate = birthdate;
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public String getGroup() {
		return group;
	}

	public void setGroup(String group) {
		this.group = group;
	}

	public String getCash() {
		return cash;
	}

	public void setCash(String cash) {
		this.cash = cash;
	}

	public String getOnlineTime() {
		return onlineTime;
	}

	public void setOnlineTime(String onlineTime) {
		this.onlineTime = onlineTime;
	}

	public String getRegisterTime() {
		return registerTime;
	}

	public void setRegisterTime(String registerTime) {
		this.registerTime = registerTime;
	}

	public String getLastLoginTime() {
		return lastLoginTime;
	}

	public void setLastLoginTime(String lastLoginTime) {
		this.lastLoginTime = lastLoginTime;
	}

	public String getSelfIntroduce() {
		return selfIntroduce;
	}

	public void setSelfIntroduce(String selfIntroduce) {
		this.selfIntroduce = selfIntroduce;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public void addInfo(String text) {
		// TODO Auto-generated method stub
		String[] infos = text.split(" ");
		System.out.println(infos.length);
		for (int i = 0; i < infos.length; i = i + 2) {

			if (infos[i].contains("数字ID"))
				this.id = Integer.valueOf(infos[i + 1]);
			if (infos[i].contains("所在地"))
				this.address = infos[i + 1];
			if (infos[i].contains("论坛等级"))
				this.level = Integer.valueOf(infos[i + 1]);
			if (infos[i].contains("注册"))
				this.registerTime = infos[i + 1];
			if (infos[i].contains("所在地"))
				this.address = infos[i + 1];
			if (infos[i].contains("最后登录"))
				this.lastLoginTime = infos[i + 1];
			if (infos[i].contains("银行"))
				this.cash = infos[i + 1];
			if (infos[i].contains("社团"))
				this.group = infos[i + 1];
			if (infos[i].contains("自我介绍")) {
				if (i + 1 < infos.length) {
					this.selfIntroduce = infos[i + 1];
				}
			}
			if (infos[i].contains("生日"))
				this.birthdate = infos[i + 1];
		}
	}
}
