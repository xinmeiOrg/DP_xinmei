package com.lxinet.fenxiao.pay.weixinpay;

import com.lxinet.fenxiao.pay.weixinpay.client.TenpayHttpClient;
import com.lxinet.fenxiao.pay.weixinpay.util.ConstantUtil;
import com.lxinet.fenxiao.pay.weixinpay.util.MD5Util;
import com.lxinet.fenxiao.pay.weixinpay.util.XMLUtil;
import org.jdom.JDOMException;
import org.json.JSONException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class PrepayIdRequestHandler extends RequestHandler {

	public PrepayIdRequestHandler(HttpServletRequest request,
                                  HttpServletResponse response) {
		super(request, response);
	}

	/**
	 * 创建签名SHA1
	 * 
	 * @param signParams
	 * @return
	 * @throws Exception
	 */
	public String createSHA1Sign() {
		StringBuffer sb = new StringBuffer();
		Set es = super.getAllParameters().entrySet();
		Iterator it = es.iterator();
		while (it.hasNext()) {
			Map.Entry entry = (Map.Entry) it.next();
			String k = (String) entry.getKey();
			String v = (String) entry.getValue();
			sb.append(k + "=" + v + "&");
		}
		String params = sb.substring(0, sb.lastIndexOf("&"));
		String appsign = MD5Util.MD5Encode(params, null).toUpperCase();
		this.setDebugInfo(this.getDebugInfo() + "\r\n" + "sha1 sb:" + params);
		this.setDebugInfo(this.getDebugInfo() + "\r\n" + "app sign:" + appsign);
		return appsign;
	}
	
	/**
	 * 创建签名Md5
	 * 
	 * @param signParams
	 * @return
	 * @throws Exception
	 */
	public String createMd5Sign(Object wx, String key) {
		StringBuffer sb = new StringBuffer();
		Field[] declaredFields  = wx.getClass().getDeclaredFields();
		for (Field field : declaredFields) {
			field.setAccessible(true);  
			Object o = null;
			try {
				o = field.get(wx);
			} catch (IllegalArgumentException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			//过滤内容为空的  
            if (o == null) {  
                continue;  
            }
            String k = (String) field.getName();
			String v =  (String)o;
			if(k.equals("packageValue"))
				k = "package";
			sb.append(k + "=" + v + "&");
		}
		
		String params = sb + "key="+key;
		String appsign = MD5Util.MD5Encode(params, null).toUpperCase();
		this.setDebugInfo(this.getDebugInfo() + "\r\n" + "sha1 sb:" + params);
		this.setDebugInfo(this.getDebugInfo() + "\r\n" + "app sign:" + appsign);
		return appsign;
	}

	// 提交预支付
	public String sendPrepay(String xml) throws JSONException {
		String prepayid = "";

		String requestUrl = super.getGateUrl();
		this.setDebugInfo(this.getDebugInfo() + "\r\n" + "requestUrl:"
				+ requestUrl);
		TenpayHttpClient httpClient = new TenpayHttpClient();
		httpClient.setReqContent(requestUrl);
		Map resContent = new HashMap<>();
		if (httpClient.callHttpPost(requestUrl, xml)) {
			try {
				resContent = XMLUtil.doXMLParse(httpClient.getResContent());
				System.out.println(resContent);
			} catch (JDOMException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			if ("SUCCESS".equals(resContent.get("return_code"))) {
				prepayid = (String) resContent.get("prepay_id");
			}
			this.setDebugInfo(this.getDebugInfo() + "\r\n" + "resContent:"
					+ resContent);
		}
		return prepayid;
	}

	// 提交预支付
	public String sendOrderQuery(String xml) throws JSONException {
		String return_message = "";

		String requestUrl = super.getGateUrl();
		this.setDebugInfo(this.getDebugInfo() + "\r\n" + "requestUrl:"
				+ requestUrl);
		TenpayHttpClient httpClient = new TenpayHttpClient();
		httpClient.setReqContent(requestUrl);
		Map resContent = new HashMap<>();
		if (httpClient.callHttpPost(requestUrl, xml)) {
			try {
				resContent = XMLUtil.doXMLParse(httpClient.getResContent());
			} catch (JDOMException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			if ("SUCCESS".equals(resContent.get("return_code"))) {
				return_message = (String) resContent.get("trade_state");
			}else{
				return_message="支付查询失败";
			}
			this.setDebugInfo(this.getDebugInfo() + "\r\n" + "resContent:"
					+ resContent);
		}
		return return_message;
	}

		
	// 判断access_token是否失效
	public String sendAccessToken() {
		String accesstoken = "";
		StringBuffer sb = new StringBuffer("{");
		Set es = super.getAllParameters().entrySet();
		Iterator it = es.iterator();
		while (it.hasNext()) {
			Map.Entry entry = (Map.Entry) it.next();
			String k = (String) entry.getKey();
			String v = (String) entry.getValue();
			if (null != v && !"".equals(v) && !"appkey".equals(k)) {
				sb.append("\"" + k + "\":\"" + v + "\",");
			}
		}
		String params = sb.substring(0, sb.lastIndexOf(","));
		params += "}";

		String requestUrl = super.getGateUrl();
//		this.setDebugInfo(this.getDebugInfo() + "\r\n" + "requestUrl:"
//				+ requestUrl);
		TenpayHttpClient httpClient = new TenpayHttpClient();
		httpClient.setReqContent(requestUrl);
		String resContent = "";
//		this.setDebugInfo(this.getDebugInfo() + "\r\n" + "post data:" + params);
		if (httpClient.callHttpPost(requestUrl, params)) {
			resContent = httpClient.getResContent();
			if (2 == resContent.indexOf(ConstantUtil.ERRORCODE)) {
				accesstoken = resContent.substring(11, 16);//获取对应的errcode的值
			}
		}
		return accesstoken;
	}
}
