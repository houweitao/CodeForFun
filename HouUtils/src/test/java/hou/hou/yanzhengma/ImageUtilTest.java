package hou.hou.yanzhengma;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.junit.Test;

import hou.yanzhengma.ImageUtil;
import hou.yanzhengma.ImageUtilNet;

/**
 * @author houweitao
 * @date 2016年1月16日 下午7:24:11
 */

public class ImageUtilTest {
	@Test
	public void printMap() throws Exception {
		ImageUtil util = new ImageUtil();
		BufferedImage img = ImageIO.read(new File("pic//train//4.jpg"));
		int width = img.getWidth();
		int height = img.getHeight();

		System.out.println("width: " + width);
		System.out.println("height: " + height);

		for (int i = height - 1; i >= 0; i--) {
			for (int j = width - 1; j > 0; j--) {
				if (util.isWhite(img.getRGB(j, i)) == 1)
					System.out.print("*");
				else
					System.out.print(" ");
//				System.out.print(img.getRGB(j, i)+" ");
			}
			System.out.println();
		}
	}

	@Test
	void getTrainData() throws Exception {
		ImageUtil util = new ImageUtil();
		util.getTrainData("pic/train");
	}
	@Test
	void analyseNetPicTest(){
		ImageUtilNet util = new ImageUtilNet();
		util.analyseNetPic("http://avatar.csdn.net/8/0/0/1_make19830723.jpg");
	}
}
