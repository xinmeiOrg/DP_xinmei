package com.lxinet.fenxiao.dao;

import java.util.List;

import com.lxinet.fenxiao.entities.Commission;


/**
 * 用户佣金明细
 * @author Cz
 *
 */
public interface ICommissionDao extends IBaseDao<Commission>{
	/**
	 * 通过用户ID获取佣金列表
	 * @param userId
	 * @return
	 */
	List<Commission> getByUser(Integer userId);
}

