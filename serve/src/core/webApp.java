package core;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

public class webApp {
	private static Context con;
	static {
		try {
			//1,获取解析工厂
			SAXParserFactory factory = SAXParserFactory.newInstance();
			//2，从解析工厂获取解析器
			SAXParser parser = factory.newSAXParser();
			//3，编写处理器
			//4，加载文档Document注册处理器
			WebHandler handler = new WebHandler();
			//5，解析
			parser.parse(Thread.currentThread().getContextClassLoader()
					.getResourceAsStream("html/web.xml")
					,handler);
			con = new Context(handler.entitys,handler.mappings);
		}catch(Exception e) {
			e.printStackTrace();
			System.out.println("解析失败");
		}
	}
	//通过url获取配置文件里的对象
	public static servlet getServletFromURL(String url) {
		String classname = con.getClz("/" + url);
		Class c;
		try {
			c = Class.forName(classname);
			servlet servlet = (servlet)c.getConstructor().newInstance();
			return servlet;
		} catch (Exception e) {
			System.out.println("用户访问不存在页面");
		}
		return null;
	}
}
