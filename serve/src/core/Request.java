package core;

import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;

/**
 * ��װ����Э�飺��ȡmethod uri �Լ�������� 
 * @author ������
 * @date 2020��2��23��
 */
public class Request {
	//Э����Ϣ
	private String RequestInfo;
	//����ʽ
	private String method;
	//����URL
	private String url;
	//�������
	private String queryStr;
	private String CRLF = "\r\n";
	//�洢����
	private Map<String,List<String>> parameterMap;
	public Request(InputStream inputStream) {
		
		System.out.println("��ȡ����Э��");
		
		byte[] flush = new byte[1024 * 1024];
		parameterMap = new HashMap<String ,List<String>>();
		int len = 0;
		try {
			len = inputStream.read(flush);
			RequestInfo = new String(flush,0,len);
		} catch (Exception e) {
			//e.printStackTrace();
			if(len < 0) System.out.println("������Э��");
			return;
		}
		//�ֽ��ַ���
		System.out.println("�ֽ�����Э��");
		parseRequestInfo();
		//�����ַ���
		System.out.println("�ֽ��������");
		convertMap();
	}
	public Request(Socket client) throws IOException {
		this(client.getInputStream());
	}
	//�ֽ��ַ���
	private void parseRequestInfo() {
		//����ʽ
		this.method = this.RequestInfo.substring(0,this.RequestInfo.indexOf("/")).trim();
		System.out.println(method);
		//URL
		int startIndex = this.RequestInfo.indexOf("/") + 1;
		int endIndex = this.RequestInfo.indexOf("HTTP/");
		this.url = this.RequestInfo.substring(startIndex,endIndex).trim();
		int queryIndex = url.indexOf("?");
		if(queryIndex >= 0) { //������������
			String[] temp = url.split("\\?");
			this.url = temp[0];
			this.queryStr = temp[1].trim();
		}
		System.out.println(url);
		//��ȡ�������
		if(this.method.equals("POST")) {
			String que = this.RequestInfo.substring(this.RequestInfo.lastIndexOf(CRLF)).trim();
			if(null == queryStr) {
				queryStr = que;
			}else {
				queryStr += "&" + que;
			}
		}
		queryStr = queryStr == null?"":Utils.decode(queryStr,"GBK");
		System.out.println(this.queryStr);
	}
	//�����������
	private void convertMap() {
//		parameterMap
		String[] keyValues = this.queryStr.split("&");
		for(String keyValue: keyValues) {
			String[] kv = keyValue.split("=");
			kv = Arrays.copyOf(kv, 2);
			if(!parameterMap.containsKey(kv[0])) {
				parameterMap.put(kv[0], new ArrayList());
			}
			String value = kv[1] == null?null:Utils.decode(kv[1],"GBK");
			parameterMap.get(kv[0]).add(kv[1]);
		}
	}
	//���ز���
	public String[] findParameterList(String key) {
		List<String> value =  parameterMap.get(key);
		if(value == null||value.size() < 1) {
			return null;
		}
		return value.toArray(new String[0]);
	}
	public String findParameter(String key) {
		String[] values = findParameterList(key);
		return values == null?null:values[0];
	}
	public String getMethod() {
		return method;
	}
	public String getUrl() {
		return url;
	}
	public String getQueryStr() {
		return queryStr;
	}
}
