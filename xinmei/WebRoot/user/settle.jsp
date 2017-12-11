<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<meta charset="UTF-8" />
<meta name="viewport"
	content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=0" />
<meta name="apple-mobile-web-app-capable" content="yes" />
<meta name="apple-mobile-web-app-status-bar-style" content="black" />
<meta content="telephone=no" name="format-detection" />
<title>结算信息</title>
<%@ include file="../header.jsp" %>
<link rel="stylesheet" href="../css/cart2.css?${ts }" />
<script type="text/javascript" src="../js/jquery.min.js?${ts }"></script>
<script type="text/javascript" src="../js/zepto.min.js?${ts }"></script>
<script type="text/javascript" src="../js/base64.js?${ts }"></script>
<script type="text/javascript"
	src="https://res.wx.qq.com/open/js/jweixin-1.0.0.js"></script>



<script type="text/javascript">
	$(function() {
		$.ajax({
			url : "userInfoJson",
			type : "GET",
			dataType : "json",
			async : false,
			success : function(data) {
				$("#receiver").val(data.receiver);
				$("#receiverPhone").val(data.receiverPhone);
				$("#receiverAddress").val(data.receiverAddress);
			}
		});
	});

	function pay() {
		var receiver = $('#receiver').val();
		var receiverPhone = $('#receiverPhone').val();
		var receiverAddress = $('#receiverAddress').val();
		if ('' == receiver) {
			alertDefaultStyle("mini", "请输入姓名");
			return;
		}
		if ('' == receiverPhone) {
			alertDefaultStyle("mini", "请输入电话");
			return;
		}
		if ('' == receiverAddress) {
			alertDefaultStyle("mini", "请输入地址");
			return;
		}
		var receiverParam = "{'receiver':'" + receiver + "','receiverPhone':'"
				+ receiverPhone + "','receiverAddress':'" + receiverAddress
				+ "'}";

		var payChannel = $('#payChannel').val();
		$.ajax({
			url : "../ordersPay?no=${requestScope.orders.no }&payChannel="
					+ payChannel,
			type : "get",
			dataType : "json",
			async : false,
			headers : {
				Accept : "application/json; charset=utf-8",
				"receiverParam" : Base64.encode(receiverParam)
			},
			success : function(data) {
				alertDefaultStyle("mini", data.message);
				if (data.href != null) {
					setTimeout("window.location.href='" + data.href + "';",
							2000);
					return;
				}
				if (data.aliPayForm != null) {
					$('#aliPayForm').html(data.aliPayForm);
					return;
				}
				if (data.weixinParam != null) {
					if (typeof WeixinJSBridge == "undefined") {
						if (document.addEventListener) {
							document.addEventListener('WeixinJSBridgeReady',
									onBridgeReady, false);
						} else if (document.attachEvent) {
							document.attachEvent('WeixinJSBridgeReady',
									onBridgeReady);
							document.attachEvent('onWeixinJSBridgeReady',
									onBridgeReady);
						}
					} else {
						onBridgeReady(data.weixinParam);
					}
					return;

				}
				if ("1" == data.status) {
					setTimeout("window.location.href='ordersDetail?no="
							+ data.no + "';", 2000);
				}
			},
			error : function() {
				alertDefaultStyle("mini", "请求失败");
			}
		});
	}

	function onBridgeReady(weixinParam) {
		WeixinJSBridge.invoke('getBrandWCPayRequest', {
			"appId" : weixinParam.appid, //公众号名称，由商户传入     
			"timeStamp" : weixinParam.timestamp, //时间戳，自1970年以来的秒数     
			"nonceStr" : weixinParam.noncestr, //随机串     
			"package" : weixinParam.packageValue,
			"signType" : weixinParam.signType, //微信签名方式：     
			"paySign" : weixinParam.paySign
		//微信签名 
		}, function(res) {
			if (res.err_msg == "get_brand_wcpay_request:ok") {// 使用以上方式判断前端返回,微信团队郑重提示：res.err_msg将在用户支付成功后返回    ok，但并不保证它绝对可靠。 
				alertDefaultStyle("mini", "支付完成");
			}
		});
	}
