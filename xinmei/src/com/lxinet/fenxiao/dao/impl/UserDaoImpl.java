package com.lxinet.fenxiao.dao.impl;

import java.util.List;

import javax.annotation.Resource;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Repository;

import com.lxinet.fenxiao.dao.IUserDao;
import com.lxinet.fenxiao.entities.User;


/**
 * 
 * 作者：Cz
 */
@Repository("userDao")
@Scope("prototype")
public class UserDaoImpl extends BaseDaoImpl<User> implements IUserDao {
	@Resource(name = "sessionFactory")
	private SessionFactory sessionFactory;

	private Session getSession() {
		return this.sessionFactory.getCurrentSession();
	}

	@Override
	public User getUserByName(String name) {
		String hql = "from User where name=:name and deleted=0";
		User user = (User) this.getSession().createQuery(hql)
				.setString("name", name).uniqueResult();
		return user;
	}

	@Override
	public User login(String name, String password) {
		String hql = "from User where name=:name and password=:password and deleted=0";
		User user = (User) this.getSession().createQuery(hql)
				.setString("name", name).setString("password", password).uniqueResult();
		return user;
	}

	@Override
	public User getUserByPhone(String phone) {
		String hql = "from User where phone=:phone and deleted=0";
		User user = (User) this.getSession().createQuery(hql)
				.setString("phone", phone).uniqueResult();
		return user;
	}

	@Override
	public User getUserByNo(String no) {
		String hql = "from User where no=:no and deleted=0";
		User user = (User) this.getSession().createQuery(hql)
				.setString("no", no).uniqueResult();
		return user;
	}

	@Override
	public List<User> levelUserList(String no) {
		String hql = "from User where superior like :no and deleted=0";
		@SuppressWarnings("unchecked")
		List<User> levelUserList = this.getSession().createQuery(hql)
				.setString("no", "%;"+no+";%").list();
		return levelUserList;
	}

	@Override
	public List<User> levelUserTodayList(String no) {
		String hql = "from User where superior like '%:no%' and deleted=0 and date(createDate)=curdate()";
		@SuppressWarnings("unchecked")
		List<User> levelUserTodayList = this.getSession().createQuery(hql)
				.setString("no", no).list();
		return levelUserTodayList;
	}

	@Override
	public List<User> levelUserTodayStatusList(String no) {
		String hql = "from User where superior like '%:no%' and deleted=0 and date(statusDate)=curdate()";
		@SuppressWarnings("unchecked")
		List<User> levelUserTodayStatusList = this.getSession().createQuery(hql)
				.setString("no", no).list();
		return levelUserTodayStatusList;
	}

	@Override
	public User getUserByNameAndPhone(String name, String phone) {
		String hql = "from User where name=:name and phone=:phone and deleted=0";
		User user = (User) this.getSession().createQuery(hql)
				.setString("name", name).setString("phone", phone).uniqueResult();
		return user;
	}
}
