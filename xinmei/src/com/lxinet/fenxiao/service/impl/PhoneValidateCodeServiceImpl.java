package com.lxinet.fenxiao.service.impl;

import javax.annotation.Resource;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Repository;

import com.lxinet.fenxiao.dao.IPhoneValidateCodeDao;
import com.lxinet.fenxiao.entities.PhoneValidateCode;
import com.lxinet.fenxiao.service.IPhoneValidateCodeService;

/**
 * 作者：Cz
 * 网址：http://www.919dns.com
 */
@Repository("phoneValidateCodeService")
@Scope("prototype")
public class PhoneValidateCodeServiceImpl<T extends PhoneValidateCode> extends BaseServiceImpl<T> implements IPhoneValidateCodeService<T>{
	@Resource(name="phoneValidateCodeDao")
	private IPhoneValidateCodeDao<PhoneValidateCode> phoneValidateCodeDao;
	@Override
	public PhoneValidateCode getPhoneValidateCode(String phone, String code) {
		return phoneValidateCodeDao.getPhoneValidateCode(phone, code);
	}

}
