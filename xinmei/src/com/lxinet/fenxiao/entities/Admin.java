package com.lxinet.fenxiao.entities;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;


/**
 * 
 * 创建日期：2014-9-24下午5:13:16
 * 作者：Cz
 */
@Entity
@Table(name = "admin")
public class Admin extends BaseBean implements java.io.Serializable {
	private static final long serialVersionUID = 1L;
	/**
	 * 用户名
	 */
	private String name;
	/**
	 * 密码
	 */
	private String password;
	/**
	 * 状态，0为禁用，1为可用
	 */
	private Integer status;
	/**
	 * 权限，1为超级管理员，0为普通管理员
	 */
	@Column(columnDefinition="int default 0",name="juri",nullable=false)
	private int juri;
	/**
	 * 最后一次登录时间
	 */
	@Temporal(value = TemporalType.TIMESTAMP)
	private Date lastLoginTime;
	/**
	 * 最后一次登录IP
	 */
	private String lastLoginIp;
	/**
	 * 登录次数
	 */
	private Integer loginCount;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
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
	public Date getLastLoginTime() {
		return lastLoginTime;
	}
	public void setLastLoginTime(Date lastLoginTime) {
		this.lastLoginTime = lastLoginTime;
	}
	public String getLastLoginIp() {
		return lastLoginIp;
	}
	public void setLastLoginIp(String lastLoginIp) {
		this.lastLoginIp = lastLoginIp;
	}
	public Integer getLoginCount() {
		return loginCount;
	}
	public void setLoginCount(Integer loginCount) {
		this.loginCount = loginCount;
	}
	public int getJuri() {
		return juri;
	}
	public void setJuri(int juri) {
		this.juri = juri;
	}



}