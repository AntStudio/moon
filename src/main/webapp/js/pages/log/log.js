  $(function(){
	  $("#logTable").table({
		  url: contextPath+'/log/list',
		  columns:[
		            {display: 'ID', name : 'id', width : "10%", sortable : true, align: 'center'},
		            {display: '操作人', name : 'userName', width : "20%", sortable : true, align: 'center'},
		            {display: '操作信息', name : 'action', width :"30%", sortable : false, align: 'center'},
		            {display: '操作时间', name : 'time', width :"20%", sortable : false, align: 'center'},
		            {display: '操作', name : 'time', width :"5%", sortable : false, align: 'center',render:bindClickToAction}
		           ],
		  formatData : function(data){
					  	 return data.result;
					  },
		  title:"日志列表",
		  showNumber:false
	  });
	
});
 
  function bindClickToAction(row){
		return "<a href='javascript:void(0)' onclick='showDetail("+row.id+")'>详情</a>";
	}
 
  function showDetail(id){
	  $.getJsonData(contextPath+"/log/get/"+id).done(function(data){
		  var log = data.result;
		  var html = "";
		  html+="<div><label>操作人：</label><span>"+log.user_name+"</span></div>";
		  html+="<div><label>操作时间：</label><span>"+log.time+"</span></div>";
		  html+="<div><label>详情：</label><span>"+log.detail+"</span></div>";
		  $("#content").html(html);
		  $("#logDetail").dialog({
				title:"日志详情"
			});
	  });
  }