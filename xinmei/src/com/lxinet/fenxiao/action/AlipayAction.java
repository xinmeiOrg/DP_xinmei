package com.lxinet.fenxiao.action;

import java.io.PrintWriter;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Random;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

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
import com.lxinet.fenxiao.service.IConfigService;
import com.lxinet.fenxiao.service.IFinancialService;
import com.lxinet.fenxiao.service.IOrdersService;
import com.lxinet.fenxiao.service.IRechargeService;
import com.lxinet.fenxiao.service.IUserService;
import com.lxinet.fenxiao.service.impl.UserServiceImpl;


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
	
	/**
	 *功能：即时到账交易接口接入页
	 *版本：3.3
	 *日期：2012-08-14
	 *说明：
	 *以下代码只是为了方便商户测试而提供的样例代码，商户可以根据自己网站的需要，按照技术文档编写,并非一定要使用该代码。
	 *该代码仅供学习和研究支付宝接口使用，只是提供一个参考。

	 *************************注意*****************
	 *如果您在接口集成过程中遇到问题，可以按照下面的途径来解决
	 *1、商户服务中心（https://b.alipay.com/support/helperApply.htm?action=consultationApply），提交申请集成协助，我们会有专业的技术工程师主动联系您协助解决
	 *2、商户帮助中心（http://help.alipay.com/support/232511-16307/0-16307.htm?sh=Y&info_type=9）
	 *3、支付宝论坛（http://club.alipay.com/read-htm-tid-8681712.html）
	 *如果不想使用扩展功能请把扩展功能参数赋空值。
	 **********************************************
	 */
	@SuppressWarnings("unchecked")
	public String alipayApi() throws Exception {
		////////////////////////////////////请求参数//////////////////////////////////////
		
		//支付类型
		String payment_type = "1";
		//必填，不能修改
		//服务器异步通知页面路径
		String notify_url = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+request.getContextPath()+"/" + "notifyUrl";
		//需http://格式的完整路径，不能加?id=123这类自定义参数
		
		//页面跳转同步通知页面路径
		String return_url = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+request.getContextPath()+"/" + "returnUrl";
		//需http://格式的完整路径，不能加?id=123这类自定义参数，不能写成http://localhost/
		
		//商户订单号
		//获取5位随机数
		Random random = new Random();
		int n = random.nextInt(9999);
		n = n + 10000;
		String out_trade_no = System.currentTimeMillis() + "" + n;
		//商户网站订单系统中唯一订单号，必填
		
		//订单名称
		String subject = out_trade_no;
		//必填
		
		//付款单价
		String money = request.getParameter("money");
		//必填
		//公用回传参数
//		String extra_common_param = new String(request.getParameter("extra_common_param").getBytes("ISO-8859-1"),"UTF-8");
		
		//订单描述
		
		String body = out_trade_no;
		//商品展示地址
		String show_url = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+request.getContextPath()+"/";
		//需以http://开头的完整路径，例如：http://www.商户网址.com/myorder.html
		
		//防钓鱼时间戳
		String anti_phishing_key = "";
		//若要使用请调用类文件submit中的query_timestamp函数
		
		//客户端的IP地址
		String exter_invoke_ip = "";
		//非局域网的外网IP地址，如：221.0.0.1
		
		//支付渠道,可支持多种支付渠道显示，以“^”分隔
		String enable_paymethod = request.getParameter("enable_paymethod");
		Config findConfig = configService.findById(Config.class, 1);
		//////////////////////////////////////////////////////////////////////////////////
		
		//把请求参数打包成数组
		Map<String, String> sParaTemp = new HashMap<String, String>();
		sParaTemp.put("service", "create_direct_pay_by_user");
		sParaTemp.put("partner", findConfig.getAlipayPartner());
		sParaTemp.put("optEmail", findConfig.getAlipaySellerEmail());
		sParaTemp.put("_input_charset", AlipayConfig.input_charset);
		sParaTemp.put("payment_type", payment_type);
		sParaTemp.put("notify_url", notify_url);
		sParaTemp.put("return_url", return_url);
		sParaTemp.put("out_trade_no", out_trade_no);
		sParaTemp.put("subject", subject);
		sParaTemp.put("total_fee", Double.parseDouble(money)+"");
		sParaTemp.put("body", body);
		sParaTemp.put("show_url", show_url);
		sParaTemp.put("anti_phishing_key", anti_phishing_key);
		sParaTemp.put("exter_invoke_ip", exter_invoke_ip);
		sParaTemp.put("enable_paymethod", "debitCardExpress");
		
		//建立请求
		String sHtmlText = AlipaySubmit.buildRequest(sParaTemp,"get","确认",findConfig.getAlipayKey());
		HttpSession session = request.getSession();
		User loginUser = (User) session.getAttribute("loginUser");
		Recharge recharge = new Recharge();
		recharge.setNo(out_trade_no);
		recharge.setMoney(Double.parseDouble(money));
		recharge.setUser(loginUser);
		recharge.setStatus(0);
		// 设置创建日期
		recharge.setCreateDate(new Date());
		rechargeService.saveOrUpdate(recharge);
		
		PrintWriter out = response.getWriter();
		out.println(sHtmlText);
		out.flush();
		out.close();
		return null;
	}
	
	/**
	 功能：支付宝服务器异步通知页面
	 版本：3.3
	 日期：2012-08-17
	 说明：
	 以下代码只是为了方便商户测试而提供的样例代码，商户可以根据自己网站的需要，按照技术文档编写,并非一定要使用该代码。
	 该代码仅供学习和研究支付宝接口使用，只是提供一个参考。

	 //***********页面功能说明***********
	 创建该页面文件时，请留心该页面文件中无任何HTML代码及空格。
	 该页面不能在本机电脑测试，请到服务器上做测试。请确保外部可以访问该页面。
	 该页面调试工具请使用写文本函数logResult，该函数在com.alipay.util文件夹的AlipayNotify.java类文件中
	 如果没有收到该页面返回的 success 信息，支付宝会在24小时内按一定的时间策略重发通知
	 //********************************
	 * */
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
		}
		
		//获取支付宝的通知返回参数，可参考技术文档中页面跳转同步通知参数列表(以下仅供参考)//
		//商户订单号
		String out_trade_no = new String(request.getParameter("out_trade_no").getBytes("ISO-8859-1"),"UTF-8");

		//支付宝交易号
		String trade_no = new String(request.getParameter("trade_no").getBytes("ISO-8859-1"),"UTF-8");

		//交易状态
		String trade_status = new String(request.getParameter("trade_status").getBytes("ISO-8859-1"),"UTF-8");

		//公用回传参数
