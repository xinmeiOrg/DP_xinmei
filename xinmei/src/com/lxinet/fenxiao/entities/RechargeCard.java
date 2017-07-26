package com.lxinet.fenxiao.entities;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * 充值卡
 * 作者：Cz
 * 网址：http://www.919dns.com
 */
@Entity
@Table(name = "recharge_card")
public class RechargeCard extends BaseBean implements Serializable {
	private static final long serialVersionUID = 1L;
	private String no;
	/**
	 * 金额
	 */
	private double money;
	/**
	 * 状态，0为未使用，1为已使用
	 */
	private Integer status;
	/**
	 * 使用时间
	 */
	private String useTime;
	/**
	 * 使用用户
	 */
	private String useUserNo;
	public String getNo() {
		return no;
	}
	public void setNo(String no) {
		this.no = no;
	}
	public double getMoney() {
		return money;
	}
	public void setMoney(double money) {
		this.money = money;
	}
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	public String getUseTime() {
		return useTime;
	}
	public void setUseTime(String useTime) {
		this.useTime = useTime;
	}
	public String getUseUserNo() {
		return useUserNo;
	}
	public void setUseUserNo(String useUserNo) {
		this.useUserNo = useUserNo;
	}
	
}
