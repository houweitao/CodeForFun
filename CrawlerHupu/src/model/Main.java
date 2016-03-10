package model;

import java.util.Date;

/**
 * @author houweitao 2015年8月4日 上午9:37:58
 */

public class Main {

	int id;
	String homePage;
	String name;
	int depth;
	String teamFans;
	int followNum;

	public String getTeamFans() {
		return teamFans;
	}

	public void setTeamFans(String teamFans) {
		this.teamFans = teamFans;
	}

	public int getFollowNum() {
		return followNum;
	}

	public void setFollowNum(int followNum) {
		this.followNum = followNum;
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

	public String getHomePage() {
		return homePage;
	}

	public void setHomePage(String homePage) {
		this.homePage = homePage;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public boolean equals(Object obj) {
		boolean bres = false;
		if (obj instanceof Main) {
			Main o = (Main) obj;
			bres = (this.homePage.equals(o.homePage));
		}
		return bres;
	}

}
