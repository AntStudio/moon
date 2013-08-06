<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="m" uri="/moon"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
 <script type="text/javascript" src="${pageContext.request.contextPath}/jquery/jquery-1.8.3.js"></script>
 <script type="text/javascript" src="${pageContext.request.contextPath}/jquery/ui/jquery-ui-1.9.2.custom.js"></script>
 <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/jquery/ui/css/ext/jquery-ui-1.9.2.ext.min.css"/>
<title>电力双视监控</title>
<script>
//***********************************************<公用方法(提取到公用js文件中)>*******************************************************

/**
 * reset all the field of form,call like $("#loginForm").reset();
 */
$.fn.reset = function(){
	$(':input',this)  
	 .not(':button, :submit, :reset, :hidden')  
	 .val('')  
	 .removeAttr('checked')  
	 .removeAttr('selected'); 
};
 
 /**
  * ajax submit form,use like this:
  *	<p>  $("#loginForm").ajaxSubmitForm("login/validate",
  *			 function(result) {
  *		        // todo the code when success
  *	             }, 
  *	         function(result) {
  *		        // todo the code when failure
  *	        });
  *</p>
  * @param url        : the form submit url
  * @param successFun : when ajax submit form success,also the response message 
  *                     is success(means:the success propertity of responesText is true),
  *                     call the successFun with the responesText parameter
  * @param failureFun : if not call the successFun,then call the failureFun with responesText parameter 
  */
 $.fn.ajaxSubmitForm = function(url,successFun,failureFun,errorFun){
		$.ajax({
			url:url,
			data:$(this).serialize(),
			type:'post',
			dataType:'json',
			success:function(result){
				if(result.success){
					successFun(result);
				}
				else
					failureFun(result);
			},
			error:function(XMLHttpRequest, textStatus, errorThrown){
				errorFun(XMLHttpRequest, textStatus, errorThrown);
			}
		});
 };
 $.href=function(url,type){
	 if(typeof(type)=="undefined"||type=='current')
	  window.location.href=url;
	 else
		 if(type=="new")
			 window.open(url);
 };
//***********************************************</公用方法(提取到公用js文件中)>*******************************************************
 
$(function(){
	$( "input[type='button']" ).button();
	$("#submit").click(function(){
		
	$("#loginForm").ajaxSubmitForm("${pageContext.request.contextPath}/user/login/validate",
			function(result) {
		if("${from}")
			$.href("${from}");
		else
			$.href("${pageContext.request.contextPath}/index");
			}, 
			function(result) {
				alert(result.success);
			});
		
		});

		$("#reset").click(function() {
			$("#loginForm").reset();
		});
	});
</script>
<style type="text/css">
.loginForm {
 float:left;
 width:300px;
}
.loginForm span{
text-align: right;
font-size: 20px;
}
.loginForm div{
padding-left: 50px;
}
.loginForm .btn{
margin-left: 10px;
}
</style>
<m:require src="jquery,bootstrap"/>
<m:require src="bootstrap" type="css"/>
</head>
<body style="background: url('${pageContext.request.contextPath}/css/images/login_bg.jpg')">
    <div style="margin-top:200px;margin-left:auto;margin-right:auto;width:300px; height:200px;">
    ${info}
    <form id="loginForm" class="loginForm">
       <p> <span>用户名：</span><input type="text" name="user.userName" value="system_user"/> </p>
     <p>  <span>密&nbsp;&nbsp;码：</span><input  name="user.password" type="password" value="system_user"/> <span class="errorImg"></span><span
						class="errorMsg"></span>  </p>
						<div><input  type="button" id="submit" class="btn" value="登录"/>
						<input  type="button" id="reset" class="btn" value="重置"/></div>
     </form>
     </div>
</body>
</html>