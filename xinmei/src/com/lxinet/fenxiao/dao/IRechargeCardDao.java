package com.lxinet.fenxiao.dao;

import com.lxinet.fenxiao.entities.RechargeCard;



/**
 * @author Cz
 *
 */
public interface IRechargeCardDao extends IBaseDao<RechargeCard>{
	RechargeCard getByNo(String no);
}

