
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Hashtable;

import javax.imageio.ImageIO;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;

public class zxingCode {
	
	// logo默认边框颜色
	private static final Color DEFAULT_BORDERCOLOR = Color.WHITE;
	// logo默认边框宽度
	private static final int DEFAULT_BORDER = 2;
	// logo大小默认为二维码图片的的1/5
	private static final int DEFAULT_LOGOPART = 5;

	public final Color borderColor = DEFAULT_BORDERCOLOR;
	public final int border = DEFAULT_BORDER;
	public final int logoPart = DEFAULT_LOGOPART;



	/**
	 * 生成不带logo的二维码
	 * @param text		需要转换成二维码的字段
	 * @param width		二维码图片的宽度
	 * @param height	二维码图片的高度
	 * @param format	二维码图片的格式，如果与fileName扩展名不一致，以fileName为准
	 * @param picName	二维码图片的名称
	 * @return			生成的二维码图片
	 */
	public void CodeWithoutLogo(String text, int width, int height, String format,
			String picName) {

		File outputFile = new File(picName);
		// 用于设置QR二维码参数
		Hashtable<EncodeHintType, Object> hints = new Hashtable<EncodeHintType, Object>();
		// 设置QR二维码的纠错级别（H为最高级别）具体级别信息
		hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H);
		// 设置编码方式
		hints.put(EncodeHintType.CHARACTER_SET, "utf-8");

		try {
			BitMatrix bitMatrix = new MultiFormatWriter().encode(text,
					BarcodeFormat.QR_CODE, width, height, hints);
			//生成二维码图片		另外:还有writeToStream方法
			MatrixToImageWriter.writeToFile(bitMatrix, format, outputFile);
		} catch (WriterException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println("QRcode create success !");
	}
		

	/**
	 * 生成中间带有logo的二维码
	 * 
	 * @param qrPicName			二维码图片
	 * @param logoPicName		logo图片
	 * @param logoConfig 		logo参数设置
	 * @param format			二维码的格式
	 * @param picName			二维码图片的名称
	 * 
	 */
	public void CodeWithLogo(String qrPicName, String logoPicName, String format, String picName) {
		
		File qrPic = new File(qrPicName);
		File logoPic = new File(logoPicName);
		
		try {
			if(!qrPic.isFile()){
				System.out.println("二维码图片没有找到！");
				System.exit(0);
			}else if(!logoPic.isFile()){
				System.out.println("Logo图片没有找到！");
				System.exit(0);
			}

			// 读取二维码图片，并构建绘图对象
			BufferedImage image = ImageIO.read(qrPic);
			Graphics2D g = image.createGraphics();

			//  读取Logo图片
			BufferedImage logo = ImageIO.read(logoPic);
			
			//	logo图片放到二维码中按比例缩小
			int widthLogo = image.getWidth()/logoPart;
			int heightLogo = image.getHeight()/logoPart;

			// 计算图片放置位置
			int x = (image.getWidth() - widthLogo) / 2;
			int y = (image.getHeight() - heightLogo) / 2;

			// 开始绘制图片
			g.drawImage(logo, x, y, widthLogo, heightLogo, null);
			g.drawRoundRect(x, y, widthLogo, heightLogo, 15, 15);
			g.setStroke(new BasicStroke(border));
			g.setColor(borderColor);
			g.drawRect(x, y, widthLogo, heightLogo);

			g.dispose();

			ImageIO.write(image, format, new File(picName));
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		System.out.println("QRcode with Logo create success !");
	}

}
