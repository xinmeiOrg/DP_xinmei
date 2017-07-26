package com.lxinet.fenxiao.action;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.URLEncoder;
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
import com.lxinet.fenxiao.entities.Config;
import com.lxinet.fenxiao.entities.Financial;
import com.lxinet.fenxiao.entities.User;
import com.lxinet.fenxiao.entities.Withdraw;
import com.lxinet.fenxiao.service.IConfigService;
import com.lxinet.fenxiao.service.IFinancialService;
import com.lxinet.fenxiao.service.IUserService;
import com.lxinet.fenxiao.service.IWithdrawService;
import com.lxinet.fenxiao.utils.BjuiJson;
import com.lxinet.fenxiao.utils.BjuiPage;
import com.lxinet.fenxiao.utils.FreemarkerUtils;
import com.lxinet.fenxiao.utils.PageModel;
import com.lxinet.fenxiao.utils.TemplatesPath;

import freemarker.template.Configuration;

/**
 * 
 *  作者：Cz
 */
@Controller("withdrawAction")
@Scope("prototype")
public class WithdrawAction extends BaseAction {
	private static final long serialVersionUID = 1L;
	@Resource(name = "withdrawService")
	private IWithdrawService<Withdraw> withdrawService;
	@Resource(name = "configService")
	private IConfigService<Config> configService;
	@Resource(name = "userService")
	private IUserService<User> userService;
	@Resource(name = "financialService")
	private IFinancialService<Financial> financialService;
	private Withdraw withdraw;
	private String ftlFileName;

	/**
	 * 提现列表 创建日期：2014-9-24下午10:01:07 作者：Cz
	 * 
	 * @return
	 */
	public void list() {
		String key = request.getParameter("key");
		String countHql = "select count(*) from Withdraw where deleted=0";
		String hql = "from Withdraw where deleted=0";
		if(StringUtils.isNotEmpty(key)){
			countHql += " and (user.name='"+key+"')";
			hql += " and (user.name='"+key+"')";
		}
		hql += " order by id desc";
		//获取总条数
		int count = 0;
		count = withdrawService.getTotalCount(countHql);					
		page = new BjuiPage(pageCurrent, pageSize);
		page.setTotalCount(count);
		cfg = new Configuration();
		// 设置FreeMarker的模版文件位置
		cfg.setServletContextForTemplateLoading(
		request.getServletContext(), TemplatesPath.ADMIN_PATH);
		List<Withdraw> withdrawList = withdrawService.list(hql,page.getStart(),page.getPageSize());
		Map<Object, Object> root = new HashMap<Object, Object>();
		root.put("withdrawList", withdrawList);
		root.put("page", page);
		root.put("key", key);
		FreemarkerUtils.freemarker(request, response, ftlFileName, cfg, root);
	}

	/**
	 * 添加提现页面 作者：Cz
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
	 * 添加提现 作者：Cz
	 */
	public void save() {
		PrintWriter out = null;
		try {
			out = response.getWriter();
		} catch (IOException e) {
			e.printStackTrace();
		}
		JSONObject json = new JSONObject();
		if(withdraw.getMoney() <= 0){
			json.put("status", "0");
			json.put("message", "金额必须大于0");
		}else{
			HttpSession session = request.getSession();
			User loginUser = (User) session.getAttribute("loginUser");
			User findUser = userService.findById(User.class, loginUser.getId());
			if(withdraw.getMoney() > findUser.getBalance()){
				json.put("status", "0");
				json.put("message", "余额不足");
			}else{
				withdraw.setUser(findUser);
				withdraw.setStatus(0);
				withdraw.setDeleted(false);
				withdraw.setCreateDate(new Date());
				boolean result = withdrawService.saveOrUpdate(withdraw);
				if(result){
					findUser.setCommission(findUser.getCommission()-withdraw.getMoney());
					userService.saveOrUpdate(findUser);
					//添加财务信息
					Financial financial = new Financial();
					financial.setType(0);
					financial.setMoney(withdraw.getMoney());
					financial.setNo(System.currentTimeMillis()+"");
	            	//设置该交易操作人信息
					financial.setOperator(loginUser.getName());
					//设置用户
					financial.setUser(findUser);
					// 设置用户创建日期
					financial.setCreateDate(new Date());
					financial.setDeleted(false);
					//设置余额
					financial.setBalance(findUser.getCommission());
					financial.setPayment("提现");
					financial.setRemark("提现");
					financialService.saveOrUpdate(financial);
					json.put("status", "1");
					json.put("message", "提现申请提交成功");
				}else{
					json.put("status", "0");
					json.put("message", "提现申请提交失败，请重试");
				}
			}
		}
		out.print(json.toString());
		out.flush();
		out.close();
	}

