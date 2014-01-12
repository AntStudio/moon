  $(function(){
	  $("#logTable").table({
		  url: contextPath+'/log/getLogsData',
		  columns:[
		            {display: 'ID', name : 'id', width : "10%", sortable : true, align: 'center'},
		            {display: '操作人', name : 'user_name', width : "20%", sortable : true, align: 'center'},
		            {display: '操作信息', name : 'action', width :"30%", sortable : false, align: 'center'},
		            {display: '操作时间', name : 'time', width :"20%", sortable : false, align: 'center'},
		            {display: '操作', name : 'time', width :"5%", sortable : false, align: 'center',render:bindClickToAction}
		           ],
		  formatData : function(data){
									  return data.rows;
					  },
		  title:"日志列表",
		  showNumber:false
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
				title:"日志详情"
			});
	  });
	
  }