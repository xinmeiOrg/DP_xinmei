package com.lxinet.fenxiao.pay.weixinpay;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by fukangwen on 76/2/24.
 */
public class WeixinWapPayRequest {

    private String orderCode;

    private Double price;

    private String orderName;

    private HttpServletRequest request;

    private HttpServletResponse response;

    public WeixinWapPayRequest(String orderCode, Double price, String orderName, HttpServletRequest request, HttpServletResponse response) {
        this.orderCode = orderCode;
        this.price = price;
        this.orderName = orderName;
        this.request = request;
        this.response = response;
    }

    public String getOrderCode() {
        return orderCode;
    }

    public void setOrderCode(String orderCode) {
        this.orderCode = orderCode;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public String getOrderName() {
        return orderName;
    }

    public void setOrderName(String orderName) {
        this.orderName = orderName;
    }

    public HttpServletRequest getRequest() {
        return request;
    }

    public void setRequest(HttpServletRequest request) {
        this.request = request;
    }

    public HttpServletResponse getResponse() {
        return response;
    }

    public void setResponse(HttpServletResponse response) {
        this.response = response;
    }


    class WeixinQueryPayStatus{

        private String orderCode;

        public String getOrderCode() {
            return orderCode;
        }

        public void setOrderCode(String orderCode) {
            this.orderCode = orderCode;
        }

        public WeixinQueryPayStatus(String orderCode) {
            this.orderCode = orderCode;
        }
    }
}
