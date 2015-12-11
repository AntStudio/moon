<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ include file="common/tags.jsp"%>
<html>
    <m:optimizeStructure>
    <head>
        <title>${currentMenu.menuName}</title>
    </head>
    <body>
        <jsp:include page="${content.substring(content.indexOf('pages')+6)}.jsp"/>
    </body>
    </m:optimizeStructure>
</html>