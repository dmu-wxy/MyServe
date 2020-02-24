package core;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Date;

import org.omg.CORBA.portable.InputStream;

public class Serve {
	private ServerSocket serverSocket;
	private boolean isRunning;
	
	public static void main(String[] args) {
		Serve serve = new Serve();
		serve.start();
	}
	
	
	//开始服务
	public void start() {
		try {
			serverSocket = new ServerSocket(8888);
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("服务器启动失败...");
			stop();
		}
		isRunning = true;
		receive();
	}
	//接受连接请求
	public void receive() {
		while(isRunning) {
			try {
				Socket client = serverSocket.accept();
				System.out.println("--------------一个客户端建立了连接--------------");
				//多线程处理
				new Thread(new Dispatcher(client)).start();
			} catch (IOException e) {
				e.printStackTrace();
				System.out.println("客户端建立连接失败");
			}
		}
	}
	//停止服务
	public void stop() {
		isRunning = false;
		try {
			this.serverSocket.close();
			System.out.println("服务器停止");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
