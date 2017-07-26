package com.lxinet.fenxiao.dao;

import java.util.List;

import com.lxinet.fenxiao.entities.Financial;





/**
 * 用户财务
 * @author Cz
 *
 */
public interface IFinancialDao extends IBaseDao<Financial>{
	/**
	 * 通过用户ID获取财务列表
	 * @param userId
	 * @return
	 */
	List<Financial> getByUser(Integer userId);
}

