package com.lxinet.fenxiao.dao.impl;


import javax.annotation.Resource;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Repository;

import com.lxinet.fenxiao.dao.IRechargeCardDao;
import com.lxinet.fenxiao.entities.RechargeCard;


/**
 * 
 * 作者：Cz
 */
@Repository("rechargeCardDao")
@Scope("prototype")
public class RechargeCardDaoImpl extends BaseDaoImpl<RechargeCard> implements IRechargeCardDao{
	@Resource(name="sessionFactory")
	private SessionFactory sessionFactory;
	
	private Session getSession(){
		return this.sessionFactory.getCurrentSession();
	}
	@Override
	public RechargeCard getByNo(String no) {
 		String hql = "from RechargeCard where no like '%"+no+"%' and deleted=0";
		RechargeCard rechargeCard = (RechargeCard) this.getSession().createQuery(hql).uniqueResult();
		return rechargeCard;
	}
}

