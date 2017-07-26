package com.lxinet.fenxiao.service.impl;

import javax.annotation.Resource;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.lxinet.fenxiao.dao.IRechargeCardDao;
import com.lxinet.fenxiao.entities.RechargeCard;
import com.lxinet.fenxiao.service.IRechargeCardService;



/**
 * 
 * 作者：Cz
 */
@Service("rechargeCardService")
@Scope("prototype")
public class RechargeCardServiceImpl<T extends RechargeCard> extends BaseServiceImpl<T> implements IRechargeCardService<T> {
	@Resource(name="rechargeCardDao")
	IRechargeCardDao rechargeCardDao;
	@Override
	public RechargeCard getByNo(String no) {
		return rechargeCardDao.getByNo(no);
	}
}

