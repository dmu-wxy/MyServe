package core;

import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;

public class Dispatcher implements Runnable{
	private Socket client;
	private Request request;
	private Response response;
	public Dispatcher(Socket client) {
		this.client = client;
		try {
			//获取请求
			request = new Request(client);
			//响应请求
			System.out.println("构建响应协议");
			response = new Response(client);
		} catch (IOException e) {
			e.printStackTrace();
			Utils.close(client);
		}
	}
	@Override
	public void run() {
		try {
			if(null == request.getUrl()||request.getUrl().equals("")) {
				response.print(Utils.getHtml("html/index.html"));
				response.pushToBrowser(200);	
				return;
			}
			System.out.println("实例化业务对象");
			servlet sl = webApp.getServletFromURL(request.getUrl());
			if(null != sl) {
				sl.serve(request, response);
				response.pushToBrowser(200);
			}else {
				response.print(Utils.getHtml("html/error.html"));
				response.pushToBrowser(404);	
			}
		} catch(Exception e) {
			e.printStackTrace();
			try {
				response.pushToBrowser(500);
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
		System.out.println("关闭连接");
		Utils.close(client);//短连接
	}
	
}
