package com.lxinet.fenxiao.action;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.json.JSONException;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.lxinet.fenxiao.entities.Config;
import com.lxinet.fenxiao.entities.Message;
import com.lxinet.fenxiao.service.IConfigService;
import com.lxinet.fenxiao.service.IMessageService;
import com.lxinet.fenxiao.utils.BjuiJson;
import com.lxinet.fenxiao.utils.BjuiPage;
import com.lxinet.fenxiao.utils.FreemarkerUtils;
import com.lxinet.fenxiao.utils.PageModel;

import freemarker.template.Configuration;

/**
 * 联系我们Action
 * @author Cz
 * 网址：http://www.919dns.com
 */
@Controller("messageAction")
@Scope("prototype")
public class MessageAction extends BaseAction{
	private static final long serialVersionUID = 1L;
	@Resource(name="messageService")
	private IMessageService<Message> messageService;
	private Message message;
	@Resource(name = "configService")
	private IConfigService<Config> configService;
	
	public void list(){
		//获取总条数
		int count = messageService.getTotalCount("select count(*) from Message where deleted=0");
		page = new BjuiPage(pageCurrent, pageSize);
		page.setTotalCount(count);
		List<Message> list = messageService.list("from Message where deleted=0 order by id desc",page.getStart(),page.getPageSize());
		cfg = new Configuration();
		// 设置FreeMarker的模版文件位置
		cfg.setServletContextForTemplateLoading(request.getServletContext(),
				"WEB-INF/templates/admin");
		Map<Object, Object> root = new HashMap<Object, Object>();
		root.put("list", list);
		root.put("page", page);
		FreemarkerUtils.freemarker(request, response, "messageList.ftl", cfg, root);
	}
	
	public void indexList(){
		String pStr = request.getParameter("p");
		int p = 1;
		if(pStr!=null && !"".equals(pStr)){
			p = Integer.parseInt(pStr);
		}
		//获取总条数
		int count = messageService.getTotalCount("select count(*) from Message where deleted=0");
		PageModel pageModel = new PageModel();
		pageModel.setPageSize(16);
		pageModel.setAllCount(count);
		pageModel.setCurrentPage(p);
		List<Message> list = messageService.list("from Message where deleted=0 order by id desc",pageModel.getStart(),pageModel.getPageSize());
		cfg = new Configuration();
		// 设置FreeMarker的模版文件位置
		cfg.setServletContextForTemplateLoading(request.getServletContext(),
				"WEB-INF/templates/index");
		Map<Object, Object> root = new HashMap<Object, Object>();
		root.put("messageList", list);
		root.put("page", pageModel.getPageStr("messageList.do?p="));
		Config config = configService.findById(Config.class, 1);
		root.put("config", config);
		FreemarkerUtils.freemarker(request, response, "messageList.ftl", cfg, root);
	}
	
	public void add(){
		cfg = new Configuration();
		// 设置FreeMarker的模版文件位置
		cfg.setServletContextForTemplateLoading(request.getServletContext(),
				"WEB-INF/templates/index");
		Map<Object, Object> root = new HashMap<Object, Object>();
		Config config = configService.findById(Config.class, 1);
		root.put("config", config);
		FreemarkerUtils.freemarker(request, response, "messageAdd.ftl", cfg, root);
	}

	/**
	 * 保存联系信息
	 * 创建日期：2014-10-26上午9:38:42
	 * 作者：Cz
	 * 网址:http://www.919dns.com
	 * @throws JSONException
	 */
	public void save(){
		String callbackData = "";
		// 设置创建日期
		message.setCreateDate(new Date());
		message.setDeleted(false);
		boolean result = messageService.saveOrUpdate(message);
		if (result) {
			callbackData = "<script>alert('提交成功');window.location.href='messageList.do';</script>";
		} else {
			callbackData = "<script>alert('提交失败，请重试');window.location.href='javascript:history.go(-1)';</script>";
		}
		PrintWriter out = null;
		try {
			out = response.getWriter();
		} catch (IOException e) {
			e.printStackTrace();
		}
		out.print(callbackData);
		out.flush();
		out.close();
	}
	
