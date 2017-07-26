package com.lxinet.fenxiao.dao.impl;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Repository;

import com.lxinet.fenxiao.dao.IKamiDao;
import com.lxinet.fenxiao.entities.Kami;

/**
 * 作者：Cz
 * 网址：http://www.919dns.com
 */
@Repository("kamiDao")
@Scope("prototype")
public class KamiDaoImpl<T extends Kami> extends BaseDaoImpl<T> implements IKamiDao<T>{

}
