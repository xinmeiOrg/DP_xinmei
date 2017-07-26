package com.lxinet.fenxiao.listener;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.lxinet.fenxiao.entities.Config;
import com.lxinet.fenxiao.service.IConfigService;


public class InitListener implements ServletContextListener {

	@Override
	public void contextDestroyed(ServletContextEvent arg0) {
		
	}

	@Override
	public void contextInitialized(ServletContextEvent arg0) {
		WebApplicationContext rwp = WebApplicationContextUtils.getRequiredWebApplicationContext(arg0.getServletContext());
	    @SuppressWarnings("unchecked")
		IConfigService<Config> configService = (IConfigService<Config>) rwp.getBean("configService");
	    Config config = (Config) configService.findById(Config.class, 1);
	    arg0.getServletContext().setAttribute("config", config);
	}


}
