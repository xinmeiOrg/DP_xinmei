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

import com.lxinet.fenxiao.entities.ArticleCate;
import com.lxinet.fenxiao.service.IArticleCateService;
import com.lxinet.fenxiao.utils.BjuiJson;
import com.lxinet.fenxiao.utils.FreemarkerUtils;

import freemarker.template.Configuration;

/**
 * 文章栏目Action
 * @author Cz
 * 网址：http://www.919dns.com
 */
@Controller("articleCateAction")
@Scope("prototype")
public class ArticleCateAction extends BaseAction{
	private static final long serialVersionUID = 1L;
	@Resource(name="articleCateService")
	private IArticleCateService<ArticleCate> articleCateService;
	private ArticleCate articleCate;
	
	public void list(){
		List<ArticleCate> list = articleCateService.list("from ArticleCate where deleted=0");
		String zNodes = "";
		for (ArticleCate articleCate : list) {
			zNodes += "<li data-id='"+articleCate.getId()+"' data-pid='"+articleCate.getFatherId()+"' data-tabid='"+articleCate.getId()+"'>"+articleCate.getName()+"[ID:"+articleCate.getId()+"]</li>";
		}
		cfg = new Configuration();
		// 设置FreeMarker的模版文件位置
		cfg.setServletContextForTemplateLoading(request.getServletContext(),
				"WEB-INF/templates/admin");
		Map<Object, Object> root = new HashMap<Object, Object>();
		root.put("zNodes", zNodes);
		root.put("list", list);
		FreemarkerUtils.freemarker(request, response, "articleCateList.ftl", cfg, root);
	}
	
	public void add(){
		List<ArticleCate> list = articleCateService.list("from ArticleCate where deleted=0");
		String zNodes = "<li data-id='0' data-pid='0' data-tabid='0'>顶级栏目</li>";
		for (ArticleCate articleCate : list) {
			zNodes += "<li data-id='"+articleCate.getId()+"' data-pid='"+articleCate.getFatherId()+"' data-tabid='"+articleCate.getId()+"'>"+articleCate.getName()+"</li>";
		}
		cfg = new Configuration();
		// 设置FreeMarker的模版文件位置
		cfg.setServletContextForTemplateLoading(request.getServletContext(),
				"WEB-INF/templates/admin");
		Map<Object, Object> root = new HashMap<Object, Object>();
		root.put("zNodes", zNodes);
		root.put("list", list);
		FreemarkerUtils.freemarker(request, response, "articleCateAdd.ftl", cfg, root);
	}

	/**
	 * 保存栏目
	 * 创建日期：2014-10-26上午9:38:42
	 * 作者：Cz
	 * 网址:http://www.919dns.com
	 * @throws JSONException
	 */
	public void save(){
		String callbackData = "";
		articleCate.setDeleted(false);
		articleCate.setCreateDate(new Date());
		boolean result = articleCateService.saveOrUpdate(articleCate);
		try {
			if (result) {
				callbackData = BjuiJson.json("200", "添加成功", "", "", "", "true", "", "");
			} else {
				callbackData = BjuiJson.json("300", "添加失败", "", "", "", "", "", "");
			}
		} catch (JSONException e) {
			e.printStackTrace();
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
	

	public void getNameById() {
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
		} else {
			int id = 0;
			try {
				id = Integer.parseInt(idStr);
			} catch (Exception e) {
				// 抛出异常，说明ID不是数字
				callbackData = "参数错误";
			}
			ArticleCate findArticleCate = articleCateService.findById(ArticleCate.class, id);
			if (findArticleCate == null) {
				// 栏目不存在
				callbackData = "栏目不存在";
			} else {
				callbackData = findArticleCate.getName();
			}
		}
		log.info(callbackData);
		out.print(callbackData);
		out.flush();
		out.close();
	}
	
	/**
	 * 栏目信息
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
				ArticleCate findArticleCate = articleCateService.findById(ArticleCate.class, id);
				if (findArticleCate == null) {
					// 栏目不存在
					callbackData = BjuiJson.json("300", "栏目不存在", "", "", "", "", "", "");
					out.print(callbackData);
					out.flush();
					out.close();
				} else {
					List<ArticleCate> list = articleCateService.list("from ArticleCate where deleted=0");
					String zNodes = "<li data-id='0' data-pid='0' data-tabid='0'>顶级栏目</li>";
					for (ArticleCate articleCate : list) {
						zNodes += "<li data-id='"+articleCate.getId()+"' data-pid='"+articleCate.getFatherId()+"' data-tabid='"+articleCate.getId()+"'>"+articleCate.getName()+"</li>";
					}
					//父类栏目名称
					String fatherName = "";
					if(findArticleCate.getFatherId()!=0){
						ArticleCate fatherArticleCate = articleCateService.findById(ArticleCate.class, findArticleCate.getFatherId());
						if(fatherArticleCate!=null){
							fatherName = articleCateService.findById(ArticleCate.class, findArticleCate.getFatherId()).getName();
						}else{
							fatherName = "上级栏目不存在";
						}
					}else{
						fatherName = "顶级栏目";
					}
					
					cfg = new Configuration();
					// 设置FreeMarker的模版文件位置
					cfg.setServletContextForTemplateLoading(request.getServletContext(),
							"WEB-INF/templates/admin");
					Map<Object, Object> root = new HashMap<Object, Object>();
					root.put("articleCate", findArticleCate);
					root.put("zNodes", zNodes);
					root.put("fatherName", fatherName);
					FreemarkerUtils.freemarker(request, response, "articleCateEdit.ftl", cfg, root);
				}
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 修改栏目
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
			if(articleCate==null){
				callbackData = BjuiJson.json("300", "参数错误", "", "", "", "", "", "");
			}else{
				if(articleCate.getFatherId()==articleCate.getId()){
					callbackData = BjuiJson.json("300", "上级栏目不能选择当前修改的栏目", "", "", "", "", "", "");
				}else{
					ArticleCate findArticleCate = articleCateService.findById(ArticleCate.class, articleCate.getId());
					findArticleCate.setFatherId(articleCate.getFatherId());
					findArticleCate.setName(articleCate.getName());
					boolean result = articleCateService.saveOrUpdate(findArticleCate);
					//修改成功
					if(result){
						callbackData = BjuiJson.json("200", "修改成功", "", "", "", "true", "", "");
					}else{
						//修改失败
						callbackData = BjuiJson.json("300", "修改失败", "", "", "", "", "", "");
					}
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
	 * 删除栏目
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
				ArticleCate findArticleCate = articleCateService.findById(ArticleCate.class, id);
				if (findArticleCate == null) {
					// 栏目不存在
					callbackData = BjuiJson.json("300", "栏目不存在", "", "", "", "", "", "");
				} else {
					//如果有下级栏目，则不能删除
					List<ArticleCate> sanList = articleCateService.listByFatherId(id);
					log.info(sanList);
					if(sanList.size()!=0){
						callbackData = BjuiJson.json("300", "该栏目存在下级栏目，请先删除下级栏目", "", "", "", "", "", "");
					}else{
						boolean result = articleCateService.delete(findArticleCate);
						if(result){
							callbackData = BjuiJson.json("200", "删除成功", "articleCateList", "", "", "true", "", "");
						}else{
							callbackData = BjuiJson.json("300", "删除失败", "", "", "", "", "", "");
						}
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

	public ArticleCate getArticleCate() {
		return articleCate;
	}

	public void setArticleCate(ArticleCate articleCate) {
		this.articleCate = articleCate;
	}
}
