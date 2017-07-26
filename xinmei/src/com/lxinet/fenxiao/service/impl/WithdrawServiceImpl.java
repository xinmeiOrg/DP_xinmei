package com.lxinet.fenxiao.service.impl;


import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.lxinet.fenxiao.entities.Withdraw;
import com.lxinet.fenxiao.service.IWithdrawService;



/**
 * 
 * 作者：Cz
 */
@Service("withdrawService")
@Scope("prototype")
public class WithdrawServiceImpl<T extends Withdraw> extends BaseServiceImpl<T> implements IWithdrawService<T> {

}

