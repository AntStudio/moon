<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="m" uri="/moon"%>
 <%@ include file="../common/header.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<m:require src="jquery,bootstrap,table,font"/>
<title>Table</title>
<script type="text/javascript">
	$(function(){
		$("#userTable").table({url:contextPath+"/permission/getPermissionData",
			columns:[{name:"id"},{name:"name"},{name:"code"}],
			formatData:function(data){return data.rows;}
		});
	});
</script>
</head>
<body>
	<div id="userTable"></div>
</body>
</html>