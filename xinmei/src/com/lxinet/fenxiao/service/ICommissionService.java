package com.lxinet.fenxiao.service;

import java.util.List;

import com.lxinet.fenxiao.entities.Commission;

public interface ICommissionService<T extends Commission> extends IBaseService<T> {
	/**
	 * 通过用户ID获取列表
	 * @param userId
	 * @return
	 */
	List<Commission> getByUser(Integer userId);
}

