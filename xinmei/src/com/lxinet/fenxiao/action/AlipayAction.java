package com.lxinet.fenxiao.action;

import java.io.PrintWriter;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Random;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

import com.lxinet.fenxiao.service.*;
import com.lxinet.fenxiao.utils.StaticFileUtil;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.lxinet.fenxiao.entities.Config;
import com.lxinet.fenxiao.entities.Financial;
import com.lxinet.fenxiao.entities.Orders;
import com.lxinet.fenxiao.entities.Recharge;
import com.lxinet.fenxiao.entities.User;
import com.lxinet.fenxiao.pay.alipay.AlipayConfig;
import com.lxinet.fenxiao.pay.alipay.AlipayNotify;
import com.lxinet.fenxiao.pay.alipay.AlipaySubmit;


@Controller("alipayAction")
@Scope("prototype")
public class AlipayAction extends BaseAction {
	private static final long serialVersionUID = 1L;
	@Resource(name = "ordersService")
	private IOrdersService<Orders> ordersService;
	@Resource(name = "userService")
	private IUserService<User> userService;
	private Orders orders;
	private String ftlFileName;
	@Resource(name = "configService")
	private IConfigService<Config> configService;
	@Resource(name = "financialService")
	private IFinancialService<Financial> financialService;
	@Resource(name = "rechargeService")
	private IRechargeService<Recharge> rechargeService;
	@Resource(name = "aliWapPayService")
	private IAliWapPayService aliWapPayService;

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


	/**
	 * 支付成功页面跳转 IOS不支持自动跳转
	 * @return
	 * @throws Exception
     */
	public String returnUrl() throws Exception {
		PrintWriter out = response.getWriter();
		//获取支付宝GET过来反馈信息
		Map<String, String> params = new HashMap<String, String>();
		Map requestParams = request.getParameterMap();
		for (Iterator iter = requestParams.keySet().iterator(); iter.hasNext(); ) {
			String name = (String) iter.next();
			String[] values = (String[]) requestParams.get(name);
			String valueStr = "";
			for (int i = 0; i < values.length; i++) {
				valueStr = (i == values.length - 1) ? valueStr + values[i]
						: valueStr + values[i] + ",";
			}
			//乱码解决，这段代码在出现乱码时使用。如果mysign和sign不相等也可以使用这段代码转化
			valueStr = new String(valueStr.getBytes("ISO-8859-1"), "utf-8");
			params.put(name, valueStr);
			System.out.println("notifyUrl ------ > ");
			System.out.println(name + " : " + valueStr);
		}

		//获取支付宝的通知返回参数，可参考技术文档中页面跳转同步通知参数列表(以下仅供参考)//
		//商户订单号

		String out_trade_no = new String(request.getParameter("out_trade_no").getBytes("ISO-8859-1"), "UTF-8");
		String payStatus = aliWapPayService.queryPayStatus(out_trade_no);
		if (Orders.PayStatus.PAID.toString().equals(payStatus)) {
			//判断该笔订单是否在商户网站中已经做过处理
			//如果没有做过处理，根据订单号（out_trade_no）在商户网站的订单系统中查到该笔订单的详细，并执行商户的业务程序
			//如果有做过处理，不执行商户的业务程序
			Recharge findRecharge = rechargeService.findByNo(out_trade_no);
			if (findRecharge.getStatus() == 0) {
				findRecharge.setStatus(1);
				rechargeService.saveOrUpdate(findRecharge);
				User findUser = userService.findById(User.class, findRecharge.getUser().getId());
				findUser.setBalance(findUser.getBalance() + findRecharge.getMoney());
				userService.saveOrUpdate(findUser);
			}

			//该页面可做页面美工编辑
			out.println("<br>交易成功!<br>订单号:" + out_trade_no + "<br>支付金额:" + findRecharge.getMoney() + "");
			//——请根据您的业务逻辑来编写程序（以上代码仅作参考）——

			//////////////////////////////////////////////////////////////////////////////////////////
		} else {
//			//该页面可做页面美工编辑
			out.println("验证失败");
		}
		out.flush();
		out.close();
		return null;
	}

