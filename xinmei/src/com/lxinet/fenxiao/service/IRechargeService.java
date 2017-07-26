package com.lxinet.fenxiao.service;

import com.lxinet.fenxiao.entities.Recharge;


public interface IRechargeService<T extends Recharge> extends IBaseService<T> {
	Recharge findByNo(String no);
}

