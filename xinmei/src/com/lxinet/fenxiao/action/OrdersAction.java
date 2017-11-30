package com.lxinet.fenxiao.action;

import java.io.IOException;
import java.io.PrintWriter;
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

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.lxinet.fenxiao.entities.Commission;
import com.lxinet.fenxiao.entities.Config;
import com.lxinet.fenxiao.entities.Financial;
import com.lxinet.fenxiao.entities.Kami;
import com.lxinet.fenxiao.entities.Orders;
import com.lxinet.fenxiao.entities.Orders.PayChannel;
import com.lxinet.fenxiao.entities.Orders.PayStatus;
import com.lxinet.fenxiao.entities.Product;
import com.lxinet.fenxiao.entities.User;
import com.lxinet.fenxiao.service.ICommissionService;
import com.lxinet.fenxiao.service.IConfigService;
import com.lxinet.fenxiao.service.IFinancialService;
import com.lxinet.fenxiao.service.IKamiService;
import com.lxinet.fenxiao.service.IOrdersService;
import com.lxinet.fenxiao.service.IProductService;
import com.lxinet.fenxiao.service.IUserService;
import com.lxinet.fenxiao.utils.BjuiJson;
import com.lxinet.fenxiao.utils.BjuiPage;
import com.lxinet.fenxiao.utils.BusinessException;
import com.lxinet.fenxiao.utils.FreemarkerUtils;
import com.lxinet.fenxiao.utils.PageModel;
import com.lxinet.fenxiao.utils.TemplatesPath;

import freemarker.template.Configuration;

/**
 * 订单 作者：Cz
 */
@Controller("ordersAction")
@Scope("prototype")
public class OrdersAction extends BaseAction {
	private static final long serialVersionUID = 1L;
	@Resource(name = "ordersService")
	private IOrdersService<Orders> ordersService;
	@Resource(name = "userService")
	private IUserService<User> userService;
	@Resource(name = "productService")
	private IProductService<Product> productService;
	@Resource(name = "kamiService")
	private IKamiService<Kami> kamiService;
	@Resource(name = "financialService")
	private IFinancialService<Financial> financialService;
	@Resource(name = "commissionService")
	private ICommissionService<Commission> commissionService;
	private Orders orders;
	private String ftlFileName;
	@Resource(name = "configService")
	private IConfigService<Config> configService;

	/**
	 * 订单列表 作者：Cz
	 * 
	 * @return
	 */
	public void list() {
		String key = request.getParameter("key");
		String countHql = "select count(*) from Orders where deleted=0";
		String hql = "from Orders where deleted=0";
		if (StringUtils.isNotEmpty(key)) {
			countHql += " and (user.name='" + key + "' or no='" + key + "' or productName='" + key + "')";
			hql += " and (user.name='" + key + "' or no='" + key + "' or productName='" + key + "')";
		}
		hql += " order by id desc";
		// 获取总条数
		int count = 0;
		count = ordersService.getTotalCount(countHql);
		page = new BjuiPage(pageCurrent, pageSize);
		page.setTotalCount(count);
		cfg = new Configuration();
		// 设置FreeMarker的模版文件位置
		cfg.setServletContextForTemplateLoading(request.getServletContext(), TemplatesPath.ADMIN_PATH);
		List<Orders> ordersList = ordersService.list(hql, page.getStart(), page.getPageSize());
		Map<Object, Object> root = new HashMap<Object, Object>();
		root.put("ordersList", ordersList);
		root.put("page", page);
		root.put("key", key);
		FreemarkerUtils.freemarker(request, response, ftlFileName, cfg, root);
	}

