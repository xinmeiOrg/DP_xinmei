package com.lxinet.fenxiao.service.impl;

import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.request.AlipayTradeQueryRequest;
import com.alipay.api.request.AlipayTradeWapPayRequest;
import com.alipay.api.response.AlipayTradeQueryResponse;
import com.lxinet.fenxiao.entities.Orders;
import com.lxinet.fenxiao.service.IAliWapPayService;
import com.lxinet.fenxiao.utils.StaticFileUtil;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

/**
 * 网页支付
 */
@Service("aliWapPayService")
@Scope("prototype")
public class AliWapPayServiceImpl implements IAliWapPayService {

    public static String APP_ID;
    public static String APP_PRIVATE_KEY;
    public static String ALI_PUBLIC_KEY;
    public static String URL;
    public static String SELLER;
    public static String NOTIFY_URL;
    public static String RETURN_URL;
    
    @PostConstruct
    public void init(){
    	APP_ID = StaticFileUtil.getProperty("aliWapPay", "APP_ID");
    	APP_PRIVATE_KEY = StaticFileUtil.getProperty("aliWapPay", "APP_PRIVATE_KEY");
    	ALI_PUBLIC_KEY = StaticFileUtil.getProperty("aliWapPay", "ALI_PUBLIC_KEY");
    	URL = StaticFileUtil.getProperty("aliWapPay", "URL");
    	SELLER = StaticFileUtil.getProperty("aliWapPay", "SELLER");
    	NOTIFY_URL = StaticFileUtil.getProperty("aliWapPay", "NOTIFY_URL");
    	RETURN_URL = StaticFileUtil.getProperty("aliWapPay", "RETURN_URL");
    }
    
    private static volatile AlipayClient alipayClient = null;


    public static AlipayClient getAlipayClient(){
        if(alipayClient == null){
            synchronized (AlipayClient.class){
                if (alipayClient == null){
                	alipayClient = new DefaultAlipayClient(URL,
                			APP_ID,APP_PRIVATE_KEY, "json", "UTF-8", ALI_PUBLIC_KEY, "RSA2");
                }
            }
        }
        return alipayClient;
    }

    /**
     * 创建支付宝订单
     *
     * @param orderCode
     * @return 支付宝订单 tn
     */
    public String createAliWapPayInfo(String orderCode, Double total, String subject, String body, String type, Integer orderId) {
    	return this.getOrderForm(orderCode, subject, body, total.toString(), type, orderId);
    }

    /**
     * create the order info. 创建订单信息
     *
     */
    public String getOrderForm(String orderCode, String subject, String body, String price, String type, Integer orderId) {
    	AlipayClient alipayClient = getAlipayClient();
	    AlipayTradeWapPayRequest alipayRequest = new AlipayTradeWapPayRequest();//创建API对应的request
	    alipayRequest.setReturnUrl(RETURN_URL + "?type="  + type + "&orderId=" + orderId);
	    alipayRequest.setNotifyUrl(NOTIFY_URL);//在公共参数中设置回跳和通知地址
	    alipayRequest.setBizContent("{"+
	        "\"out_trade_no\":\"" + orderCode + "\"," +
	        "\"total_amount\":" + price + "," +
	        "\"subject\":\"" + subject + "\"," +
	        "\"seller_id\":\"" + SELLER + "\"," +
	        "\"product_code\"+:\"" + body + "\"" +
	        "}");//填充业务参数
	    String form = null;
		try {
			//调用SDK生成表单
			form = alipayClient.pageExecute(alipayRequest).getBody();
		} catch (AlipayApiException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} //调用SDK生成表单

        return form;
    }

    
    /**
     * 支付宝网页查询状态
     * @param orderCode
     * @return
     */
	public String queryPayStatus(String orderCode) {
		AlipayClient alipayClient = getAlipayClient();
		AlipayTradeQueryRequest request = new AlipayTradeQueryRequest();
		request.setBizContent("{" +
				"\"out_trade_no\":\""+ orderCode +"\"}");
		try {
			AlipayTradeQueryResponse response = alipayClient.execute(request);
			JSONObject receiptData = new JSONObject(response.getBody());
			JSONObject body = new JSONObject(receiptData.get("alipay_trade_query_response").toString());
			System.out.println();
			if(body.opt("trade_status") != null && "TRADE_SUCCESS".equals(body.getString("trade_status"))){
				return Orders.PayStatus.PAID.toString();
			}
		} catch (AlipayApiException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JSONException e) {
			e.printStackTrace();
		}

		return Orders.PayStatus.NONPAYMENT.toString();
	}
}
