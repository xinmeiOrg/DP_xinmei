package com.lxinet.fenxiao.action;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONException;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.lxinet.fenxiao.entities.Admin;
import com.lxinet.fenxiao.entities.Config;
import com.lxinet.fenxiao.entities.Commission;
import com.lxinet.fenxiao.entities.User;
import com.lxinet.fenxiao.service.IConfigService;
import com.lxinet.fenxiao.service.ICommissionService;
import com.lxinet.fenxiao.service.IUserService;
import com.lxinet.fenxiao.utils.BjuiJson;
import com.lxinet.fenxiao.utils.BjuiPage;
import com.lxinet.fenxiao.utils.FreemarkerUtils;
import com.lxinet.fenxiao.utils.PageModel;
import com.lxinet.fenxiao.utils.TemplatesPath;

import freemarker.template.Configuration;

/**
 * 
 * 创建日期：2014-10-24下午1:01:32 作者：Cz
 */
@Controller("commissionAction")
@Scope("prototype")
public class CommissionAction extends BaseAction {
	private static final long serialVersionUID = 1L;
	@Resource(name = "commissionService")
	private ICommissionService<Commission> commissionService;
	@Resource(name = "configService")
	private IConfigService<Config> configService;
	@Resource(name = "userService")
	private IUserService<User> userService;
	private Commission commission;
	private String ftlFileName;

	/**
	 * 用户列表 创建日期：2014-9-24下午10:01:07 作者：Cz
	 * 
	 * @return
	 */
	public void list() {
		String key = request.getParameter("key");
		String countHql = "select count(*) from Commission where deleted=0";
		String hql = "from Commission where deleted=0";
		if(StringUtils.isNotEmpty(key)){
			countHql += " and (user.name='"+key+"' or no='"+key+"')";
			hql += " and (user.name='"+key+"' or no='"+key+"')";
		}
		hql += " order by id desc";
		//获取总条数
		int count = 0;
		count = commissionService.getTotalCount(countHql);	
		page = new BjuiPage(pageCurrent, pageSize);
		page.setTotalCount(count);
		cfg = new Configuration();
		// 设置FreeMarker的模版文件位置
		cfg.setServletContextForTemplateLoading(
		request.getServletContext(), TemplatesPath.ADMIN_PATH);
		List<Commission> commissionList = commissionService.list(hql,page.getStart(),page.getPageSize());
		Map<Object, Object> root = new HashMap<Object, Object>();
		root.put("commissionList", commissionList);
		root.put("page", page);
		root.put("key", key);
		FreemarkerUtils.freemarker(request, response, ftlFileName, cfg, root);
	}

	/**
	 * 添加用户页面 创建日期：2014-10-25下午6:20:23 作者：Cz
	 */
	public void add() {
		cfg = new Configuration();
		// 设置FreeMarker的模版文件位置
		cfg.setServletContextForTemplateLoading(
				request.getServletContext(), TemplatesPath.ADMIN_PATH);
		Map<Object, Object> root = new HashMap<Object, Object>();
		FreemarkerUtils.freemarker(request, response, ftlFileName, cfg,root);
	}

