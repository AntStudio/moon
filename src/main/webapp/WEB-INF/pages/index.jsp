<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<%@ include file="common/header.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
 <m:require src="jquery,bootstrap,common,js/pages/index.js"></m:require>
 <m:require src="css/base.css" type="css"></m:require>
<title>主页</title>

 
</head>
<body>
	<div class="navbar navbar-inverse" style="position: static;">
		<div class="navbar-inner">
			<div class="container">
				<a class="btn btn-navbar" data-toggle="collapse"
					data-target=".navbar-inverse-collapse"> <span class="icon-bar"></span>
					<span class="icon-bar"></span> <span class="icon-bar"></span>
				</a> <a class="brand" href="#">Moon</a>
				<div class="nav-collapse collapse navbar-inverse-collapse">
					<ul class="nav">
						<c:forEach items="${menus}" var="s">
							<li class="dropdown nav-menu" id="menu_${s.id}">
								<a href="#" class="dropdown-toggle" data-toggle="dropdown">
									${s.menuName}
									<b class="caret"></b>
								</a>
								<ul class="dropdown-menu">
									<li class="h-center"><i class="loading"></i></li>
								</ul>
							</li>
						</c:forEach>
					</ul>
					<ul class="nav pull-right">
						<li class="dropdown"><a href="#" class="dropdown-toggle"
							data-toggle="dropdown"> ${currentUser.userName} <b class="caret"></b></a>
							<ul class="dropdown-menu">
								<li><a href="${pageContext.request.contextPath}/user/loginOut">logout</a></li>
							</ul>
						</li>
					</ul>
				</div>
				<!-- /.nav-collapse -->
			</div>
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
		<iframe src="user" id="main" name="main"></iframe>
	</div>
</body>
</html>