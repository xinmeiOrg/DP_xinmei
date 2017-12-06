package com.lxinet.fenxiao.utils;

import java.util.ResourceBundle;

import com.alibaba.fastjson.JSONObject;

/**
 * 静态文件工具类
 * @author fzh
 *
 */
public class StaticFileUtil {
	
	/**
	 * 读取propertites文件
	 * @param configName
	 * @param propKey
	 * @return
	 */
	public static String getProperty(String configName, String propKey) {
	    return ResourceBundle.getBundle(configName).getString(propKey);
	}
	
	public static void main(String[] args){
		 String param = "{'receiver':'阿斯达','receiverPhone':'18520207711','receiverAddress':'敖德萨所大多所多啊市场秩序持续性'}";
		 JSONObject json = (JSONObject) JSONObject.parse(param);
		 
		 System.out.println(json.getString("receiverAddress"));
	}
}
