package com.lxinet.fenxiao.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.hibernate.Query;

import com.lxinet.fenxiao.dao.IBaseDao;
import com.lxinet.fenxiao.entities.BaseBean;
import com.lxinet.fenxiao.service.IBaseService;

/**
 * 创建日期：2014-10-25下午11:27:18
 * 作者：Cz
 */
public class BaseServiceImpl<T extends BaseBean> implements IBaseService<T> {
	@Resource(name="baseDao")
	protected IBaseDao<T> baseDao;
	
	@Override
	public T findById(Class<T> clazz, int id) {
		return baseDao.findById(clazz, id);
	}

	@Override
	public boolean saveOrUpdate(T baseBean) {
		return baseDao.saveOrUpdate(baseBean);
	}

	@Override
	public boolean delete(T baseBean) {
		baseBean.setDeleted(true);
		return baseDao.saveOrUpdate(baseBean);
	}

	@Override
	public List<T> list(String hql) {
		return baseDao.list(hql);
	}

	@Override
	public List<T> list(String hql, int firstResult, int maxResult,
			Object... params) {
		return baseDao.list(hql, firstResult, maxResult, params);
	}

	@Override
	public int getTotalCount(String hql, Object... params) {
		return baseDao.getTotalCount(hql, params);
	}

	@Override
	public Query createQuery(String hql) {
		return baseDao.createQuery(hql);
	}

	@Override
	public void deleteAll(String entity,String where) {
		baseDao.deleteAll(entity, where);
	}

}
