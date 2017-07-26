package com.lxinet.fenxiao.service.impl;


import java.util.List;

import javax.annotation.Resource;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.lxinet.fenxiao.dao.IFinancialDao;
import com.lxinet.fenxiao.entities.Financial;
import com.lxinet.fenxiao.service.IFinancialService;

/**
 * 
 * 作者：Cz
 */
@Service("financialService")
@Scope("prototype")
public class FinancialServiceImpl<T extends Financial> extends BaseServiceImpl<T> implements IFinancialService<T> {
	@Resource(name="financialDao")
	private IFinancialDao financialDao;

	@Override
	public List<Financial> getByUser(Integer userId) {
		return financialDao.getByUser(userId);
	}

}

