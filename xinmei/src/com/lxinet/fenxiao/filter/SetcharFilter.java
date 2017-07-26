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

/**
 * 设置页面编码
 * @author Cz
 *
 */
public class SetcharFilter implements Filter {

	@Override
	public void destroy() {

	}

	@Override
	public void doFilter(ServletRequest req, ServletResponse res,
			FilterChain chain) throws IOException, ServletException {
		HttpServletRequest request = (HttpServletRequest) req;
		HttpServletResponse response = (HttpServletResponse) res;
		request.setCharacterEncoding("UTF-8");
		response.setCharacterEncoding("UTF-8");
		response.setContentType("text/html");

//		String local=(String) request.getAttribute("local");
//
//		System.out.println("local1:"+local);
//		if(local!=null){
//			System.out.println("local:"+local);
//		    String loc[]=local.split("_");  
//		    Locale locale=new Locale(loc[0],loc[1]);  
//		    request.getSession().setAttribute("WW_TRANS_I18N_LOCALE", locale);  
//		}  
		chain.doFilter(request, response);
		
	}

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {

	}

}
