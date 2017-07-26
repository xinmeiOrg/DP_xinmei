package com.lxinet.fenxiao.dao;

import com.lxinet.fenxiao.entities.PhoneValidateCode;
/**
 * 作者：Cz
 */
public interface IPhoneValidateCodeDao<T extends PhoneValidateCode> extends IBaseDao<T> {
	
	PhoneValidateCode getPhoneValidateCode(String phone,String code);

}
