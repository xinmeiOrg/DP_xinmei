package com.lxinet.fenxiao.action;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.annotation.Resource;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.struts2.ServletActionContext;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.lxinet.fenxiao.entities.Config;
import com.lxinet.fenxiao.entities.Kami;
import com.lxinet.fenxiao.entities.Product;
import com.lxinet.fenxiao.service.IConfigService;
import com.lxinet.fenxiao.service.IKamiService;
import com.lxinet.fenxiao.service.IProductService;
import com.lxinet.fenxiao.utils.BjuiJson;
import com.lxinet.fenxiao.utils.BjuiPage;
import com.lxinet.fenxiao.utils.FreemarkerUtils;
import com.opensymphony.xwork2.ActionContext;

import freemarker.template.Configuration;

/**
 * 分销码
 * 作者：Cz
 */
@Controller("kamiAction")
@Scope("prototype")
public class KamiAction extends BaseAction{
	private static final long serialVersionUID = 1L;
	@Resource(name="kamiService")
	private IKamiService<Kami> kamiService;
	@Resource(name = "configService")
	private IConfigService<Config> configService;
	@Resource(name="productService")
	private IProductService<Product> productService;
	private Kami Kami;
	
	/** 文件对象 */
	private List<File> Filedata;
	/** 文件名 */
	private List<String> FiledataFileName;
	/** 文件内容类型 */
	private List<String> FiledataContentType;
	private String name;
	
	public void list(){
		String key = request.getParameter("key");
		//分销码ID
		String pid = request.getParameter("pid");
		//获取总条数
		int count = 0;
		String countHql = "select count(*) from Kami where deleted=0";
		String hql = "from Kami where deleted=0";
		if(StringUtils.isNotEmpty(key)){
			countHql += " and no like '"+key+"'";
			hql += " and no like '"+key+"'";
		}
		if(StringUtils.isNotEmpty(pid)){
			countHql += " and product.id="+pid;
			hql += " and product.id="+pid;
		}
		hql += " order by id desc";
		count = kamiService.getTotalCount(countHql);
		page = new BjuiPage(pageCurrent, pageSize);
		page.setTotalCount(count);
		List<Kami> list = kamiService.list(hql,page.getStart(),page.getPageSize());
		cfg = new Configuration();
		// 设置FreeMarker的模版文件位置
		cfg.setServletContextForTemplateLoading(request.getServletContext(),
				"WEB-INF/templates/admin");
		Map<Object, Object> root = new HashMap<Object, Object>();
		root.put("list", list);
		root.put("page", page);
		FreemarkerUtils.freemarker(request, response, "kamiList.ftl", cfg, root);
	}
	
