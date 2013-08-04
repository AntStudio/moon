
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<%--引入核心js和css --%>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<script type="text/javascript">
     <%-- 上下文路径--%>
     var contextPath = "${pageContext.request.contextPath}";
</script>
<script type="text/javascript" src="${pageContext.request.contextPath}/jquery/jquery-1.8.3.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/jquery/ui/jquery-ui-1.9.2.custom.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery.extend.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/jquery/ui/flexigrid/flexigrid.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/jquery/ui/ztree/jquery.ztree.all-3.5.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/js/ztree.extend.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/js/validate/jquery.formValidation-1.0.js"></script>
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/jquery/ui/css/ext/jquery-ui-1.9.2.ext.min.css"/>
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/jquery/ui/flexigrid/css/flexigrid.css"/>
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/jquery/ui/ztree/css/zTreeStyle.css"/>
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/icon/icon.css"/>
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/js/validate/css/tip-darkgray.css"/>
</head>
</html>