	/**
	 * 添加用户 创建日期：2014-9-24下午10:01:20 作者：Cz
	 */
	public void save() {
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
			if(commission==null){
				callbackData = BjuiJson.json("300", "参数错误", "", "", "","", "", "");
			}else{
				User findUser = userService.getUserByName(commission.getUser().getName());
				if(findUser == null){
					callbackData = BjuiJson.json("300", "用户名不存在", "", "", "","", "", "");
				}else{
				            	commission.setMoney(-commission.getMoney());
				            	//设置该交易操作人信息
								commission.setOperator(loginAdmin.getName());
								//设置用户
								commission.setUser(findUser);
								// 设置用户创建日期
								commission.setCreateDate(new Date());
								commission.setDeleted(false);
								findUser.setBalance(findUser.getBalance()+commission.getMoney());
								commission.setRemark("扣除游戏额度");
								boolean res = commissionService.saveOrUpdate(commission);
								if (res) {
									callbackData = BjuiJson.json("200", "添加成功", "", "", "","true", "", "");
									//保存用户最新余额
									userService.saveOrUpdate(findUser);
								} else {
									callbackData = BjuiJson.json("300", "添加失败，请重试", "", "", "","", "", "");
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
	 * 获取信息 创建日期：2014-9-24下午10:01:36 作者：Cz
	 * 
	 * @return
	 */
	public void info() {
		String idStr = request.getParameter("id");
		String callbackData = "";
		PrintWriter out = null;
		try {
			out = response.getWriter();
		} catch (IOException e) {
			e.printStackTrace();
		}
		try {
			// ID参数为空
			if (idStr == null || "".equals(idStr)) {
				callbackData = BjuiJson.json("300", "参数不能为空", "", "", "",
						"", "", "");
			} else {
				int id = 0;
				try {
					id = Integer.parseInt(idStr);
				} catch (Exception e) {
					// 抛出异常，说明ID不是数字
					callbackData = BjuiJson.json("300", "参数错误", "", "", "",
							"", "", "");
				}
				Commission findcommission = (Commission) commissionService.findById(Commission.class, id);
				if (findcommission == null) {
					// 用户不存在
					callbackData = BjuiJson.json("300", "用户不存在", "", "",
							"", "", "", "");
				} else {
					cfg = new Configuration();
					// 设置FreeMarker的模版文件位置
					cfg.setServletContextForTemplateLoading(
							request.getServletContext(),
							TemplatesPath.ADMIN_PATH);
					Map<Object, Object> root = new HashMap<Object, Object>();
					root.put("commission", findcommission);
					FreemarkerUtils.freemarker(request, response, ftlFileName, cfg, root);
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
	 * 修改 作者：Cz
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
			if (commission == null) {
				callbackData = BjuiJson.json("300", "参数错误", "", "", "", "",
						"", "");
			} else {
				Commission findcommission = (Commission) commissionService.findById(Commission.class, commission.getId());
				commission.setCreateDate(findcommission.getCreateDate());
				commission.setDeleted(findcommission.isDeleted());
				commission.setVersion(findcommission.getVersion());
				boolean result = commissionService.saveOrUpdate(commission);
				// 修改成功
				if (result) {
					callbackData = BjuiJson.json("200", "修改成功", "",
						"", "", "true", "", "");
				} else {
					// 修改失败
					callbackData = BjuiJson.json("300", "修改失败", "",
							"", "", "", "", "");
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
	 * 删除用户 创建日期：2014-9-24下午10:01:55 作者：Cz
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
			String idStr = request.getParameter("id");
			// ID参数为空
			if (idStr == null || "".equals(idStr)) {
				callbackData = BjuiJson.json("300", "参数错误", "", "", "", "",
					"", "");
			} else {
				int id = 0;
				try {
					id = Integer.parseInt(idStr);
				} catch (Exception e) {
					// 抛出异常，说明ID不是数字
					callbackData = BjuiJson.json("300", "参数错误", "", "", "",
							"", "", "");
				}
				Commission findcommission = (Commission) commissionService.findById(Commission.class, id);
				if (findcommission == null) {
					// 用户不存在
					callbackData = BjuiJson.json("300", "用户不存在", "", "",
							"", "true", "", "");
				} else {
					boolean result = commissionService.delete(findcommission);
					if (result) {
						callbackData = BjuiJson.json("200", "删除成功", "",
								"", "", "", "", "");
					} else {
						callbackData = BjuiJson.json("300", "删除失败", "",
								"", "", "", "", "");
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
	 * 会员中心佣金列表
	 */
	public void userCommissionList() {
		String pStr = request.getParameter("p");
		int p = 1;
		if(!StringUtils.isEmpty(pStr)){
			p = Integer.parseInt(pStr);
		}
		
		HttpSession session = request.getSession();
		User loginUser = (User) session.getAttribute("loginUser");
		String countHql = "select count(*) from Commission where deleted=0 and user.id="+loginUser.getId();
		String hql = "from Commission where deleted=0 and user.id="+loginUser.getId();
		hql += " order by id desc";
		//获取总条数
		int count = 0;
		count = commissionService.getTotalCount(countHql);					
		PageModel pageModel = new PageModel();
		pageModel.setAllCount(count);
		pageModel.setCurrentPage(p);
		List<Commission> ordersList = commissionService.list(hql,pageModel.getStart(),pageModel.getPageSize());
		JSONObject json = new JSONObject();
		if(ordersList.size()==0){
			//说明没数据
			json.put("status", "0");
			//说明没有下一页
			json.put("isNextPage", "0");
		}else{
			//说明有数据
			json.put("status", "1");
			if(ordersList.size()==pageModel.getPageSize()){
				//可能有下一页
				//有下一页
				json.put("isNextPage", "1");
			}else{
				//没有下一页数据
				json.put("isNextPage", "0");
			}
			JSONArray listJson = (JSONArray) JSONArray.toJSON(ordersList);
			json.put("list", listJson);
		}
		PrintWriter out = null;
		try {
			out = response.getWriter();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		out.print(json);
		out.flush();
		out.close();
	}

	public Commission getCommission() {
		return commission;
	}

	public void setCommission(Commission commission) {
		this.commission = commission;
	}

	public String getFtlFileName() {
		return ftlFileName;
	}

	public void setFtlFileName(String ftlFileName) {
		this.ftlFileName = ftlFileName;
	}

}
