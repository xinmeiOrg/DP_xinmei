package com.lxinet.fenxiao.action;
/**
 * 创建日期：2014-9-1下午3:16:32
 * 作者：Cz
 */
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import javax.imageio.ImageIO;
import javax.servlet.ServletContext;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import com.lxinet.fenxiao.utils.StaticFileUtil;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.struts2.ServletActionContext;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;
/**
 * 文件上传
 * @author Cz
 *
 */
@Controller("uploadAction")
@Scope("prototype")
public class UploadAction extends BaseAction {
	private static final long serialVersionUID = -4848248679889814408L;
	/** 文件对象 */
	private List<File> Filedata;
	/** 文件名 */
	private List<String> FiledataFileName;
	/** 文件内容类型 */
	private List<String> FiledataContentType;
	private String name;

	public void doUpload() {

		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		String filePath = StaticFileUtil.getProperty("fileSystem", "filePath");
		String ymd = sdf.format(new Date());
		String path = filePath + "/"+ymd+"/";
		File f1 = new File(path);
		if (!f1.exists()) {
			f1.mkdirs();
		}
		int size = Filedata.size();
		String extName = null;
		String name = null;
		for (int i = 0; i < size; i++) {
			extName = FiledataFileName.get(i).substring(
					FiledataFileName.get(i).lastIndexOf("."));
			name = UUID.randomUUID().toString();
			File file = new File(path + name + extName);
			try {
				FileUtils.copyFile(Filedata.get(i), file);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		HttpServletResponse response = ServletActionContext.getResponse();
		try {
			JSONObject json = new JSONObject();
			json.put("statusCode", 200);
			json.put("message", "");
			json.put("filename", "" + path + name + extName);
			response.getWriter().print(json.toString());
			response.getWriter().flush();
			response.getWriter().close();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	public void download() throws IOException {

		String fileName = request.getParameter("fileName");
		if(StringUtils.isNotBlank(fileName)){
			// 载入图像
			BufferedImage buffImg = ImageIO.read(new FileInputStream(fileName));

			HttpServletResponse response = ServletActionContext.getResponse();


			// 将四位数字的验证码保存到Session中。
			// 禁止图像缓存。
			response.setHeader("Pragma", "no-cache");
			response.setHeader("Cache-Control", "no-cache");
			response.setDateHeader("Expires", 0);
			response.setContentType("image/jpeg");
			// 将图像输出到Servlet输出流中。
			ServletOutputStream sos = response.getOutputStream();
			ImageIO.write(buffImg, "jpeg", sos);
			sos.close();
		}
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