</script>
<style>
.receiver {
	width: 70%;
	border: 0 none;
}
</style>
</head>
<body>
	<div id="aliPayForm"></div>
	<section class="ww">
		<div class="loagMask" id="loading"
			style="-webkit-transform-origin: 0px 0px 0px; opacity: 1; -webkit-transform: scale(1, 1); display: none;">
			<div class="sn-mask-loading fixedLoading"></div>
		</div>
		<section class="cart-main">
			<div class="sn-nav sn-block">
				<div id="back" class="sn-nav-back">
					<a class="sn-iconbtn">返回</a>
				</div>
				<div class="sn-nav-title of">结算信息</div>
				<div class="sn-nav-right tr pr">
					<a href="index.jsp">首页</a>
				</div>
			</div>
			<div class="sn-block cart-send-box fs30">
				<ul class="sn-list-input">
					<li><label>配送方式：</label>
						<div class="wbox-flex tr">
							<div id="selectShipModel"
								style="-webkit-transform-origin: 0px 0px 0px; opacity: 1; -webkit-transform: scale(1, 1);">
								自动发货</div>
						</div></li>
					<li><label>姓名：</label> <input id="receiver" class="receiver"
						maxlength="10" /></li>
					<li><label>电话：</label> <input id="receiverPhone"
						class="receiver" maxlength="20" /></li>
					<li><label>地址：</label> <input id="receiverAddress"
						class="receiver" maxlength="200"></li>
				</ul>
			</div>
			<div class="sn-block mt30 fs30">
				<ul class="sn-list-input">
					<li><label>支付方式</label>
						<div class="wbox-flex tr">
							<div id="selectShipModel"
								style="-webkit-transform-origin: 0px 0px 0px; opacity: 1; -webkit-transform: scale(1, 1);">
								<select id="payChannel">
									<option value="AMOUNT_PAY">余额支付</option>
									<option value="ALI_PAY">支付宝</option>
<!-- 									<option value="WEIXIN_WAP_PAY">微信</option>
 -->								</select>
							</div>
						</div></li>
				</ul>
			</div>
			<div id="snProdListDivId">
				<div class="cart-list mt30">
					<div class="cart-title wbox of">
						<div class="fs30">商品信息</div>
						<div class="wbox-flex tr sn-txt-muted"></div>
					</div>
					<div class="cart-more-list pr ">
						<ul class="sn-list-common sn-block">
							<li>
								<div class="wbox-flex ovf">
									<a class="pro-list">
										<div class="wbox ">
											<div class="pro-info wbox-flex">
												<div class="pro-name of">
													${requestScope.orders.productName }</div>
												<div class="snPrice">
													￥${requestScope.orders.productMoney }</div>
												<span class="cart-number">x
													${requestScope.orders.productNum }</span>
											</div>
										</div>
									</a>
								</div>
							</li>
						</ul>
					</div>
				</div>
			</div>
			<div class="cart-order-total cart-box mb30">
				<dl>
					<dt>
						<label>订单总额</label>
						<div class="wbox-flex tr">
							<div id="needPayAmountId" class="snPrice">
								￥${requestScope.orders.money }</div>
						</div>
					</dt>
					<dd>
						<label class="mt10 sn-txt-muted">商品总额</label>
						<div class="wbox-flex tr">
							<div id="productAllPrice" class="snPrice fs30">
								￥${requestScope.orders.money }</div>
						</div>
					</dd>
				</dl>
			</div>
		</section>
	</section>
	<section class="cart-foot-bar h120">
		<div class="cart-fixed">
			<div class="wbox pd">
				<div class="wbox-flex sn-txt-muted">
					<div class="cart-total-price">
						应付金额: <i id="totalAmountId" class="sn-txt-assertive">￥${requestScope.orders.money }</i>
					</div>
				</div>
				<div class="cart-btn-box">
					<a href="javascript:pay()" id="submitOrderDivId"
						class="sn-btn sn-btn-assertive sn-btn-big">付款</a>
				</div>
			</div>
		</div>
	</section>
</body>
</html>