;
(function(){
	var setting = {
	        data: {  
	            simpleData: {  
	                enable: true  
	            }  
	        },  
			async: {
				enable: true,
				url:contextPath+"/menu/getSubMenus",
				type:"get",
				autoParam:["id=pid"],
				dataType:'json',
				dataFilter: filter
			},
			edit:{
				drag:{
					isMove:true,
					next:true,
					prev:true
				},
				enable:true,
				showRemoveBtn:false,
				showRenameBtn:false
			},
			callback: {
				onRightClick: function(event, treeId, treeNode){
					$.fn.zTree.getZTreeObj("menuTree").selectNode(treeNode);
					if ($.fn.zTree.getZTreeObj("menuTree").getSelectedNodes()[0]&&(treeNode!=null)){
					$("*:not('.ztree')").one("click",function(e){
						 $("#rmenu").css("display","none");
					});
					if(treeNode.urlPath){
						$(".preview").removeClass("hide");
					}else{
						$(".preview").addClass("hide");
					}
					$("#rmenu").css("left",event.pageX).css("top",event.pageY).css("display","inline");
					}
				},
				onDrop:function(event, treeId, treeNodes, targetNode, moveType, isCopy){
					if(moveType=="inner"){//如果是将菜单拖到目标节点的内部
						var array = new Array();
						$.each(targetNode.children,function(index,node){
							array.push(node.id);
						});
						sortMenu.call(this,targetNode.id,array);
					}else{
						var array = new Array();
						var parentNode = targetNode.getParentNode();
						$.each(parentNode.children,function(index,node){
							array.push(node.id);
						});
						sortMenu.call(this,parentNode.id,array);
					}
				}
			}

	}; 

	function filter(treeId, parentNode, childNodes) {
		if (!childNodes) {
			return null;
		}
		childNodes = childNodes.result;
		for (var i=0, l=childNodes.length; i<l; i++) {
			childNodes[i].name = (childNodes[i].menuName||'').replace(/\.n/g, '.');
			childNodes[i].isParent = true;
			
			//取消左键单击进行对应路径的打开，修改为右键预览
			if(childNodes[i].url){
				childNodes[i].urlPath = contextPath+childNodes[i].url;
			}
			childNodes[i].url = "";
				
		}
		return childNodes;
	}

/**
 * @param parentMenuId 父菜单id
 * @param childrenMenuIds 子菜单id数组
 */
function sortMenu(parentMenuId,childrenMenuIds){
	$.getJsonData(contextPath+"/menu/sort",
				  {
					parentMenuId:parentMenuId,
					childrenMenuIds:childrenMenuIds
				  }).done(function(data){
					  alert("成功");
				  });
}
var znodes = [{name:'菜单管理',id:-1,isParent:true}];


 var ztree;
$(document).ready(function(){
	$.fn.zTree.init($("#menuTree"), setting,znodes);
	 ztree = $.fn.zTree.getZTreeObj("menuTree");
	 
	 $(".add-menu").click(addMenu);
	 $(".edit-menu").click(editMenu);
	 $(".delete-menu").click(deleteMenu);
	 $(".preview-menu").click(preview);
});

function addMenu(){
	$("#menuForm").dialog({
		title:'添加菜单',
		buttons:[{
			text:'添加',
			css:"btn btn-primary",
			click:function(){
				var dialog = this;
				$.getJsonData(contextPath+"/menu/add",
							  $("#menuForm").formData({"menu.parentId":ztree.getSelectedNodes()[0].id}),
							  {type:"Post"}
				).done(function(){
					moon.success("菜单添加成功");
					dialog.close();
					$("#menuForm").reset();
					var nodes = ztree.getSelectedNodes();
					if (nodes[0].children) {
						ztree.reAsyncChildNodes(nodes[0], "refresh", true);
					} else {
						ztree.reAsyncChildNodes(nodes[0], "", true);
					}
					ztree.expandNode(nodes[0],true);
				});
			}
		},{
			text:'取消',
			css:"btn",
			click:function(){
				this.close();
				$("#menuForm").reset();
			}
		}]
	});
}

function editMenu(){
	var selectNode = ztree.getSelectedNodes()[0];
	if (selectNode.system) {
		moon.alert("对不起,系统菜单不能编辑");
		return false;
	}
	$(":input[name='menu.menuName']").val(selectNode.menuName);
	$(":input[name='menu.url']").val(selectNode.urlPath);
	$("#menuForm").dialog({
		title:'编辑菜单',
		buttons:[{
			text:'保存',
			css:"btn btn-primary",
			click:function(){
				$.getJsonData(contextPath+"/menu/update",
							  $("#menuForm").formData({"menu.id":selectNode.id}),
							  {type:"Post"}
				).done(function(data){
					if(data.success){
						moon.success("菜单编辑成功");
						$("#menuForm").dialog("close");
						$("#menuForm").reset();
						ztree.reAsyncChildNodes(selectNode.getParentNode(),"refresh",true);
						ztree.expandNode(selectNode.getParentNode(),true); 
					}
				});
				
			}
		},{
			text:'取消',
			css:"btn",
			click:function(){
				this.close();
			}
		}]
	});
}


function preview(){
	var selectNode = ztree.getSelectedNodes()[0];
	window.open(selectNode.urlPath,"_blank");
}
function deleteMenu(){
	if (ztree.getSelectedNodes()[0].system) {
		moon.alert("对不起,系统菜单不能删除");
		return false;
	}
	 if (confirm("确认删除该菜单?")) {
		 $.post(contextPath+"/menu/delete",{"menu.id":ztree.getSelectedNodes()[0].id},function(result){
			 var parentNode = ztree.getSelectedNodes()[0].getParentNode();
			 ztree.reAsyncChildNodes(parentNode,"refresh",true);
			 ztree.expandNode(parentNode,true); 
		 });
	}
}

})();