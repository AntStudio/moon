<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="../common/header.jsp" %>
     
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<m:require src="jquery,noty,common,zt,js/ztree.extend.js,bootstrap,dialog,font,{rbac/menu}"></m:require>
<title>菜单</title>
 
</head>
<body>
    <!-- 菜单树 -->
    <div id="menuTree" class="ztree">  </div>
    <%--右键菜单--%>
    <ul id="rmenu"  class="dropdown-menu rmenu">
	    <li><a href="#" class="add-menu">    <i class="fa fa-plus"></i>添加菜单</a></li>
	    <li><a href="#" class="edit-menu">   <i class="fa fa-edit"></i>编辑菜单</a></li>
	    <li><a href="#" class="delete-menu"> <i class="fa fa-minus-square-o"></i>删除菜单</a></li>
	    <li class="preview hide"><a href="#" class="preview-menu"> <i class="fa fa-indent"></i>预览</a></li>
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