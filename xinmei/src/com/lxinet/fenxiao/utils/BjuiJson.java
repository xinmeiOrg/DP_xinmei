package com.lxinet.fenxiao.utils;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * BJUI返回json的格式
 * 创建日期：2014-10-25下午8:39:06
 * 作者：Cz
 */
public class BjuiJson {
	public static String json(String statusCode,String message,String tabid,String dialogid,String divid,String closeCurrent,
			String forward,String forwardConfirm) throws JSONException{
		JSONObject json = new JSONObject();
		json.put("statusCode", statusCode);//必选。状态码(ok = 200, error = 300, timeout = 301)，可以在BJUI.init时配置三个参数的默认值。
		json.put("message", message);//可选。信息内容。
		json.put("tabid", tabid);//可选。待刷新navtab id，多个id以英文逗号分隔开，当前的navtab id不需要填写，填写后可能会导致当前navtab重复刷新。
		json.put("dialogid", dialogid);//可选。待刷新dialog id，多个id以英文逗号分隔开，请不要填写当前的dialog id，要控制刷新当前dialog，请设置dialog中表单的reload参数。
		json.put("divid", divid);//可选。待刷新div id，多个id以英文逗号分隔开，请不要填写当前的div id，要控制刷新当前div，请设置该div中表单的reload参数。
		json.put("closeCurrent", closeCurrent);//可选。是否关闭当前窗口(navtab或dialog)。
		json.put("forward", forward);//可选。跳转到某个url。
		json.put("forwardConfirm", forwardConfirm);//可选。跳转url前的确认提示信息。
		return json.toString();
	}

}
