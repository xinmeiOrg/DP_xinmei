<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
String no = request.getParameter("no");
String orderType = request.getParameter("orderType");
%>
<!DOCTYPE html>
<html>
 <head>
  <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
  <meta charset="UTF-8" />
  <meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=0" />
  <meta name="apple-mobile-web-app-capable" content="yes" />
  <meta name="apple-mobile-web-app-status-bar-style" content="black" />
  <meta content="telephone=no" name="format-detection" />
  <title>支付成功</title>
  <script type="text/javascript" src="../js/jquery.min.js"></script>
  <script type="text/javascript" src="../js/zepto.min.js"></script>
  <script type="text/javascript">
  var no = "<%=no%>";
  var orderType = "<%=orderType%>";
  window.onload = function (){
        alertDefaultStyle("mini", "支付成功");
        if('RECHARGE' == orderType ){
        	setTimeout("window.location.href='index.jsp'",1000);
        }else if('ORDERS' == orderType){
        	setTimeout("window.location.href='ordersDetail?no="+no+"';",1000);
        }
  }
  </script>
 </head>
 <body>
 </body>
</html>