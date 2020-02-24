package core;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

public class webApp {
	private static Context con;
	static {
		try {
			//1,��ȡ��������
			SAXParserFactory factory = SAXParserFactory.newInstance();
			//2���ӽ���������ȡ������
			SAXParser parser = factory.newSAXParser();
			//3����д������
			//4�������ĵ�Documentע�ᴦ����
			WebHandler handler = new WebHandler();
			//5������
			parser.parse(Thread.currentThread().getContextClassLoader()
					.getResourceAsStream("html/web.xml")
					,handler);
			con = new Context(handler.entitys,handler.mappings);
		}catch(Exception e) {
			e.printStackTrace();
			System.out.println("����ʧ��");
		}
	}
	//ͨ��url��ȡ�����ļ���Ķ���
	public static servlet getServletFromURL(String url) {
		String classname = con.getClz("/" + url);
		Class c;
		try {
			c = Class.forName(classname);
			servlet servlet = (servlet)c.getConstructor().newInstance();
			return servlet;
		} catch (Exception e) {
			System.out.println("�û����ʲ�����ҳ��");
		}
		return null;
	}
}
