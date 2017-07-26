package com.lxinet.fenxiao.entities;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.Table;


/**
 * 
 * 创建日期：2014-9-24下午5:13:16
 * 作者：Cz
 */
@Entity
@Table(name = "message")
public class Message extends BaseBean implements java.io.Serializable {
	private static final long serialVersionUID = 1L;
	/**
	 * 标题
	 */
	private String title;
	/**
	 * 留言
	 */
	@JoinColumn(columnDefinition = "text")
	private String content;
	/**
	 * 回复
	 */
	@JoinColumn(columnDefinition = "text")
	private String reply;
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getReply() {
		return reply;
	}
	public void setReply(String reply) {
		this.reply = reply;
	}

}