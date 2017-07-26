package com.lxinet.fenxiao.service.impl;


import java.util.List;

import javax.annotation.Resource;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Repository;

import com.lxinet.fenxiao.dao.IArticleCateDao;
import com.lxinet.fenxiao.entities.ArticleCate;
import com.lxinet.fenxiao.service.IArticleCateService;

/**
 * 创建日期：2014-10-25下午11:23:53
 * 作者：Cz
 */
@Repository("articleCateService")
@Scope("prototype")
public class ArticleCateServiceImpl<T extends ArticleCate> extends BaseServiceImpl<T> implements IArticleCateService<T> {
	@Resource(name="articleCateDao")
	private IArticleCateDao<T> articleCateDao;
	
	/**
	 * 通过上级栏目ID查找子栏目
	 * 创建日期：2014-10-26下午7:36:15
	 * 作者：Cz
	 * 网址:http://www.919dns.com
	 * @return
	 */
	public List<T> listByFatherId(int fid) {
		return articleCateDao.listByFatherId(fid);
	}

}
