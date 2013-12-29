<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
     <%@ include file="../common/header.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<m:require src="jquery,bootstrap"></m:require>
<style type="text/css">
	.height{
		height: 20%;
		margin-bottom: 10%;
	}
	
	.mobile button{
		font-size: 88px;
	}
</style>
<script type="text/javascript">
	$(function(){
		$("#getUp").click(function(){
			 $.ajax({
				 url:contextPath+"/blog/registration/save",
				 dataType:'json',
				 data:{
					 type:1
				 },
				 type:'Post'
			 }).done(function(data){
				 console.log(data);
			 });
		});
		$("#sleep").click(function(){
			 $.ajax({
				 url:contextPath+"/blog/registration/save",
				 dataType:'json',
				 data:{
					 type:2
				 },
				 type:'Post'
			 }).done(function(data){
				 console.log(data);
			 });
		});
	});
</script>
<title>每日签到页面</title>
</head>
<body>
	<div  class="mobile" style="height:100%;position: absolute;width: 80%;margin-left: 10%;margin-top: 20%;">
		<button type="button" id="getUp" class="btn btn-primary btn-block btn-large height" >起床了</button>
		<button type="button" id="sleep" class="btn btn-primary btn-block btn-large height">睡觉去了</button>
	</div>
</body>
</html>