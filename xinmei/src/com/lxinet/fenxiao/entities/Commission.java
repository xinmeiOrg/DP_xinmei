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
 * 佣金列表
 * 作者：Cz
 */
@Entity
@Table(name = "commission")
public class Commission extends BaseBean implements java.io.Serializable {
	private static final long serialVersionUID = 1L;
	/**
	 * 所属用户
	 */
	@ManyToOne(cascade = {CascadeType.PERSIST},fetch = FetchType.EAGER)
	@JoinColumn(name = "user")
	@NotFound(action=NotFoundAction.IGNORE)
	private User user;
	/**
	 * 类型，0为扣款，1为入款
	 */
	private Integer type;
	/**
	 * 金额
	 */
	private Double money;
	/**
	 * 交易号
	 */
	private String no;
	
	/**
	 * 第几级用户
	 */
	private Integer level;
	
	/**
	 * 下级用户编号
	 */
	private String lowerLevelNo;
	/**
	 * 备注
	 */
	private String remark;
	/**
	 * 操作人
	 */
	private String operator;
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
	public Integer getType() {
		return type;
	}
	public void setType(Integer type) {
		this.type = type;
	}
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
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public String getOperator() {
		return operator;
	}
	public void setOperator(String operator) {
		this.operator = operator;
	}
	public String getLowerLevelNo() {
		return lowerLevelNo;
	}
	public void setLowerLevelNo(String lowerLevelNo) {
		this.lowerLevelNo = lowerLevelNo;
	}
	public Integer getLevel() {
		return level;
	}
	public void setLevel(Integer level) {
		this.level = level;
	}
	
}