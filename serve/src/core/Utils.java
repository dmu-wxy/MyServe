package core;

import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;

public class Utils {
	public static String getHtml(String path) {
		InputStream is = Thread.currentThread().getContextClassLoader().getResourceAsStream(path);
		byte[] data = new byte[1024 * 1024 * 6];
		try {
			int len = is.read(data);
			is.close();
			return new String(data,0,len);
		} catch (Exception e) {
			System.out.println("html文件读取失败");
			return null;
		}
	}
	public static void close(Closeable ... targets) {
		for(Closeable target : targets) {
			try {
				if(target != null) target.close();
			}catch(Exception e) {
				e.printStackTrace();
			}
		}
	}
	//处理中文
	public static String decode(String value,String code) {
		try {
			return java.net.URLDecoder.decode(value, code);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return null;
	}
}
