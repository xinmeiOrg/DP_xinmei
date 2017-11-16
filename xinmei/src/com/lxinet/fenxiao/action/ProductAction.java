package com.lxinet.fenxiao.action;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.ServletException;


import org.apache.commons.lang3.StringUtils;
import org.json.JSONException;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.lxinet.fenxiao.entities.Config;
import com.lxinet.fenxiao.entities.Product;
import com.lxinet.fenxiao.entities.ProductCate;
import com.lxinet.fenxiao.service.IConfigService;
import com.lxinet.fenxiao.service.IProductCateService;
import com.lxinet.fenxiao.service.IProductService;
import com.lxinet.fenxiao.utils.BjuiJson;
import com.lxinet.fenxiao.utils.BjuiPage;
import com.lxinet.fenxiao.utils.FreemarkerUtils;
import com.lxinet.fenxiao.utils.PageModel;

import freemarker.template.Configuration;

/**
 * 创建日期：2014-9-25上午9:26:36
 * 作者：Cz
 */
@Controller("productAction")
@Scope("prototype")
public class ProductAction extends BaseAction{
	private static final long serialVersionUID = 1L;
	@Resource(name="productService")
	private IProductService<Product> productService;
	@Resource(name="productCateService")
	private IProductCateService<ProductCate> productCateService;
	@Resource(name = "configService")
	private IConfigService<Config> configService;
	private Product product;
	
	public void list(){
		String key = request.getParameter("key");
		//获取总条数
		int count = 0;
		if("".equals(key) || key==null){
			count = productService.getTotalCount("select count(*) from Product where deleted=0");
			key = "";
		}else{
			count = productService.getTotalCount("from Product where title like '%"+key+"%' and deleted=0");
		}
		
		page = new BjuiPage(pageCurrent, pageSize);
		page.setTotalCount(count);
		List<Product> list = productService.list("from Product where deleted=0 order by id desc",page.getStart(),page.getPageSize());
		cfg = new Configuration();
		// 设置FreeMarker的模版文件位置
		cfg.setServletContextForTemplateLoading(request.getServletContext(),
				"WEB-INF/templates/admin");
		Map<Object, Object> root = new HashMap<Object, Object>();
		root.put("list", list);
		root.put("page", page);
		FreemarkerUtils.freemarker(request, response, "productList.ftl", cfg, root);
	}
	
	public void add(){
		List<ProductCate> productCatelist = productCateService.list("from ProductCate where deleted=0");
		String zNodes = "";
		for (ProductCate productCate : productCatelist) {
			zNodes += "<li data-id='"+productCate.getId()+"' data-pid='"+productCate.getFatherId()+"' data-tabid='"+productCate.getId()+"'>"+productCate.getName()+"</li>";
		}
		cfg = new Configuration();
		// 设置FreeMarker的模版文件位置
		cfg.setServletContextForTemplateLoading(request.getServletContext(),
				"WEB-INF/templates/admin");
		Map<Object, Object> root = new HashMap<Object, Object>();
		root.put("zNodes", zNodes);
		FreemarkerUtils.freemarker(request, response, "productAdd.ftl", cfg, root);
	}
	
