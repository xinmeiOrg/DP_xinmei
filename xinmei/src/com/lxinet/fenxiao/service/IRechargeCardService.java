package com.lxinet.fenxiao.service;

import com.lxinet.fenxiao.entities.RechargeCard;


public interface IRechargeCardService<T extends RechargeCard> extends IBaseService<T> {
	RechargeCard getByNo(String no);
}


