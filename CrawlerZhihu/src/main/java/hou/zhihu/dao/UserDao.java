package hou.zhihu.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Map;
import java.util.Map.Entry;

import com.alibaba.fastjson.JSON;

import hou.zhihu.model.User;
import hou.zhihu.util.ConnectionFactory;

/**
 * @author houweitao
 * @date 2016年1月27日 下午9:58:07
 */

public class UserDao {
	Connection conn = ConnectionFactory.getInstance().makeConnection();

	public void insertList(Map<String, String> list) {
		int count = 1;
		for (Entry<String, String> entry : list.entrySet()) {
			try {
				System.out.println(count++ + "  " + entry.getValue());
				insertUser(JSON.parseObject(entry.getValue(), User.class).addInfomation());
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	public void insertUser(User user) throws SQLException {
		PreparedStatement ps = conn.prepareStatement(
				"INSERT INTO user(id,url,userName,address,job,weibo,followNum,followerNum,searchNum,depth) VALUES(?,?,?,?,?,?,?,?,?,?)");
		int p = 1;
		ps.setString(p++, user.getId());
		ps.setString(p++, user.getUrl());
		ps.setString(p++, user.getUserName());
		ps.setString(p++, user.getAddress());
		ps.setString(p++, user.getJob());
		ps.setString(p++, user.getWeibo());
		ps.setInt(p++, user.getFollowNum());
		ps.setInt(p++, user.getFollowerNum());
		ps.setInt(p++, user.getSearchNum());
		ps.setInt(p++, user.getDepth());
		ps.execute();
	}
}
