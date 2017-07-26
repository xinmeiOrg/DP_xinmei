package com.lxinet.fenxiao.entities;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Version;

/**
 * 创建日期：2014-10-25下午1:31:27
 * 作者：Cz
 */
@MappedSuperclass
public class BaseBean {
	/**
	 * ID，自动增长
	 */
	@Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
	protected Integer id;
	/**
	 * 删除标志位，默认false，false为正常，true为删除
	 */
	private boolean deleted;
	/**
	 * 创建时间
	 */
	@Temporal(value = TemporalType.TIMESTAMP)
	private Date createDate;
	/**
	 * 版本号
	 */
	@Version
	private int version;
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public boolean isDeleted() {
		return deleted;
	}
	public void setDeleted(boolean deleted) {
		this.deleted = deleted;
	}
	public Date getCreateDate() {
		return createDate;
	}
	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}
	public int getVersion() {
		return version;
	}
	public void setVersion(int version) {
		this.version = version;
	}

}
