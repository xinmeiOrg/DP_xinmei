package com.lxinet.fenxiao.service;

import com.lxinet.fenxiao.entities.Admin;


public interface IAdminService<T extends Admin> extends IBaseService<T> {
	/**
	 * 管理员登录
	 * 创建日期：2014-9-24下午10:02:38
	 * 作者：Cz
	 * @param admin
	 * @return
	 */
	Admin login(Admin admin);
	
	/**
	 * 通过用户名获取管理员
	 * 创建日期：2014-9-24下午10:03:47
	 * 作者：Cz
	 * @param name
	 * @return
	 */
	Admin getAdminName(String name);
}