	@SuppressWarnings("unchecked")
	public String alipayApi() throws Exception {
		//商户订单号
		//获取5位随机数
		Random random = new Random();
		int n = random.nextInt(9999);
		n = n + 10000;
		//商户网站订单系统中唯一订单号，必填
		String outTradeNo = System.currentTimeMillis() + "" + n;
		//付款单价
		String money = request.getParameter("money");
		//订单类型
		String orderType = request.getParameter("orderType");

		String subject = "购买";
		String form = "";
		if("RECHARGE".equals(orderType)){
			outTradeNo = "RECHARGE" + outTradeNo;
			subject = "在线充值";
			//保存充值订单
			HttpSession session = request.getSession();
			User loginUser = (User) session.getAttribute("loginUser");
			Recharge recharge = new Recharge();
			recharge.setNo(outTradeNo);
			recharge.setMoney(Double.parseDouble(money));
			recharge.setUser(loginUser);
			recharge.setStatus(0);
			// 设置创建日期
			recharge.setCreateDate(new Date());
			rechargeService.saveOrUpdate(recharge);


			System.out.println("outTradeNo --- > " + outTradeNo
						+  ", money ----> " + money
						+ ", subject --->" + subject
						+ "orderType --->" + orderType
						+ "recharge.getId() ---> " + recharge.getId());


			form = aliWapPayService.createAliWapPayInfo(outTradeNo, Double.parseDouble(money), subject, subject, orderType, recharge.getId());

				System.out.println("form --- > " + form);
		}
		PrintWriter out = response.getWriter();
		out.println(form);
		out.flush();
		out.close();
		return null;
	}

	/**
	 * 支付宝 异步通知接口
	 * @return
	 * @throws Exception
	 */
	public String notifyUrl() throws Exception{
		PrintWriter out = response.getWriter();
		//获取支付宝POST过来反馈信息
		Map<String,String> params = new HashMap<String,String>();
		Map requestParams = request.getParameterMap();
		for (Iterator iter = requestParams.keySet().iterator(); iter.hasNext();) {
			String name = (String) iter.next();
			String[] values = (String[]) requestParams.get(name);
			String valueStr = "";
			for (int i = 0; i < values.length; i++) {
				valueStr = (i == values.length - 1) ? valueStr + values[i]
						: valueStr + values[i] + ",";
			}
			//乱码解决，这段代码在出现乱码时使用。如果mysign和sign不相等也可以使用这段代码转化
			//valueStr = new String(valueStr.getBytes("ISO-8859-1"), "gbk");
			params.put(name, valueStr);
			System.out.println("notifyUrl ------ > ");
			System.out.println(name + " : " + valueStr);

		}

		//支付成功
		if("TRADE_SUCCESS".equals(params.get("trade_status")) || "TRADE_FINISHED".equals(params.get("trade_status"))){
			String out_trade_no = params.get("out_trade_no");//订单号
			String sellerId = params.get("seller_id");//商家ID
			if(StaticFileUtil.getProperty("aliWapPay", "SELLER").equals(sellerId)){
				//判断该笔订单是否在商户网站中已经做过处理
				//如果没有做过处理，根据订单号（out_trade_no）在商户网站的订单系统中查到该笔订单的详细，并执行商户的业务程序
				//如果有做过处理，不执行商户的业务程序
				Recharge findRecharge = rechargeService.findByNo(out_trade_no);
				if(findRecharge.getStatus()==0){
					findRecharge.setStatus(1);
					rechargeService.saveOrUpdate(findRecharge);
					User findUser = userService.findById(User.class, findRecharge.getUser().getId());
					findUser.setBalance(findUser.getBalance()+findRecharge.getMoney());
					userService.saveOrUpdate(findUser);
				}
				out.println("success");	//请不要修改或删除
			}else {
				System.out.println("订单[out_trade_no="+out_trade_no+"]验证失败, 商家ID不一致.");
				out.println("fail");
			}
		}else{//验证失败
			out.println("fail");
		}
		out.flush();
		out.close();
		return null;
	}
}