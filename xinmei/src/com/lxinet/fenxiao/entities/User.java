package com.lxinet.fenxiao.entities;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;


/**
 * 会员
 * 作者：Cz
 */
@Entity
@Table(name = "user")
public class User extends BaseBean implements java.io.Serializable {
	private static final long serialVersionUID = 1L;
	/**
	 * 编号
	 */
	private String no;
	/**
	 * 用户名
	 */
	private String name;
	/**
	 * 手机号
	 */
	private String phone;
	/**
	 * 密码
	 */
	private String password;
	/**
	 * 余额
	 */
	private Double balance;
	/**
	 * 佣金
	 */
	private Double commission;
	/**
	 * 上级ID，中间用,分隔
	 */
	private String superior;
	/**
	 * 状态，0为未激活，1为已激活
	 */
	private Integer status;
	/**
	 * 激活时间
	 */
	@Temporal(value = TemporalType.TIMESTAMP)
	private Date statusDate;
	/**
	 * 最后一次登录时间
	 */
	@Temporal(value = TemporalType.TIMESTAMP)
	private Date lastLoginTime;
	/**
	 * 最后一次登录IP
	 */
	private String lastLoginIp;
	/**
	 * 注册IP
	 */
	private String registerIp;
	/**
	 * 登录次数
	 */
	private Integer loginCount;
	
	/**
	 * 收货人
	 */
	private String receiver; 
	/**
	 * 收货人电话
	 */
	private String receiverPhone;
	/**
	 * 收货人地址
	 */
	private String receiverAddress;
	
	

	public String getReceiver() {
		return receiver;
	}
	public void setReceiver(String receiver) {
		this.receiver = receiver;
	}
	public String getReceiverPhone() {
		return receiverPhone;
	}
	public void setReceiverPhone(String receiverPhone) {
		this.receiverPhone = receiverPhone;
	}
	public String getReceiverAddress() {
		return receiverAddress;
	}
	public void setReceiverAddress(String receiverAddress) {
		this.receiverAddress = receiverAddress;
	}

	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	public Date getLastLoginTime() {
		return lastLoginTime;
	}
	public void setLastLoginTime(Date lastLoginTime) {
		this.lastLoginTime = lastLoginTime;
	}
	public String getLastLoginIp() {
		return lastLoginIp;
	}
	public void setLastLoginIp(String lastLoginIp) {
		this.lastLoginIp = lastLoginIp;
	}
	public Integer getLoginCount() {
		return loginCount;
	}
	public void setLoginCount(Integer loginCount) {
		this.loginCount = loginCount;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public Double getBalance() {
		return balance;
	}
	public void setBalance(Double balance) {
		this.balance = balance;
	}
	public Double getCommission() {
		return commission;
	}
	public void setCommission(Double commission) {
		this.commission = commission;
	}
	public String getRegisterIp() {
		return registerIp;
	}
	public void setRegisterIp(String registerIp) {
		this.registerIp = registerIp;
	}
	public String getSuperior() {
		return superior;
	}
	public void setSuperior(String superior) {
		this.superior = superior;
	}
	public String getNo() {
		return no;
	}
	public void setNo(String no) {
		this.no = no;
	}
	public Date getStatusDate() {
		return statusDate;
	}
	public void setStatusDate(Date statusDate) {
		this.statusDate = statusDate;
	}



}