package com.lxinet.fenxiao.pay.weixinpay;

import com.lxinet.fenxiao.entities.Orders;
import com.lxinet.fenxiao.pay.weixinpay.util.ConstantUtil;
import com.lxinet.fenxiao.pay.weixinpay.util.WXUtil;
import com.lxinet.fenxiao.utils.HttpClientUtil;
import com.lxinet.fenxiao.utils.StaticFileUtil;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.naming.NoNameCoder;
import com.thoughtworks.xstream.io.xml.DomDriver;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 微信网页支付服务
 * @author fukangwen
 * @date 2015-7-23
 * @version 1.0
 */
@Service
public class WeixinWapPayService {
	
	public static Logger logger = LoggerFactory.getLogger(WeixinWapPayService.class);
	
	public static final String encoding = "UTF-8";
	
	public static String openid = StaticFileUtil.getProperty("payment", "weixin_wap_openid");
	
	public static String APP_ID = StaticFileUtil.getProperty("payment", "weixin_wap_appid");
	
	private static String PARTNER_KEY = StaticFileUtil.getProperty("payment", "PARTNER_KEY");
	
	public static String mch_id = StaticFileUtil.getProperty("payment", "weixin_mch_id");
	
    private static String weixin_notify_url = StaticFileUtil.getProperty("payment", "weixin_notify_url");

	@PostConstruct
	public void init(){
	}
	
	
	public WeixinPayWapParams doWeinXinWapRequest(String orderCode, Double amount,
                                                  String orderName, HttpServletRequest request, HttpServletResponse response){
		String code = (String)request.getAttribute("code");
		String openid = null;
		try {
			openid = getOpenIdByCode(code);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		//接收财付通通知的URL
		PrepayIdRequestHandler prepayReqHandler = new PrepayIdRequestHandler(request, response);//获取prepayid的请求类
		ClientRequestHandler clientHandler = new ClientRequestHandler(request, response);//返回客户端支付参数的请求类

		String noncestr = WXUtil.getNonceStr();
		WeixinUnifiedorderParams wx = new WeixinUnifiedorderParams();
		wx.setAppid(APP_ID);
		wx.setBody(orderName);
		wx.setMch_id(mch_id);
		wx.setNonce_str(noncestr);
		wx.setNotify_url(weixin_notify_url);
		wx.setOpenid(openid);
		wx.setOut_trade_no(orderCode);
		wx.setSpbill_create_ip(request.getRemoteAddr());
		wx.setTotal_fee(String.valueOf(amount * 100).substring(0, String.valueOf(amount * 100).indexOf(".")));
		wx.setTrade_type("JSAPI");
		
		//生成获取预支付签名
		String sign = prepayReqHandler.createMd5Sign(wx, PARTNER_KEY);
		wx.setSign(sign);
		prepayReqHandler.setGateUrl(ConstantUtil.GATEURL);
		
	    XStream xstream = new XStream(new DomDriver("UTF-8", new NoNameCoder()));
        xstream.alias("xml", WeixinUnifiedorderParams.class);

		//获取prepayId
        String prepayid = "";
        System.out.println(xstream.toXML(wx));
		try {
			prepayid = prepayReqHandler.sendPrepay(xstream.toXML(wx));
		} catch (JSONException e) {
			e.printStackTrace();
		}

		logger.info("获取prepayid------值 " + prepayid);
	
		WeixinPayWapParams wp = new WeixinPayWapParams();  
     
		//吐回给客户端的参数
		if (null != prepayid && !"".equals(prepayid)) {
			//输出参数列表
	        wp.setAppId(APP_ID);
	        String timestamp = WXUtil.getTimeStamp();
			wp.setTimeStamp(timestamp);
			wp.setNonceStr(noncestr);
			wp.setPackageValue("prepay_id="+prepayid);
			wp.setSignType("MD5");
			sign = clientHandler.createMd5Sign(wp, PARTNER_KEY);
			wp.setPaySign(sign);
			wp.setPrepayid(prepayid);
		}
		
		return wp;
	}
	
	public String queryPayStatus(String orderCode){
		PrepayIdRequestHandler prepayReqHandler = new PrepayIdRequestHandler(null, null);//获取prepayid的请求类
		String noncestr = WXUtil.getNonceStr();
		WeixinOrderQueryParams wx = new WeixinOrderQueryParams();
		wx.setAppid(APP_ID);
		wx.setMch_id(mch_id);
		wx.setOut_trade_no(orderCode);
		wx.setNonce_str(noncestr);
		
		//签名签名
		String sign = prepayReqHandler.createMd5Sign(wx, PARTNER_KEY);
		wx.setSign(sign);
		prepayReqHandler.setGateUrl(ConstantUtil.ORDERQUERY_URL);
		XStream xstream = new XStream(new DomDriver("UTF-8", new NoNameCoder()));
        xstream.alias("xml", WeixinOrderQueryParams.class);
		try {
			if("SUCCESS".equals(prepayReqHandler.sendOrderQuery(xstream.toXML(wx))))
                return Orders.PayStatus.PAID.toString();
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return Orders.PayStatus.NONPAYMENT.toString();
	}
	
	public static void main(String[] args){
		WeixinWapPayService test = new WeixinWapPayService();
		System.out.println(test.doWeinXinWapRequest("123", 1.0, "test", null, null));
	}


	/**
	 * 网页授权，根据code获取openId
	 * @param code
	 * @return
	 */
	public String getOpenIdByCode(String code) throws JSONException {
		String url = "https://api.weixin.qq.com/sns/oauth2/access_token";
		StringBuffer sb = new StringBuffer();
		sb.append("appid=" + StaticFileUtil.getProperty("webLicense", "appid"));
		sb.append("&secret=" + StaticFileUtil.getProperty("webLicense", "secret"));
		sb.append("&code=" + code);
		sb.append("&grant_type=authorization_code");
		String respone = HttpClientUtil.sendGet(url, sb.toString());
		JSONObject data = new JSONObject(respone);
		if(data.isNull("openid") || data.get("openid") == null){
			throw new RuntimeException("网页授权返回参数错误");
		}
		return (String) data.get("openid");
	}
}
