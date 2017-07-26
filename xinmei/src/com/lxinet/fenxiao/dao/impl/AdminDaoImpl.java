package com.lxinet.fenxiao.dao.impl;


import javax.annotation.Resource;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Repository;

import com.lxinet.fenxiao.dao.IAdminDao;
import com.lxinet.fenxiao.entities.Admin;


/**
 * 
 * 创建日期：2014-9-23上午11:12:51
 * 作者：Cz
 */
@Repository("adminDao")
@Scope("prototype")
public class AdminDaoImpl extends BaseDaoImpl<Admin> implements IAdminDao{
	@Resource(name="sessionFactory")
	private SessionFactory sessionFactory;
	
	private Session getSession(){
		return this.sessionFactory.getCurrentSession();
	}
	
	@Override
	public Admin login(Admin admin) {
		String hql = "from Admin as a where a.name=:name and a.password=:password";
		Admin findAdmin = (Admin) this.getSession().createQuery(hql).setString("name", admin.getName())
				.setString("password", admin.getPassword()).uniqueResult();
		return findAdmin;
	}

	@Override
	public Admin getAdminName(String name) {
		String hql = "from Admin where name=:name";
		Admin admin = (Admin) this.getSession().createQuery(hql).setString("name", name).uniqueResult();
		return admin;
	}
	
}

