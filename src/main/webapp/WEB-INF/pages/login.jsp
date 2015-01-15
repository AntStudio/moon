<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="m" uri="/moon"%>
<%@ include file="common/header.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<m:require src="jquery,common,ev,font,bootstrap,noty,{login}"/>
<script type="text/javascript">
	var from = "${from}";
</script>
<title>携心医疗</title>
</head>
<body class="body">

<div class="system-info-container">
	<div class="system-info">用户名:system_user</div>
    <div class="system-info">密码：<m:systemUserPassword/></div>
</div>


<div class="form-container">
   
   <div class="title-container">
       <span class="title">携心医疗</span>
   </div>
   <div class="split-line"></div>
   <form id="loginForm" class="loginForm">
     <div class="form-inline">
     	<i class="fa fa-user input-icon"></i>
     	<input type="text" name="userName" value="system_user" validate="validate[minsize(6),maxsize(15)]" errMsg="用户名为6~15个字符"/>
     </div>
     <div class="form-inline">
     	<i class="fa fa-lock input-icon"></i>
     	<input  name="password" type="password" validate="validate[minsize(6)]" errMsg="密码须6位以上"/>
     </div> 
     
    <input  type="button" id="submit" class="btn btn-default  btn-info" value="登   录"/>
</form>
  
</div>


<div class="footer">
    推荐使用Chrome浏览器以获取更好的体验,GavinCook提供技术支持,最新代码
    <a href="https://github.com/AntStudio/moon">地址</a>
</div>
</body>
</html>