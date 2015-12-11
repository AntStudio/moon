<%@ page language="java" contentType="text/html; charset=utf-8"
         pageEncoding="utf-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="m" uri="/moon"%>

<!DOCTYPE html>
<m:optimizeStructure>
<html>
    <head>
        <%@ include file="common/header.jsp" %>
        <m:require src="jquery,moonTheme,font,bootstrap,common,kt,{{index}}"/>
        <title>${currentMenu.menuName}</title>
    </head>
    <body class="index ${theme}">
        <%@ include file="common/nav.jsp" %>
        <section class="content-section">
            <h3 class="header-title">${currentMenu.menuName}</h3>
            <jsp:include page="${content.substring(content.indexOf('pages')+6)}.jsp"/>
        </section>
    </body>
</html>
</m:optimizeStructure>
