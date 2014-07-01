<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="../common/header.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<m:require
	src="jquery,zt,js/ztree.extend.js,common,bootstrap,table,font,dialog,js/pages/log/log.js"></m:require>
<title>日志信息</title>
</head>
<body>
	<div id="logTable"></div>
	<div id="logDetail" style="display: none;">
		<div id="content"></div>
	</div>
</body>
</html>