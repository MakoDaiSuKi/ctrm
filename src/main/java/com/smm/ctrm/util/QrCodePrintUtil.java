package com.smm.ctrm.util;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;

/**
 * Created by tangshulei on 16/8/1.
 */
@org.springframework.stereotype.Component
public class QrCodePrintUtil {

	private static final int QRCOLOR = 0x00000000; // 默认是黑色
	private static final int BGWHITE = 0xFFFFFFFF; // 背景颜色

	public static void main(String[] args) {
		String content = "test";
		QrCodePrintUtil util = new QrCodePrintUtil();
		BufferedImage bim = util.getQR_CODEBufferedImage(content, BarcodeFormat.QR_CODE, 300, 300,
				util.getDecodeHintType());
		List<String> list = new ArrayList<>();
		list.add("1.1");
		list.add("1.2");
		list.add("1.3");
		list.add("1.4");
		bim = util.addContent_QRCode(bim, "A010101010101", list);
		try {
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			baos.flush();
			ImageIO.write(bim, "png", baos);

			// 二维码生成的路径，但是实际项目中，我们是把这生成的二维码显示到界面上的，因此下面的折行代码可以注释掉
			// 可以看到这个方法最终返回的是这个二维码的imageBase64字符串
			// 前端用 <img src="data:image/png;base64,${imageBase64QRCode}"/>
			// 其中${imageBase64QRCode}对应二维码的imageBase64字符串
			ImageIO.write(bim, "png", new File("c:/Users/zhaoyutao/Desktop/" + new Date().getTime() + "test.png")); // TODO

			// String imageBase64QRCode =
			// Base64.byteArrayToBase64(baos.toByteArray());
			baos.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/** * 给二维码图片文字内容 */
	public BufferedImage addContent_QRCode(BufferedImage bim, String productName, List<String> pars) {
		/** * 读取二维码图片，并构建绘图对象 */

		BufferedImage image = bim;

		BufferedImage outImage = new BufferedImage(700, 400, BufferedImage.TYPE_4BYTE_ABGR);
		Graphics2D outg = outImage.createGraphics();

		if (productName != null && !"".equals(productName)) {
			// 新的图片，把带logo的二维码下面加上文字

			// 画二维码到新的面板
			outg.drawImage(image, 50, 80, image.getWidth(), image.getHeight(), null);
			// 画文字到新的面板
			outg.setColor(Color.BLACK);
			outg.setFont(new Font("宋体", Font.BOLD, 30)); // 字体、字型、字号

			outg.drawString(productName, 50, 50); // 画文字

		}

		if (pars != null && pars.size() > 0) {
			int i = 0;
			for (String par : pars) {
				outg.drawString(par, 450, 150 + (50 * i)); // 画文字
				i++;
			}
		}

		outg.dispose();
		outImage.flush();
		image = outImage;
		image.flush();
		return image;
	}

	/**
	 * * 生成二维码bufferedImage图片 * * @param content * 编码内容 * @param barcodeFormat *
	 * 编码类型 * @param width * 图片宽度 * @param height * 图片高度 * @param hints * 设置参数
	 * * @return
	 */
	public BufferedImage getQR_CODEBufferedImage(String content, BarcodeFormat barcodeFormat, int width, int height,
			Map<EncodeHintType, ?> hints) {
		MultiFormatWriter multiFormatWriter = null;
		BitMatrix bm = null;
		BufferedImage image = null;
		try {
			multiFormatWriter = new MultiFormatWriter();
			// 参数顺序分别为：编码内容，编码类型，生成图片宽度，生成图片高度，设置参数
			bm = multiFormatWriter.encode(content, barcodeFormat, width, height, hints);
			int w = bm.getWidth();
			int h = bm.getHeight();
			image = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);

			// 开始利用二维码数据创建Bitmap图片，分别设为黑（0xFFFFFFFF）白（0xFF000000）两色
			for (int x = 0; x < w; x++) {
				for (int y = 0; y < h; y++) {
					image.setRGB(x, y, bm.get(x, y) ? QRCOLOR : BGWHITE);
				}
			}
		} catch (WriterException e) {
			e.printStackTrace();
		}
		return image;
	}

	/** * 设置二维码的格式参数 * * @return */
	public Map<EncodeHintType, Object> getDecodeHintType() {
		// 用于设置QR二维码参数
		Map<EncodeHintType, Object> hints = new HashMap<EncodeHintType, Object>();
		// 设置QR二维码的纠错级别（H为最高级别）具体级别信息
		hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H);
		// 设置编码方式
		hints.put(EncodeHintType.CHARACTER_SET, "utf-8");
		hints.put(EncodeHintType.MARGIN, 0);
		hints.put(EncodeHintType.MAX_SIZE, 350);
		hints.put(EncodeHintType.MIN_SIZE, 100);

		return hints;
	}
}
