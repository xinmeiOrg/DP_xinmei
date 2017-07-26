package com.lxinet.fenxiao.dao;

import java.util.List;

import com.lxinet.fenxiao.entities.ProductCate;

/**
 * 作者：Cz
 */
public interface IProductCateDao<T extends ProductCate> extends IBaseDao<T> {
	/**
	 * 通过上级栏目ID查找子栏目
	 * 作者：Cz
	 * 网址:http://www.919dns.com
	 * @return
	 */
	public List<T> listByFatherId(int fid);

}
