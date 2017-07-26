package com.lxinet.fenxiao.service.impl;


import java.util.List;

import javax.annotation.Resource;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Repository;

import com.lxinet.fenxiao.dao.IProductCateDao;
import com.lxinet.fenxiao.entities.Product;
import com.lxinet.fenxiao.entities.ProductCate;
import com.lxinet.fenxiao.service.IProductCateService;
import com.lxinet.fenxiao.service.IProductService;

/**
 * 作者：Cz
 */
@Repository("productCateService")
@Scope("prototype")
public class ProductCateServiceImpl<T extends ProductCate> extends BaseServiceImpl<T> implements IProductCateService<T> {
	@Resource(name="productCateDao")
	private IProductCateDao<T> productCateDao;
	@Resource(name="productService")
	private IProductService<Product> productService;
	
	@Override
	public boolean delete(T baseBean) {
//		List<Product> productList = productService.list("from Product where productCate.id="+baseBean.getId());
//		for (Product product : productList) {
//			productService.delete(product);
//		}
		return productCateDao.delete(baseBean);
	}
	
	/**
	 * 通过上级栏目ID查找子栏目
	 * 作者：Cz
	 * 网址:http://www.919dns.com
	 * @return
	 */
	public List<T> listByFatherId(int fid) {
		return productCateDao.listByFatherId(fid);
	}

}
