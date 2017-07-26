package com.lxinet.fenxiao.utils;

import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.Message.RecipientType;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.lxinet.fenxiao.entities.Config;
import com.lxinet.fenxiao.service.IConfigService;


public class MailSenderUtils {
	/**
	 * 发送邮件
	 * @param user
	 * @param content
	 * @param title
	 * @param request
	 * @return
	 */
	public static boolean sendMail(String email, String content,String title,
			HttpServletRequest request) 
			 {
		ServletContext context = request.getServletContext();
		ApplicationContext ctx =WebApplicationContextUtils.getRequiredWebApplicationContext(context);
//		取得特定bean
		IConfigService configService=(IConfigService)ctx.getBean("configService");
		Config config = (Config) configService.findById(Config.class, 1);
		
		final String userName = config.getSendEmail();
		final String passWord = config.getSendEmailPass();
		final String smtp = config.getSendEmailSmtp();
		
		Properties props = new Properties();
		props.setProperty("mail.smtp.auth", "true");
		props.setProperty("mail.transport.protocol", "smtp");
		props.setProperty("mail.host", smtp);// 用户主机
		Session session = Session.getInstance(props, new Authenticator() {
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(userName, passWord);// 获取用户名和密码
			}
		});
		Message msg = new MimeMessage(session);
		session.setDebug(false);// 查看调试信息:true,不查看：false;
		try {
			msg.setFrom(new InternetAddress(userName));
			msg.setSubject(title);
			msg.setRecipients(RecipientType.TO, InternetAddress.parse(email));// 多个收件人
			msg.setContent(content, "text/html;charset=utf-8");// 文本/指定文本类型，字符集
			Transport.send(msg);
		} catch (javax.mail.MessagingException e) {
			//发送失败
			return false;
		}// 发送端
		return true;
	}
	/**
	 * 获取中文内容-注册
	 * @param user
	 * @param code
	 * @param request
	 * @return
	 */
//	public static String getPassContend(UserInfo user,String code,HttpServletRequest request){
//		String conten = "<h4>您好，"+user.getName()+"：</h4><p>您刚刚申请了找回密码的请求，请点击以下链接完成操作：<br  />"+
//							 "<a href='"+request.getScheme() + "://"
//							 + request.getServerName()
//							 + request.getContextPath() +
//							 "/passBack.do?userId="+user.getId()+"&code="+code+"&request_locale=zh_CN' target='_blank'>"
//							 +request.getScheme() + "://"
//							 + request.getServerName() 
//							 + request.getContextPath() +
//							 "/passBack.do?userId="+user.getId()+"&code="+code+"&request_locale=zh_CN</a><br  />" +
//					 		"本链接3天内有效。<br />" +
//							 "(如果点击链接无反应，请复制链接到浏览器里直接打开)<p>" ;
//		return conten;
//	}
//	/**
//	 * 获取英文内容-注册
//	 * @param user
//	 * @param code
//	 * @param request
//	 * @return
//	 */
//	public static String getPassContendEn(UserInfo user,String code,HttpServletRequest request){
//		String conten = "<h4>Hello，"+user.getName()
//				+"：</h4><p>You have just applied for retrieving password request, please click the following link to complete the operation:<br  />"+
//							 "<a href='"+request.getScheme() + "://"
//							 + request.getServerName()
//							 + request.getContextPath() +
//							 "/passBack.do?userId="+user.getId()+"&code="+code+"&request_locale=en_US' target='_blank'>"
//							 +request.getScheme() + "://"
//							 + request.getServerName() 
//							 + request.getContextPath() +
//							 "/passBack.do?userId="+user.getId()+"&code="+code+"&request_locale=en_US</a><br  />" +
//					 		"This link valid for 3 days.<br />" +
//							 "(do not respond if you click on the link, please copy the link directly to the browser open)<p>" ;
//		return conten;
//	}
//	/**
//	 * 获取中文内容-找回密码
//	 * @param user
//	 * @param code
//	 * @return
//	 */
//	public static String getPassContendApp(UserInfo user,String code){
//		String conten = "<h4>您好，"+user.getName()+"：</h4><p>您刚刚申请了找回密码的请求，验证码如下：<br  />" +
//								code+"<br />" +
//					 		"本验证码10分钟内有效。<p>"  ;
//		return conten;
//	}
//	/**
//	 * 获取英文内容-找回密码
//	 * @param user
//	 * @param code
//	 * @return
//	 */
//	public static String getPassContendAppEn(UserInfo user,String code){
//		String conten = "<h4>Hello，"+user.getName()+"：</h4><p>You have just applied for retrieving password request, verification code is as follows:<br  />" +
//								code+"<br />" +
//					 		"The verification code in 10 minutes.<p>"  ;
//		return conten;
//	}
//	/**
//	 * 生成验证码-找回密码
//	 * @return
//	 */
//	public static String validataCodeApp(){
//		Random random = new Random();
//		String result = "";
//		String[] str = { "A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K",
//				"L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X",
//				"Y", "Z", "a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k",
//				"l", "m", "n", "o", "p", "q", "r", "s", "t", "u", "v", "w", "x",
//				"y", "0", "1", "2", "3", "4", "5", "6", "7", "8", "9"};
//		int length = str.length;
//		for (int i = 0; i < 6; i++) {
//			int temp = random.nextInt(length);
//			result += str[temp];
//		}
//		return result;
//	}
//	/**
//	 * 生成验证码-注册
//	 * @return
//	 */
//	public static String validataCode(){
//		Random random = new Random();
//		String result = "";
//		String[] str = { "A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K",
//				"L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X",
//				"Y", "Z", "a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k",
//				"l", "m", "n", "o", "p", "q", "r", "s", "t", "u", "v", "w", "x",
//				"y", "0", "1", "2", "3", "4", "5", "6", "7", "8", "9"};
//		int length = str.length;
//		for (int i = 0; i < 32; i++) {
//			int temp = random.nextInt(length);
//			result += str[temp];
//		}
//		return result;
//	}
}
