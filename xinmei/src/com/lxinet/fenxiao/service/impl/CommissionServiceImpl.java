package com.lxinet.fenxiao.service.impl;


import java.util.List;

import javax.annotation.Resource;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.lxinet.fenxiao.dao.ICommissionDao;
import com.lxinet.fenxiao.entities.Commission;
import com.lxinet.fenxiao.service.ICommissionService;

/**
 * 
 * 作者：Cz
 */
@Service("commissionService")
@Scope("prototype")
public class CommissionServiceImpl<T extends Commission> extends BaseServiceImpl<T> implements ICommissionService<T> {
	@Resource(name="commissionDao")
	private ICommissionDao commissionDao;

	@Override
	public List<Commission> getByUser(Integer userId) {
		return commissionDao.getByUser(userId);
	}

}

