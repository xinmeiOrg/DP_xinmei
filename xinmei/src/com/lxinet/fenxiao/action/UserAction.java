package com.lxinet.fenxiao.action;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.annotation.Resource;
import javax.servlet.ServletException;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONException;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.alibaba.fastjson.JSONObject;
import com.lxinet.fenxiao.entities.Config;
import com.lxinet.fenxiao.entities.Financial;
import com.lxinet.fenxiao.entities.User;
import com.lxinet.fenxiao.service.IConfigService;
import com.lxinet.fenxiao.service.IFinancialService;
import com.lxinet.fenxiao.service.IUserService;
import com.lxinet.fenxiao.utils.BjuiJson;
import com.lxinet.fenxiao.utils.BjuiPage;
import com.lxinet.fenxiao.utils.FreemarkerUtils;
import com.lxinet.fenxiao.utils.IpUtils;
import com.lxinet.fenxiao.utils.Md5;
import com.lxinet.fenxiao.utils.TemplatesPath;

import freemarker.template.Configuration;

/**
 * 会员
 * 作者：Cz
 */
@Controller("userAction")
@Scope("prototype")
public class UserAction extends BaseAction {
	private static final long serialVersionUID = 1L;
	@Resource(name = "userService")
	private IUserService<User> userService;
	@Resource(name = "configService")
	private IConfigService<Config> configService;
	@Resource(name = "financialService")
	private IFinancialService<Financial> financialService;
	private User user;
	private String ftlFileName;

	/**
	 * 用户列表 
	 * 作者：Cz
	 * @return
	 */
	public void list() {
		String key = request.getParameter("key");
		String countHql = "select count(*) from User where deleted=0";
		String hql = "from User where deleted=0";
		if(StringUtils.isNotEmpty(key)){
			countHql += " and (name='"+key+"' or no='"+key+"' or phone='"+key+"')";
			hql += " and (name='"+key+"' or no='"+key+"' or phone='"+key+"')";
		}
		hql += " order by id desc";
		//获取总条数
		int count = 0;
		count = userService.getTotalCount(countHql);					
		page = new BjuiPage(pageCurrent, pageSize);
		page.setTotalCount(count);
		cfg = new Configuration();
		// 设置FreeMarker的模版文件位置
		cfg.setServletContextForTemplateLoading(
		request.getServletContext(), TemplatesPath.ADMIN_PATH);
		List<User> userList = userService.list(hql,page.getStart(),page.getPageSize());
		Map<Object, Object> root = new HashMap<Object, Object>();
		root.put("userList", userList);
		root.put("page", page);
		root.put("key", key);
		FreemarkerUtils.freemarker(request, response, ftlFileName, cfg, root);
	}

	/**
	 * 添加用户页面 
	 * 作者：Cz
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
	 * 
	 * 注册用户 
	 * 作者：Cz
	 */
	public void register() {
		PrintWriter out = null;
		try {
			out = response.getWriter();
		} catch (IOException e) {
			e.printStackTrace();
		}
		String tuijianren = request.getParameter("tuijianren");
		//获取推荐人信息
		User tjrUser = userService.getUserByNo(tuijianren);
		JSONObject json = new JSONObject();
			if(user==null){
				json.put("status", "0");
				json.put("message", "参数错误");
			}else if (userService.getUserByName(user.getName()) != null) {
				json.put("status", "0");
				json.put("message", "账号已存在");
			} else if (userService.getUserByName(user.getPhone()) != null) {
				json.put("status", "0");
				json.put("message", "手机号已存在");
			}
//			else if (tjrUser == null) {
//				json.put("status", "0");
//				json.put("message", "推荐人不存在");
//			} else if (tjrUser.getStatus() == 0) {
//				json.put("status", "0");
//				json.put("message", "推荐人未激活");
//			} 
			else {
				try {
					String ip = IpUtils.getIpAddress(request);
					user.setRegisterIp(ip);
				} catch (Exception e) {
					user.setRegisterIp("0.0.0.0");
				}
				if(tjrUser != null){
					if(StringUtils.isEmpty(tjrUser.getSuperior())){
						user.setSuperior(";"+tuijianren+";");
					}else{
						if(tjrUser.getSuperior().split(";").length>3){
							user.setSuperior(";"+tjrUser.getSuperior().split(";", 3)[2]+tuijianren+";");
						}else{
							user.setSuperior(tjrUser.getSuperior()+tuijianren+";");
						}
					}
				}
				
				user.setPassword(Md5.getMD5Code(user.getPassword()));
				user.setLoginCount(0);
				user.setStatus(0);
				user.setBalance(0.0);
				user.setCommission(0.0);
				user.setDeleted(false);
				// 设置用户创建日期
				user.setCreateDate(new Date());
				boolean res = userService.saveOrUpdate(user);
				if (res) {
					User loginUser = userService.getUserByName(user.getName());
					//先增加登录次数
					loginUser.setLoginCount(loginUser.getLoginCount()+1);
					//先设置session，再设置最后的登录IP和时间，在会员中心看到上次的登录IP和时间，而不是这次的登录IP和时间
					String ip;
					try {
						ip = IpUtils.getIpAddress(request);
						loginUser.setLastLoginIp(ip);
					} catch (Exception e) {
						loginUser.setLastLoginIp("0.0.0.0");
					}
					loginUser.setLastLoginTime(new Date());
					userService.saveOrUpdate(loginUser);
					HttpSession session = request.getSession();
					session.setAttribute("loginUser", loginUser);
					json.put("status", "1");
					json.put("message", "注册成功");
				} else {
					json.put("status", "0");
					json.put("message", "注册失败，请重试");
				}
			}
		out.print(json.toString());
		out.flush();
		out.close();
	}

