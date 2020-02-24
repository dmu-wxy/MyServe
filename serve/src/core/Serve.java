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
	
	
	//��ʼ����
	public void start() {
		try {
			serverSocket = new ServerSocket(8888);
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("����������ʧ��...");
			stop();
		}
		isRunning = true;
		receive();
	}
	//������������
	public void receive() {
		while(isRunning) {
			try {
				Socket client = serverSocket.accept();
				System.out.println("--------------һ���ͻ��˽���������--------------");
				//���̴߳���
				new Thread(new Dispatcher(client)).start();
			} catch (IOException e) {
				e.printStackTrace();
				System.out.println("�ͻ��˽�������ʧ��");
			}
		}
	}
	//ֹͣ����
	public void stop() {
		isRunning = false;
		try {
			this.serverSocket.close();
			System.out.println("������ֹͣ");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
