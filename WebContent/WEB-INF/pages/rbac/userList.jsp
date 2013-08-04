<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<%@ include file="../common/header.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<script type="text/javascript" src="${pageContext.request.contextPath}/js/pages/rbac/userList.js"></script>
<title>用户管理</title>

<style type="text/css">
.label{
width:80px;
text-align: right;
float:left;
}
</style>
</head>
<body style="margin:0;">
<!-- 用户列表 -->
     <table id="userTable"></table> 
     
     <!-- 用户信息表单 -->
     <form action="" style="display:none;" id="userForm">
     <p><span class="label">用户名:</span><input type="text" name="user.userName"/>
       <p><span class="label">密&nbsp;&nbsp;码:</span><input type="password" name="user.password">
      <p> <span class="label">重复密码:</span><input type="password" name="repassword">
      <p><span class="label">真实姓名:</span><input type="text" name="user.realName"/>
     </form>
     <!-- 角色分配 -->
     <div id="roleTree" class="ztree" style="display:none;">  </div>
    
</body>
</html>