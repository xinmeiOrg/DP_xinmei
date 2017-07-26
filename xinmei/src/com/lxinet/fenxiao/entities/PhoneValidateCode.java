package com.lxinet.fenxiao.entities;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * @author Cz
 * 网址：http://www.919dns.com
 */
@Entity
@Table(name = "phone_validate_code")
public class PhoneValidateCode extends BaseBean {
	private String phone;
	private String code;
	//0为未使用，1为已使用
	private Integer status;
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	
}