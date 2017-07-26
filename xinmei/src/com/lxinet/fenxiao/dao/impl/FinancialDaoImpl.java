package com.lxinet.fenxiao.dao.impl;

import java.util.List;

import javax.annotation.Resource;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Repository;

import com.lxinet.fenxiao.dao.IFinancialDao;
import com.lxinet.fenxiao.entities.Financial;



/**
 * 财务
 * @author cz
 *
 */
@Repository("financialDao")
@Scope("prototype")
public class FinancialDaoImpl extends BaseDaoImpl<Financial> implements IFinancialDao {
	@Resource(name = "sessionFactory")
	private SessionFactory sessionFactory;

	private Session getSession() {
		return this.sessionFactory.getCurrentSession();
	}
	
	@Override
	public List<Financial> getByUser(Integer userId) {
		String hql = "from Financial where user.id=:userId and deleted=0";
		@SuppressWarnings("unchecked")
		List<Financial> financialList = this.getSession().createQuery(hql)
				.setInteger("userId", userId).list();
		return financialList;
	}

}
