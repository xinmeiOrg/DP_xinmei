package com.lxinet.fenxiao.service.impl;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Repository;

import com.lxinet.fenxiao.entities.Article;
import com.lxinet.fenxiao.service.IArticleService;

/**
 * 创建日期：2014-10-27下午10:33:02
 * 作者：Cz
 * 网址：http://www.919dns.com
 */
@Repository("articleService")
@Scope("prototype")
public class ArticleServiceImpl<T extends Article> extends BaseServiceImpl<T> implements IArticleService<T>{

}
