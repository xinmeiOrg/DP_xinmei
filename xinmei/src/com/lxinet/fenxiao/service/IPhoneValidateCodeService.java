package com.lxinet.fenxiao.service;

import com.lxinet.fenxiao.entities.PhoneValidateCode;

/**
 * 作者：Cz
 * 网址：http://www.919dns.com
 */
public interface IPhoneValidateCodeService<T extends PhoneValidateCode> extends IBaseService<T>{

	PhoneValidateCode getPhoneValidateCode(String phone,String code);
}
