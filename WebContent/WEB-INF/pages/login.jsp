<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="m" uri="/moon"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<m:require src="jquery,bootstrap,ev"/>
<title>Moon</title>
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
		
		$('#myModal').modal({backdrop:false});
		
		$('#myModal').on('shown', function () {
			  $(this).css("top","50%");
		});
		
		$("#loginForm").validate({align:'right',theme:"darkblue"});
	});
</script>
<style type="text/css">
 
 #myModal.modal{
	margin-top: -130px;
	top:-200px;
 }
.loginForm span{
	text-align: right;
	font-size: 20px;
	width:100px;
	display: inline-block;
}

.form-inline{
	margin-top:20px;
}
.modal-header{
	text-align: center;
}
</style>

</head>
<body style="background: url('${pageContext.request.contextPath}/css/images/login_bg.jpg')">

<div class="modal hide fade" id="myModal">
  <div class="modal-header">
    <h3>登&nbsp;&nbsp;录</h3>
  </div>
 
  <div class="modal-body">
   <form id="loginForm" class="loginForm">
   ${info}
    
     <div class="form-inline">
     	<span>用户名：</span>
     	<input type="text" name="user.userName" value="system_user" validate="validate[minsize(6),maxsize(15)]" errMsg="用户名为6~15个字符"/>
     </div>
     <div class="form-inline">
     	<span>密&nbsp;&nbsp;码：</span>
     	<input  name="user.password" type="password" value="system_user" validate="validate[minsize(6)]" errMsg="密码须6位以上"/>
     </div> 
</form>
</div>
  <div class="modal-footer">
    <input  type="button" id="submit" class="btn" value="登录"/>
	<input  type="button" id="reset" class="btn" value="重置"/>
	</div>
  
</div>
</body>
</html>