package core;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.Date;

public class Response {
	private BufferedWriter bw;
	private StringBuilder content;//正文
	private StringBuilder headInfo;//响应行，响应头
	private int len;//正文字节数
	private final String BLANK = " ";  
	private final String CRLF = "\r\n";
	private Response() {
		this.content = new StringBuilder();
		this.headInfo = new StringBuilder();
		this.len = 0;
	}
	public Response(Socket client) {
		this();
		try {
			bw = new BufferedWriter(new OutputStreamWriter(client.getOutputStream()));
		} catch (IOException e) {
			e.printStackTrace();
			bw = null;
		}
	}
	public Response(OutputStream os) {
		this();
		bw = new BufferedWriter(new OutputStreamWriter(os));
	}
	//动态添加内容。流模式
	public Response print(String msg){
		//if(null == msg) throw new Exception("html文件读取失败");
		if(null == msg) System.out.println("html文件读取失败");
		content.append(msg);
		len += msg.getBytes().length;
		return this;
	}
	public Response println(String msg) {
		content.append(msg).append(CRLF);
		len += (msg + CRLF).getBytes().length;
		return this;
	}
	public void pushToBrowser(int code) throws IOException {
		if(bw == null) code = 505;
		
		createHeadInfo(code);
		bw.append(headInfo);
		bw.append(content);
		bw.flush();
	}
	//构建头信息
	private void createHeadInfo(int code) {
		headInfo.append("HTTP/1.1").append(BLANK);
		headInfo.append(code).append(BLANK);
		switch(code) {
		case 200:
			headInfo.append("OK").append(CRLF);
			break;
		case 404:
			headInfo.append("NOT FOUND").append(CRLF);
			break;
		case 505:
			headInfo.append("SERVE ERROR").append(CRLF);
			break;
		}
		headInfo.append("Date:").append(new Date()).append(CRLF);
		headInfo.append("Serve:").append("Johnny serve/0.0.1;charset=GBK").append(CRLF);
		headInfo.append("Content-type:text/html").append(CRLF);
		headInfo.append("Content-length:").append(len).append(CRLF);
		headInfo.append(CRLF);
	}
}
