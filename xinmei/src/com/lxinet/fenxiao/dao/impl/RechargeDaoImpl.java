package com.lxinet.fenxiao.dao.impl;


import javax.annotation.Resource;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Repository;

import com.lxinet.fenxiao.dao.IRechargeDao;
import com.lxinet.fenxiao.entities.Recharge;


/**
 * 
 * 创建日期：2014-9-23上午11:12:51 作者：Cz
 */
@Repository("rechargeDao")
@Scope("prototype")
public class RechargeDaoImpl extends BaseDaoImpl<Recharge> implements IRechargeDao {
	@Resource(name = "sessionFactory")
	private SessionFactory sessionFactory;

	private Session getSession() {
		return this.sessionFactory.getCurrentSession();
	}
	@Override
	public Recharge findByNo(String no) {
		String hql = "from Recharge where no=:no";
		Recharge recharge = (Recharge) this.getSession().createQuery(hql)
				.setString("no", no).uniqueResult();
		return recharge;
	}
}
