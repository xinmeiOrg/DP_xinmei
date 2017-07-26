package com.lxinet.fenxiao.dao.impl;

import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Repository;

import com.lxinet.fenxiao.dao.IBaseDao;

/**
 * 创建日期：2014-10-25下午1:15:08
 * 作者：Cz
 * @param <T>
 */
@Repository("baseDao")
@Scope("prototype")
public class BaseDaoImpl<T> implements IBaseDao<T> {
	@Resource(name="sessionFactory")
	protected SessionFactory sessionFactory;
	Log log = LogFactory.getLog(AdminDaoImpl.class);
	
	private Session getSession(){
		return this.sessionFactory.getCurrentSession();
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public T findById(Class<T> clazz, int id) {
		return (T) this.getSession().get(clazz, id);
	}

	@Override
	public boolean saveOrUpdate(T baseBean) {
		try{
			this.getSession().saveOrUpdate(baseBean);
			return true;
		}catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	@Override
	public boolean delete(T baseBean) {
		try{
			this.getSession().delete(baseBean);
			return true;
		}catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<T> list(String hql) {
		return this.getSession().createQuery(hql).list();
	}

	@Override
	public List<T> list(String hql, int firstResult, int maxResult,
			Object... params) {
		Query query = createQuery(hql);
		for(int i = 0;params != null && i<params.length;i++){
			query.setParameter(i+1, params[i]);
		}
		@SuppressWarnings("unchecked")
		List<T> list = createQuery(hql).setFirstResult(firstResult).setMaxResults(maxResult).list();
		return list;
	}

	@Override
	public int getTotalCount(String hql, Object... params) {
		Query query = createQuery(hql);
		for(int i = 0;params != null && i<params.length;i++){
			query.setParameter(i+1, params[i]);
		}
		
		Object obj = query.uniqueResult();
		return ((Long) obj).intValue();
	}

	@Override
	public Query createQuery(String hql) {
		return this.getSession().createQuery(hql);
	}

	@Override
	public void deleteAll(String entity,String where) {
		Query query = this.createQuery("delete from "+entity+" where "+where);
		query.executeUpdate();
	}

}
