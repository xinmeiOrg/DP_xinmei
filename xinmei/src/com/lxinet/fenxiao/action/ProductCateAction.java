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

import com.lxinet.fenxiao.entities.ProductCate;
import com.lxinet.fenxiao.service.IProductCateService;
import com.lxinet.fenxiao.utils.BjuiJson;
import com.lxinet.fenxiao.utils.FreemarkerUtils;

import freemarker.template.Configuration;

/**
 * 产品分类Action
 * @author Cz
 * 网址：http://www.919dns.com
 */
@Controller("productCateAction")
@Scope("prototype")
public class ProductCateAction extends BaseAction{
	private static final long serialVersionUID = 1L;
	@Resource(name="productCateService")
	private IProductCateService<ProductCate> productCateService;
	private ProductCate productCate;
	
	public void list(){
		List<ProductCate> list = productCateService.list("from ProductCate where deleted=0");
		String zNodes = "";
		for (ProductCate ProductCate : list) {
			zNodes += "<li data-id='"+ProductCate.getId()+"' data-pid='"+ProductCate.getFatherId()+"' data-tabid='"+ProductCate.getId()+"'>"+ProductCate.getName()+"[ID:"+ProductCate.getId()+"]</li>";
		}
		cfg = new Configuration();
		// 设置FreeMarker的模版文件位置
		cfg.setServletContextForTemplateLoading(request.getServletContext(),
				"WEB-INF/templates/admin");
		Map<Object, Object> root = new HashMap<Object, Object>();
		root.put("zNodes", zNodes);
		root.put("list", list);
		FreemarkerUtils.freemarker(request, response, "productCateList.ftl", cfg, root);
	}
	
	public void add(){
		List<ProductCate> list = productCateService.list("from ProductCate where deleted=0");
		String zNodes = "<li data-id='0' data-pid='0' data-tabid='0'>顶级分类</li>";
		for (ProductCate ProductCate : list) {
			zNodes += "<li data-id='"+ProductCate.getId()+"' data-pid='"+ProductCate.getFatherId()+"' data-tabid='"+ProductCate.getId()+"'>"+ProductCate.getName()+"</li>";
		}
		cfg = new Configuration();
		// 设置FreeMarker的模版文件位置
		cfg.setServletContextForTemplateLoading(request.getServletContext(),
				"WEB-INF/templates/admin");
		Map<Object, Object> root = new HashMap<Object, Object>();
		root.put("zNodes", zNodes);
		root.put("list", list);
		FreemarkerUtils.freemarker(request, response, "productCateAdd.ftl", cfg, root);
	}

	/**
	 * 保存分类
	 * 创建日期：2014-10-26上午9:38:42
	 * 作者：Cz
	 * 网址:http://www.919dns.com
	 * @throws JSONException
	 */
	public void save(){
		String callbackData = "";
		productCate.setDeleted(false);
		productCate.setCreateDate(new Date());
		boolean result = productCateService.saveOrUpdate(productCate);
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
			ProductCate findProductCate = productCateService.findById(ProductCate.class, id);
			if (findProductCate == null) {
				// 分类不存在
				callbackData = "分类不存在";
			} else {
				callbackData = findProductCate.getName();
			}
		}
		log.info(callbackData);
		out.print(callbackData);
		out.flush();
		out.close();
	}
	
	/**
	 * 分类信息
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
				ProductCate findProductCate = productCateService.findById(ProductCate.class, id);
				if (findProductCate == null) {
					// 分类不存在
					callbackData = BjuiJson.json("300", "分类不存在", "", "", "", "", "", "");
					out.print(callbackData);
					out.flush();
					out.close();
				} else {
					List<ProductCate> list = productCateService.list("from ProductCate where deleted=0");
					String zNodes = "<li data-id='0' data-pid='0' data-tabid='0'>顶级分类</li>";
					for (ProductCate ProductCate : list) {
						zNodes += "<li data-id='"+ProductCate.getId()+"' data-pid='"+ProductCate.getFatherId()+"' data-tabid='"+ProductCate.getId()+"'>"+ProductCate.getName()+"</li>";
					}
					//父类分类名称
					String fatherName = "";
					if(findProductCate.getFatherId()!=0){
						ProductCate fatherProductCate = productCateService.findById(ProductCate.class, findProductCate.getFatherId());
						if(fatherProductCate!=null){
							fatherName = productCateService.findById(ProductCate.class, findProductCate.getFatherId()).getName();
						}else{
							fatherName = "上级分类不存在";
						}
					}else{
						fatherName = "顶级分类";
					}
					
					cfg = new Configuration();
					// 设置FreeMarker的模版文件位置
					cfg.setServletContextForTemplateLoading(request.getServletContext(),
							"WEB-INF/templates/admin");
					Map<Object, Object> root = new HashMap<Object, Object>();
					root.put("productCate", findProductCate);
					root.put("zNodes", zNodes);
					root.put("fatherName", fatherName);
					FreemarkerUtils.freemarker(request, response, "productCateEdit.ftl", cfg, root);
				}
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 修改分类
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
			if(productCate==null){
				callbackData = BjuiJson.json("300", "参数错误", "", "", "", "", "", "");
			}else{
				if(productCate.getFatherId()==productCate.getId()){
					callbackData = BjuiJson.json("300", "上级分类不能选择当前修改的分类", "", "", "", "", "", "");
				}else{
					ProductCate findProductCate = productCateService.findById(ProductCate.class, productCate.getId());
					findProductCate.setFatherId(productCate.getFatherId());
					findProductCate.setName(productCate.getName());
					boolean result = productCateService.saveOrUpdate(findProductCate);
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
	 * 删除分类
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
				ProductCate findProductCate = productCateService.findById(ProductCate.class, id);
				if (findProductCate == null) {
					// 分类不存在
					callbackData = BjuiJson.json("300", "分类不存在", "", "", "", "", "", "");
				} else {
					//如果有下级分类，则不能删除
					List<ProductCate> sanList = productCateService.listByFatherId(id);
					log.info(sanList);
					if(sanList.size()!=0){
						callbackData = BjuiJson.json("300", "该分类存在下级分类，请先删除下级分类", "", "", "", "", "", "");
					}else{
						boolean result = productCateService.delete(findProductCate);
						if(result){
							//callbackData = BjuiJson.json("200", "删除成功", "ProductCateList", "", "", "true", "", "");
							callbackData = BjuiJson.json("200", "删除成功", "", "", "", "true", "", "");

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

	public ProductCate getProductCate() {
		return productCate;
	}

	public void setProductCate(ProductCate productCate) {
		this.productCate = productCate;
	}
}
