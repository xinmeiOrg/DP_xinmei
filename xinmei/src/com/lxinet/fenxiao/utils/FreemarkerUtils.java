package com.lxinet.fenxiao.utils;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;

/**
 * 创建日期：2014-10-25下午4:06:30 作者：Cz
 */
public class FreemarkerUtils {
	public static void freemarker(HttpServletRequest request,
			HttpServletResponse response,String ftlName,Configuration cfg,Map<Object, Object> root) {
		// 取得模版文件
		Template t = null;
		try {
			t = cfg.getTemplate(ftlName, "utf-8");
		} catch (IOException e3) {
			e3.printStackTrace();
		} // FreeMarker会到系统的/WebRoot/templates/目录下找hello.ftl文件
			// 开始准备生成输出
			// 使用模版文件的charset作为本页面的charset
			// 使用text/html MIME-type
		PrintWriter out = null;
		try {
			out = response.getWriter();
		} catch (IOException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}

		// 合并数据模型和模版，并将结果输出到out中
		try {
			t.process(root, out);// 用模板来开发servlet可以只在代码里面加入动态的数据
		} catch (TemplateException e) {
			try {
				throw new ServletException("处理Template模版中出现错误", e);
			} catch (ServletException e1) {
				e1.printStackTrace();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
