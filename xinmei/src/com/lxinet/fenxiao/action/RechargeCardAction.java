package com.lxinet.fenxiao.action;

import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.json.JSONException;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.alibaba.fastjson.JSONObject;
import com.lxinet.fenxiao.entities.Admin;
import com.lxinet.fenxiao.entities.Financial;
import com.lxinet.fenxiao.entities.RechargeCard;
import com.lxinet.fenxiao.entities.User;
import com.lxinet.fenxiao.service.IAdminService;
import com.lxinet.fenxiao.service.IFinancialService;
import com.lxinet.fenxiao.service.IRechargeCardService;
import com.lxinet.fenxiao.service.IUserService;
import com.lxinet.fenxiao.utils.BjuiJson;
import com.lxinet.fenxiao.utils.BjuiPage;
import com.lxinet.fenxiao.utils.FreemarkerUtils;
import com.lxinet.fenxiao.utils.Uuid;

import freemarker.template.Configuration;

/**
 * 充值卡
 * 
 */
@Controller("rechargeCardAction")
@Scope("prototype")
public class RechargeCardAction extends BaseAction {
	private static final long serialVersionUID = 1L;
	@Resource(name = "rechargeCardService")
	private IRechargeCardService<RechargeCard> rechargeCardService;
	private RechargeCard rechargeCard;
	@Resource(name = "adminService")
	private IAdminService<Admin> adminService;
	@Resource(name = "userService")
	private IUserService<User> userService;
	@Resource(name = "financialService")
	private IFinancialService<Financial> financialService;
	/**
	 * 充值卡列表 创建日期：2014-9-24下午10:01:07 作者：Cz
	 * 
	 * @return
	 */
	public void list() {
		String no = request.getParameter("no");
		String status = request.getParameter("status");
		//获取总条数
		int count = 0;
		String countHql = "select count(*) from RechargeCard where deleted=0";
		if(StringUtils.isNotEmpty(no)){
			countHql += " and no like '%"+no+"%'";
		}
		if(StringUtils.isNotEmpty(status)){
			countHql += " and status="+status;
		}
		count = rechargeCardService.getTotalCount(countHql);
								
		page = new BjuiPage(pageCurrent, pageSize);
		page.setTotalCount(count);
		cfg = new Configuration();
		// 设置FreeMarker的模版文件位置
		cfg.setServletContextForTemplateLoading(request.getServletContext(),"WEB-INF/templates/admin");
		String hql = "from RechargeCard where deleted=0";
		if(StringUtils.isNotEmpty(no)){
			hql += " and no like '%"+no+"%'";
		}
		if(StringUtils.isNotEmpty(status)){
			hql += " and status="+status;
		}
		hql += " order by id desc";
		List<RechargeCard> rechargeCardList = rechargeCardService.list(hql,page.getStart(),page.getPageSize());
		Map<Object, Object> root = new HashMap<Object, Object>();
		root.put("rechargeCardList", rechargeCardList);
		root.put("page", page);
		root.put("no", no);
		FreemarkerUtils.freemarker(request, response, "rechargeCardList.ftl", cfg, root);
	}

	/**
	 * 添加充值卡页面 创建日期：2014-10-25下午6:20:23 作者：Cz
	 */
	public void add() {
		cfg = new Configuration();
		// 设置FreeMarker的模版文件位置
		cfg.setServletContextForTemplateLoading(request.getServletContext(),"WEB-INF/templates/admin");
		Map<Object, Object> root = new HashMap<Object, Object>();
		FreemarkerUtils.freemarker(request, response, "rechargeCardAdd.ftl", cfg, root);
	}
	

