package com.lxinet.fenxiao.service.impl;


import javax.annotation.Resource;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.lxinet.fenxiao.dao.IRechargeDao;
import com.lxinet.fenxiao.entities.Recharge;
import com.lxinet.fenxiao.service.IRechargeService;



/**
 * 
 * 创建日期：2014-9-23上午11:12:39
 * 作者：Cz
 */
@Service("rechargeService")
@Scope("prototype")
public class RechargeServiceImpl<T extends Recharge> extends BaseServiceImpl<T> implements IRechargeService<T> {
	@Resource(name="rechargeDao")
	private IRechargeDao rechargeDao;
	@Override
	public Recharge findByNo(String no) {
		return rechargeDao.findByNo(no);
	}

}

