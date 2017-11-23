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
 * 充值
 * 创建日期：2014-9-24下午5:13:16
 * 作者：Cz
 */
@Entity
@Table(name = "recharge")
public class Recharge extends BaseBean implements java.io.Serializable {
	private static final long serialVersionUID = 1L;
	/**
	 * 充值金额
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
	 * 单号
	 */
	private String no;
	/**
	 * 状态，0未未付款，1为已付款
	 */
	private Integer status;
	public Double getMoney() {
		return money;
	}
	public void setMoney(Double money) {
		this.money = money;
	}
	public String getNo() {
		return no;
	}
	public void setNo(String no) {
		this.no = no;
	}
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}

	@Override
	public String toString() {
		return "Recharge{" +
				"money=" + money +
				", user=" + user +
				", no='" + no + '\'' +
				", status=" + status +
				'}';
	}
}