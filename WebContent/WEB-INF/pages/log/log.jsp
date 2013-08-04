<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    <%@ include file="../common/header.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>日志信息</title>
</head>
<body>
<script type="text/javascript">
  $(function(){
	grid = $("#logTable").flexigrid({
		url: contextPath+'/log/getLogsData',
		dataType: 'json',
		singleSelect:false,
		colModel : [
			{display: 'ID', name : 'id', width : "10%", sortable : true, align: 'center'},
			{display: '操作人', name : 'user_name', width : "20%", sortable : true, align: 'center'},
			{display: '操作信息', name : 'action', width :"30%", sortable : false, align: 'center'},
			{display: '操作时间', name : 'time', width :"20%", sortable : false, align: 'center'},
			{display: '操作', name : 'time', width :"5%", sortable : false, align: 'center',renderer:bindClickToAction}
			],
		sortname: "id",
		sortorder: "asc",
		usepager: true,
		title: '日志列表',
		useRp: true,
		rp: 15,
		showTableToggleBtn: true,
		height:  $(window).height()-123
	});   
	
});
 
  function bindClickToAction(row){
		return "<a href='javascript:void(0)' onclick='showDetail("+row.id+")'>详情</a>";
	}
 
  function showDetail(id){
	  $.postData(contextPath+"/log/getLogDetail",{id:id},function(result){
		  var html = "";
		  html+="<div><label>操作人：</label><span>"+result.log.user_name+"</span></div>";
		  html+="<div><label>操作时间：</label><span>"+result.log.time+"</span></div>";
		  html+="<div><label>详情：</label><span>"+result.log.detail+"</span></div>";
		  $("#content").html(html);
		  $("#logDetail").dialog({
				modal:true,
				title:"日志详情",
				width:500
			});
	  });
	
  }
</script>
<div id="logTable"></div>
<div id="logDetail" style="display:none;">
<div class="loading"></div>
<div id="content"></div>
</div>
</body>
</html>