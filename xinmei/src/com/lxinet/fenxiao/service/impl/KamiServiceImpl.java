package com.lxinet.fenxiao.service.impl;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Repository;

import com.lxinet.fenxiao.entities.Kami;
import com.lxinet.fenxiao.service.IKamiService;

/**
 * 作者：Cz
 * 网址：http://www.919dns.com
 */
@Repository("kamiService")
@Scope("prototype")
public class KamiServiceImpl<T extends Kami> extends BaseServiceImpl<T> implements IKamiService<T>{

}
