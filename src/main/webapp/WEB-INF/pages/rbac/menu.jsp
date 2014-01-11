<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="../common/header.jsp" %>
     
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<m:require src="jquery,common,zt,js/ztree.extend.js,bootstrap,dialog,font,js/pages/rbac/menu.js"></m:require>
<title>菜单</title>
 
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

#menuForm input {
	width: 80%;
}

#menuForm {
	margin: 0px;
}
</style>
</head>
<body>
    <!-- 菜单树 -->
    <div id="menuTree" class="ztree">  </div>
    <%--右键菜单--%>
    <ul id="rmenu"  class="dropdown-menu rmenu">
	    <li><a href="#" onclick="addMenu()">    <i class="fa fa-plus"></i>添加菜单</a></li>
	    <li><a href="#" onclick="editMenu()">   <i class="fa fa-edit"></i>编辑菜单</a></li>
	    <li><a href="#" onclick="deleteMenu()"> <i class="fa fa-minus-square-o"></i>删除菜单</a></li>
    </ul>

		<%--菜单信息 --%>
		<form action="" id="menuForm" style="display: none;">
		 <div><span class="label-text">菜单名:</span><input type="text" name="menu.menuName"/></div>
		 <div><span class="label-text">菜单url:</span><input type="text" name="menu.url"/></div>
		</form>

    <!-- 角色树 -->
    <div id="roleTree" class="ztree"></div>
</body>
</html>