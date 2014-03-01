<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="m" uri="/moon"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<m:require src="jquery,common,ev,font,bootstrap,noty"/>
<title>Moon</title>
<script>
$(function(){
	moon.notify("Welcome to Moon!");
	$("#submit").click(function(){
		$("form").validate("validate").done(function(result){
			if(result){
				$("#loginForm").ajaxSubmitForm("${pageContext.request.contextPath}/user/login/validate",{},
						function(result) {
						if("${from}")
							$.href("${from}");
						else
							$.href("${pageContext.request.contextPath}/index");
							}, 
							function(result) {
								moon.error("用户名或密码错误");
							});
			}
		});
		
	});

		$("#reset").click(function() {
			$("#loginForm").reset();
		});
		
		$(".form-container").animate({
			"margin-top":"-120px"
		},2000,"linear",function(){
			$(".system-info-container").animate({
				"margin-left":0
			});
		});
		$("#loginForm").validate({align:'bottom',theme:"darkblue"});
	});
</script>
<style type="text/css">

.system-info-container{
	padding-top:30px;
	color:#C4BDC3;
	margin-left:-150%;
}
.system-info {
	text-align: center;
	font-size: 20px;
	padding:10px;
}

  input[type="text"],input[type="password"]{
  	background: url('${pageContext.request.contextPath}/css/images/input.png');
  	border: 0;
	outline: none;
	height: 40px;
	width: 330px;
	line-height: 30px;
	text-align: center;
	font-size:25px;
	color:#FFFFFF;
	padding-left: 35px;
  }
  
  .bg-image{
  	position: absolute;
	z-index: -1;
	width: 100%;
	height: 100%;
  }
  
  .body{
  	margin:0;
  	background: url("${pageContext.request.contextPath}/css/images/bg.png");
  }
  
  .form-inline{
  	margin-bottom: 20px;
  	display: inline-block;;
  }
  
  .form-container{
  	text-align: center;
  	position: absolute;
	top: 50%;
	/* margin-top: -120px; */
	margin-top:-50%;
	left: 50%;
	margin-left: -430px;
  }
  
  .input-icon{
  	color: #FFFFFF;
	font-size: 25px;
	margin-right: -40px;
  }
  
  input[type="button"]{
  	width: 90px;
	font-size: 20px;
	height: 46px;
	margin-left: -11px;
  }
  
  #submit{
  	margin-right:25px;
  }
  
  .title{
  	color: white;
	font-size: 70px;
	font-family: cursive;
  }
  
  .sub-title{
  	color: #816944;
	font-size: 25px;
	margin-left:20px;
  }
  
  .split-line{
  	border: solid 1px rgba(159, 185, 231, 0.08);
	margin: 40px 0;
	width: 120%;
	display: inline-block;
	margin-left: -10%;
  }
</style>
  
</head>
<body class="body">

<div class="system-info-container">
	<div class="system-info">用户名:System_user</div>
    <div class="system-info">密码：<m:systemUserPassword/></div>
</div>

<%-- 
 	 <div id="msg" class="alert alert-error  <c:choose>
   <c:when test="${info}">   
   </c:when>
   <c:otherwise>  hide
   </c:otherwise>
</c:choose>">${info}</div> --%>

<div class="form-container">
   <%-- <div style="margin-bottom:20px;"><img src="${pageContext.request.contextPath}/css/images/title.png"/></div> --%>
   
   <div class="title-container">
   	<span class="title">Moon</span>
   	<span class="sub-title">Web Platform</span>
   </div>
   <div class="split-line"></div>
   <form id="loginForm" class="loginForm">
     <div class="form-inline">
     	<i class="fa fa-user input-icon"></i>
     	<input type="text" name="user.userName" value="system_user" validate="validate[minsize(6),maxsize(15)]" errMsg="用户名为6~15个字符"/>
     </div>
     <div class="form-inline">
     	<i class="fa fa-lock input-icon"></i>
     	<input  name="user.password" type="password" validate="validate[minsize(6)]" errMsg="密码须6位以上"/>
     </div> 
     
    <input  type="button" id="submit" class="btn btn-default  btn-info" value="登   录"/>
</form>
  
</div>
</body>
</html>