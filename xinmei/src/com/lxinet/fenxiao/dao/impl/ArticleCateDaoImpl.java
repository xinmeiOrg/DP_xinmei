package com.lxinet.fenxiao.dao.impl;

import java.util.List;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Repository;

import com.lxinet.fenxiao.dao.IArticleCateDao;
import com.lxinet.fenxiao.entities.ArticleCate;

/**
 * 创建日期：2014-10-25下午11:18:31
 * 作者：Cz
 */
@Repository("articleCateDao")
@Scope("prototype")
public class ArticleCateDaoImpl<T extends ArticleCate> extends BaseDaoImpl<T> implements
		IArticleCateDao<T> {
	
	@SuppressWarnings("unchecked")
	@Override
	public List<T> listByFatherId(int fid) {
		String hql = "from ArticleCate where fatherId="+fid;
		List<T> list = createQuery(hql).list();
		return list;
	}


}
