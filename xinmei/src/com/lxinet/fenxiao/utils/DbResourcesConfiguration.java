package com.lxinet.fenxiao.utils;

import java.util.ResourceBundle;

 
/**
 * 获取资源文件
 *
 */
public class DbResourcesConfiguration {
	

	private static Object lock              = new Object();
	private static DbResourcesConfiguration config     = null;
	private static ResourceBundle rb        = null;
	private static final String CONFIG_FILE = "database";
	
	private DbResourcesConfiguration() {
		rb = ResourceBundle.getBundle(CONFIG_FILE);
	}
	
	public static DbResourcesConfiguration getInstance() {
		synchronized(lock) {
			if(null == config) {
				config = new DbResourcesConfiguration();
			}
		}
		return (config);
	}
	
	public String getValue(String key) {
		return (rb.getString(key));
	}
	
	public static void main(String[] args) {
		System.out.println(DbResourcesConfiguration.getInstance().getValue("REGISTER"));
	}
}
