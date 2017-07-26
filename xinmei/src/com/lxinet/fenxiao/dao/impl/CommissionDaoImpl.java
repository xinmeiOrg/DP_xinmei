package com.lxinet.fenxiao.dao.impl;

import java.util.List;

import javax.annotation.Resource;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Repository;

import com.lxinet.fenxiao.dao.ICommissionDao;
import com.lxinet.fenxiao.entities.Commission;

/**
 * 佣金
 * @author cz
 *
 */
@Repository("commissionDao")
@Scope("prototype")
public class CommissionDaoImpl extends BaseDaoImpl<Commission> implements ICommissionDao {
	@Resource(name = "sessionFactory")
	private SessionFactory sessionFactory;

	private Session getSession() {
		return this.sessionFactory.getCurrentSession();
	}
	
	@Override
	public List<Commission> getByUser(Integer userId) {
		String hql = "from Commission where user.id=:userId and deleted=0";
		@SuppressWarnings("unchecked")
		List<Commission> commissionList = this.getSession().createQuery(hql)
				.setInteger("userId", userId).list();
		return commissionList;
	}

}
