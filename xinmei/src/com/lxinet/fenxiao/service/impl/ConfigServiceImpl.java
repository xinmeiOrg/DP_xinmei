package com.lxinet.fenxiao.service.impl;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.lxinet.fenxiao.entities.Config;
import com.lxinet.fenxiao.service.IConfigService;



/**
 * 
 * 创建日期：2014-9-23上午11:12:39
 * 作者：Cz
 */
@Service("configService")
@Scope("prototype")
public class ConfigServiceImpl<T extends Config> extends BaseServiceImpl<T> implements IConfigService<T> {

}