//		String extra_common_param = new String(request.getParameter("extra_common_param").getBytes("ISO-8859-1"),"UTF-8");
		
		//获取支付宝的通知返回参数，可参考技术文档中页面跳转同步通知参数列表(以上仅供参考)//
		Config findConfig = configService.findById(Config.class, 1);
		if(AlipayNotify.verify(params,findConfig.getAlipayKey())){//验证成功
			//////////////////////////////////////////////////////////////////////////////////////////
			//请在这里加上商户的业务逻辑程序代码

			//——请根据您的业务逻辑来编写程序（以下代码仅作参考）——
			
			if(trade_status.equals("TRADE_FINISHED")){
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
				//注意：
				//退款日期超过可退款期限后（如三个月可退款），支付宝系统发送该交易状态通知
			} else if (trade_status.equals("TRADE_SUCCESS")){
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
				//注意：
				//付款完成后，支付宝系统发送该交易状态通知
			}

			//——请根据您的业务逻辑来编写程序（以上代码仅作参考）——
				
			out.println("success");	//请不要修改或删除

			//////////////////////////////////////////////////////////////////////////////////////////
		}else{//验证失败
			out.println("fail");
		}
		out.flush();
		out.close();
		return null;
	}

	/**
	 功能：支付宝页面跳转同步通知页面
	 版本：3.2
	 日期：2011-03-17
	 说明：
	 以下代码只是为了方便商户测试而提供的样例代码，商户可以根据自己网站的需要，按照技术文档编写,并非一定要使用该代码。
	 该代码仅供学习和研究支付宝接口使用，只是提供一个参考。

	 //***********页面功能说明***********
	 该页面可在本机电脑测试
	 可放入HTML等美化页面的代码、商户业务逻辑程序代码
	 TRADE_FINISHED(表示交易已经成功结束，并不能再对该交易做后续操作);
	 TRADE_SUCCESS(表示交易已经成功结束，可以对该交易做后续操作，如：分润、退款等);
	 //********************************
	 * */
	public String returnUrl() throws Exception{
		PrintWriter out = response.getWriter();
		//获取支付宝GET过来反馈信息
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
			valueStr = new String(valueStr.getBytes("ISO-8859-1"), "utf-8");
			params.put(name, valueStr);
		}
		
		//获取支付宝的通知返回参数，可参考技术文档中页面跳转同步通知参数列表(以下仅供参考)//
		//商户订单号

		String out_trade_no = new String(request.getParameter("out_trade_no").getBytes("ISO-8859-1"),"UTF-8");

		//支付宝交易号

//		String trade_no = new String(request.getParameter("trade_no").getBytes("ISO-8859-1"),"UTF-8");

		//交易状态
		String trade_status = new String(request.getParameter("trade_status").getBytes("ISO-8859-1"),"UTF-8");
		Config findConfig = configService.findById(Config.class, 1);
		//获取支付宝的通知返回参数，可参考技术文档中页面跳转同步通知参数列表(以上仅供参考)//
		//计算得出通知验证结果
		boolean verify_result = AlipayNotify.verify(params,findConfig.getAlipayKey());
		
		if(verify_result){//验证成功
			//////////////////////////////////////////////////////////////////////////////////////////
			//请在这里加上商户的业务逻辑程序代码
			Recharge findRecharge = rechargeService.findByNo(out_trade_no);
			//——请根据您的业务逻辑来编写程序（以下代码仅作参考）——
			if(trade_status.equals("TRADE_FINISHED") || trade_status.equals("TRADE_SUCCESS")){
				//判断该笔订单是否在商户网站中已经做过处理
					//如果没有做过处理，根据订单号（out_trade_no）在商户网站的订单系统中查到该笔订单的详细，并执行商户的业务程序
					//如果有做过处理，不执行商户的业务程序
				if(findRecharge.getStatus()==0){
					findRecharge.setStatus(1);
					rechargeService.saveOrUpdate(findRecharge);
					User findUser = userService.findById(User.class, findRecharge.getUser().getId());
					findUser.setBalance(findUser.getBalance()+findRecharge.getMoney());
					userService.saveOrUpdate(findUser);
				}
			}
			
			//该页面可做页面美工编辑
			out.println("<br>交易成功!<br>订单号:" + out_trade_no + "<br>支付金额:" + findRecharge.getMoney() + "");
			//——请根据您的业务逻辑来编写程序（以上代码仅作参考）——

			//////////////////////////////////////////////////////////////////////////////////////////
		}else{
//			//该页面可做页面美工编辑
			out.println("验证失败");
		}
		out.flush();
		out.close();
		return null;
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