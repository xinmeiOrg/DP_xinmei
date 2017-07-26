package com.lxinet.fenxiao.dao.impl;

import java.util.List;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Repository;

import com.lxinet.fenxiao.dao.IProductCateDao;
import com.lxinet.fenxiao.entities.ProductCate;

/**
 * 作者：Cz
 */
@Repository("productCateDao")
@Scope("prototype")
public class ProductCateDaoImpl<T extends ProductCate> extends BaseDaoImpl<T> implements
		IProductCateDao<T> {
	
	@SuppressWarnings("unchecked")
	@Override
	public List<T> listByFatherId(int fid) {
		String hql = "from ProductCate where fatherId="+fid;
		List<T> list = createQuery(hql).list();
		return list;
	}


}