	/**
	 * 联系信息信息
	 * 创建日期：2014-10-26下午12:40:00
	 * 作者：Cz
	 * 网址:http://www.919dns.com
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
				callbackData = BjuiJson.json("300", "参数错误", "", "", "", "", "", "");
				out.print(callbackData);
				out.flush();
				out.close();
			} else {
				int id = 0;
				try {
					id = Integer.parseInt(idStr);
				} catch (Exception e) {
					// 抛出异常，说明ID不是数字
					callbackData = BjuiJson.json("300", "参数错误", "", "", "", "", "", "");
					out.print(callbackData);
					out.flush();
					out.close();
				}
				Message findMessage = messageService.findById(Message.class, id);
				if (findMessage == null) {
					// 联系信息不存在
					callbackData = BjuiJson.json("300", "留言不存在", "", "", "", "", "", "");
					out.print(callbackData);
					out.flush();
					out.close();
				} else {
					cfg = new Configuration();
					// 设置FreeMarker的模版文件位置
					cfg.setServletContextForTemplateLoading(request.getServletContext(),
							"WEB-INF/templates/admin");
					Map<Object, Object> root = new HashMap<Object, Object>();
					root.put("message", findMessage);
					FreemarkerUtils.freemarker(request, response, "messageReply.ftl", cfg, root);
				}
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
	
	public void indexInfo() {
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
				callbackData = "参数错误";
				out.print(callbackData);
				out.flush();
				out.close();
			} else {
				int id = 0;
				try {
					id = Integer.parseInt(idStr);
				} catch (Exception e) {
					// 抛出异常，说明ID不是数字
					callbackData = "参数错误";
					out.print(callbackData);
					out.flush();
					out.close();
				}
				Message findMessage = messageService.findById(Message.class, id);
				if (findMessage == null) {
					// 联系信息不存在
					callbackData = "信息不存在";
					out.print(callbackData);
					out.flush();
					out.close();
				} else {
					cfg = new Configuration();
					// 设置FreeMarker的模版文件位置
					cfg.setServletContextForTemplateLoading(request.getServletContext(),
							"WEB-INF/templates/index");
					Map<Object, Object> root = new HashMap<Object, Object>();
					root.put("message", findMessage);
					Config config = configService.findById(Config.class, 1);
					root.put("config", config);
					FreemarkerUtils.freemarker(request, response, "message.ftl", cfg, root);
				}
			}
	}
	
	/**
	 * 修改联系信息
	 * 创建日期：2014-10-26下午12:40:09
	 * 作者：Cz
	 * 网址:http://www.919dns.com
	 * @return
	 */
	public void update(){
		PrintWriter out = null;
		try {
			out = response.getWriter();
		} catch (IOException e) {
			e.printStackTrace();
		}
		String callbackData = "";
		try{
			if(message==null){
				callbackData = BjuiJson.json("300", "参数错误", "", "", "", "", "", "");
			}else{
					Message findMessage = messageService.findById(Message.class, message.getId());
					message.setDeleted(findMessage.isDeleted());
					message.setCreateDate(findMessage.getCreateDate());
					message.setVersion(findMessage.getVersion());
					boolean result = messageService.saveOrUpdate(message);
					//修改成功
					if(result){
						callbackData = BjuiJson.json("200", "回复成功", "", "", "", "true", "", "");
					}else{
						//修改失败
						callbackData = BjuiJson.json("300", "回复失败", "", "", "", "", "", "");
					}
			}
		}catch (JSONException e) {
			e.printStackTrace();
		}
		out.print(callbackData);
		out.flush();
		out.close();
	}
	
	/**
	 * 删除联系信息
	 * 创建日期：2014-10-26下午12:40:19
	 * 作者：Cz
	 * 网址:http://www.919dns.com
	 * @return
	 */
	public void delete() {
		String idStr = request.getParameter("id");
		String callbackData = "";
		PrintWriter out = null;
		try {
			out = response.getWriter();
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
				Message findMessage = messageService.findById(Message.class, id);
				if (findMessage == null) {
					// 联系信息不存在
					callbackData = BjuiJson.json("300", "联系信息不存在", "", "", "", "", "", "");
				} else {
					boolean result = messageService.delete(findMessage);
					if(result){
						callbackData = BjuiJson.json("200", "删除成功", "", "", "", "", "", "");
					}else{
						callbackData = BjuiJson.json("300", "删除失败", "", "", "", "", "", "");
					}
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		} catch (JSONException e) {
			e.printStackTrace();
		}
		out.print(callbackData);
		out.flush();
		out.close();
	}

	public Message getMessage() {
		return message;
	}

	public void setMessage(Message message) {
		this.message = message;
	}
}
