package com.lxinet.fenxiao.pay.weixinpay;

public class WeixinPayWapParams {
	public String appId;
	public String nonceStr;
	public String packageValue;
	public String prepayid;
	public String signType;
	public String timeStamp;
	public String paySign;
	
	
	public String getPaySign() {
		return paySign;
	}
	public void setPaySign(String paySign) {
		this.paySign = paySign;
	}
	public String getSignType() {
		return signType;
	}
	public void setSignType(String signType) {
		this.signType = signType;
	}
	public String getPackageValue() {
		return packageValue;
	}
	public void setPackageValue(String packageValue) {
		this.packageValue = packageValue;
	}
	public String getPrepayid() {
		return prepayid;
	}
	public void setPrepayid(String prepayid) {
		this.prepayid = prepayid;
	}
	public String getAppId() {
		return appId;
	}
	public void setAppId(String appId) {
		this.appId = appId;
	}
	public String getNonceStr() {
		return nonceStr;
	}
	public void setNonceStr(String nonceStr) {
		this.nonceStr = nonceStr;
	}
	public String getTimeStamp() {
		return timeStamp;
	}
	public void setTimeStamp(String timeStamp) {
		this.timeStamp = timeStamp;
	}
	@Override
	public String toString() {
		return "WeixinPayWapParams [appId=" + appId + ", nonceStr=" + nonceStr
				+ ", packageValue=" + packageValue + ", prepayid=" + prepayid
				+ ", signType=" + signType + ", timeStamp=" + timeStamp
				+ ", paySign=" + paySign + "]";
	}
	
}
