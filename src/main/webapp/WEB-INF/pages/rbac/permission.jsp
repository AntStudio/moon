<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<%@ include file="../common/header.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<m:require src="jquery,font,common,zt,js/ztree.extend.js,bootstrap,table,dialog,js/pages/rbac/permission.js"></m:require>
<title>权限管理</title>
</head>
<body style="margin:0">

	<!-- 权限列表 -->
     <div id="permissionTable" ></div> 
     
    <!-- 角色分配 -->
     <div id="roleTree" class="ztree">  </div>
    
</body>
</html>