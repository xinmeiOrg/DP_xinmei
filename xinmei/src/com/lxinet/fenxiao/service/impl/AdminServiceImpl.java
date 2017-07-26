package com.lxinet.fenxiao.service.impl;

import javax.annotation.Resource;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.lxinet.fenxiao.dao.IAdminDao;
import com.lxinet.fenxiao.entities.Admin;
import com.lxinet.fenxiao.service.IAdminService;


/**
 * 
 * 创建日期：2014-9-23上午11:12:39
 * 作者：Cz
 */
@Service("adminService")
@Scope("prototype")
public class AdminServiceImpl<T extends Admin> extends BaseServiceImpl<T> implements IAdminService<T> {
	@Resource(name="adminDao")
	private IAdminDao adminDao;

	@Override
	public Admin login(Admin admin) {
		return adminDao.login(admin);
	}

	@Override
	public Admin getAdminName(String name) {
		return adminDao.getAdminName(name);
	}

}

