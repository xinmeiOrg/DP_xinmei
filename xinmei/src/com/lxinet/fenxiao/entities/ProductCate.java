package com.lxinet.fenxiao.entities;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * 产品
 * 作者：Cz
 */
@Entity
@Table(name = "product_cate")
public class ProductCate extends BaseBean {
	private String name;
	private int fatherId;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getFatherId() {
		return fatherId;
	}
	public void setFatherId(int fatherId) {
		this.fatherId = fatherId;
	}
	
}
