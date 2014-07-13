<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<%@ include file="../common/header.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<m:require src="jquery,noty,common,bootstrap,zt,js/ztree.extend.js,font,dialog,table,js/pages/rbac/role.js"></m:require>
<title>角色管理</title>
 <style type="text/css">
.rmenu {
	position: absolute;
	display: none;
}

.rmenu i {
	color: #44A1CC;
	margin-right: 10px;
}

.label-text {
	width: 70px;
	display: inline-block;
}

</style>
 
</head>
<body>
<!-- 角色树 -->
   <div id="roleTree" class="ztree"></div>
   <!-- 右键菜单 -->
<ul id="rmenu"  class="dropdown-menu rmenu">
    <li><a href="#" onclick="addRole()"><i class="fa fa-plus"></i>添加角色</a></li>
    <li><a href="#" onclick="editRole()"><i class="fa fa-edit"></i>编辑角色</a></li>
    <li><a href="#" onclick="deleteRole()"><i class="fa fa-minus-square-o"></i>删除角色</a></li>
    <li><a href="#" onclick="assignMenu()"><i class="fa fa-share"></i>分配菜单</a></li>
    <li><a href="#" onclick="assignPermission()"><i class="fa fa-share"></i>分配权限</a></li>
</ul>
   
   <!-- 创建角色表单 -->
   <form action="" id="roleForm" style="display:none;">
    <p><span class="label-text">角色名:</span><input type="text" name="role.roleName"/>
   </form>
   <!-- 菜单树 -->
   <div id="menuTree" class="ztree"></div>
   
   <!-- 权限列表 -->
    <div id="permissionTable" style="display:none;"></div> 
</body>
</html>