package com.lxinet.fenxiao.entities;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;
/**
 * 提现
 * 作者：Cz
 */
@Entity
@Table(name = "withdraw")
public class Withdraw extends BaseBean implements java.io.Serializable {
	private static final long serialVersionUID = 1L;
	/**
	 * 提现金额
	 */
	private Double money;
	/**
	 * 所属用户
	 */
	@ManyToOne(cascade = {CascadeType.PERSIST},fetch = FetchType.EAGER)
	@JoinColumn(name = "user")
	@NotFound(action=NotFoundAction.IGNORE)
	private User user;
	/**
	 * 手机号
	 */
	private String phone;
	/**
	 * 银行
	 */
	private String bank;
	/**
	 * 银行户名
	 */
	private String bankName;
	/**
	 * 银行卡号
	 */
	private String bankNo;
	/**
	 * 开户行地址
	 */
	private String bankAddress;
	/**
	 * 状态，0为未处理，1为已处理
	 */
	private Integer status;
	public Double getMoney() {
		return money;
	}
	public void setMoney(Double money) {
		this.money = money;
	}
	public String getBank() {
		return bank;
	}
	public void setBank(String bank) {
		this.bank = bank;
	}
	public String getBankName() {
		return bankName;
	}
	public void setBankName(String bankName) {
		this.bankName = bankName;
	}
	public String getBankNo() {
		return bankNo;
	}
	public void setBankNo(String bankNo) {
		this.bankNo = bankNo;
	}
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	public String getBankAddress() {
		return bankAddress;
	}
	public void setBankAddress(String bankAddress) {
		this.bankAddress = bankAddress;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	
}