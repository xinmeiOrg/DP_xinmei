package com.lxinet.fenxiao.service;

import java.util.List;

import com.lxinet.fenxiao.entities.Financial;

public interface IFinancialService<T extends Financial> extends IBaseService<T> {
	/**
	 * 通过用户ID获取列表
	 * @param userId
	 * @return
	 */
	List<Financial> getByUser(Integer userId);
}

