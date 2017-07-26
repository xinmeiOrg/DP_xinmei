package com.lxinet.fenxiao.dao.impl;


import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Repository;

import com.lxinet.fenxiao.dao.IWithdrawDao;
import com.lxinet.fenxiao.entities.Withdraw;


/**
 * 
 *  作者：Cz
 */
@Repository("withdrawDao")
@Scope("prototype")
public class WithdrawDaoImpl extends BaseDaoImpl<Withdraw> implements IWithdrawDao {
}
