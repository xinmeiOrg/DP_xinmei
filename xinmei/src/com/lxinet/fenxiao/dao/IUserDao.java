package com.lxinet.fenxiao.dao;

import java.util.List;

import com.lxinet.fenxiao.entities.User;



/**
 * 用户
 * @author Cz
 *
 */
public interface IUserDao extends IBaseDao<User>{
	/**
	 * 通过用户名获取用户
	 * 作者：Cz
	 * @param name
	 * @return
	 */
	User getUserByName(String name);
	/**
	 * 通过手机号获取用户
	 * @param phone
	 * @return
	 */
	User getUserByPhone(String phone);
	/**
	 * 通过用户名和手机号获取用户
	 * @param name
	 * @param phone
	 * @return
	 */
	User getUserByNameAndPhone(String name,String phone);
	/**
	 * 登录
	 * @param name
	 * @param password
	 * @return
	 */
	User login(String name,String password);
	/**
	 * 通过编号获取用户
	 * @param no
	 * @return
	 */
	User getUserByNo(String no);
	
	/**
	 * 通过编号获取所有下级用户
	 * @param no
	 * @return
	 */
	List<User> levelUserList(String no);
	/**
	 * 通过编号获取今天注册的下级用户
	 * @param no
	 * @return
	 */
	List<User> levelUserTodayList(String no);
	
	/**
	 * 通过编号获取当日报单人数
	 * @param no
	 * @return
	 */
	List<User> levelUserTodayStatusList(String no);
}

