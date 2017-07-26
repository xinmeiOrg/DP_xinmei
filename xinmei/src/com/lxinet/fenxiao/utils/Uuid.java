package com.lxinet.fenxiao.utils;

import java.util.UUID;
/**
 * 生成无横杆的UUID
 * 凌夕网络QQ业务下单平台
 * QQ:582866070
 * 网址:http://www.919dns.com
 * 	   http://www.lxinet.com
 * 淘宝:http://919net.taobao.com
 * @author cz
 */
public class Uuid {
	public static String getUUID() {  
		UUID uuid = UUID.randomUUID();  
		String str = uuid.toString();  
		// 去掉"-"符号  
		String temp = str.replace("-", "");
		return temp;  
	}  
	//获得指定数量的UUID  
	public static String[] getUUID(int number) {  
		if (number < 1) {  
			return null;  
		}  
		String[] ss = new String[number];  
		for (int i = 0; i < number; i++) {  
			ss[i] = getUUID();  
		}  
		return ss;  
	}  
}
