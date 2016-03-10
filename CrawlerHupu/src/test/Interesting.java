package test;

import java.util.ArrayList;

/**
 * @author houweitao 2015年8月4日 上午10:49:21
 */

public class Interesting {

	public static void main(String[] args) {
		// TODO Auto-generated method stub

		ArrayList<String> al = new ArrayList<String>();
		
		al.add("s");
		al.add("s");
		al.add("s");
		al.add("s");
		al.add("s");
		al.add("s");
		al.add("s");
		al.add("s");
		al.add("s");
		al.add("s");
		
		for(int i=0;i<al.size();i++){
			System.out.println(i);
			al.remove(0);
		}
	}

}
