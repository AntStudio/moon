<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<%@ include file="../common/header.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<script type="text/javascript" src="${pageContext.request.contextPath}/js/pages/rbac/permission.js"></script>
<title>权限管理</title>
 
<style type="text/css">
.label{
width:80px;
text-align: right;
float:left;
}
</style>
</head>
<body style="margin:0">
<!-- 权限列表 -->
     <table id="permissionTable" ></table> 
     
    <!-- 角色分配 -->
     <div id="roleTree" class="ztree">  </div>
    
</body>
</html>