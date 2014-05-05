<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="../common/header.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<m:require src="jquery,noty,common,bootstrap,font"></m:require>
<title>Moon数据库维护</title>
<script type="text/javascript">
	$(function(){
		$(".init-db").click(function(){
			moon.confirm("确认要初始化数据库?").done(function(confirm){
				if(confirm){
					$(".loading").removeClass("hide");
					$.getJsonData(contextPath+"/dbMaintenance/initDb",{},{type:"Post"}).done(function(data){
						if(data){
							moon.info("数据库初始化完成");
						}else{
							moon.info("数据库初始化失败，详情请查看系统日志");
						}
						$(".loading").addClass("hide");
					});
				}
			});
		});
	});
</script>
<style type="text/css">
</style>
</head>
<body>
	 <div class="container">
	 	<div class="db-init">
	 		<input type="button" class="btn btn-danger init-db" value="初始化数据库">
	 		<i class="loading hide"></i>
	 		<span class="alert">注意:此操作会清空已有数据</span>
	 	</div>
	 </div>
</body>
</html>