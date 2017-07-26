package com.lxinet.fenxiao.dao;

import com.lxinet.fenxiao.entities.Orders;



/**
 * 订单
 * @author Cz
 *
 */
public interface IOrdersDao extends IBaseDao<Orders>{
	Orders findByNo(String no);
}

