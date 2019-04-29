package cn.gmw.api.meiyou.utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;

import javax.annotation.PostConstruct;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;


/**
 * 貌似当前类并未使用
 */
@Slf4j
public class ImageUtil {

    @Value("${channel.image.width}")
    private int imageWidthInjected;
	private static int REQUIRED_IMG_WIDTH;

	@PostConstruct
	public void init(){
        //采用间接注入方式
        REQUIRED_IMG_WIDTH = imageWidthInjected;
    }
	
	public static boolean isImageExist(String imgUrl) {
		BufferedImage bdImg = getImageFromUrl(imgUrl);
		if(bdImg == null)
			return false;
        return true;
	}
	
	public static boolean isRequiredImage(String imgUrl) {
		boolean ret = false;
		BufferedImage bdImg = getImageFromUrl(imgUrl);
		if(bdImg.getWidth() >= REQUIRED_IMG_WIDTH) {
			ret = true;
		}
        return ret;
	}
	
	public static BufferedImage getImageFromUrl(String imgUrl) {
		URL url = null; 
        InputStream is = null; 
        BufferedImage img = null; 
        HttpURLConnection urlConn = null;
        try { 
            url = new URL(imgUrl);
            urlConn = (HttpURLConnection)url.openConnection();
            urlConn.setConnectTimeout(5 * 1000);
            urlConn.setReadTimeout(5 * 1000);
            is = urlConn.getInputStream();
            img = ImageIO.read(is);
        } catch (Exception e) { 
        	log.warn("----图片获取失败!----imgUrl:" + imgUrl, e);
        } finally { 
            try {
            	if(is != null)
            		is.close(); 
            } catch (IOException e) { 
                e.printStackTrace(); 
            } 
        }
        return img;
	}
}
