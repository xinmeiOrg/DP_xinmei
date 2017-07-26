package com.lxinet.fenxiao.service.impl;


import javax.annotation.Resource;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.lxinet.fenxiao.dao.IOrdersDao;
import com.lxinet.fenxiao.entities.Orders;
import com.lxinet.fenxiao.service.IOrdersService;


/**
 * 
 * 作者：Cz
 */
@Service("ordersService")
@Scope("prototype")
public class OrdersServiceImpl<T extends Orders> extends BaseServiceImpl<T> implements IOrdersService<T> {
	@Resource(name="ordersDao")
	private IOrdersDao ordersDao;
	@Override
	public Orders findByNo(String no) {
		return ordersDao.findByNo(no);
	}

}

