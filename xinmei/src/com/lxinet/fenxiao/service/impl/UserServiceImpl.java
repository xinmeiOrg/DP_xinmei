package com.lxinet.fenxiao.service.impl;


import java.util.List;

import javax.annotation.Resource;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.lxinet.fenxiao.dao.IUserDao;
import com.lxinet.fenxiao.entities.User;
import com.lxinet.fenxiao.service.IUserService;


/**
 * 
 * 作者：Cz
 */
@Service("userService")
@Scope("prototype")
public class UserServiceImpl<T extends User> extends BaseServiceImpl<T> implements IUserService<T> {
	@Resource(name="userDao")
	private IUserDao userDao;


	@Override
	public User getUserByName(String name) {
		return userDao.getUserByName(name);
	}


	@Override
	public User login(String name, String password) {
		return userDao.login(name, password);
	}


	@Override
	public User getUserByPhone(String phone) {
		return userDao.getUserByPhone(phone);
	}


	@Override
	public User getUserByNo(String no) {
		return userDao.getUserByNo(no);
	}


	@Override
	public List<User> levelUserList(String no) {
		return userDao.levelUserList(no);
	}


	@Override
	public List<User> levelUserTodayList(String no) {
		return userDao.levelUserTodayList(no);
	}


	@Override
	public List<User> levelUserTodayStatusList(String no) {
		return userDao.levelUserTodayStatusList(no);
	}


	@Override
	public User getUserByNameAndPhone(String name, String phone) {
		return userDao.getUserByNameAndPhone(name, phone);
	}

}

