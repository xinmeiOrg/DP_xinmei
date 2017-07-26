package com.lxinet.fenxiao.entities;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * 创建日期：2014-10-25下午10:12:27
 * 作者：Cz
 */
@Entity
@Table(name = "article_cate")
public class ArticleCate extends BaseBean {
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
