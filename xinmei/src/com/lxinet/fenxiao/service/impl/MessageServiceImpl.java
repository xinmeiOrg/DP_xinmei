package com.lxinet.fenxiao.service.impl;


import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Repository;

import com.lxinet.fenxiao.entities.Message;
import com.lxinet.fenxiao.service.IMessageService;

/**
 * 创建日期：2014-10-25下午11:23:53
 * 作者：Cz
 */
@Repository("messageService")
@Scope("prototype")
public class MessageServiceImpl<T extends Message> extends BaseServiceImpl<T> implements IMessageService<T> {

}
