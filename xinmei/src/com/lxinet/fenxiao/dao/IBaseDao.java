package com.lxinet.fenxiao.dao;

import java.util.List;

import org.hibernate.Query;

/**
 * 创建日期：2014-10-25下午1:07:59
 * 作者：Cz
 */
public interface IBaseDao<T> {
	public T findById(Class<T> clazz,int id);
	public boolean saveOrUpdate(T baseBean);
	public boolean delete(T baseBean);
	public void deleteAll(String entity,String where);
	public List<T> list(String hql);
	public List<T> list(String hql,int firstResult,int maxResult,Object...params);
	public int getTotalCount(String hql,Object...params);
	public Query createQuery(String hql);
}
