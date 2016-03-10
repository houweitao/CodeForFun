package Dao;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import model.Main;
import util.ConnectionFactory;

/**
 * @author houweitao 2015年8月4日 上午10:01:43
 */

public class MainDao {
	Connection conn = ConnectionFactory.getInstance().makeConnection();

	public void insert(Main main) throws SQLException {
		PreparedStatement ps = conn.prepareStatement("INSERT INTO vister(homepage,name,depth,followNum) VALUES(?,?,?,?)");
		ps.setString(1, main.getHomePage());
		ps.setString(2, main.getName());
		ps.setInt(3, main.getDepth());
		ps.setInt(4, main.getFollowNum());
		
		ps.execute();
		System.out.println("插入："+main.getName());
	}
	
	public void insertNum(String name,int num) throws SQLException {
		PreparedStatement ps = conn.prepareStatement("INSERT INTO vister2(name,num) VALUES(?,?)");
		ps.setString(1, name);
		ps.setInt(2, num);
		
		ps.execute();
		System.out.println("插入："+name);
	}
	
	public void loadInFile(String fileName)throws SQLException {
		Connection conn = ConnectionFactory.getInstance().makeConnection();
		
		Statement st;
		String tableName = "vister";

		try {
			st = conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,
					ResultSet.CONCUR_UPDATABLE);
			String query = "LOAD DATA LOCAL INFILE '" + fileName
					+ "' INTO TABLE " + tableName
					+ "  (homepage,name,depth);";

			// String query = "LOAD DATA INFILE \"" + fileName +
			// "\" INTO TABLE " + tableName + " FIELDS TERMINATED BY ','";

//			System.out.println("string load local file " + query);
			st.executeUpdate(query);
//			System.out.println(url+"写入成功！！");
		} catch (Exception e) {
			e.printStackTrace();
			System.out
					.println("Error in loading file into wdyq_autoinfo_detail \n");
			st = null;
		}
	}
	
	public void loadInFile2(String fileName)throws SQLException {
		Connection conn = ConnectionFactory.getInstance().makeConnection();
		
		Statement st;
		String tableName = "vister3";

		try {
			st = conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,
					ResultSet.CONCUR_UPDATABLE);
			String query = "LOAD DATA LOCAL INFILE '" + fileName
					+ "' INTO TABLE " + tableName
					+ "  (name,num);";

			// String query = "LOAD DATA INFILE \"" + fileName +
			// "\" INTO TABLE " + tableName + " FIELDS TERMINATED BY ','";

//			System.out.println("string load local file " + query);
			st.executeUpdate(query);
//			System.out.println(url+"写入成功！！");
		} catch (Exception e) {
			e.printStackTrace();
			System.out
					.println("Error in loading file into wdyq_autoinfo_detail \n");
			st = null;
		}
	}
}
