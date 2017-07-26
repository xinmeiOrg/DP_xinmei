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
 * @author Cz
 * 网址：http://www.919dns.com
 */
@Entity
@Table(name = "kami")
public class Kami extends BaseBean {
	@ManyToOne(cascade = {CascadeType.PERSIST,CascadeType.MERGE},fetch = FetchType.LAZY)
	@JoinColumn(name = "product")
	@NotFound(action=NotFoundAction.IGNORE)
	private Product product;
	private String no;
	private String password;
	//0为正常销售，1为已卖出
	private Integer status;
	//卖出时间
	@Temporal(value = TemporalType.TIMESTAMP)
	private Date saleTime;
	//所属订单号
	private String ordersNo;
	public String getNo() {
		return no;
	}
	public void setNo(String no) {
		this.no = no;
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
	public Date getSaleTime() {
		return saleTime;
	}
	public void setSaleTime(Date saleTime) {
		this.saleTime = saleTime;
	}
	public Product getProduct() {
		return product;
	}
	public void setProduct(Product product) {
		this.product = product;
	}
	public String getOrdersNo() {
		return ordersNo;
	}
	public void setOrdersNo(String ordersNo) {
		this.ordersNo = ordersNo;
	}
	
}