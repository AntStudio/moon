var setting = {
        data: {  
            simpleData: {  
                enable: true  
            }  
        },  
		async: {
			enable: true,
			url:contextPath+"/menu/getSubMenus",
			autoParam:["id=pid"],
			dataType:'json',
			dataFilter: filter
		},
		callback: {
			onRightClick: function(event, treeId, treeNode){
				$.fn.zTree.getZTreeObj("menuTree").selectNode(treeNode);
				if ($.fn.zTree.getZTreeObj("menuTree").getSelectedNodes()[0]&&(treeNode!=null)){
				$("*:not('.ztree')").one("click",function(e){
					 $("#rmenu").css("display","none");
				});
				$("#rmenu").css("left",event.pageX).css("top",event.pageY).css("display","inline");
				}
			}
		}

}; 

var znodes = [{name:'菜单管理',id:-1,isParent:true}];

function filter(treeId, parentNode, childNodes) {
	if (!childNodes) 
		{
		return null;
		}
	for (var i=0, l=childNodes.length; i<l; i++) {
		childNodes[i].name = childNodes[i].menuName.replace(/\.n/g, '.');
		childNodes[i].isParent = true;
	}
	return childNodes;
}

 var ztree;
$(document).ready(function(){
	$("#rmenu").menu();
	$.fn.zTree.init($("#menuTree"), setting,znodes);
	 ztree = $.fn.zTree.getZTreeObj("menuTree");
});

function addMenu(){
	$("#menuForm").dialog({
		title:'添加菜单',
		modal:true,
		buttonAlign:'center',
		show:'flod',
		hide: "explode",
		beforeClose: function( event, ui ) {
			$("#menuForm").reset();
		},
		buttons:[{
			text:'添加',
			click:function(){
				$("#menuForm").ajaxSubmitForm(contextPath+"/menu/addMenu",{"menu.parentId":ztree.getSelectedNodes()[0].id},
						function(){
					alert("菜单添加成功");
					$("#menuForm").dialog("close");
					$("#menuForm").reset();
					var nodes = ztree.getSelectedNodes();
					if(nodes[0].children)
						ztree.reAsyncChildNodes(nodes[0],"refresh",true);
					else
					ztree.reAsyncChildNodes(nodes[0],"",true);
					ztree.expandNode(nodes[0],true);
				       },
				       function(){
					alert("菜单添加出错");
				        });
			}
		},{
			text:'取消',
			click:function(){
				$(this).dialog("close");
				$("#menuForm").reset();
			}
		}]
	});
}

function editMenu(){
	var selectNode = ztree.getSelectedNodes()[0];
	if(!selectNode.edit)
		{
		alert("对不起,系统菜单不能编辑");
		return false;
		}
	$(":input[name='menu.menuName']").val(selectNode.menuName);
	$(":input[name='menu.url']").val(selectNode.url);
	$("#menuForm").dialog({
		title:'编辑菜单',
		modal:true,
		buttonAlign:'center',
		show:'flod',
		hide: "explode",
		buttons:[{
			text:'添加',
			click:function(){
				$("#menuForm").ajaxSubmitForm(contextPath+"/menu/updateMenu",{"menu.id":selectNode.id},
						function(){
					alert("菜单编辑成功");
					$("#menuForm").dialog("close");
					$("#menuForm").reset();
					ztree.reAsyncChildNodes(selectNode.getParentNode(),"refresh",true);
					ztree.expandNode(selectNode.getParentNode(),true); 
				       },
				       function(){
					alert("菜单编辑出错");
				        });
			}
		},{
			text:'取消',
			click:function(){
				$(this).dialog("close");
			}
		}]
	});
}


function deleteMenu(){
	if(!ztree.getSelectedNodes()[0].edit)
	{
	alert("对不起,系统菜单不能删除");
	return false;
	}
	 if(confirm("确认删除该菜单?"))
			{
		 $.post(contextPath+"/menu/deleteMenu",{"menu.id":ztree.getSelectedNodes()[0].id},function(result){
			 ztree.reAsyncChildNodes(ztree.getSelectedNodes()[0].getParentNode(),"refresh",true);
				ztree.expandNode(ztree.getSelectedNodes()[0].getParentNode(),true); 

			  });
			}
		 
}




function assignRole(){
	alert("待开发....");
	return false;
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
				url:contextPath+"/role/getRoleData",
				autoParam:["id"],
				dataType:'json',
				otherParam:{rid:ztree.getSelectedNodes()[0].id},
				dataFilter: filter
			}

	}; 
	function filter(treeId, parentNode, childNodes) {
	if (!childNodes) 
		{
		return null;
		}
	for (var i=0, l=childNodes.length; i<l; i++) {
		childNodes[i].name = childNodes[i].roleName.replace(/\.n/g, '.');
		childNodes[i].isParent = true;
	}
	return childNodes;
}
	var menuNodes = [{name:'角色管理',id:-1,isParent:true,checked:true}];
	$.fn.zTree.init($("#roleTree"), menuTreeSetting,menuNodes);
	$("#roleTree").dialog({
		modal:true,
		title:'分配角色',
		width:500,
		buttonAlign:'center',
		show:'flod',
		hide: "explode",
		buttons:[{
			text:'保存',
			click:function(){
				var ids="",checkStatus="" ;
				$.each($.fn.zTree.getZTreeObj("menuTree").getChangeCheckedNodes(),function(index,e){
					ids+="ids="+e.id+"&";
					checkStatus+="checkStatus="+e.checked+"&";
				});
				alert(ids+checkStatus+"rid="+ztree.getSelectedNodes()[0].id);
				$.post(contextPath+"/menu/assignMenu",ids+checkStatus+"rid="+ztree.getSelectedNodes()[0].id,function(result){
					alert("成功");
				});
				
				
			}
		},{
			text:'取消',
			click:function(){
				$("#menuTree").dialog("close");
			}
			}
		 
		         ]
	});
}