	/**
	 * 添加充值卡 创建日期：2014-9-24下午10:01:20 作者：Cz
	 */
	public void save() {
		PrintWriter out = null;
		try {
			out = response.getWriter();
		} catch (IOException e) {
			e.printStackTrace();
		}
		String numStr = request.getParameter("num");
		String moneyStr = request.getParameter("money");
		int num = Integer.parseInt(numStr);
		double money = Double.parseDouble(moneyStr);
		String callbackData = "";
		try {
			Date date = new Date();
			for (int i = 0; i < num; i++) {
				String no = Uuid.getUUID();
				RechargeCard rechargeCard = new RechargeCard();
				rechargeCard.setDeleted(false);
				rechargeCard.setCreateDate(date);
				rechargeCard.setMoney(money);
				rechargeCard.setStatus(0);
				rechargeCard.setNo(no);
				rechargeCardService.saveOrUpdate(rechargeCard);
			}
			callbackData = BjuiJson.json("200", "成功生成"+num+"张充值卡", "", "","", "true","", "");
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
		String no = request.getParameter("no");
		cfg = new Configuration();
		// 设置FreeMarker的模版文件位置
		cfg.setServletContextForTemplateLoading(
		request.getServletContext(),"WEB-INF/templates/admin");
		Map<Object, Object> root = new HashMap<Object, Object>();
		root.put("no", no);
		FreemarkerUtils.freemarker(request, response,"rechargeCardChongzhi.ftl", cfg, root);
	}
	

	/**
	 * 删除充值卡 创建日期：2014-9-24下午10:01:55 作者：Cz
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
				callbackData = BjuiJson.json("300", "参数错误", "", "", "", "","", "");
			} else {
				int id = 0;
				try {
					id = Integer.parseInt(idStr);
				} catch (Exception e) {
					// 抛出异常，说明ID不是数字
					callbackData = BjuiJson.json("300", "参数错误", "", "", "","","", "");
				}
				RechargeCard findRechargeCard = (RechargeCard) rechargeCardService.findById(RechargeCard.class,id);
				if (findRechargeCard == null) {
					// 充值卡不存在
					callbackData = BjuiJson.json("300", "充值卡不存在", "", "","", "","", "");
				} else {
					boolean result = rechargeCardService.delete(findRechargeCard);
					if (result) {
						callbackData = BjuiJson.json("200", "删除成功", "","","", "","", "");
					} else {
						callbackData = BjuiJson.json("300", "删除失败", "","", "", "","", "");
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
	
	public void userUseRechargeCard() {
		PrintWriter out = null;
		try {
			out = response.getWriter();
		} catch (IOException e) {
			e.printStackTrace();
		}
		HttpSession session = request.getSession();
		User loginUser = (User) session.getAttribute("loginUser");
		User findUser = userService.findById(User.class, loginUser.getId());
		String no = request.getParameter("no");
		RechargeCard findRechargeCard = rechargeCardService.getByNo(no);
		JSONObject json = new JSONObject();
			if(findRechargeCard == null){
				json.put("status", "0");
				json.put("message", "充值卡不存在");
			}else{
				if(findRechargeCard.getStatus()==1){
					json.put("status", "0");
					json.put("message", "充值卡已被使用");
				}else{
					//添加财务信息
					Financial financial = new Financial();
					financial.setType(1);
					financial.setMoney(findRechargeCard.getMoney());
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
					financial.setPayment("充值卡充值");
					financial.setRemark("充值卡充值,充值卡卡号:"+findRechargeCard.getNo());
					financialService.saveOrUpdate(financial);
					findUser.setBalance(findUser.getBalance()+findRechargeCard.getMoney());
					findUser.setBalance(new BigDecimal(findUser.getBalance()).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());
					userService.saveOrUpdate(findUser);
					//设置为已被使用
					findRechargeCard.setStatus(1);
					SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
					findRechargeCard.setUseTime(sdf.format(new Date()));
					findRechargeCard.setUseUserNo(findUser.getNo());
					rechargeCardService.saveOrUpdate(findRechargeCard);
					json.put("status", "1");
					json.put("message", "充值成功，充值金额:"+findRechargeCard.getMoney()+"元");
				}
		}
		out.print(json.toString());
		out.flush();
		out.close();
	}

	public RechargeCard getRechargeCard() {
		return rechargeCard;
	}

	public void setRechargeCard(RechargeCard rechargeCard) {
		this.rechargeCard = rechargeCard;
	}

}
