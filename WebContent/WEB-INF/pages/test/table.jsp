<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="m" uri="/moon"%>
 <%@ include file="../common/header.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<m:require src="jquery,bootstrap,table,font"/>
<title>Table</title>
<script type="text/javascript">
var topY,bottomY;
	$(function(){
		$("#userTable").table({url:contextPath+"/permission/getPermissionData",
			columns:[{name:"id"},{name:"name"},{name:"code"}],
			formatData:function(data){return data.rows;}
		});
		var mouseDown = false;
		$("#userTable").mousedown(function(event){
			mouseDown = true;
			var x = event.clientX,y=event.clientY;
			 $(this).mousemove(function(e){
				 if(mouseDown){
					 $("#area").removeClass("hide");
					var ex = e.clientX,ey=e.clientY;
					$("#area").css({
						top:ey>y?y:ey,
					    left:ex>x?x:ex,
					    width:Math.abs(ex-x),
					    height:Math.abs(ey-y)
					});
					topY = ey>y?y:ey;
					bottomY = topY+Math.abs(ey-y);
					select();
					e.preventDefault();
				 }
			 });
			 $("#area").mousemove(function(e1){
				 	
				 if(mouseDown){
					 $("#area").removeClass("hide");
					var ex1 = e1.clientX,ey1=e1.clientY;
					$("#area").css({
						top: (ey1>y?y:ey1),
					    left: (ex1>x?x:ex1),
					    width:Math.abs(ex1-x),
					    height:Math.abs(ey1-y)
					});
					topY = ey1>y?y:ey1;
					bottomY = topY+Math.abs(ey1-y);
					select();
					e1.preventDefault();
				 }
				 });
			 event.preventDefault();
		});
		 $("#area").mouseup(function(){
			 $("#area").css({width:0,height:0}).addClass("hide");
			 mouseDown = false;
		 });

		$("#userTable").mouseup(function(){
			console.log("userTable====mouseup");
			$(this).unbind("mousemove");
			$("#area").css({width:0});
			mouseDown = false;
		});
	});
	
	function select(){
		$("#userTable tr").each(function(index,e){
			if($(e).offset().top+$(e).height()>=topY&&$(e).offset().top<=bottomY){
				$(e).addClass("selected");
			}else{
				$(e).removeClass("selected");
			}
		});
	}
</script>
</head>
<body>
	<div id="userTable"></div>
	<div style="position: absolute;border: 1px dashed #91B4F1;background:rgba(185, 213, 241, 0.7);" id="area"></div>
</body>
</html>