	/**
	 * 保存产品
	 * 创建日期：2014-9-25下午11:15:59
	 * 作者：Cz
	 */
	public void save(){
		String callbackData = "";
		try {
			if(product.getProductCate().getId()==0){
				callbackData = BjuiJson.json("300", "请选择栏目", "", "", "", "", "", "");
			}else if("".equals(product.getContent())){
				callbackData = BjuiJson.json("300", "请输入内容", "", "", "", "", "", "");
			}else{
				if(StringUtils.isEmpty(product.getPicture())){
					product.setPicture("images/nopicture.jpg");
				}
				product.setDeleted(false);
				// 设置创建日期
				product.setCreateDate(new Date());
				boolean result = productService.saveOrUpdate(product);
				if (result) {
					callbackData = BjuiJson.json("200", "添加成功", "", "", "", "true", "", "");
				} else {
					callbackData = BjuiJson.json("300", "添加失败", "", "", "", "", "", "");
				}
			}
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
	 * 产品信息
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
				Product findProduct = productService.findById(Product.class, id);
				if (findProduct == null) {
					// 产品不存在
					callbackData = BjuiJson.json("300", "产品不存在", "", "", "", "", "", "");
					out.print(callbackData);
					out.flush();
					out.close();
				} else {
					//获取栏目
					List<ProductCate> productCatelist = productCateService.list("from ProductCate where deleted=0");
					String zNodes = "";
					for (ProductCate productCate : productCatelist) {
						zNodes += "<li data-id='"+productCate.getId()+"' data-pid='"+productCate.getFatherId()+"' data-tabid='"+productCate.getId()+"'>"+productCate.getName()+"</li>";
					}
					cfg = new Configuration();
					// 设置FreeMarker的模版文件位置
					cfg.setServletContextForTemplateLoading(request.getServletContext(),
							"WEB-INF/templates/admin");
					Map<Object, Object> root = new HashMap<Object, Object>();
					root.put("product", findProduct);
					root.put("zNodes", zNodes);
					FreemarkerUtils.freemarker(request, response, "productEdit.ftl", cfg, root);
				}
			}
		}catch (IOException e) {
			e.printStackTrace();
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
	
	public void update(){
		String callbackData = "";
		try {
			PrintWriter out = response.getWriter();
			if(product==null){
				callbackData = BjuiJson.json("300", "参数错误", "", "", "", "", "", "");
			}else{
				Product findProduct = productService.findById(Product.class, product.getId());
				findProduct.setProductCate(product.getProductCate());
				findProduct.setPicture(product.getPicture());
				findProduct.setTitle(product.getTitle());
				findProduct.setContent(product.getContent());
				findProduct.setBills(product.getBills());
				findProduct.setMoney(product.getMoney());
				boolean result = productService.saveOrUpdate(findProduct);
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
				Product findProduct = productService.findById(Product.class, id);
				if (findProduct == null) {
					// 产品不存在
					callbackData = BjuiJson.json("300", "产品不存在", "", "", "", "", "", "");
				} else {
					try {
						boolean result = productService.delete(findProduct);
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
	
	public void indexProductList(){
		String idStr = request.getParameter("id");
		String key = request.getParameter("key");
		String pStr = request.getParameter("p");
		int p = 1;
		if(!StringUtils.isEmpty(pStr)){
			p = Integer.parseInt(pStr);
		}
		
		//获取总条数
		int count = 0;
		String countHql = "select count(*) from Product where deleted=0";
		String hql = "select new Product(id,picture,title,bills,money) from Product where deleted=0";
		if(!StringUtils.isEmpty(idStr)){
			countHql += " and productCate.id="+idStr;
			hql += " and productCate.id="+idStr;
		}
		if(!StringUtils.isEmpty(key)){
			try {
				key = new String(key.getBytes("ISO-8859-1"),"utf-8");
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
			countHql += " and title like '%"+key+"%'";
			hql += " and title like '%"+key+"%'";
		}
		hql += " order by id desc";
		count = productService.getTotalCount(countHql);
		PageModel pageModel = new PageModel();
		pageModel.setAllCount(count);
		pageModel.setCurrentPage(p);
		List<Product> list = productService.list(hql,pageModel.getStart(),pageModel.getPageSize());
		JSONObject json = new JSONObject();
		if(list.size()==0){
			//说明没数据
			json.put("status", "0");
			//说明没有下一页
			json.put("isNextPage", "0");
		}else{
			//说明有数据
			json.put("status", "1");
			if(list.size()==pageModel.getPageSize()){
				//可能有下一页
				//有下一页
				json.put("isNextPage", "1");
			}else{
				//没有下一页数据
				json.put("isNextPage", "0");
			}
			JSONArray listJson = (JSONArray) JSONArray.toJSON(list);
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
	
	public void indexProduct() {
		String idStr = request.getParameter("id");
		JSONObject json = new JSONObject();
		PrintWriter out = null;
		try {
			out = response.getWriter();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		// ID参数为空
		if (idStr == null || "".equals(idStr)) {
			json.put("status", "0");
			json.put("message", "参数错误");
		} else {
			int id = 0;
			try {
				id = Integer.parseInt(idStr);
			} catch (Exception e) {
				// 抛出异常，说明ID不是数字
				json.put("status", "0");
				json.put("message", "参数错误");
			}
			Product findproduct = productService.findById(Product.class, id);
			if (findproduct == null) {
				// 产品不存在
				json.put("status", "0");
				json.put("message", "产品不存在");
			} else {
				JSONObject jsonObj = (JSONObject) JSONObject.toJSON(findproduct);
				json.put("status", "1");
				json.put("product", jsonObj);
			}
		}
		out.print(json);
		out.flush();
		out.close();
	}
	
	public void productDetail() {
		String idStr = request.getParameter("id");
		PrintWriter out = null;
		String callback = "";
		try {
			out = response.getWriter();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		// ID参数为空
		if (idStr == null || "".equals(idStr)) {
			callback = "参数错误";
			out.print(callback);
			out.flush();
			out.close();
		} else {
			int id = 0;
			try {
				id = Integer.parseInt(idStr);
			} catch (Exception e) {
				// 抛出异常，说明ID不是数字
				callback = "参数错误";
				out.print(callback);
				out.flush();
				out.close();
			}
			Product findproduct = productService.findById(Product.class, id);
			if (findproduct == null) {
				// 产品不存在
				callback = "产品不存在";
				out.print(callback);
				out.flush();
				out.close();
			} else {
				request.setAttribute("product",findproduct);
				try {
					request.getRequestDispatcher("detail.jsp").forward(request, response);
				} catch (ServletException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		
	}
	
	public Product getProduct() {
		return product;
	}

	public void setProduct(Product product) {
		this.product = product;
	}

}