	/**
	 * 作者：Cz
	 * @return
	 * @throws JSONException 
	 */
	public void info() throws JSONException {
		String idStr = request.getParameter("id");
		String callbackData = "";
		PrintWriter out = null;
		try {
			out = response.getWriter();
		} catch (IOException e) {
			e.printStackTrace();
		}
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
				User findUser = (User) userService.findById(
						User.class, id);
				if (findUser == null) {
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
					root.put("user", findUser);
					FreemarkerUtils.freemarker(request, response, ftlFileName, cfg, root);
				}
			}
		out.print(callbackData);
		out.flush();
		out.close();
	}

	/**
	 * 作者：Cz
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
			if (user == null) {
				callbackData = BjuiJson.json("300", "参数错误", "", "", "", "",
						"", "");
			} else {
				User findUser = (User) userService.findById(User.class, user.getId());
				if(StringUtils.isNotEmpty(user.getPassword())){
					findUser.setPassword(Md5.getMD5Code(user.getPassword()));
				}
				boolean result = userService.saveOrUpdate(findUser);
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
				if(id==1){
					callbackData = BjuiJson.json("300", "该用户不能删除", "", "",
							"", "true", "", "");
				}else{
					User findUser = (User) userService.findById(User.class, id);
					if (findUser == null) {
						// 用户不存在
						callbackData = BjuiJson.json("300", "用户不存在", "", "",
								"", "true", "", "");
					} else {
						boolean result = userService.delete(findUser);
						if (result) {
							callbackData = BjuiJson.json("200", "删除成功", "",
									"", "", "", "", "");
						} else {
							callbackData = BjuiJson.json("300", "删除失败", "",
									"", "", "", "", "");
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
	 * 用户登录
	 */
	public void login() {
		PrintWriter out = null;
		try {
			out = response.getWriter();
		} catch (IOException e) {
			e.printStackTrace();
		}
		HttpSession session = request.getSession();
		JSONObject json = new JSONObject();
			if(user==null){
				json.put("status", "0");
				json.put("message", "参数错误");
			}else {
				User loginUser = userService.login(user.getName(),Md5.getMD5Code(user.getPassword()));
				if(loginUser == null){
					json.put("status", "0");
					json.put("message", "用户名或者密码错误");
				} else {
					//先增加登录次数
					loginUser.setLoginCount(loginUser.getLoginCount()+1);
					session.setAttribute("loginUser", loginUser);
					//先设置session，再设置最后的登录IP和时间，在会员中心看到上次的登录IP和时间，而不是这次的登录IP和时间
					String ip;
					try {
						ip = IpUtils.getIpAddress(request);
						loginUser.setLastLoginIp(ip);
					} catch (Exception e) {
						loginUser.setLastLoginIp("0.0.0.0");
					}
					loginUser.setLastLoginTime(new Date());
					userService.saveOrUpdate(loginUser);
					json.put("status", "1");
					json.put("message", "登录成功");
				}
			}
		out.print(json.toString());
		out.flush();
		out.close();
	}
	
	/**
	 * 推广链接
	 */
	public void promote() {
		String no = request.getParameter("no");
		User findUser = userService.getUserByNo(no);
		if(findUser==null){
			request.setAttribute("status", "0");
			request.setAttribute("message", "推广链接无效");
		}else {
			if(findUser.getStatus()==0){
				request.setAttribute("status", "0");
				request.setAttribute("message", "推广链接无效");
			}else{
				request.setAttribute("status", "1");
				request.setAttribute("no", no);
			}
		}
		try {
			request.getRequestDispatcher("promoteRegister.jsp").forward(request, response);
		} catch (ServletException e1) {
			e1.printStackTrace();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}
	
	public void logout() throws IOException {
		HttpSession session = request.getSession();
		session.setAttribute("loginUser", null);
		response.sendRedirect("../login.jsp");
	}
	
	public void changePassword() {
		PrintWriter out = null;
		try {
			out = response.getWriter();
		} catch (IOException e) {
			e.printStackTrace();
		}
		String oldPassword = request.getParameter("oldPassword");
		String newPassword = request.getParameter("newPassword");
		HttpSession session = request.getSession();
		User loginUser = (User) session.getAttribute("loginUser");
		User findUser = userService.findById(User.class, loginUser.getId());
		JSONObject json = new JSONObject();
		try {
			if(!StringUtils.equals(findUser.getPassword(), Md5.getMD5Code(oldPassword))){
				json.put("status", "0");
				json.put("message", "旧密码错误");
			}else{
				findUser.setPassword(Md5.getMD5Code(newPassword));
		    	userService.saveOrUpdate(findUser);
		    	json.put("status", "1");
				json.put("message", "密码修改成功");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		out.print(json);
		out.flush();
		out.close();
	}
	
	public void resetPassword() {
		PrintWriter out = null;
		try {
			out = response.getWriter();
		} catch (IOException e) {
			e.printStackTrace();
		}
		String password = request.getParameter("password");
		String phone = request.getParameter("phone");
		User findUser = userService.getUserByPhone(phone);
		JSONObject json = new JSONObject();
		try {
			if(findUser==null){
				json.put("status", "0");
				json.put("message", "用户不存在");
			}else{
				findUser.setPassword(Md5.getMD5Code(password));
				userService.saveOrUpdate(findUser);
				json.put("status", "1");
				json.put("message", "密码重置成功");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		out.print(json);
		out.flush();
		out.close();
	}
	
	/**
	 * 生成编号
	 */
	public void createUserNo(){
		User findUser = null;
		String no = "";
		do{
			Random random = new Random();
			int num = random.nextInt(899999)+100000;
			no = num+"";
			user = userService.getUserByNo(no);
		}while(findUser!=null);
		PrintWriter out = null;
		try {
			out = response.getWriter();
		} catch (IOException e) {
			e.printStackTrace();
		}
		out.print(no);
		out.flush();
		out.close();
	}

	/**
	 * 获取登录用户信息
	 */
	public void userInfoJson() {
		PrintWriter out = null;
		try {
			out = response.getWriter();
		} catch (IOException e) {
			e.printStackTrace();
		}
		HttpSession session = request.getSession();
		User loginUser = (User) session.getAttribute("loginUser");
		User findUser = userService.findById(User.class, loginUser.getId());
		JSONObject json = (JSONObject) JSONObject.toJSON(findUser);
		out.print(json.toString());
		out.flush();
		out.close();
	}
	
	/**
	 * 佣金转入货币
	 */
	public void commissionToBalance() {
		PrintWriter out = null;
		try {
			out = response.getWriter();
		} catch (IOException e) {
			e.printStackTrace();
		}
		JSONObject json = new JSONObject();
		String moneyStr = request.getParameter("money");
		Double money = 0.0;
		try {
			money = Double.parseDouble(moneyStr);
		} catch (Exception e) {
			json.put("status", "0");
			json.put("message", "参数错误");
			out.print(json.toString());
			out.flush();
			out.close();
			return;
		}
		if(money <= 0){
			json.put("status", "0");
			json.put("message", "金额必须大于0");
		}else{
			HttpSession session = request.getSession();
			User loginUser = (User) session.getAttribute("loginUser");
			User findUser = userService.findById(User.class, loginUser.getId());
			if(money > findUser.getCommission()){
				json.put("status", "0");
				json.put("message", "佣金额度不足");
			}else{
				findUser.setBalance(findUser.getBalance()+money);
				findUser.setBalance(new BigDecimal(findUser.getBalance()).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());
				findUser.setCommission(findUser.getCommission()-money);
				userService.saveOrUpdate(findUser);
				//添加财务信息
				Financial financial = new Financial();
				financial.setType(1);
				financial.setMoney(money);
				financial.setNo(System.currentTimeMillis()+"");
            	//设置该交易操作人信息
				financial.setOperator(loginUser.getName());
				//设置用户
				financial.setUser(findUser);
				// 设置用户创建日期
				financial.setCreateDate(new Date());
				financial.setDeleted(false);
				//设置余额
				financial.setBalance(findUser.getBalance());
				financial.setPayment("佣金转入");
				financial.setRemark("佣金转入");
				financialService.saveOrUpdate(financial);
				json.put("status", "1");
				json.put("message", "转入成功");
			}
		}
		out.print(json.toString());
		out.flush();
		out.close();
	}
	
	/**
	 * 货币转账
	 */
	public void balanceToUser() {
		PrintWriter out = null;
		try {
			out = response.getWriter();
		} catch (IOException e) {
			e.printStackTrace();
		}
		JSONObject json = new JSONObject();
		String moneyStr = request.getParameter("money");
		String userno = request.getParameter("userno");
		Double money = 0.0;
		try {
			money = Double.parseDouble(moneyStr);
		} catch (Exception e) {
			json.put("status", "0");
			json.put("message", "参数错误");
			out.print(json.toString());
			out.flush();
			out.close();
			return;
		}
		User toUser = userService.getUserByNo(userno);
		if(toUser == null){
			json.put("status", "0");
			json.put("message", "会员编号不存在");
		}else{
			if(money <= 0){
				json.put("status", "0");
				json.put("message", "金额必须大于0");
			}else{
				HttpSession session = request.getSession();
				User loginUser = (User) session.getAttribute("loginUser");
				User findUser = userService.findById(User.class, loginUser.getId());
				if(money > findUser.getBalance()){
					json.put("status", "0");
					json.put("message", "货币额度不足");
				}else{
					findUser.setBalance(findUser.getBalance()-money);
					findUser.setBalance(new BigDecimal(findUser.getBalance()).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());
					toUser.setBalance(toUser.getBalance()+money);
					toUser.setBalance(new BigDecimal(toUser.getBalance()).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());
					userService.saveOrUpdate(findUser);
					userService.saveOrUpdate(toUser);
					//添加财务信息
					Financial financial = new Financial();
					financial.setType(0);
					financial.setMoney(-money);
					financial.setNo(System.currentTimeMillis()+"");
	            	//设置该交易操作人信息
					financial.setOperator(loginUser.getName());
					//设置用户
					financial.setUser(findUser);
					// 设置用户创建日期
					financial.setCreateDate(new Date());
					financial.setDeleted(false);
					//设置余额
					financial.setBalance(findUser.getBalance());
					financial.setPayment("会员转账");
					financial.setRemark("会员转账，转入到会员编号【"+userno+"】");
					financialService.saveOrUpdate(financial);
					
					//添加财务信息
					Financial financial2 = new Financial();
					financial2.setType(1);
					financial2.setMoney(money);
					financial2.setNo(System.currentTimeMillis()+"");
	            	//设置该交易操作人信息
					financial2.setOperator(loginUser.getName());
					//设置用户
					financial2.setUser(toUser);
					// 设置用户创建日期
					financial2.setCreateDate(new Date());
					financial2.setDeleted(false);
					//设置余额
					financial2.setBalance(toUser.getBalance());
					financial2.setPayment("会员转账");
					financial2.setRemark("由会员编号【"+loginUser.getNo()+"】转入");
					financialService.saveOrUpdate(financial2);
					json.put("status", "1");
					json.put("message", "转入成功");
				}
			}
		}
		out.print(json.toString());
		out.flush();
		out.close();
	}
	
	public void levelUserList(){
		HttpSession session = request.getSession();
		User loginUser = (User) session.getAttribute("loginUser");
		List<User> levelUserList = userService.levelUserList(loginUser.getNo());
		//1级下级用户数量
		int firstLevelNum = 0;
		//2级下级用户数量
		int secondLevelNum = 0;
		//3级下级用户数量
		int thirdLevelNum = 0;
		//总的下级用户数量
		int allLevelNum = levelUserList.size();
		//已注册未报单人数
		int unStatusUserNum = 0;
		//当日注册人数
		int todayRegUserNum = 0;
		//当日报单人数
		int todayStatusUserNum = 0;
//		//存放已注册未报单会员编号，以免重复统计
//		List<String> unStatusUserNumList = new ArrayList<String>();
//		//存放当日注册人数会员编号，以免重复统计
//		List<String> todayRegUserNumList = new ArrayList<String>();
//		//存放当日报单人数会员编号，以免重复统计
//		List<String> todayStatusUserNumList = new ArrayList<String>();
		for (User user : levelUserList) {
			//未报单人数
			if(user.getStatus()==0){
				unStatusUserNum ++;
			}
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			//今天日期
			String todayDate = sdf.format(new Date());
			//获取注册日期
			//注册时间
			String createDate = sdf.format(user.getCreateDate());
			//激活时间
			String statusDate = "";
			if(user.getStatusDate()!=null){
				statusDate = sdf.format(user.getStatusDate());
			}
			//当日注册人数
			if(StringUtils.equals(createDate, todayDate)){
				todayRegUserNum ++;
			}
			
			//当日报单人数
			if(StringUtils.equals(statusDate, todayDate)){
				todayStatusUserNum ++;
			}
			
			//当前用户的上级
			String levelNos = user.getSuperior();
			if(!StringUtils.isEmpty(levelNos)){
				String leverNoArr[] = levelNos.split(";");
				for (int i = leverNoArr.length-1,j = 1;i > 0;i --,j++) {
					if(!StringUtils.isEmpty(leverNoArr[i])){
						User levelUser = userService.getUserByNo(leverNoArr[i]);
						if(levelUser != null){
							
//							if(levelUser.getStatus()==0){
//								//不存在
//								if(!unStatusUserNumList.contains(leverNoArr[i])){
//									unStatusUserNum ++;
//									unStatusUserNumList.add(leverNoArr[i]);
//								}
//							}
							
							
//							//当日注册人数
//							if(StringUtils.equals(createDate, todayDate)){
//								//不存在
//								if(!todayRegUserNumList.contains(leverNoArr[i])){
//									todayRegUserNum ++;
//									todayRegUserNumList.add(leverNoArr[i]);
//								}
//							}
//							//当日报单人数
//							if(StringUtils.equals(statusDate, todayDate)){
//								//不存在
//								if(!todayStatusUserNumList.contains(leverNoArr[i])){
//									todayStatusUserNum ++;
//									todayStatusUserNumList.add(leverNoArr[i]);
//								}
//							}
							
							if(j==1 && StringUtils.equals(loginUser.getNo(), leverNoArr[i])){
								firstLevelNum ++;
							}else if(j==2 && StringUtils.equals(loginUser.getNo(), leverNoArr[i])){
								secondLevelNum ++;
							}else if(j==3 && StringUtils.equals(loginUser.getNo(), leverNoArr[i])){
								thirdLevelNum ++;
							}
						}
					}
				}
			}
		}
		JSONObject json = new JSONObject();
		json.put("firstLevelNum", firstLevelNum);
		json.put("secondLevelNum", secondLevelNum);
		json.put("thirdLevelNum", thirdLevelNum);
		json.put("allLevelNum", allLevelNum);
		json.put("unStatusUserNum", unStatusUserNum);
		json.put("todayRegUserNum", todayRegUserNum);
		json.put("todayStatusUserNum", todayStatusUserNum);
		PrintWriter out = null;
		try {
			out = response.getWriter();
		} catch (IOException e) {
			e.printStackTrace();
		}
		out.print(json.toString());
		out.flush();
		out.close();
	}
	
	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public String getFtlFileName() {
		return ftlFileName;
	}

	public void setFtlFileName(String ftlFileName) {
		this.ftlFileName = ftlFileName;
	}
	public static void main(String[] args) {
		String a = ";1;2;3;";
		System.out.println(a.split(";").length);
		String[] b;
		if(a.split(";").length>3){
			b = a.split(";", 3);
			System.out.println(b[2]);
		}
	}
}
