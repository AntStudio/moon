<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="../common/header.jsp" %>
     
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
 <script type="text/javascript" src="${pageContext.request.contextPath}/js/pages/rbac/menu.js"></script>
<title>菜单</title>
 
<style type="text/css">
.label{
width:80px;
text-align: right;
float:left;
}
</style>
</head>
<body>
     <!-- 菜单树 -->
     <div id="menuTree" class="ztree">  </div>
    <%--右键菜单,有jquery menu处理ui --%>
    <ul id="rmenu" style="width: 100px;position:absolute;display:none">
    <li><a href="#" onclick="addMenu()"><span class="ui-icon ui-icon-disk"></span>添加菜单</a></li>
    <li><a href="#" onclick="editMenu()"><span class="ui-icon ui-icon-zoomin"></span>编辑菜单</a></li>
    <li><a href="#" onclick="deleteMenu()"><span class="ui-icon ui-icon-zoomout"></span>删除菜单</a></li>
    <li><a href="#" onclick="assignRole()"><span class="ui-icon ui-icon-zoomin"></span>分配角色</a></li>
</ul>

<%--菜单信息 --%>

<form action="" id="menuForm" style="display: none;">
 <p><span class="label">菜单名:</span><input type="text" name="menu.menuName"/>
  <p><span class="label">菜单url:</span><input type="text" name="menu.url"/>
</form>

 <!-- 菜单树 -->
     <div id="roleTree" class="ztree">  </div>
</body>
</html>