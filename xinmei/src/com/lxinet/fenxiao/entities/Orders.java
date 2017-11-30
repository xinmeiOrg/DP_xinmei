package com.lxinet.fenxiao.entities;

import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;
/**
 * 订单
 * 作者：Cz
 */
@Entity
@Table(name = "orders")
public class Orders extends BaseBean implements java.io.Serializable {
	private static final long serialVersionUID = 1L;
	/**
	 * 订单金额
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
	 * 商品ID
	 */
	private String productId;
	/**
	 * 商品名称
	 */
	private String productName;
	/**
	 * 商品价格
	 */
	private Double productMoney;
	/**
	 * 商品数量
	 */
	private Integer productNum;
	/**
	 * 单号
	 */
	private String no;
	/**
	 * 状态，0未未付款，1为已付款
	 */
	private Integer status;
	/**
	 * 商品摘要，虚拟商品记录分销码等信息
	 */
	private String summary;
	/**
	 * 付款时间
	 */
	@Temporal(value = TemporalType.TIMESTAMP)
	private Date payDate;
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
	public String getProductId() {
		return productId;
	}
	public void setProductId(String productId) {
		this.productId = productId;
	}
	public String getProductName() {
		return productName;
	}
	public void setProductName(String productName) {
		this.productName = productName;
	}
	public Integer getProductNum() {
		return productNum;
	}
	public void setProductNum(Integer productNum) {
		this.productNum = productNum;
	}
	public String getSummary() {
		return summary;
	}
	public void setSummary(String summary) {
		this.summary = summary;
	}
	public Double getProductMoney() {
		return productMoney;
	}
	public void setProductMoney(Double productMoney) {
		this.productMoney = productMoney;
	}
	public Date getPayDate() {
		return payDate;
	}
	public void setPayDate(Date payDate) {
		this.payDate = payDate;
	}

	public enum PayStatus{
		NONPAYMENT("未支付"),PAID("已支付");

		private String label;

		private PayStatus(String label) {
			this.label = label;
		}

		public String getLabel() {
			return label;
		}

	}
	
	public enum PayChannel{
		AMOUNT_PAY("余额支付"),
		ALI_PAY("支付宝支付"),
		WEIXIN_PAY("微信支付");

		private String label;

		private PayChannel(String label) {
			this.label = label;
		}

		public String getLabel() {
			return label;
		}

	}
	
}