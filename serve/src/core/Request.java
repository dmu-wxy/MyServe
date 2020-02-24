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
 * 封装请求协议：获取method uri 以及请求参数 
 * @author 王星宇
 * @date 2020年2月23日
 */
public class Request {
	//协议信息
	private String RequestInfo;
	//请求方式
	private String method;
	//请求URL
	private String url;
	//请求参数
	private String queryStr;
	private String CRLF = "\r\n";
	//存储参数
	private Map<String,List<String>> parameterMap;
	public Request(InputStream inputStream) {
		
		System.out.println("获取请求协议");
		
		byte[] flush = new byte[1024 * 1024];
		parameterMap = new HashMap<String ,List<String>>();
		int len = 0;
		try {
			len = inputStream.read(flush);
			RequestInfo = new String(flush,0,len);
		} catch (Exception e) {
			//e.printStackTrace();
			if(len < 0) System.out.println("无请求协议");
			return;
		}
		//分解字符串
		System.out.println("分解请求协议");
		parseRequestInfo();
		//处理字符串
		System.out.println("分解请求参数");
		convertMap();
	}
	public Request(Socket client) throws IOException {
		this(client.getInputStream());
	}
	//分解字符串
	private void parseRequestInfo() {
		//请求方式
		this.method = this.RequestInfo.substring(0,this.RequestInfo.indexOf("/")).trim();
		System.out.println(method);
		//URL
		int startIndex = this.RequestInfo.indexOf("/") + 1;
		int endIndex = this.RequestInfo.indexOf("HTTP/");
		this.url = this.RequestInfo.substring(startIndex,endIndex).trim();
		int queryIndex = url.indexOf("?");
		if(queryIndex >= 0) { //如果有请求参数
			String[] temp = url.split("\\?");
			this.url = temp[0];
			this.queryStr = temp[1].trim();
		}
		System.out.println(url);
		//获取请求参数
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
	//处理请求参数
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
	//返回参数
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
