<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
  <%@ include file="../common/header.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>修改密码</title>
<style type="text/css">
.form .label{
width: 100px;
display:inline-block;
}
</style>
<script type="text/javascript">
$(function(){
	$("#confirm").button();
	$("form").validate({errorMsg:{'required':'该项为必填项,请填写...<br/>'}});
	$("#confirm").click(function(){
		$.postData(contextPath+"/user/doChangePassword",{password:$("#newPassword").val()},
				function(){
			alert("密码修改成功.");
			$("form").reset();
		  },function(){
			  alert("密码修改出错.");
				$("form").reset();
			});
	});
});

function checkOldPassword(){
	var msg = "";
  $.postData(contextPath+"/user/matchOldPassword",{password:$("#oldPassword").val()},
			function(){
	  
  },function(){
		msg = "原密码不正确，请核对.<br/>";
	},function(){
		
	},false);
  return msg;
}


</script>
</head>
<body>
<c:if test="${info!=null}">
${info } 

</c:if>
 <c:if test="${info==null}">
<form class="form">
<div><span class="label">原密码：</span><input name="oldPassword" id="oldPassword" type="password" validate="validate[required,call(checkOldPassword)]"/></div>
<div><span class="label">新密码：</span><input name="newPassword" id="newPassword" type="password" validate="validate[required]"/></div>
<div><span class="label">确认密码：</span><input name="rePassword" type="password" validate="validate[required,eq(#newPassword)]"/></div>
<input type="button" value="确认" id="confirm" />
</form>
 </c:if>
 
</body>
</html>