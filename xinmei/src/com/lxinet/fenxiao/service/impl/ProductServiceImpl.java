package com.lxinet.fenxiao.service.impl;


import javax.annotation.Resource;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Repository;

import com.lxinet.fenxiao.entities.Kami;
import com.lxinet.fenxiao.entities.Product;
import com.lxinet.fenxiao.service.IKamiService;
import com.lxinet.fenxiao.service.IProductService;

/**
 * 作者：Cz
 * 网址：http://www.919dns.com
 */
@Repository("productService")
@Scope("prototype")
public class ProductServiceImpl<T extends Product> extends BaseServiceImpl<T> implements IProductService<T>{
	@Resource(name="kamiService")
	private IKamiService<Kami> kamiService;
	@Override
	public boolean delete(T baseBean) {
//		kamiService.deleteAll("Kami","product.id="+baseBean.getId());
		return baseDao.delete(baseBean);
	}
}
