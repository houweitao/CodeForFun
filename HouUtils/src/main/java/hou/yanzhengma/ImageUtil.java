package hou.yanzhengma;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.awt.image.ConvolveOp;
import java.awt.image.Kernel;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.io.IOUtils;

import com.sun.image.codec.jpeg.JPEGCodec;
import com.sun.image.codec.jpeg.JPEGEncodeParam;
import com.sun.image.codec.jpeg.JPEGImageEncoder;

/**
 * @author houweitao
 * @date 2016年1月16日 下午2:54:36
 */

public class ImageUtil {
	public static void main(String[] args) throws Exception {
		ImageUtil util = new ImageUtil();
//		util.getTrainData("pic");

		util.analysePath("pic");
//		util.getTrainData("pic/train");
	}

	public HashMap<String, String> analysePath(String path) throws Exception {
		HashMap<String, String> ret = new HashMap<>();
		File dir = new File(path);
		File[] files = dir.listFiles();
		for (File file : files) {
			if (file.isFile()) {
				String info = getAllOcr(file.getPath());
				System.out.println(file.getName() + ": " + info);
				ret.put(file.getName(), info);
			}
		}
		return ret;
	}

	public void resize(File originalFile, File resizedFile, int newWidth, float quality) throws IOException {
		if (quality > 1) {
			throw new IllegalArgumentException("Quality has to be between 0 and 1");
		}

		ImageIcon ii = new ImageIcon(originalFile.getCanonicalPath());
		Image i = ii.getImage();
		Image resizedImage = null;

		int iWidth = i.getWidth(null);
		int iHeight = i.getHeight(null);

		if (iWidth > iHeight) {
			resizedImage = i.getScaledInstance(newWidth, (newWidth * iHeight) / iWidth, Image.SCALE_SMOOTH);
		} else {
			resizedImage = i.getScaledInstance((newWidth * iWidth) / iHeight, newWidth, Image.SCALE_SMOOTH);
		}

		// This code ensures that all the pixels in the image are loaded.
		Image temp = new ImageIcon(resizedImage).getImage();

		// Create the buffered image.
		BufferedImage bufferedImage = new BufferedImage(temp.getWidth(null), temp.getHeight(null),
				BufferedImage.TYPE_INT_RGB);

		// Copy image to buffered image.
		Graphics g = bufferedImage.createGraphics();

		// Clear background and paint the image.
		g.setColor(Color.white);
		g.fillRect(0, 0, temp.getWidth(null), temp.getHeight(null));
		g.drawImage(temp, 0, 0, null);
		g.dispose();

		// Soften.
		float softenFactor = 0.05f;
		float[] softenArray = { 0, softenFactor, 0, softenFactor, 1 - (softenFactor * 4), softenFactor, 0, softenFactor,
				0 };
		Kernel kernel = new Kernel(3, 3, softenArray);
		ConvolveOp cOp = new ConvolveOp(kernel, ConvolveOp.EDGE_NO_OP, null);
		bufferedImage = cOp.filter(bufferedImage, null);

		// Write the jpeg to a file.
		FileOutputStream out = new FileOutputStream(resizedFile);

		// Encodes image as a JPEG data stream
		JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(out);

		JPEGEncodeParam param = encoder.getDefaultJPEGEncodeParam(bufferedImage);

		param.setQuality(quality, true);

		encoder.setJPEGEncodeParam(param);
		encoder.encode(bufferedImage);
	} // Example usage

	

	public int isWhite(int colorInt) {
		Color color = new Color(colorInt);
		if (color.getRed() + color.getGreen() + color.getBlue() > 100) {
			return 1;
		}
		return 0;
	}

	public int isBlack(int colorInt) {
		Color color = new Color(colorInt);
		if (color.getRed() + color.getGreen() + color.getBlue() <= 100) {
			return 1;
		}
		return 0;
	}

	public BufferedImage removeBackgroud(String picFile) throws Exception {
		BufferedImage img = ImageIO.read(new File(picFile));
		int width = img.getWidth();
		int height = img.getHeight();
//		System.out.println("width: " + width);
//		System.out.println("height: " + height);
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
	
	public BufferedImage removeBackgroud(BufferedImage img) throws Exception {
		int width = img.getWidth();
		int height = img.getHeight();
//		System.out.println("width: " + width);
//		System.out.println("height: " + height);
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


	// 分割图片
	public List<BufferedImage> splitImage(BufferedImage img) throws Exception {
		List<BufferedImage> subImgs = new ArrayList<BufferedImage>();
//      System.out.println(Image.SCALE_AREA_AVERAGING);
		int num = img.getWidth() / 8;// 分割宽度为8个位
		System.out.println("分成了 " + num);
		int width = img.getWidth() / num;
		int height = img.getHeight();
		for (int i = 0; i < num; i++) {
			subImgs.add(img.getSubimage(i * width, 0, width, height));
		}
		return subImgs;
	}

	public Map<BufferedImage, String> loadTrainData() throws Exception {
		Map<BufferedImage, String> map = new HashMap<BufferedImage, String>();
		File dir = new File("pic/train");
		File[] files = dir.listFiles();
		for (File file : files) {
			map.put(ImageIO.read(file), file.getName().charAt(0) + "");
//			System.out.println(file.getName());
		}
		return map;
	}

	// 获取分割后的单个的识别信息。
	public String getSingleCharOcr(BufferedImage img, Map<BufferedImage, String> map) {
		String result = "";
		int width = img.getWidth();
		int height = img.getHeight();
		int min = width * height;
		for (BufferedImage bi : map.keySet()) {
			int count = 0;
			Label1: for (int x = 0; x < width; ++x) {
				for (int y = 0; y < height; ++y) {
//					if (x < bi.getWidth() && y < bi.getHeight())
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

	// 获取某一个文件的识别信息。
	public String getAllOcr(String file) throws Exception {
		BufferedImage img = removeBackgroud(file);

		if (img.getWidth() > 32) {
			File now = new File(file);
			File after = new File(now.getParent() + "/" + now.getName().split("\\.")[0] + "after.jpg");
			resize(now, after, 32, 0.9f);
			img = ImageIO.read(after);
		}

		List<BufferedImage> listImg = splitImage(img);
		Map<BufferedImage, String> map = loadTrainData();
		String result = "";
		for (BufferedImage bi : listImg) {
			result += getSingleCharOcr(bi, map);
		}
		ImageIO.write(img, "JPG", new File("pic//识别//" + result + ".jpg"));
		return result;
	}

	public void downloadImage() {
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

	public void getTrainData(String path) throws Exception {
		File dir = new File(path);
		File[] files = dir.listFiles();
		for (File file : files) {
			if (file.isFile()) {
				System.out.println(file.getPath());
				System.out.println(file.getName());
				System.out.println(file.getParent() + "/" + file.getName().split("\\.")[0] + "after.jpg");
				BufferedImage img = ImageIO.read(file);

				if (img.getWidth() > 32) {
					File after = new File(file.getParent() + "/" + file.getName().split("\\.")[0] + "after.jpg");
					resize(file, after, 32, 0.9f);
					img = ImageIO.read(after);
				}
				System.out.println("hei: " + img.getHeight());
				System.out.println("wid: " + img.getWidth());
				System.out.println("raster: " + img.getRaster());
				System.out.println(file.getName());
				List<BufferedImage> imgs = splitImage(img);

				for (BufferedImage result : imgs) {
					String name = (int) (Math.random() * 100) + "";
					System.out.println(name);
					ImageIO.write(removeBackgroud(result), "JPG", new File(path + "//train " + name + ".jpg"));
				}
			}
		}

	}
}
