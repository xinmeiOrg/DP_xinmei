package com.lxinet.fenxiao.action;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.json.JSONException;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.lxinet.fenxiao.entities.Article;
import com.lxinet.fenxiao.entities.ArticleCate;
import com.lxinet.fenxiao.entities.Config;
import com.lxinet.fenxiao.service.IArticleCateService;
import com.lxinet.fenxiao.service.IArticleService;
import com.lxinet.fenxiao.service.IConfigService;
import com.lxinet.fenxiao.utils.BjuiJson;
import com.lxinet.fenxiao.utils.BjuiPage;
import com.lxinet.fenxiao.utils.FreemarkerUtils;
import com.lxinet.fenxiao.utils.PageModel;

import freemarker.template.Configuration;

/**
 * 文章
 * 作者：Cz
 */
@Controller("articleAction")
@Scope("prototype")
public class ArticleAction extends BaseAction{
	private static final long serialVersionUID = 1L;
	@Resource(name="articleService")
	private IArticleService<Article> articleService;
	@Resource(name="articleCateService")
	private IArticleCateService<ArticleCate> articleCateService;
	@Resource(name = "configService")
	private IConfigService<Config> configService;
	private Article article;
	
	public void list(){
		String key = request.getParameter("key");
		
		//获取总条数
		int count = 0;
		if("".equals(key) || key==null){
			count = articleService.getTotalCount("select count(*) from Article where deleted=0");
			key = "";
		}else{
			count = articleService.getTotalCount("from Article where title like '%"+key+"%' and deleted=0");
		}
		
		page = new BjuiPage(pageCurrent, pageSize);
		page.setTotalCount(count);
		
		List<Article> list = articleService.list("from Article where deleted=0 order by id desc",page.getStart(),page.getPageSize());
		cfg = new Configuration();
		// 设置FreeMarker的模版文件位置
		cfg.setServletContextForTemplateLoading(request.getServletContext(),
				"WEB-INF/templates/admin");
		Map<Object, Object> root = new HashMap<Object, Object>();
		root.put("list", list);
		root.put("page", page);
		FreemarkerUtils.freemarker(request, response, "articleList.ftl", cfg, root);
	}
	
	public void add(){
		List<ArticleCate> articleCatelist = articleCateService.list("from ArticleCate where deleted=0");
		String zNodes = "";
		for (ArticleCate articleCate : articleCatelist) {
			zNodes += "<li data-id='"+articleCate.getId()+"' data-pid='"+articleCate.getFatherId()+"' data-tabid='"+articleCate.getId()+"'>"+articleCate.getName()+"</li>";
		}
		cfg = new Configuration();
		// 设置FreeMarker的模版文件位置
		cfg.setServletContextForTemplateLoading(request.getServletContext(),
				"WEB-INF/templates/admin");
		Map<Object, Object> root = new HashMap<Object, Object>();
		root.put("zNodes", zNodes);
		FreemarkerUtils.freemarker(request, response, "articleAdd.ftl", cfg, root);
	}
	
