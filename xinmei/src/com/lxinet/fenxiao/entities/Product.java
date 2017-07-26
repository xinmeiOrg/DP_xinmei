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
 * @author Cz
 * 网址：http://www.919dns.com
 */
@Entity
@Table(name = "product")
public class Product extends BaseBean {
	@ManyToOne(cascade = {CascadeType.PERSIST,CascadeType.MERGE},fetch = FetchType.LAZY)
	@JoinColumn(name = "product_cate")
	@NotFound(action=NotFoundAction.IGNORE)
	private ProductCate productCate;
	private String picture;
	private String title;
	@JoinColumn(columnDefinition = "text")
	private String content;
	//面额
	private Double bills;
	//售价
	private Double money;
	
	public Product(){
		
	}
	public Product(Integer id,String picture,String title,Double bills,Double money){
		super.id = id;
		this.picture = picture;
		this.title = title;
		this.bills = bills;
		this.money = money;
	}
	
	public String getTitle() {
		return this.title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getContent() {
		return this.content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getPicture() {
		return picture;
	}

	public void setPicture(String picture) {
		this.picture = picture;
	}
	
	public ProductCate getProductCate() {
		return productCate;
	}
	public void setProductCate(ProductCate productCate) {
		this.productCate = productCate;
	}

	public Double getBills() {
		return bills;
	}

	public void setBills(Double bills) {
		this.bills = bills;
	}

	public Double getMoney() {
		return money;
	}

	public void setMoney(Double money) {
		this.money = money;
	}
}