package com.lxinet.fenxiao.dao.impl;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Repository;

import com.lxinet.fenxiao.dao.IArticleDao;
import com.lxinet.fenxiao.entities.Article;

/**
 * 创建日期：2014-10-27下午10:29:24
 * 作者：Cz
 * 网址：http://www.919dns.com
 */
@Repository("articleDao")
@Scope("prototype")
public class ArticleDaoImpl<T extends Article> extends BaseDaoImpl<T> implements IArticleDao<T>{

}
