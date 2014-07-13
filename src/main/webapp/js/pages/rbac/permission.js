$(function(){
	$("#permissionTable").table({
		url: contextPath+'/permission/getPermissionData',
		columns : [
			{display: 'ID', name : 'id', align: 'center'},
			{display: '权限代码', name : 'code',align: 'center'},
			{display: '权限名称', name : 'name', align: 'center'}
			
			],
		buttons : [
			{text: '分配权限',name:"assignBtn",click : btnHandler}
			],
		title: '权限列表',
		formatData:function(data){return data.result;}
	});   
	
});

 
	
var pid ;
var ztree;
function btnHandler(btn){
	var name = btn.name;
	var table = this;
	
	if(name=="assignBtn"){
		var selectRows = table.getSelect();
		if(selectRows.length!=1){
			alert("请选择一个权限进行操作.");
			return false;
		}
		pid = selectRows[0].id;
		showRoleTree(pid);
		   $("#roleTree").dialog({
			   title:'分配权限给角色',
			   buttons:[{
				   text:'确定',
				   css:"btn btn-primary",
				   click:function(){
					   var ids="",checkStatus="" ;var dialog = this;
						$.each(ztree.getChangeCheckedNodes(),function(index,e){
							ids+="rids="+e.id+"&";
							checkStatus+="status="+e.checked+"&";
						});
						$.post(contextPath+"/permission/assignPermission",ids+checkStatus+"pids="+pid,function(result){
							alert("成功");
							dialog.close();
						});
				   }
			   },{
				   text:'取消',
				   css:"btn btn-default",
				   click:function(){
					   ztree.selectNode(ztree.getNodeByParam("uid",2));
					   this.close();
				   }
			   }]
		   });
	}
};

function showRoleTree(pid){
var setting = {
        data: {  
            simpleData: {  
                enable: true  
            }  
        },
        check:{
        	enable:true,
        	chkStyle:'checkbox'

        },
		async: {
			enable: true,
			url:contextPath+"/role/getRoleDataByPermission",
			autoParam:["id=rid"],
			dataType:'json',
			type:"Get",
			otherParam:{pid:pid},
			dataFilter: filter
		}
}; 
		
var znodes = [ {
		name : '角色管理',
		id : -1,
		isParent : true
	} ];

	function filter(treeId, parentNode, childNodes) {
		if (!childNodes) {
			return null;
		}
		childNodes = childNodes.result;
		for ( var i = 0, l = childNodes.length; i < l; i++) {
			childNodes[i].name = childNodes[i].roleName.replace(/\.n/g, '.');
			childNodes[i].isParent = true;
			childNodes[i].uid = childNodes[i].id;
		}
		return childNodes;
	}

	$.fn.zTree.init($("#roleTree"), setting, znodes);
	ztree = $.fn.zTree.getZTreeObj("roleTree");

}
