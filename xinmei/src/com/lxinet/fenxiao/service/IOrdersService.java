package com.lxinet.fenxiao.service;

import com.lxinet.fenxiao.entities.Orders;


public interface IOrdersService<T extends Orders> extends IBaseService<T> {
	Orders findByNo(String no);
	public void generateCosumeInfo(String no);
}

