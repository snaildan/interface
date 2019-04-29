package cn.gmw.api.meiyou.utils;

import lombok.extern.slf4j.Slf4j;
import org.jdom2.Document;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * 输出xml文档工具类
 * @date 2015年11月20日 下午4:56:52
 * @author 0-Vector
 */
@Slf4j
public class XMLOutUtil {

	public static void outputXML(String filePath, String fileName, Document doc, String encoding){
		Format format = Format.getPrettyFormat(); // 格式化xml（缩进等）
		format.setEncoding(encoding); // 文档编码UTF-8
		XMLOutputter XMLOut = new XMLOutputter();
		XMLOut.setFormat(format);
		FileOutputStream write = null;
		try {
			if (!new File(filePath).exists()) {	// 如果文档的存放目录不存在，则创建该目录
				new File(filePath).mkdirs();
			}
			write = new FileOutputStream(filePath + fileName);
			XMLOut.output(doc, write);
		} catch (IOException e) {
			log.error(e.getMessage());
		} finally {
			if (write != null) {
				try {
					write.flush();
					write.close();
				} catch (IOException e) {
					log.error(e.getMessage());
				}
			}
		}
	}
}
