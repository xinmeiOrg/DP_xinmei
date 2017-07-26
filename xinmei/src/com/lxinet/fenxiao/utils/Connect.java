package com.lxinet.fenxiao.utils;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class Connect {
	
	public static String httpConnect(String url){
		String result = "";
        BufferedReader in = null;
		try {
			URL realUrl = new URL(url);
	        // 打开和URL之间的连接
	        HttpURLConnection connection = (HttpURLConnection) realUrl.openConnection();
	        // 设置通用的请求属性
	        connection.setRequestProperty("accept", "*/*");
	        connection.setRequestProperty("connection", "Keep-Alive");
	        connection.setRequestProperty("user-agent","Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
	        int statusCode = connection.getResponseCode();
	        // 建立实际的连接
	        connection.connect();
	        // 定义 BufferedReader输入流来读取URL的响应
	        in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
	        String line;
	        while ((line = in.readLine()) != null) {
	        	result += line;
	        }
	        System.out.println("url:"+url);
	        System.out.println("statusCode:"+statusCode);
	        System.out.println("result:"+result);
	        in.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
        return result;
	}
	
}
