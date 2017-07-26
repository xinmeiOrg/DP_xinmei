package com.lxinet.fenxiao.dao.impl;


import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Repository;

import com.lxinet.fenxiao.dao.IConfigDao;
import com.lxinet.fenxiao.entities.Config;


/**
 * 
 * 创建日期：2014-9-23上午11:12:51
 * 作者：Cz
 */
@Repository("configDao")
@Scope("prototype")
public class ConfigDaoImpl extends BaseDaoImpl<Config> implements IConfigDao{

}