	/**
	 * 获取提现信息 创建日期：2014-9-24下午10:01:36 作者：Cz
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
				Withdraw findwithdraw = (Withdraw) withdrawService.findById(
						Withdraw.class, id);
				if (findwithdraw == null) {
					// 提现不存在
					callbackData = BjuiJson.json("300", "提现不存在", "", "",
							"", "", "", "");
				} else {
					cfg = new Configuration();
					// 设置FreeMarker的模版文件位置
					cfg.setServletContextForTemplateLoading(
							request.getServletContext(),
							TemplatesPath.ADMIN_PATH);
					Map<Object, Object> root = new HashMap<Object, Object>();
					root.put("withdraw", findwithdraw);
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
	
	public void detail() {
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
				Withdraw findwithdraw = (Withdraw) withdrawService.findById(
						Withdraw.class, id);
				if (findwithdraw == null) {
					// 提现不存在
					callbackData = BjuiJson.json("300", "提现不存在", "", "",
							"", "", "", "");
				} else {
					findwithdraw.setStatus(1);
					withdrawService.saveOrUpdate(findwithdraw);
					callbackData = BjuiJson.json("200", "处理完成", "", "", "", "", "", "");
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
	 * 修改提现 创建日期：2014-9-24下午10:01:46 作者：Cz
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
			if (withdraw == null) {
				callbackData = BjuiJson.json("300", "参数错误", "", "", "", "",
						"", "");
			} else {
				Withdraw findwithdraw = (Withdraw) withdrawService.findById(Withdraw.class, withdraw.getId());
				withdraw.setCreateDate(findwithdraw.getCreateDate());
				withdraw.setDeleted(findwithdraw.isDeleted());
				withdraw.setVersion(findwithdraw.getVersion());
				boolean result = withdrawService.saveOrUpdate(withdraw);
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
	 * 删除提现 创建日期：2014-9-24下午10:01:55 作者：Cz
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
				Withdraw findwithdraw = (Withdraw) withdrawService.findById(Withdraw.class, id);
				if (findwithdraw == null) {
					// 提现不存在
					callbackData = BjuiJson.json("300", "提现不存在", "", "",
							"", "true", "", "");
				} else {
					boolean result = withdrawService.delete(findwithdraw);
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

	public void userWithdrawList() {
		String pStr = request.getParameter("p");
		int p = 1;
		if(!StringUtils.isEmpty(pStr)){
			p = Integer.parseInt(pStr);
		}
		
		HttpSession session = request.getSession();
		User loginUser = (User) session.getAttribute("loginUser");
		String countHql = "select count(*) from Withdraw where deleted=0 and user.id="+loginUser.getId();
		String hql = "from Withdraw where deleted=0 and user.id="+loginUser.getId();
		hql += " order by id desc";
		//获取总条数
		int count = 0;
		count = withdrawService.getTotalCount(countHql);					
		PageModel pageModel = new PageModel();
		pageModel.setAllCount(count);
		pageModel.setCurrentPage(p);
		List<Withdraw> withdrawList = withdrawService.list(hql,pageModel.getStart(),pageModel.getPageSize());
		JSONObject json = new JSONObject();
		if(withdrawList.size()==0){
			//说明没数据
			json.put("status", "0");
			//说明没有下一页
			json.put("isNextPage", "0");
		}else{
			//说明有数据
			json.put("status", "1");
			if(withdrawList.size()==pageModel.getPageSize()){
				//可能有下一页
				//有下一页
				json.put("isNextPage", "1");
			}else{
				//没有下一页数据
				json.put("isNextPage", "0");
			}
			JSONArray listJson = (JSONArray) JSONArray.toJSON(withdrawList);
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

	public Withdraw getWithdraw() {
		return withdraw;
	}

	public void setWithdraw(Withdraw withdraw) {
		this.withdraw = withdraw;
	}

	public String getFtlFileName() {
		return ftlFileName;
	}

	public void setFtlFileName(String ftlFileName) {
		this.ftlFileName = ftlFileName;
	}

}