	/**
	 * 保存文章
	 * 创建日期：2014-9-25下午11:15:59
	 * 作者：Cz
	 */
	public void save(){
		String callbackData = "";
		try {
			if(article.getArticleCate().getId()==0){
				callbackData = BjuiJson.json("300", "请选择栏目", "", "", "", "", "", "");
			}else if("".equals(article.getContent())){
				callbackData = BjuiJson.json("300", "请输入内容", "", "", "", "", "", "");
			}else{
				article.setDeleted(false);
				// 设置创建日期
				article.setCreateDate(new Date());
				boolean result = articleService.saveOrUpdate(article);
				if (result) {
					callbackData = BjuiJson.json("200", "添加成功", "", "", "", "true", "", "");
				} else {
					callbackData = BjuiJson.json("300", "添加失败", "", "", "", "", "", "");
				}
			}
		} catch (JSONException e) {
			e.printStackTrace();
		} catch (Exception e) {
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
	
	/**
	 * 文章信息
	 * 创建日期：2014-9-25下午10:48:50
	 * 作者：Cz
	 * @return
	 */
	public void info() {
		String callbackData = "";
		String idStr = request.getParameter("id");
		try {
			PrintWriter out = response.getWriter();
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
				Article findArticle = articleService.findById(Article.class, id);
				if (findArticle == null) {
					// 文章不存在
					callbackData = BjuiJson.json("300", "文章不存在", "", "", "", "", "", "");
					out.print(callbackData);
					out.flush();
					out.close();
				} else {
					//获取栏目
					List<ArticleCate> articleCatelist = articleCateService.list("from ArticleCate where deleted=0");
					String zNodes = "";
					for (ArticleCate articleCate : articleCatelist) {
						zNodes += "<li data-id='"+articleCate.getId()+"' data-pid='"+articleCate.getFatherId()+"' data-tabid='"+articleCate.getId()+"'>"+articleCate.getName()+"</li>";
					}
					cfg = new Configuration();
					// 设置FreeMarker的模版文件位置
					cfg.setServletContextForTemplateLoading(request.getServletContext(),
							"WEB-INF/templates/admin");
					Map<Object, Object> root = new HashMap<Object, Object>();
					root.put("article", findArticle);
					root.put("zNodes", zNodes);
					FreemarkerUtils.freemarker(request, response, "articleEdit.ftl", cfg, root);
				}
			}
		} catch (JSONException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void update(){
		String callbackData = "";
		try {
			PrintWriter out = response.getWriter();
			if(article==null){
				callbackData = BjuiJson.json("300", "参数错误", "", "", "", "", "", "");
			}else{
				Article findArticle = articleService.findById(Article.class, article.getId());
				article.setCreateDate(findArticle.getCreateDate());
				article.setVersion(findArticle.getVersion());
				article.setDeleted(findArticle.isDeleted());
				boolean result = articleService.saveOrUpdate(article);
				//修改成功
				if(result){
					callbackData = BjuiJson.json("200", "修改成功", "", "", "", "true", "", "");
				}else{
					//修改失败
					callbackData = BjuiJson.json("300", "修改失败", "", "", "", "", "", "");
				}
			}
			out.print(callbackData);
			out.flush();
			out.close();
		} catch (JSONException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void delete() {
		String callbackData = "";
		String idStr = request.getParameter("id");
		try {
			PrintWriter out = response.getWriter();
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
				Article findArticle = articleService.findById(Article.class, id);
				if (findArticle == null) {
					// 文章不存在
					callbackData = BjuiJson.json("300", "文章不存在", "", "", "", "", "", "");
				} else {
					try {
						boolean result = articleService.delete(findArticle);
						if(result){
							callbackData = BjuiJson.json("200", "删除成功", "", "", "", "", "", "");
						}else{
							callbackData = BjuiJson.json("300", "删除失败", "", "", "", "", "", "");
						}
					} catch (JSONException e) {
						e.printStackTrace();
					}
				}
			}
			out.print(callbackData);
			out.flush();
			out.close();
		} catch (JSONException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void indexArticleList(){
		String idStr = request.getParameter("id");
		String key = request.getParameter("key");
		String pStr = request.getParameter("p");
		Config config = configService.findById(Config.class, 1);
		int p = 1;
		if(pStr!=null && !"".equals(pStr)){
			p = Integer.parseInt(pStr);
		}
		
		//获取总条数
		int count = 0;
		String countHql = "select count(*) from Article where deleted=0 and status=1";
		if(idStr!=null && !"".equals(idStr)){
			countHql += " and articleCate.id="+idStr;
		}
		if(key!=null && !"".equals(key)){
			try {
				key = new String(key.getBytes("ISO-8859-1"),"utf-8");
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
			countHql += " and title like '%"+key+"%'";
		}
		count = articleService.getTotalCount(countHql);
		PageModel pageModel = new PageModel();
		pageModel.setAllCount(count);
		pageModel.setCurrentPage(p);
		List<Article> list = null;
		ArticleCate articleCate = null;
		String hql = "from Article where deleted=0 and status=1";
		if(idStr!=null && !"".equals(idStr)){
			hql += " and articleCate.id="+idStr;
			articleCate = articleCateService.findById(ArticleCate.class, Integer.parseInt(idStr));
		}
		if(key!=null && !"".equals(key)){
			hql += " and title like '%"+key+"%'";
		}
		hql += " order by id desc";
		list = articleService.list(hql,pageModel.getStart(),pageModel.getPageSize());
		cfg = new Configuration();
		// 设置FreeMarker的模版文件位置
		cfg.setServletContextForTemplateLoading(request.getServletContext(),
				"WEB-INF/templates/index");
		Map<Object, Object> root = new HashMap<Object, Object>();
		
		root.put("articleList", list);
		root.put("articleCate", articleCate);
		root.put("page", pageModel.getPageStr("list.do?id="+idStr+"&p="));
		root.put("config", config);
		FreemarkerUtils.freemarker(request, response, ftlFileName, cfg, root);
	}
	public void indexArticle() {
		String callbackData = "";
		String idStr = request.getParameter("id");
			PrintWriter out = null;
			try {
				out = response.getWriter();
			} catch (IOException e1) {
				e1.printStackTrace();
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
				Article findArticle = articleService.findById(Article.class, id);
				if (findArticle == null) {
					// 文章不存在
					callbackData = "文章不存在";
					out.print(callbackData);
					out.flush();
					out.close();
				} else {
					Config config = configService.findById(Config.class, 1);
					cfg = new Configuration();
					// 设置FreeMarker的模版文件位置
					cfg.setServletContextForTemplateLoading(request.getServletContext(),
							"WEB-INF/templates/index");
					Map<Object, Object> root = new HashMap<Object, Object>();
					root.put("article", findArticle);
					root.put("config", config);
					FreemarkerUtils.freemarker(request, response, ftlFileName, cfg, root);
				}
			}
	}
	
	public Article getArticle() {
		return article;
	}

	public void setArticle(Article article) {
		this.article = article;
	}

}

