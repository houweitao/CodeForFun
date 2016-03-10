package hou.yanzhengma;

import java.awt.Color;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.io.IOUtils;

/**
 * @author houweitao
 * @date 2016年1月15日 下午11:21:03
 * @end 懂了。2016年1月16日01:17:01
 * http://blog.csdn.net/problc/article/details/5794460
 */

public class ImagePreProcess {

	public static int isWhite(int colorInt) {
		Color color = new Color(colorInt);
		if (color.getRed() + color.getGreen() + color.getBlue() > 100) {
			return 1;
		}
		return 0;
	}

	public static int isBlack(int colorInt) {
		Color color = new Color(colorInt);
		if (color.getRed() + color.getGreen() + color.getBlue() <= 100) {
			return 1;
		}
		return 0;
	}

	public static BufferedImage removeBackgroud(String picFile) throws Exception {
		BufferedImage img = ImageIO.read(new File(picFile));
		int width = img.getWidth();
		int height = img.getHeight();
		System.out.println("width: " + width);
		System.out.println("height: " + height);
		for (int x = 0; x < width; ++x) {
			for (int y = 0; y < height; ++y) {
				if (isWhite(img.getRGB(x, y)) == 1) {
					img.setRGB(x, y, Color.WHITE.getRGB());
				} else {
					img.setRGB(x, y, Color.BLACK.getRGB());
				}
			}
		}
		return img;
	}

	public static List<BufferedImage> splitImage(BufferedImage img) throws Exception {
		List<BufferedImage> subImgs = new ArrayList<BufferedImage>();
//        System.out.println(Image.SCALE_AREA_AVERAGING);
		int num = img.getWidth() / 8;
		int width = img.getWidth() / num;
		System.out.println("宽度： " + width);
		int height = img.getHeight();
		for (int i = 0; i < num; i++) {
			subImgs.add(img.getSubimage(i * width, 0, width, height));
		}
		return subImgs;
	}

	public static Map<BufferedImage, String> loadTrainData() throws Exception {
		Map<BufferedImage, String> map = new HashMap<BufferedImage, String>();
		File dir = new File("pic/train");
		File[] files = dir.listFiles();
		for (File file : files) {
			map.put(ImageIO.read(file), file.getName().charAt(0) + "");
			System.out.println(file.getName());
		}
		return map;
	}

	public static String getSingleCharOcr(BufferedImage img, Map<BufferedImage, String> map) {
		String result = "";
		int width = img.getWidth();
		int height = img.getHeight();
		int min = width * height;
		for (BufferedImage bi : map.keySet()) {
			int count = 0;
			Label1: for (int x = 0; x < width; ++x) {
				for (int y = 0; y < height; ++y) {
					if (isWhite(img.getRGB(x, y)) != isWhite(bi.getRGB(x, y))) {
						count++;
						if (count >= min)
							break Label1;
					}
				}
			}
			if (count < min) {
				min = count;
				result = map.get(bi);
			}
		}
		return result;
	}

	public static String getAllOcr(String file) throws Exception {
		BufferedImage img = removeBackgroud(file);
		List<BufferedImage> listImg = splitImage(img);
		Map<BufferedImage, String> map = loadTrainData();
		String result = "";
		for (BufferedImage bi : listImg) {
			result += getSingleCharOcr(bi, map);
		}
		ImageIO.write(img, "JPG", new File("pic//" + result + ".jpg"));
		return result;
	}

	public static void downloadImage() {
		HttpClient httpClient = new HttpClient();
		GetMethod getMethod = new GetMethod("http://img.my.csdn.net/uploads/201008/6/0_1281114108DH7U.gif");
		for (int i = 0; i < 1; i++) {
			try {
				// 执行getMethod
				int statusCode = httpClient.executeMethod(getMethod);
				if (statusCode != HttpStatus.SC_OK) {
					System.err.println("Method failed: " + getMethod.getStatusLine());
				}
				// 读取内容
				String picName = "pic/" + i + ".jpg";
				InputStream inputStream = getMethod.getResponseBodyAsStream();
				OutputStream outStream = new FileOutputStream(picName);
				IOUtils.copy(inputStream, outStream);
				outStream.close();
				System.out.println("OK!");
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				// 释放连接
				getMethod.releaseConnection();
			}
		}
	}

	void init() throws Exception {
		File dir = new File("pic");
		File[] files = dir.listFiles();
		for (File file : files) {
			if (file.isFile()) {
				BufferedImage img = ImageIO.read(file);
				System.out.println("hei: " + img.getHeight());
				System.out.println("wid: " + img.getWidth());
				System.out.println("raster: " + img.getRaster());
				System.out.println(file.getName());
				List<BufferedImage> imgs = splitImage(img);

				for (BufferedImage result : imgs) {
					String name = (int) (Math.random() * 100) + "";
					System.out.println(name);
					ImageIO.write(result, "JPG", new File("pic//" + name + ".jpg"));

				}
			}
		}
	}

	/** 
	 * @param args 
	 * @throws Exception 
	 */
	public static void main(String[] args) throws Exception {
		File dir = new File("pic");
		File[] files = dir.listFiles();
		for (File file : files) {
			if (file.isFile()) {
				System.out.println(file.getName());
				String text = getAllOcr("pic//" + file.getName());
				System.out.println(file.getName() + ".jpg = " + text);
			}
		}
	}

}
