package com.lxinet.fenxiao.service;

/**
 * Created by yanyuan on 2017/11/15.
 */
public interface IAliWapPayService {

    public String createAliWapPayInfo(String orderCode, Double total, String subject, String body, String type, Integer orderId);

    public String queryPayStatus(String orderCode);
}
