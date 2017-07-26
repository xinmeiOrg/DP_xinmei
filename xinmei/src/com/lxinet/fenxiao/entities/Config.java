package com.lxinet.fenxiao.entities;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * 创建日期：2015-1-3上午8:58:11
 * 作者：Cz
 * 网址：http://www.919dns.com
 */
@Entity
@Table(name = "config")
public class Config extends BaseBean implements Serializable {
	private static final long serialVersionUID = 1L;
	private String logo;
	private String siteName;
	private String siteUrl;
	private String siteKeys;
	private String siteDescription;
	private String address;
	private String phone;
	private String qq;
	private String weixin;
	private String weibo;
	private String email;
	
	private String sendEmail;
	
	private String sendEmailPass;
	
	private String sendEmailSmtp;
	/**
	 * 一级奖金比例
	 */
	private Double firstLevel;
	/**
	 * 二级奖金比例
	 */
	private Double secondLevel;
	/**
	 * 三级奖金比例
	 */
	private Double thirdLevel;
	/**
	 * 软件下载链接
	 */
	private String downloadUrl;
	/**
	 * 合作身份者ID，以2088开头由16位纯数字组成的字符串
	 */
	private String alipayPartner;
	/**
	 * 收款支付宝账号
	 */
	private String alipaySellerEmail;
	/**
	 * 商户的私钥
	 */
	private String alipayKey;
	/**
	 * 在线充值是否打开，0为关闭，1为打开
	 */
	private Integer onlinePayIsOpen;
	/**
	 * 充值卡充值是否打开，0为关闭，1为打开
	 */
	private Integer rechargeCardIsOpen;
	public String getLogo() {
		return logo;
	}
	public void setLogo(String logo) {
		this.logo = logo;
	}
	public String getSiteName() {
		return siteName;
	}
	public void setSiteName(String siteName) {
		this.siteName = siteName;
	}
	public String getSiteUrl() {
		return siteUrl;
	}
	public void setSiteUrl(String siteUrl) {
		this.siteUrl = siteUrl;
	}
	public String getSiteKeys() {
		return siteKeys;
	}
	public void setSiteKeys(String siteKeys) {
		this.siteKeys = siteKeys;
	}
	public String getSiteDescription() {
		return siteDescription;
	}
	public void setSiteDescription(String siteDescription) {
		this.siteDescription = siteDescription;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getQq() {
		return qq;
	}
	public void setQq(String qq) {
		this.qq = qq;
	}
	public String getWeixin() {
		return weixin;
	}
	public void setWeixin(String weixin) {
		this.weixin = weixin;
	}
	public String getWeibo() {
		return weibo;
	}
	public void setWeibo(String weibo) {
		this.weibo = weibo;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getSendEmail() {
		return sendEmail;
	}
	public void setSendEmail(String sendEmail) {
		this.sendEmail = sendEmail;
	}
	public String getSendEmailPass() {
		return sendEmailPass;
	}
	public void setSendEmailPass(String sendEmailPass) {
		this.sendEmailPass = sendEmailPass;
	}
	public String getSendEmailSmtp() {
		return sendEmailSmtp;
	}
	public void setSendEmailSmtp(String sendEmailSmtp) {
		this.sendEmailSmtp = sendEmailSmtp;
	}
	public Double getFirstLevel() {
		return firstLevel;
	}
	public void setFirstLevel(Double firstLevel) {
		this.firstLevel = firstLevel;
	}
	public Double getSecondLevel() {
		return secondLevel;
	}
	public void setSecondLevel(Double secondLevel) {
		this.secondLevel = secondLevel;
	}
	public Double getThirdLevel() {
		return thirdLevel;
	}
	public void setThirdLevel(Double thirdLevel) {
		this.thirdLevel = thirdLevel;
	}
	public String getDownloadUrl() {
		return downloadUrl;
	}
	public void setDownloadUrl(String downloadUrl) {
		this.downloadUrl = downloadUrl;
	}
	public String getAlipayPartner() {
		return alipayPartner;
	}
	public void setAlipayPartner(String alipayPartner) {
		this.alipayPartner = alipayPartner;
	}
	public String getAlipaySellerEmail() {
		return alipaySellerEmail;
	}
	public void setAlipaySellerEmail(String alipaySellerEmail) {
		this.alipaySellerEmail = alipaySellerEmail;
	}
	public String getAlipayKey() {
		return alipayKey;
	}
	public void setAlipayKey(String alipayKey) {
		this.alipayKey = alipayKey;
	}
	public Integer getOnlinePayIsOpen() {
		return onlinePayIsOpen;
	}
	public void setOnlinePayIsOpen(Integer onlinePayIsOpen) {
		this.onlinePayIsOpen = onlinePayIsOpen;
	}
	public Integer getRechargeCardIsOpen() {
		return rechargeCardIsOpen;
	}
	public void setRechargeCardIsOpen(Integer rechargeCardIsOpen) {
		this.rechargeCardIsOpen = rechargeCardIsOpen;
	}
	
	
}