	public void add(){
		String pidStr = request.getParameter("pid");
		try {
			if(StringUtils.isNotEmpty(pidStr)){
				Product findProduct = productService.findById(Product.class, Integer.parseInt(pidStr));
				if(findProduct != null){
					cfg = new Configuration();
					// 设置FreeMarker的模版文件位置
					cfg.setServletContextForTemplateLoading(request.getServletContext(),
							"WEB-INF/templates/admin");
					Map<Object, Object> root = new HashMap<Object, Object>();
					root.put("product", findProduct);
					FreemarkerUtils.freemarker(request, response, "kamiAdd.ftl", cfg, root);
				}else{
					PrintWriter out = null;
					try {
						out = response.getWriter();
					} catch (IOException e) {
						e.printStackTrace();
					}
					String callbackData = BjuiJson.json("300", "所属产品不存在", "", "", "", "", "", "");
					out.print(callbackData);
					out.flush();
					out.close();
				}
			}else{
				PrintWriter out = null;
				try {
					out = response.getWriter();
				} catch (IOException e) {
					e.printStackTrace();
				}
				String callbackData = BjuiJson.json("300", "所属产品不存在", "", "", "", "", "", "");
				out.print(callbackData);
				out.flush();
				out.close();
			}
		} catch (JSONException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	/**
	 * 保存分销码
	 * 创建日期：2014-9-25下午11:15:59
	 * 作者：Cz
	 */
	public void save(){
		String infos = request.getParameter("infos");
		String pidStr = request.getParameter("pid");
		String callbackData = "";
		try {
			if(StringUtils.isNotEmpty(pidStr)){
				Product findProduct = productService.findById(Product.class, Integer.parseInt(pidStr));
				if(findProduct != null){
					if(StringUtils.isNotEmpty(infos)){
						String[] infosArr = infos.split("\r\n");
						//卡号
						String no = "";
						//密码
						String password = "";
						//成功添加分销码信息个数
						int successNum = 0;
						//失败添加分销码信息个数
						int errorNum = 0;
						for (String info : infosArr) {
							info = info.trim();
							if(StringUtils.isNotEmpty(info)){
								String[] infoArr = info.split(":");
								no = infoArr[0];
								//避免没有输入密码报错
								if(infoArr.length>1){
									password = infoArr[1];
								}
								Kami newKami = new Kami();
								newKami.setCreateDate(new Date());
								newKami.setDeleted(false);
								newKami.setStatus(0);
								newKami.setNo(no);
								newKami.setPassword(password);
								newKami.setProduct(findProduct);
								boolean res = kamiService.saveOrUpdate(newKami);
								if(res){
									//添加成功之后数量+1
									successNum ++;
								}
							}
						}
						errorNum = infosArr.length-successNum;
						callbackData = BjuiJson.json("200", "添加成功分销码数量:"+successNum+",添加失败分销码数量:"+errorNum, "", "", "", "true", "", "");
					}else{
						callbackData = BjuiJson.json("300", "分销码信息不能为空", "", "", "", "", "", "");
					}
				}else{
					callbackData = BjuiJson.json("300", "所属产品不存在", "", "", "", "", "", "");
				}
			}else{
				callbackData = BjuiJson.json("300", "所属产品不存在", "", "", "", "", "", "");
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
	public void uploadKamiPage(){
		String pidStr = request.getParameter("pid");
		try {
			if(StringUtils.isNotEmpty(pidStr)){
				Product findProduct = productService.findById(Product.class, Integer.parseInt(pidStr));
				if(findProduct != null){
					cfg = new Configuration();
					// 设置FreeMarker的模版文件位置
					cfg.setServletContextForTemplateLoading(request.getServletContext(),
							"WEB-INF/templates/admin");
					Map<Object, Object> root = new HashMap<Object, Object>();
					root.put("product", findProduct);
					FreemarkerUtils.freemarker(request, response, "uploadKami.ftl", cfg, root);
				}else{
					PrintWriter out = null;
					try {
						out = response.getWriter();
					} catch (IOException e) {
						e.printStackTrace();
					}
					String callbackData = BjuiJson.json("300", "所属产品不存在", "", "", "", "", "", "");
					out.print(callbackData);
					out.flush();
					out.close();
				}
			}else{
				PrintWriter out = null;
				try {
					out = response.getWriter();
				} catch (IOException e) {
					e.printStackTrace();
				}
				String callbackData = BjuiJson.json("300", "所属产品不存在", "", "", "", "", "", "");
				out.print(callbackData);
				out.flush();
				out.close();
			}
		} catch (JSONException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	public void uploadKami(){
		String pidStr = request.getParameter("pid");
		String filePath = request.getParameter("filePath");
//		Product findProduct = productService.findById(Product.class, Integer.parseInt(pidStr));
//		ActionContext ac=ActionContext.getContext();
//		ServletContext sc = (ServletContext) ac
//				.get(ServletActionContext.SERVLET_CONTEXT);
//		String savePath = sc.getRealPath("/");
//		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
//		String ymd = sdf.format(new Date());
//		String path = "upload/"+ymd+"/";
//		savePath = savePath + path;
//		File f1 = new File(savePath);
//		if (!f1.exists()) {
//			f1.mkdirs();
//		}
//		int size = Filedata.size();
//		String extName = null;
//		String name = null;
//		String callbackData = "";
//		for (int i = 0; i < size; i++) {
//			extName = FiledataFileName.get(i).substring(
//					FiledataFileName.get(i).lastIndexOf("."));
//			name = UUID.randomUUID().toString();
		Product findProduct = productService.findById(Product.class, Integer.parseInt(pidStr));
		ActionContext ac=ActionContext.getContext();
		ServletContext sc = (ServletContext) ac.get(ServletActionContext.SERVLET_CONTEXT);
		String savePath = sc.getRealPath("/");
		String callbackData = "";
			File file = new File(savePath + filePath);
			try {
				 String encoding="GBK";
		         if(file.isFile() && file.exists()){ //判断文件是否存在
		            InputStreamReader read = new InputStreamReader(
		            new FileInputStream(file),encoding);//考虑到编码格式
		            BufferedReader bufferedReader = new BufferedReader(read);
		            String lineTxt = null;
		            //卡号
					String no = "";
					//密码
					String password = "";
					//成功添加分销码信息个数
					int successNum = 0;
					//失败添加分销码信息个数
					int errorNum = 0;
		            while((lineTxt = bufferedReader.readLine()) != null){
						String[] infoArr = lineTxt.split(",");
						no = infoArr[0];
						//避免没有输入密码报错
						if(infoArr.length>1){
							password = infoArr[1];
						}
						Kami newKami = new Kami();
						newKami.setCreateDate(new Date());
						newKami.setDeleted(false);
						newKami.setStatus(0);
						newKami.setNo(no);
						newKami.setPassword(password);
						newKami.setProduct(findProduct);
						boolean res = kamiService.saveOrUpdate(newKami);
						if(res){
							//添加成功之后数量+1
							successNum ++;
						}else{
							errorNum ++;
						}
		            }
		            read.close();
		            callbackData = BjuiJson.json("200", "添加成功分销码数量:"+successNum+",添加失败分销码数量:"+errorNum, "", "", "", "true", "", "");
				}else{
					callbackData = BjuiJson.json("300", "找不到指定的文件", "", "", "", "", "", "");
				}
			} catch (IOException e) {
				e.printStackTrace();
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
	
	
	/**
	 * 分销码信息
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
				Kami findKami = kamiService.findById(Kami.class, id);
				if (findKami == null) {
					// 分销码不存在
					callbackData = BjuiJson.json("300", "分销码不存在", "", "", "", "", "", "");
					out.print(callbackData);
					out.flush();
					out.close();
				} else {
					cfg = new Configuration();
					// 设置FreeMarker的模版文件位置
					cfg.setServletContextForTemplateLoading(request.getServletContext(),
							"WEB-INF/templates/admin");
					Map<Object, Object> root = new HashMap<Object, Object>();
					root.put("Kami", findKami);
					FreemarkerUtils.freemarker(request, response, "KamiEdit.ftl", cfg, root);
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
			if(Kami==null){
				callbackData = BjuiJson.json("300", "参数错误", "", "", "", "", "", "");
			}else{
				Kami findKami = kamiService.findById(Kami.class, Kami.getId());
				boolean result = kamiService.saveOrUpdate(findKami);
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
				Kami findKami = kamiService.findById(Kami.class, id);
				if (findKami == null) {
					// 分销码不存在
					callbackData = BjuiJson.json("300", "分销码不存在", "", "", "", "", "", "");
				} else {
					try {
						boolean result = kamiService.delete(findKami);
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
	
	
	public Kami getKami() {
		return Kami;
	}

	public void setKami(Kami Kami) {
		this.Kami = Kami;
	}

	public List<File> getFiledata() {
		return Filedata;
	}

	public void setFiledata(List<File> filedata) {
		Filedata = filedata;
	}

	public List<String> getFiledataFileName() {
		return FiledataFileName;
	}

	public void setFiledataFileName(List<String> filedataFileName) {
		FiledataFileName = filedataFileName;
	}

	public List<String> getFiledataContentType() {
		return FiledataContentType;
	}

	public void setFiledataContentType(List<String> filedataContentType) {
		FiledataContentType = filedataContentType;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}

