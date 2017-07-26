package com.lxinet.fenxiao.dao.impl;


import javax.annotation.Resource;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Repository;

import com.lxinet.fenxiao.dao.IOrdersDao;
import com.lxinet.fenxiao.entities.Orders;


/**
 * 
 * 作者：Cz
 */
@Repository("ordersDao")
@Scope("prototype")
public class OrdersDaoImpl extends BaseDaoImpl<Orders> implements IOrdersDao {
	@Resource(name = "sessionFactory")
	private SessionFactory sessionFactory;

	private Session getSession() {
		return this.sessionFactory.getCurrentSession();
	}
	@Override
	public Orders findByNo(String no) {
		String hql = "from Orders where no=:no";
		Orders orders = (Orders) this.getSession().createQuery(hql)
				.setString("no", no).uniqueResult();
		return orders;
	}
}
