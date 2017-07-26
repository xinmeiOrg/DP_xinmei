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
 * 文章
 * @author Cz
 * 网址：http://www.919dns.com
 */
@Entity
@Table(name = "article")
public class Article extends BaseBean {
	
	@ManyToOne(cascade = {CascadeType.PERSIST},fetch = FetchType.EAGER)
	@JoinColumn(name = "article_cate")
	@NotFound(action=NotFoundAction.IGNORE)
	private ArticleCate articleCate;
	
	private String title;
	private String summary;
	@JoinColumn(columnDefinition = "text")
	private String content;
	private Integer views;
	private Integer status;


	public ArticleCate getArticleCate() {
		return this.articleCate;
	}

	public void setArticleCate(ArticleCate articleCate) {
		this.articleCate = articleCate;
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

	public Integer getViews() {
		return this.views;
	}

	public void setViews(Integer views) {
		this.views = views;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public String getSummary() {
		return summary;
	}

	public void setSummary(String summary) {
		this.summary = summary;
	}

}