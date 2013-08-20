<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<%@ include file="../common/header.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<m:require src="jquery,zt,js/ztree.extend.js,jqueryui,flexgrid,js/pages/rbac/role.js"></m:require>
<title>角色管理</title>
 <style type="text/css">
.label{
width:80px;
text-align: right;
float:left;
}
</style>
 
</head>
<body>
<!-- 角色树 -->
   <div id="roleTree" class="ztree"></div>
   <!-- 右键菜单 -->
<ul id="rmenu" style="width: 100px;position:absolute;display:none">
    <li><a href="#" onclick="addRole()"><span class="ui-icon ui-icon-disk"></span>添加角色</a></li>
    <li><a href="#" onclick="editRole()"><span class="ui-icon ui-icon-zoomin"></span>编辑角色</a></li>
    <li><a href="#" onclick="deleteRole()"><span class="ui-icon ui-icon-zoomout"></span>删除角色</a></li>
    <li><a href="#" onclick="assignMenu()"><span class="ui-icon ui-icon-zoomin"></span>分配菜单</a></li>
    <li><a href="#" onclick="assignPermission()"><span class="ui-icon ui-icon-zoomin"></span>分配权限</a></li>
</ul>
   
   <!-- 创建角色表单 -->
   <form action="" id="roleForm" style="display:none;">
    <p><span class="label">角色名:</span><input type="text" name="role.roleName"/>
   </form>
   <!-- 菜单树 -->
   <div id="menuTree" class="ztree"></div>
   
   <!-- 权限列表 -->
    <table id="permissionTable" style="display:none;"></table> 
</body>
</html>