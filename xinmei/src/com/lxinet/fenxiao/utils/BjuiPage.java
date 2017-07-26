package com.lxinet.fenxiao.utils;

/**
 * 创建日期：2014-10-27下午11:36:54 
 * 作者：Cz 
 * 网址：http://www.919dns.com
 */
public class BjuiPage {
	// 当前页数
	private int pageCurrent;
	// 每页显示数量
	private int pageSize;
	// 总页数
	private int totalPage;
	// 总数量
	private int totalCount;

	// 可以将dwz传过来的pageNum、everyPage进行初始化
	public BjuiPage(int pageCurrent, int pageSize) {
		this.setPageCurrent(pageCurrent);
		this.setPageSize(pageSize);
	}

	public int getTotalPage() {
		return totalPage;
	}
	
	/**
	 * 当前页从第几条记录开始查询
	 * @return
	 */
	public int getStart(){
		return (pageCurrent-1)*pageSize;
	}

	public void setTotalPage(int totalPage) {
		this.totalPage = totalPage;
	}

	// 设置总数量的同时，设置总页数
	public void setTotalCount(int totalCount) {
		this.totalCount = totalCount;
		int temp = 0;
		if (totalCount % this.pageSize != 0) {
			temp++;
		}
		this.totalPage = totalCount / this.pageSize + temp;
	}


	public int getTotalCount() {
		return totalCount;
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


}
