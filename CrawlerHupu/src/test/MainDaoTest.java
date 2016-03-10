package test;
import java.sql.SQLException;

import Dao.MainDao;
import model.Main;

/**
 * @author houweitao
 * 2015年8月4日 上午10:22:54
 */

public class MainDaoTest {

	public static void main(String[] args) throws SQLException {
		// TODO Auto-generated method stub

		MainDao mainDao = new MainDao();
		Main main = new Main();
		main.setHomePage("hehe");
		main.setName("hc");
		
		mainDao.insert(main);
		
		mainDao.loadInFile("F:/hou/test/" + "hupu" + ".txt");
	}

}
