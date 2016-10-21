
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;

import com.google.zxing.Binarizer;
import com.google.zxing.BinaryBitmap;
import com.google.zxing.DecodeHintType;
import com.google.zxing.LuminanceSource;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.NotFoundException;
import com.google.zxing.Result;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.common.HybridBinarizer;


public class zxingDecode {

	/**
	 * 
	 * @param picName 	二维码图片
	 * @param left		二维码图片识别的最左边位置
	 * @param top		二维码图片识别的最上方位置
	 * @param width		二维码图片识别的宽度
	 * @param height	二维码图片识别的高度
	 * @return context	解码后对应的字符串
	 */
	public String getDecoding(String picName, int left, int top, int width, int height) {
		//解码后对应的字符串
		String context = null;

		MultiFormatReader formatReader = new MultiFormatReader();

		//通过二维码图片获取BufferedImage
		BufferedImage image = getImage(picName);

		int sourceWidth = image.getWidth();
		int sourceHeight = image.getHeight();

		// 识别二维码的结束位置不能超出二维码的边界位置
		if (left + width > sourceWidth || top + height > sourceHeight) {
			throw new IllegalArgumentException(
					"Crop rectangle does not fit within image data.");
		}
		
		//RGB颜色能够获取到的值中，只有一个是none(0x00000000),应该是取值错误，所以容错设置为white(0xFFFFFFFF)
		for (int y = top; y < top + height; y++) {
			for (int x = left; x < left + width; x++) {
				if ((image.getRGB(x, y) & 0xFF000000) == 0) {
					image.setRGB(x, y, 0xFFFFFFFF); // = white
				}
			}
		}

		LuminanceSource source = new BufferedImageLuminanceSource(image);
		Binarizer binarizer = new HybridBinarizer(source);
		BinaryBitmap binaryBitmap = new BinaryBitmap(binarizer);
		Map<DecodeHintType, String> hints = new HashMap<DecodeHintType, String>();
		hints.put(DecodeHintType.CHARACTER_SET, "UTF-8");
		Result result = null;
		try {
			result = formatReader.decode(binaryBitmap, hints);
		} catch (NotFoundException e) {
			e.printStackTrace();
		}
		System.out.println("result = " + result.toString());
		System.out.println("resultFormat = " + result.getBarcodeFormat());
		System.out.println("resultText = " + result.getText());

		context = result.getText();
		return context;
	}
	
	
	public String getDecoding(String picName){
		BufferedImage image = getImage(picName);
		int width = image.getWidth();
		int height = image.getHeight();
		return getDecoding(picName, 0, 0, width, height);
	}
	
	public BufferedImage getImage(String picName){
		File file = new File(picName);
		if(!file.isFile()){
			System.out.println("二维码图片未找到！");
			System.exit(0);
		}
		BufferedImage image = null;
		try {
			image = ImageIO.read(file);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return image;
	}

}
