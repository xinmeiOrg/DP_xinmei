package com.lxinet.fenxiao.action;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONException;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.lxinet.fenxiao.entities.Config;
import com.lxinet.fenxiao.entities.Recharge;
import com.lxinet.fenxiao.entities.User;
import com.lxinet.fenxiao.service.IConfigService;
import com.lxinet.fenxiao.service.IRechargeService;
import com.lxinet.fenxiao.service.IUserService;
import com.lxinet.fenxiao.utils.BjuiJson;
import com.lxinet.fenxiao.utils.BjuiPage;
import com.lxinet.fenxiao.utils.FreemarkerUtils;
import com.lxinet.fenxiao.utils.TemplatesPath;

import freemarker.template.Configuration;


/**
 * 
 * 创建日期：2014-10-24下午1:01:32 作者：Cz
 */
@Controller("rechargeAction")
@Scope("prototype")
public class RechargeAction extends BaseAction {
	private static final long serialVersionUID = 1L;
	@Resource(name = "rechargeService")
	private IRechargeService<Recharge> rechargeService;
	@Resource(name = "userService")
	private IUserService<User> userService;
	private Recharge recharge;
	private String ftlFileName;
	@Resource(name = "configService")
	private IConfigService<Config> configService;

	/**
	 * 充值列表 创建日期：2014-9-24下午10:01:07 作者：Cz
	 * 
	 * @return
	 */
	public void list() {
		String key = request.getParameter("key");
		String countHql = "select count(*) from Recharge where deleted=0";
		String hql = "from Recharge where deleted=0";
		if(StringUtils.isNotEmpty(key)){
			countHql += " and (user.name='"+key+"' or no='"+key+"')";
			hql += " and (user.name='"+key+"' or no='"+key+"')";
		}
		hql += " order by id desc";
		//获取总条数
		int count = 0;
		count = rechargeService.getTotalCount(countHql);					
		page = new BjuiPage(pageCurrent, pageSize);
		page.setTotalCount(count);
		cfg = new Configuration();
		// 设置FreeMarker的模版文件位置
		cfg.setServletContextForTemplateLoading(
		request.getServletContext(), TemplatesPath.ADMIN_PATH);
		List<Recharge> rechargeList = rechargeService.list(hql,page.getStart(),page.getPageSize());
		Map<Object, Object> root = new HashMap<Object, Object>();
		root.put("rechargeList", rechargeList);
		root.put("page", page);
		root.put("key", key);
		FreemarkerUtils.freemarker(request, response, ftlFileName, cfg, root);
	}
	
	String formatString(String text){ 
		if(text == null) {
			return ""; 
		}
		return text;
	}

	/**
	 * 添加充值 创建日期：2014-9-24下午10:01:20 作者：Cz
	 */
	public void save() {
		PrintWriter out = null;
		try {
			out = response.getWriter();
		} catch (IOException e) {
			e.printStackTrace();
		}
		String callbackData = "";
		if(recharge==null){
			callbackData = "<script>alert('参数错误');window.location.href='javascript:history.go(-1);'</script>";
		} else {
			//获取5位随机数
			Random random = new Random();
	        int n = random.nextInt(9999);
	        n = n + 10000;
	        //生成订单号
			String no = System.currentTimeMillis() + "" + n;
			recharge.setNo(no);
			// 设置充值创建日期
			recharge.setCreateDate(new Date());
			boolean result = rechargeService.saveOrUpdate(recharge);
			if (result) {
				callbackData = "<script>alert('提交成功');window.location.href='recharge.jsp'</script>";
			} else {
				callbackData = "<script>alert('提交失败，请重试');window.location.href='javascript:history.go(-1);'</script>";
			}
		}
		out.print(callbackData);
		out.flush();
		out.close();
	}

	/**
	 * 获取充值信息 创建日期：2014-9-24下午10:01:36 作者：Cz
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
				Recharge findrecharge = (Recharge) rechargeService.findById(
						Recharge.class, id);
				if (findrecharge == null) {
					// 充值不存在
					callbackData = BjuiJson.json("300", "充值不存在", "", "",
							"", "", "", "");
				} else {
					cfg = new Configuration();
					// 设置FreeMarker的模版文件位置
					cfg.setServletContextForTemplateLoading(
							request.getServletContext(),
							TemplatesPath.ADMIN_PATH);
					Map<Object, Object> root = new HashMap<Object, Object>();
					root.put("recharge", findrecharge);
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
	 * 修改充值 创建日期：2014-9-24下午10:01:46 作者：Cz
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
			if (recharge == null) {
				callbackData = BjuiJson.json("300", "参数错误", "", "", "", "",
						"", "");
			} else {
				Recharge findrecharge = (Recharge) rechargeService.findById(Recharge.class, recharge.getId());
				recharge.setCreateDate(findrecharge.getCreateDate());
				recharge.setDeleted(findrecharge.isDeleted());
				recharge.setVersion(findrecharge.getVersion());
				boolean result = rechargeService.saveOrUpdate(recharge);
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
	 * 删除充值 创建日期：2014-9-24下午10:01:55 作者：Cz
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
				Recharge findrecharge = (Recharge) rechargeService.findById(Recharge.class, id);
				if (findrecharge == null) {
					// 充值不存在
					callbackData = BjuiJson.json("300", "充值不存在", "", "",
							"", "true", "", "");
				} else {
					boolean result = rechargeService.delete(findrecharge);
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


	public Recharge getRecharge() {
		return recharge;
	}

	public void setRecharge(Recharge recharge) {
		this.recharge = recharge;
	}

	public String getFtlFileName() {
		return ftlFileName;
	}

	public void setFtlFileName(String ftlFileName) {
		this.ftlFileName = ftlFileName;
	}

}
