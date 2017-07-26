package com.lxinet.fenxiao.action;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

import org.json.JSONException;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.lxinet.fenxiao.entities.Admin;
import com.lxinet.fenxiao.entities.Config;
import com.lxinet.fenxiao.service.IConfigService;
import com.lxinet.fenxiao.utils.BjuiJson;
import com.lxinet.fenxiao.utils.FreemarkerUtils;

import freemarker.template.Configuration;

/**
 * 
 * 创建日期：2014-10-24下午1:01:32 作者：Cz
 */
@Controller("configAction")
@Scope("prototype")
public class ConfigAction extends BaseAction {
	private static final long serialVersionUID = 1L;
	@Resource(name = "configService")
	private IConfigService<Config> configService;
	private Config config;


	/**
	 * 获取信息 创建日期：2014-9-24下午10:01:36 作者：Cz
	 * 
	 * @return
	 */
	public void info() {
		HttpSession session = request.getSession();
		Admin loginAdmin = (Admin) session.getAttribute("loginAdmin");
		String callbackData = "";
		PrintWriter out = null;
		try {
			out = response.getWriter();
		} catch (IOException e) {
			e.printStackTrace();
		}
		try {
			if (loginAdmin.getJuri() != 1) {
				callbackData = BjuiJson.json("300", "权限不足", "", "", "", "","", "");
			}else{
				Config findConfig = (Config) configService.findById(Config.class, 1);
				if (findConfig == null) {
					// 不存在
					callbackData = BjuiJson.json("300", "参数错误", "", "", "", "","", "");
				} else {
					cfg = new Configuration();
					// 设置FreeMarker的模版文件位置
					cfg.setServletContextForTemplateLoading(request.getServletContext(),"WEB-INF/templates/admin");
					Map<Object, Object> root = new HashMap<Object, Object>();
					findConfig.setFirstLevel(findConfig.getFirstLevel()*100);
					findConfig.setSecondLevel(findConfig.getSecondLevel()*100);
					findConfig.setThirdLevel(findConfig.getThirdLevel()*100);
					root.put("config", findConfig);
					FreemarkerUtils.freemarker(request, response,"configEdit.ftl", cfg, root);
				}
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		out.print(callbackData);
		out.flush();
		out.close();
	}
	

	/**
	 * 修改 创建日期：2014-9-24下午10:01:46 作者：Cz
	 * 
	 * @return
	 */
	public void update() {
		HttpSession session = request.getSession();
		Admin loginAdmin = (Admin) session.getAttribute("loginAdmin");
		PrintWriter out = null;
		try {
			out = response.getWriter();
		} catch (IOException e) {
			e.printStackTrace();
		}
		String callbackData = "";
		try {
			if (loginAdmin.getJuri() != 1) {
				callbackData = BjuiJson.json("300", "权限不足", "", "", "", "","", "");
			}else{
				if (config == null) {
					callbackData = BjuiJson.json("300", "参数错误", "", "", "", "","", "");
				} else {
					Config findConfig = (Config) configService.findById(Config.class, config.getId());
					findConfig.setLogo(config.getLogo());
					findConfig.setSiteName(config.getSiteName());
					findConfig.setSiteKeys(config.getSiteKeys());
					findConfig.setSiteDescription(config.getSiteDescription());
					findConfig.setSiteUrl(config.getSiteUrl());
					findConfig.setAddress(config.getAddress());
					findConfig.setEmail(config.getEmail());
					findConfig.setPhone(config.getPhone());
					findConfig.setQq(config.getQq());
					findConfig.setWeibo(config.getWeibo());
					findConfig.setWeixin(config.getWeixin());
					findConfig.setSendEmail(config.getSendEmail());
					findConfig.setSendEmailPass(config.getSendEmailPass());
					findConfig.setSendEmailSmtp(config.getSendEmailSmtp());
					findConfig.setFirstLevel(config.getFirstLevel()/100);
					findConfig.setSecondLevel(config.getSecondLevel()/100);
					findConfig.setThirdLevel(config.getThirdLevel()/100);
					findConfig.setDownloadUrl(config.getDownloadUrl());
					findConfig.setAlipayPartner(config.getAlipayPartner());
					findConfig.setAlipaySellerEmail(config.getAlipaySellerEmail());
					findConfig.setAlipayKey(config.getAlipayKey());
					findConfig.setOnlinePayIsOpen(config.getOnlinePayIsOpen());
					findConfig.setRechargeCardIsOpen(config.getRechargeCardIsOpen());
					boolean result = configService.saveOrUpdate(findConfig);
					// 修改成功
					if (result) {
						request.getServletContext().setAttribute("config", findConfig);
						callbackData = BjuiJson.json("200", "修改成功","", "","", "","", "");
					} else {
						// 修改失败
						callbackData = BjuiJson.json("300", "修改失败", "", "", "", "","", "");
					}
				}
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		out.print(callbackData);
		out.flush();
		out.close();
	}
	
	public void contact(){
		Config findConfig = (Config) configService.findById(Config.class, 1);
		cfg = new Configuration();
		// 设置FreeMarker的模版文件位置
		cfg.setServletContextForTemplateLoading(request.getServletContext(),"WEB-INF/templates/index");
		Map<Object, Object> root = new HashMap<Object, Object>();
		root.put("config", findConfig);
		FreemarkerUtils.freemarker(request, response,ftlFileName, cfg, root);
	}
	

	public Config getConfig() {
		return config;
	}

	public void setConfig(Config config) {
		this.config = config;
	}

}
