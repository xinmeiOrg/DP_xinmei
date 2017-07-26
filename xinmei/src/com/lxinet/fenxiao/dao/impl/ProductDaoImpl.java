package com.lxinet.fenxiao.dao.impl;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Repository;

import com.lxinet.fenxiao.dao.IProductDao;
import com.lxinet.fenxiao.entities.Product;

/**
 * 创建日期：2014-10-27下午10:29:24
 * 作者：Cz
 * 网址：http://www.919dns.com
 */
@Repository("productDao")
@Scope("prototype")
public class ProductDaoImpl<T extends Product> extends BaseDaoImpl<T> implements IProductDao<T>{

}