	/**
	 * 添加订单
	 */
	public void add() {
		String pidStr = request.getParameter("pid");
		int pid = 0;
		try {
			pid = Integer.parseInt(pidStr);
		} catch (Exception e) {
			request.setAttribute("status", "0");
			request.setAttribute("message", "参数错误");
			try {
				request.getRequestDispatcher("cart.jsp").forward(request, response);
			} catch (ServletException e1) {
				e1.printStackTrace();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			return;
		}
		Product findProduct = productService.findById(Product.class, pid);
		if (findProduct == null) {
			request.setAttribute("status", "0");
			request.setAttribute("message", "商品不存在");
		} else {
			request.setAttribute("status", "1");
			request.setAttribute("product", findProduct);
		}
		try {
			request.getRequestDispatcher("cart.jsp").forward(request, response);
		} catch (ServletException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 添加订单 作者：Cz
	 */
	public void save() {
		String pidStr = request.getParameter("pid");
		String numStr = request.getParameter("num");
		int pid = 0;
		int num = 1;
		try {
			pid = Integer.parseInt(pidStr);
			num = Integer.parseInt(numStr);
		} catch (Exception e) {
			request.setAttribute("status", "0");
			request.setAttribute("message", "参数错误");
			try {
				request.getRequestDispatcher("orderAdd.jsp").forward(request, response);
			} catch (ServletException e1) {
				e1.printStackTrace();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			return;
		}
		Product findProduct = productService.findById(Product.class, pid);
		if (findProduct == null) {
			request.setAttribute("status", "0");
			request.setAttribute("message", "商品不存在");
		} else {
			HttpSession session = request.getSession();
			User loginUser = (User) session.getAttribute("loginUser");
			if (loginUser == null || loginUser.getId() == null) {
				request.setAttribute("status", "0");
				request.setAttribute("message", "您未登陆或者登陆失效，请重新登陆");
			} else {
				Orders newOrders = new Orders();
				newOrders.setProductId(findProduct.getId() + "");
				newOrders.setProductName(findProduct.getTitle());
				newOrders.setProductNum(num);
				newOrders.setProductMoney(findProduct.getMoney());
				newOrders.setUser(loginUser);
				newOrders.setStatus(0);
				newOrders.setMoney(num * findProduct.getMoney());
				// 获取5位随机数
				Random random = new Random();
				int n = random.nextInt(9999);
				n = n + 10000;
				// 生成订单号
				String no = System.currentTimeMillis() + "" + n;
				newOrders.setNo(no);
				// 设置订单创建日期
				newOrders.setCreateDate(new Date());
				newOrders.setDeleted(false);
				ordersService.saveOrUpdate(newOrders);
				try {
					response.sendRedirect("settle?no=" + no);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	/**
	 * 订单结算
	 */
	public void settle() {
		String no = request.getParameter("no");
		Orders findOrders = ordersService.findByNo(no);
		if (findOrders == null) {
			request.setAttribute("status", "0");
			request.setAttribute("message", "订单不存在");
		} else {
			HttpSession session = request.getSession();
			User loginUser = (User) session.getAttribute("loginUser");
			if (loginUser == null || loginUser.getId() == null) {
				request.setAttribute("status", "0");
				request.setAttribute("message", "您未登陆或者登陆失效，请重新登陆");
			} else {
				request.setAttribute("orders", findOrders);
				try {
					request.getRequestDispatcher("settle.jsp").forward(request, response);
				} catch (ServletException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	/**
	 * 订单支付
	 */
	public void pay() {
		String no = request.getParameter("no");
		Orders findOrders = ordersService.findByNo(no);
		HttpSession session = request.getSession();
		User loginUser = (User) session.getAttribute("loginUser");

		String payChannel = request.getParameter("payChannel");

		JSONObject json = new JSONObject();
		if (loginUser == null || loginUser.getId() == null) {
			json.put("status", "0");
			json.put("message", "您未登陆或者登陆失效，请重新登陆");
			json.put("href", "../login.jsp");
		}
		
		try{
			
			checkOrder(loginUser, findOrders, no, payChannel);

			User findUser = userService.findById(User.class, loginUser.getId());
			List<Kami> kamiList = kamiService.list(
					"from Kami where deleted=0 and status=0 and product.id=" + findOrders.getProductId(), 0,
					findOrders.getProductNum());
			if (kamiList.size() < findOrders.getProductNum()) {
				throw new BusinessException("库存不足，请联系管理员");
			}

			PayStatus payStatus = payOrder(findUser, findOrders, payChannel);

			if (payStatus != PayStatus.PAID) {
				throw new BusinessException("支付失败");

			}
			
			// 支付完成生成消费信息
			generateCosumeInfo(findUser, findOrders, kamiList);

			json.put("status", "1");
			json.put("message", "付款成功");
			json.put("no", findOrders.getNo());
			
		}catch(BusinessException e){
			System.out.println("BusinessException : " + e.getMessage());
			json.put("message", e.getMessage());
			json.put("status", "0");
		}

		
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

	/**
	 * 支付完成，生成消费信息
	 * @param findUser
	 * @param findOrders
	 * @param kamiList
	 */
	private void generateCosumeInfo(User findUser, Orders findOrders, List<Kami> kamiList) {
		String summary = "分销码信息:<br/>";
		Date date = new Date();
		for (Kami kami : kamiList) {
			summary += "卡号:" + kami.getNo() + ",密码:" + kami.getPassword() + "<br/>";
			kami.setSaleTime(date);
			kami.setOrdersNo(findOrders.getNo());
			kami.setStatus(1);
			kamiService.saveOrUpdate(kami);
		}
		findOrders.setSummary(summary);
		findOrders.setPayDate(date);
		ordersService.saveOrUpdate(findOrders);

		// 添加财务信息
		Financial financial = new Financial();
		financial.setType(0);
		financial.setMoney(-findOrders.getMoney());
		financial.setNo(System.currentTimeMillis() + "");
		// 设置该交易操作人信息
		financial.setOperator(findUser.getName());
		// 设置用户
		financial.setUser(findUser);
		// 设置用户创建日期
		financial.setCreateDate(new Date());
		financial.setDeleted(false);
		// 设置余额
		financial.setBalance(findUser.getBalance());
		financial.setPayment("余额付款");
		financial.setRemark("购买" + findOrders.getProductName());
		financialService.saveOrUpdate(financial);
		Config findConfig = configService.findById(Config.class, 1);
		// 当前用户的上级
		String levelNos = findUser.getSuperior();
		if (!StringUtils.isEmpty(levelNos)) {
			String leverNoArr[] = levelNos.split(";");
			for (int i = leverNoArr.length - 1, j = 1; i > 0; i--, j++) {
				if (!StringUtils.isEmpty(leverNoArr[i])) {
					User levelUser = userService.getUserByNo(leverNoArr[i]);
					if (levelUser != null) {
						// 获取佣金比例
						double commissionRate = 0.0;
						if (j == 1) {
							commissionRate = findConfig.getFirstLevel();
						} else if (j == 2) {
							commissionRate = findConfig.getSecondLevel();
						} else if (j == 3) {
							commissionRate = findConfig.getThirdLevel();
						}

						// 计算佣金
						double commissionNum = findOrders.getMoney() * commissionRate;
						levelUser.setCommission(levelUser.getCommission() + commissionNum);
						userService.saveOrUpdate(levelUser);

						// 添加佣金信息
						Commission commission = new Commission();
						commission.setType(1);
						commission.setMoney(commissionNum);
						commission.setNo(System.currentTimeMillis() + "");
						// 设置该交易操作人信息
						commission.setOperator(findUser.getName());
						// 设置用户
						commission.setUser(levelUser);
						// 设置用户创建日期
						commission.setCreateDate(date);
						commission.setDeleted(false);
						commission.setLevel(j);
						commission.setRemark("第" + j + "级用户:编号【" + findUser.getNo() + "】购买商品奖励");
						commissionService.saveOrUpdate(commission);
					}
				}
			}
		}
	}

	/**
	 * 校验订单
	 * @param loginUser
	 * @param findOrders
	 * @param no
	 * @param payChannel
	 */
	private void checkOrder(User loginUser, Orders findOrders, String no, String payChannel) {
		User findUser = userService.findById(User.class, loginUser.getId());
		if (findOrders == null) {
			throw new BusinessException("订单不存在");
		}
		if (findOrders.getUser().getId() != findUser.getId()) {
			throw new BusinessException("没有权限");
		}

		if (findUser.getBalance() < findOrders.getMoney()) {
			throw new BusinessException("余额不足，请先充值");
		}

		if (findOrders.getStatus() == 1) {
			throw new BusinessException("该订单已付款，请不要重复提交支付");
		}
	}

	/**
	 * 订单支付
	 * @param findUser
	 * @param findOrders
	 * @param payChannel
	 * @return
	 */
	private PayStatus payOrder(User findUser, Orders findOrders, String payChannel) {
		PayStatus payStatus = PayStatus.NONPAYMENT;

		if (StringUtils.equals(payChannel, PayChannel.AMOUNT_PAY.toString())) {
			findUser.setBalance(findUser.getBalance() - findOrders.getMoney());
			if (findUser.getStatus() == 0) {
				findUser.setStatus(1);
				findUser.setStatusDate(new Date());
			}
			userService.saveOrUpdate(findUser);
			findOrders.setStatus(1);
			payStatus = PayStatus.PAID;

		} else if (StringUtils.equals(payChannel, PayChannel.WEIXIN_PAY.toString())) {

		} else if (StringUtils.equals(payChannel, PayChannel.ALI_PAY.toString())) {

		}
		return payStatus;
	}

	/**
	 * 订单详情
	 */
	public void detail() {
		String no = request.getParameter("no");
		Orders findOrders = ordersService.findByNo(no);
		if (findOrders == null) {
			request.setAttribute("status", "0");
			request.setAttribute("message", "订单不存在");
		} else {
			HttpSession session = request.getSession();
			User loginUser = (User) session.getAttribute("loginUser");
			if (findOrders.getUser().getId() != loginUser.getId()) {
				request.setAttribute("status", "0");
				request.setAttribute("message", "没有权限");
			} else {
				request.setAttribute("orders", findOrders);
				try {
					request.getRequestDispatcher("ordersDetail.jsp").forward(request, response);
				} catch (ServletException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	public void indexList() {
		String pStr = request.getParameter("p");
		int p = 1;
		if (!StringUtils.isEmpty(pStr)) {
			p = Integer.parseInt(pStr);
		}

		String type = request.getParameter("type");
		HttpSession session = request.getSession();
		User loginUser = (User) session.getAttribute("loginUser");
		String countHql = "select count(*) from Orders where deleted=0 and user.id=" + loginUser.getId();
		String hql = "from Orders where deleted=0 and user.id=" + loginUser.getId();
		if ("0".equals(type) || "1".equals(type)) {
			countHql += " and status=" + type;
			hql += " and status=" + type;
		}
		hql += " order by id desc";
		// 获取总条数
		int count = 0;
		count = ordersService.getTotalCount(countHql);
		PageModel pageModel = new PageModel();
		pageModel.setAllCount(count);
		pageModel.setCurrentPage(p);
		List<Orders> ordersList = ordersService.list(hql, pageModel.getStart(), pageModel.getPageSize());
		JSONObject json = new JSONObject();
		if (ordersList.size() == 0) {
			// 说明没数据
			json.put("status", "0");
			// 说明没有下一页
			json.put("isNextPage", "0");
		} else {
			// 说明有数据
			json.put("status", "1");
			if (ordersList.size() == pageModel.getPageSize()) {
				// 可能有下一页
				// 有下一页
				json.put("isNextPage", "1");
			} else {
				// 没有下一页数据
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

	/**
	 * 获取订单信息 创建日期：2014-9-24下午10:01:36 作者：Cz
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
				callbackData = BjuiJson.json("300", "参数不能为空", "", "", "", "", "", "");
			} else {
				int id = 0;
				try {
					id = Integer.parseInt(idStr);
				} catch (Exception e) {
					// 抛出异常，说明ID不是数字
					callbackData = BjuiJson.json("300", "参数错误", "", "", "", "", "", "");
				}
				Orders findorders = (Orders) ordersService.findById(Orders.class, id);
				if (findorders == null) {
					// 订单不存在
					callbackData = BjuiJson.json("300", "订单不存在", "", "", "", "", "", "");
				} else {
					cfg = new Configuration();
					// 设置FreeMarker的模版文件位置
					cfg.setServletContextForTemplateLoading(request.getServletContext(), TemplatesPath.ADMIN_PATH);
					Map<Object, Object> root = new HashMap<Object, Object>();
					root.put("orders", findorders);
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
	 * 修改订单 创建日期：2014-9-24下午10:01:46 作者：Cz
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
			if (orders == null) {
				callbackData = BjuiJson.json("300", "参数错误", "", "", "", "", "", "");
			} else {
				Orders findorders = (Orders) ordersService.findById(Orders.class, orders.getId());
				orders.setCreateDate(findorders.getCreateDate());
				orders.setDeleted(findorders.isDeleted());
				orders.setVersion(findorders.getVersion());
				boolean result = ordersService.saveOrUpdate(orders);
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

	/**
	 * 删除订单 创建日期：2014-9-24下午10:01:55 作者：Cz
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
				callbackData = BjuiJson.json("300", "参数错误", "", "", "", "", "", "");
			} else {
				int id = 0;
				try {
					id = Integer.parseInt(idStr);
				} catch (Exception e) {
					// 抛出异常，说明ID不是数字
					callbackData = BjuiJson.json("300", "参数错误", "", "", "", "", "", "");
				}
				Orders findorders = (Orders) ordersService.findById(Orders.class, id);
				if (findorders == null) {
					// 订单不存在
					callbackData = BjuiJson.json("300", "订单不存在", "", "", "", "true", "", "");
				} else {
					boolean result = ordersService.delete(findorders);
					if (result) {
						callbackData = BjuiJson.json("200", "删除成功", "", "", "", "", "", "");
					} else {
						callbackData = BjuiJson.json("300", "删除失败", "", "", "", "", "", "");
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

	public Orders getOrders() {
		return orders;
	}

	public void setOrders(Orders orders) {
		this.orders = orders;
	}

	public String getFtlFileName() {
		return ftlFileName;
	}

	public void setFtlFileName(String ftlFileName) {
		this.ftlFileName = ftlFileName;
	}

}
