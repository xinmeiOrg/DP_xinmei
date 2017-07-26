package com.lxinet.fenxiao.utils;

import java.util.ResourceBundle;

 
/**
 * 获取资源文件
 *
 */
public class ResourcesConfiguration {
	

	private static Object lock              = new Object();
	private static ResourcesConfiguration config     = null;
	private static ResourceBundle rb        = null;
	private static final String CONFIG_FILE = "resources";
	
	private ResourcesConfiguration() {
		rb = ResourceBundle.getBundle(CONFIG_FILE);
	}
	
	public static ResourcesConfiguration getInstance() {
		synchronized(lock) {
			if(null == config) {
				config = new ResourcesConfiguration();
			}
		}
		return (config);
	}
	
	public String getValue(String key) {
		return (rb.getString(key));
	}
	
	public static void main(String[] args) {
		System.out.println(ResourcesConfiguration.getInstance().getValue("REGISTER"));
	}
}
