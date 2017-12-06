package com.lxinet.fenxiao.service.impl;


import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.lxinet.fenxiao.dao.IOrdersDao;
import com.lxinet.fenxiao.entities.Commission;
import com.lxinet.fenxiao.entities.Config;
import com.lxinet.fenxiao.entities.Financial;
import com.lxinet.fenxiao.entities.Kami;
import com.lxinet.fenxiao.entities.Orders;
import com.lxinet.fenxiao.entities.Product;
import com.lxinet.fenxiao.entities.User;
import com.lxinet.fenxiao.service.IAliWapPayService;
import com.lxinet.fenxiao.service.ICommissionService;
import com.lxinet.fenxiao.service.IConfigService;
import com.lxinet.fenxiao.service.IFinancialService;
import com.lxinet.fenxiao.service.IKamiService;
import com.lxinet.fenxiao.service.IOrdersService;
import com.lxinet.fenxiao.service.IProductService;
import com.lxinet.fenxiao.service.IUserService;
import com.lxinet.fenxiao.utils.BusinessException;


/**
 * 
 * 作者：Cz
 */
@Service("ordersService")
@Scope("prototype")
public class OrdersServiceImpl<T extends Orders> extends BaseServiceImpl<T> implements IOrdersService<T> {
	@Resource(name="ordersDao")
	private IOrdersDao ordersDao;
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
	@Resource(name = "configService")
	private IConfigService<Config> configService;
	@Resource(name = "aliWapPayService")
	private IAliWapPayService aliWapPayService;
	@Override
	public Orders findByNo(String no) {
		return ordersDao.findByNo(no);
	}
	
	@Override
	public void generateCosumeInfo(String no) {
		Orders findOrders = findByNo(no);
		if(findOrders == null){
			throw new BusinessException("订单不存在");
		}
//		if(findOrders.getStatus() == 0){
//			List<Kami> kamiList = kamiService.list(
//					"from Kami where deleted=0 and status=0 and product.id=" + findOrders.getProductId(), 0,
//					findOrders.getProductNum());
//			if (kamiList.size() < findOrders.getProductNum()) {
//				throw new BusinessException("库存不足，请联系管理员");
//			}
//			User findUser = userService.findById(User.class, findOrders.getUser().getId());
//			generateCosumeInfo(findUser, findOrders, kamiList);
//		}
		User findUser = userService.findById(User.class, findOrders.getUser().getId());
		generateCosumeInfo(findUser, findOrders);
		
	}
	
	/**
	 * 支付完成，生成消费信息
	 * @param findUser
	 * @param findOrders
	 * @param kamiList
	 */
	void generateCosumeInfo(User findUser, Orders findOrders) {
		Date date = new Date();
		findOrders.setPayDate(date);
		findOrders.setStatus(1);
		ordersDao.saveOrUpdate(findOrders);

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
	 * 支付完成，生成消费信息
	 * @param findUser
	 * @param findOrders
	 * @param kamiList
	 */
	void generateCosumeInfo(User findUser, Orders findOrders, List<Kami> kamiList) {
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
		findOrders.setStatus(1);
		ordersDao.saveOrUpdate(findOrders);

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

}

