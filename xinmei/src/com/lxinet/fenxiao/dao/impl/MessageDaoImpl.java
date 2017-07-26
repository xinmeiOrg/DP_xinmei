package com.lxinet.fenxiao.dao.impl;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Repository;

import com.lxinet.fenxiao.dao.IMessageDao;
import com.lxinet.fenxiao.entities.Message;

/**
 * 创建日期：2014-10-25下午11:18:31
 * 作者：Cz
 */
@Repository("messageDao")
@Scope("prototype")
public class MessageDaoImpl<T extends Message> extends BaseDaoImpl<T> implements
		IMessageDao<T> {
	
}
