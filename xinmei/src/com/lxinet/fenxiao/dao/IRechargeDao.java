package com.lxinet.fenxiao.dao;

import com.lxinet.fenxiao.entities.Recharge;



/**
 * 充值
 * @author Cz
 *
 */
public interface IRechargeDao extends IBaseDao<Recharge>{
	Recharge findByNo(String no);
}

