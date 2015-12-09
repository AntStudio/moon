<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<%@ include file="common/header.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
 <m:require src="jquery,noty,huanxin,font,bootstrap,common,kt,{{index}}"></m:require>
 <m:require src="css/base.css" type="css"></m:require>
<title>主页</title>


</head>
<body class="index">
<c:set var="ss" value="log/log.jsp"></c:set>
	<div class="navbar navbar-inverse nav-header">
		<div class="container-fluid">
			<div class="navbar-header">
				<a class="navbar-toggle collapsed" data-toggle="collapse"
					data-target=".navbar-inverse-collapse">
                    <span class="icon-bar"></span>
					<span class="icon-bar"></span>
                    <span class="icon-bar"></span>
				</a> <a class="navbar-brand" href="#">Moon</a>
            </div>

            <div class="collapse navbar-collapse navbar-inverse-collapse">
                <ul class="nav navbar-nav">
                    <c:forEach items="${menus}" var="s">
                        <li class="dropdown nav-menu" id="menu_${s.id}">
                            <a href="#" class="dropdown-toggle" data-toggle="dropdown">
                                ${s.menuName}
                                <b class="caret"></b>
                            </a>
                            <ul class="dropdown-menu">
                                <li class="h-center"><i class="loading fa fa-spinner fa-spin"></i></li>
                            </ul>
                        </li>
                    </c:forEach>
                </ul>

                <span class="app-version">
                    版本号: 1.0.0
                </span>
                <ul class="nav navbar-nav navbar-right">
                    <li class="dropdown"><a href="#" class="dropdown-toggle"
                        data-toggle="dropdown"> ${currentUser.userName} <b class="caret"></b></a>
                        <ul class="dropdown-menu">
                            <li><a class="no-tab" href="${pageContext.request.contextPath}/user/loginOut">logout</a></li>
                        </ul>
                    </li>
                </ul>
            </div>
				<!-- /.nav-collapse -->
		</div>
		<!-- /navbar-inner -->
	</div>
	<%-- <div class="mainContainer">
		<div class="mainIframeContainer  fade in left ">
			<iframe id="main" class="mainIframe" src="${pageContext.request.contextPath}/user"></iframe>
		</div>
		<div class="mainIframeContainer  fade in right current" >
			<iframe id="main" class="mainIframe" src="${pageContext.request.contextPath}/user"></iframe>
		</div>
	</div> --%>
	<div class="main-container">
		<%--<iframe src="user" id="main" name="main" allowfullscreen="true"></iframe>--%>
        <div class="tab">
            <h1>携心医疗</h1>
            <div>欢迎使用携心医疗，请选择顶部菜单进行操作.</div>
        </div>
	</div>

</body>
</html>