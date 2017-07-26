package com.lxinet.fenxiao.dao.impl;

import javax.annotation.Resource;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Repository;

import com.lxinet.fenxiao.dao.IPhoneValidateCodeDao;
import com.lxinet.fenxiao.entities.PhoneValidateCode;

/**
 * 作者：Cz
 * 网址：http://www.919dns.com
 */
@Repository("phoneValidateCodeDao")
@Scope("prototype")
public class PhoneValidateCodeDaoImpl<T extends PhoneValidateCode> extends BaseDaoImpl<T> implements IPhoneValidateCodeDao<T>{
	@Resource(name = "sessionFactory")
	private SessionFactory sessionFactory;

	private Session getSession() {
		return this.sessionFactory.getCurrentSession();
	}
	
	@Override
	public PhoneValidateCode getPhoneValidateCode(String phone, String code) {
		String hql = "from PhoneValidateCode where phone=:phone and code=:code and deleted=0 and status=0";
		PhoneValidateCode phoneValidateCode = (PhoneValidateCode) this.getSession().createQuery(hql)
				.setString("phone", phone).setString("code", code).uniqueResult();
		return phoneValidateCode;
	}

}
