package com.lxinet.fenxiao.filter;

import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.lxinet.fenxiao.entities.Admin;
import org.apache.commons.codec.binary.StringUtils;


public class AdminLoginFilter implements Filter {

	
	@Override
	public void destroy() {
		// TODO Auto-generated method stub

	}

	@Override
	public void doFilter(ServletRequest req, ServletResponse res,
			FilterChain chain) throws IOException, ServletException {
		
		HttpServletRequest request = (HttpServletRequest) req;
		HttpServletResponse response = (HttpServletResponse) res;
		HttpSession session = request.getSession();
		Admin loginAdmin = null;
		if(!"".equals(session.getAttribute("loginAdmin"))){
			loginAdmin = (Admin) session.getAttribute("loginAdmin");
		}

		StringBuffer requestUrl = request.getRequestURL();
		//排除指定URL过滤
		if(requestUrl != null
			&& requestUrl.indexOf("downloadImages") > -1){
			chain.doFilter(request, response);
		}else if(loginAdmin==null || loginAdmin.getId()==null){
			response.sendRedirect(request.getContextPath()+"/adminLogin");
		}else{
			chain.doFilter(request, response);
		}
		
	}

	@Override
	public void init(FilterConfig arg0) throws ServletException {
		
	}



}
