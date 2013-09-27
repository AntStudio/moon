<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    <%@ include file="../common/header.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<m:require src="jquery,bootstrap"></m:require>
<script type="text/javascript">
   $(function(){
	   $.ajax({
		   url:contextPath+"/time/save",
		   type:"post",
		   data:{
			   "timeStatistics.type":1
		   },
		   dataType:"json"
	   }).done(function(data){
		   console.log(data);
	   })
   });
</script>
<title>时间统计</title>
</head>
<body>
<div>
	<h3 style="text-align: center;">个人时间统计</h3>
 <button class="btn btn-primary btn-block btn-large">起床了</button>
 <button class="btn btn-primary btn-block btn-large">睡觉了</button>
</div>
</body>
</html>