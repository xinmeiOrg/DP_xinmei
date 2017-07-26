package com.lxinet.fenxiao.service;

import java.util.List;

import org.hibernate.Query;

/**
 * 创建日期：2014-10-25下午11:26:09
 * 作者：Cz
 */
public interface IBaseService<T> {
	public T findById(Class<T> clazz,int id);
	public boolean saveOrUpdate(T baseBean);
	public boolean delete(T baseBean);
	public void deleteAll(String entity,String where);
	public List<T> list(String hql);
	public List<T> list(String hql,int firstResult,int maxResult,Object...params);
	public int getTotalCount(String hql,Object...params);
	public Query createQuery(String hql);
}
