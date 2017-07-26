package com.lxinet.fenxiao.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts2.interceptor.ServletRequestAware;
import org.apache.struts2.interceptor.ServletResponseAware;

import com.lxinet.fenxiao.utils.BjuiPage;
import com.opensymphony.xwork2.ActionSupport;

import freemarker.template.Configuration;

/**
 * 创建日期：2014-10-25上午11:35:45 作者：Cz
 */
public class BaseAction extends ActionSupport implements ServletRequestAware,ServletResponseAware {
	private static final long serialVersionUID = 1L;
	protected HttpServletRequest request;
	protected HttpServletResponse response;
	protected Configuration cfg;
	//dwz分页使用,当前页数
	protected int pageCurrent=1;
	//每页条数
	protected int pageSize=10;
	protected BjuiPage page;
	protected String ftlFileName; 
	Log log = LogFactory.getLog(BaseAction.class);
	
	@Override
	public void setServletResponse(HttpServletResponse arg0) {
		this.response = arg0;
	}

	@Override
	public void setServletRequest(HttpServletRequest arg0) {
		this.request = arg0;
	}
	
	public void setPage(BjuiPage page) {
		this.page = page;
	}
	public BjuiPage getPage() {
		return page;
	}

	public int getPageCurrent() {
		return pageCurrent;
	}

	public void setPageCurrent(int pageCurrent) {
		this.pageCurrent = pageCurrent;
	}

	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	public String getFtlFileName() {
		return ftlFileName;
	}

	public void setFtlFileName(String ftlFileName) {
		this.ftlFileName = ftlFileName;
	}
	

}