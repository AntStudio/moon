<%@ include file="tags.jsp" %>
<!--moon required resources start-->
<script type="text/javascript">
    <%-- 上下文路径--%>
    var contextPath = "${pageContext.request.contextPath}";
    var fullServerPath = "${pageContext.request.requestURL.toString().replace(pageContext.request.requestURI.replaceFirst(pageContext.request.contextPath,""),"")}";
</script>
<!--moon required resources end-->