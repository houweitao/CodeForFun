package hou.yanzhengma;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.awt.image.ConvolveOp;
import java.awt.image.Kernel;
import java.io.File;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

/**
 * @author houweitao
 * @date 2016年1月16日 下午9:06:45
 */

public class ImageUtilNet {

	public void analyseNetPic(String dealUrl) {
		HttpURLConnection httpUrl = null;
		try {
			URL url = new URL(dealUrl);
			httpUrl = (HttpURLConnection) url.openConnection();
			httpUrl.connect();

			BufferedImage img = ImageIO.read(httpUrl.getInputStream());
//			img = ImageIO.read(new File("pic//9999.jpg"));
			String info = getAllOcr(img);
			System.out.println(info);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassCastException e) {
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				httpUrl.disconnect();
			} catch (NullPointerException e) {
				e.printStackTrace();
			}
		}
	}

	public BufferedImage resize(BufferedImage originalFile, int newHeight) throws IOException {
		Image i = originalFile;
		Image resizedImage = null;

		int iWidth = i.getWidth(null);
		int iHeight = i.getHeight(null);

		if (iWidth > iHeight) {
			resizedImage = i.getScaledInstance((newHeight * iWidth) / iHeight, newHeight, Image.SCALE_SMOOTH);
		} else {
			resizedImage = i.getScaledInstance(newHeight, (newHeight * iWidth) / iHeight, Image.SCALE_SMOOTH);
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

		return bufferedImage;
	} // Example usage

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

	public String getAllOcr(BufferedImage pic) throws Exception {
		BufferedImage img = removeBackgroud(pic);

		if (img.getHeight() > 10) {
			img = resize(img, 10);
		}

		List<BufferedImage> listImg = splitImage(img);
		Map<BufferedImage, String> map = loadTrainData();
		String result = "";
		for (BufferedImage bi : listImg) {
			result += getSingleCharOcr(bi, map);
//			ImageIO.write(bi, "JPG", new File("pic//识别//" + result + ".jpg"));
		}
		return result;
	}

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
	}// 获取分割后的单个的识别信息。

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
	}// 分割图片

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
}
