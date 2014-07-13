var ztree;
var setting = {
        data: {  
            simpleData: {  
                enable: true  
            }  
        },  
		async: {
			enable: true,
			url:contextPath+"/role/getSubRoles",
			autoParam:["id"],
			dataType:'json',
			type:"Get",
			dataFilter: filter,
			otherParam:{"_random":Math.random()}
		},
		callback: {
			onRightClick: function(event, treeId, treeNode){
				$.fn.zTree.getZTreeObj("roleTree").selectNode(treeNode);
				if ($.fn.zTree.getZTreeObj("roleTree").getSelectedNodes()[0]){
					$("*:not('.ztree')").one("click",function(e){
						 $("#rmenu").css("display","none");
					});
					$("#rmenu").css("left",event.pageX).css("top",event.pageY).css("display","inline");
				}
				else{
					$("#rmenu").css("display","none");
				}
				
			}
		}

}; 

var znodes = [{name:'角色管理',id:-1,isParent:true}];

function filter(treeId, parentNode, childNodes) {

	if (!childNodes) {
		return null;
	}
	childNodes = childNodes.result;
	for (var i = 0, l = childNodes.length; i < l; i++) {
		childNodes[i].name = childNodes[i].roleName.replace(/\.n/g, '.');
		childNodes[i].isParent = true;
	}
	return childNodes;
}
 
 function closeRMenu(){
	
		 $("#rmenu").css("display","none");
		 $("*").unbind("mousedown");
 }
 
$(function(){
	$.fn.zTree.init($("#roleTree"), setting,znodes);
	 ztree = $.fn.zTree.getZTreeObj("roleTree");
});

function addRole(){
	$("#roleForm").dialog({
		title:'添加角色',
		buttons:[{
			text:'添加',
			css:"btn btn-primary",
			click:function(){
				$.getJsonData(contextPath+"/role/add",
						$("#roleForm").formData({"role.parentId":$.fn.zTree.getZTreeObj("roleTree").getSelectedNodes()[0].id}),
						{type:"Post"}
				).done(function(data){
					if(data.success){
						moon.success("角色添加成功");
						$("#roleForm").dialog("close");
						$("#roleForm").reset();
						var nodes = ztree.getSelectedNodes();
						ztree.reAsyncChildNodes(nodes[0],"",true);
						ztree.expandNode(nodes[0],true);
					}
				});
			}
		},{
			text:'取消',
			css:"btn",
			click:function(){
				$("#roleForm").dialog("close");
				$("#roleForm").reset();
			}
			}
		 
		         ]
	});
}

function deleteRole(){
	if(ztree.getSelectedNodes().length>=1){
		if(confirm("确认要删除该角色吗?")){
			 var parentNode = ztree.getSelectedNodes()[0].getParentNode()
			 $.post(contextPath+"/role/logicDelete",{ids:ztree.getSelectedNodes()[0].id},function(result){
				 ztree.reAsyncChildNodes(parentNode,"refresh",true);
			     ztree.expandNode(parentNode,true); 
			 });
		}
	}
}

function editRole(){
	$(":input[name='role.roleName']","#roleForm").val($.fn.zTree.getZTreeObj("roleTree").getSelectedNodes()[0].name);
	$("#roleForm").dialog({
		title:'编辑角色',
		buttons:[{
					text:'保存',
					css:"btn btn-primary",
					click:function(){
						$.getJsonData(contextPath+"/role/update",
									  $("#roleForm").formData({"role.id":$.fn.zTree.getZTreeObj("roleTree").getSelectedNodes()[0].id}),
									  {type:"Post"}
						).done(function(data){
							if(data.success){
								moon.success("角色编辑成功");
								$("#roleForm").dialog("close");
								var nodes = ztree.getSelectedNodes();
								nodes[0].name = $(":input[name='role.roleName']","#roleForm").val();
								ztree.updateNode(nodes[0]);
								$("#roleForm").reset();
							}
						});
						
					}
				},
				{
					text:'取消',
					css:"btn",
					click:function(){
						$("#roleForm").dialog("close");
						$("#roleForm").reset();
					}
				}
			  ]
	});  
}

function assignMenu(){
	var menuTreeSetting = {
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
				url:contextPath+"/menu/getAssignMenuData",
				autoParam:["id=pid"],
				dataType:'json',
				type:"Get",
				otherParam:{rid:ztree.getSelectedNodes()[0].id},
				dataFilter: filter
			}

	}; 
	function filter(treeId, parentNode, childNodes) {
		if (!childNodes) {
			return null;
		}
		childNodes = childNodes.result;
		for (var i=0, l=childNodes.length; i<l; i++) {
			childNodes[i].name = childNodes[i].menuName.replace(/\.n/g, '.')+"   路径："+childNodes[i].url;
			childNodes[i].url = "";
			childNodes[i].isParent = true;
		}
		return childNodes;
	}
	
	var menuNodes = [{name:'菜单管理',id:-1,isParent:true,checked:true}];
	$.fn.zTree.init($("#menuTree"), menuTreeSetting,menuNodes);
	$("#menuTree").dialog({
		title:'分配菜单',
		buttons:[
		         {
					text:'保存',
					css:"btn btn-primary",
					click:function(){
						var ids="",checkStatus="" ;
						$.each($.fn.zTree.getZTreeObj("menuTree").getChangeCheckedNodes(),function(index,e){
							ids+="ids="+e.id+"&";
							checkStatus+="checkStatus="+e.checked+"&";
						});
						$.post(contextPath+"/menu/assignMenu",ids+checkStatus+"rid="+ztree.getSelectedNodes()[0].id,function(result){
							alert("菜单分配成功");
							$("#menuTree").dialog("close");
						});
					}
		         },
		         {
					text:'取消',
					css:"btn",
					click:function(){
						$("#menuTree").dialog("close");
					}
		         }
                ]
	});
}

function assignPermission(){
	var rid = ztree.getSelectedNodes()[0].id;
	if(!rid){
		return false;
	}
	var grid = $("#permissionTable").table({
		url: contextPath+'/permission/getPermissionDataByRole?rid='+rid,
		multiSelect:true,
		columns : [
					{display: 'ID', name : 'id', width : 20, sort : false, align: 'center'},
					{display: '权限代码', name : 'code', width : 150, sort : false, align: 'center'},
					{display: '权限名称', name : 'name', width :250, sort : false, align: 'center'}
				   ],
		title: '权限列表',
		formatData:function(data){
			 return data.result;
		}
	});   
	$("#permissionTable").dialog({
		title:'分配权限',
		width:480,
		buttons:[
		         {
					text:'确定',
					css:"btn btn-primary",
					click:function(){
						var dialog = this;
						var ids = "",status="";
						$(grid.getChangedRows()).each(function(index,e){
							ids +="pids="+e.id+"&";
							status += "status="+(e.checked||false)+"&";
						});
						if(ids!=""){
						  $.post(contextPath+"/permission/assignPermission",ids+status+"rids="+ztree.getSelectedNodes()[0].id,function(result){
								alert("权限分配成功");
								dialog.close();
						  });  
						}
					}
		         },
		         {
					text:'取消',
					css:"btn",
					click:function(){
						$(this).dialog("close");
					}
		         }
		        ]
	});
}
