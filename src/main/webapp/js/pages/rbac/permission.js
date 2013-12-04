
$.fn.reset = function(){
	$(':input',this)  
	 .not(':button, :submit, :reset, :hidden')  
	 .val('')  
	 .removeAttr('checked')  
	 .removeAttr('selected'); 
};
$.serializeToUrl=function(o,prefix){
	if(typeof(prefix)=='undefined'){
		prefix = "";
	}
    for(var i in o){
    	prefix+=i+"="+o[i]+"&";
    }
	
    return prefix.substring(0,prefix.length-1);
}
 /**
  * ajax submit form,use like this:
  *	<p>  $("#loginForm").ajaxSubmitForm("login/validate",
  *			 function(result) {
  *		        // todo the code when success
  *	             }, 
  *	         function(result) {
  *		        // todo the code when failure
  *	        });
  *</p>
  * @param url        : the form submit url
  * @param successFun : when ajax submit form success,also the response message 
  *                     is success(means:the success propertity of responesText is true),
  *                     call the successFun with the responesText parameter
  * @param failureFun : if not call the successFun,then call the failureFun with responesText parameter 
  */
 $.fn.ajaxSubmitForm = function(url,data,successFun,failureFun,errorFun){
	 
		$.ajax({
			url:url,
			data:$(this).serialize()+$.serializeToUrl(data,"&"),
			type:'post',
			dataType:'json',
			success:function(result){
				if(result.success){
					successFun(result);
				
				}
				else
					failureFun(result);
			},
			failure:function(XMLHttpRequest, textStatus, errorThrown){
				errorFun(XMLHttpRequest, textStatus, errorThrown);
			}
		});
 };
 
$(function(){
	$("#permissionTable").flexigrid({
		url: contextPath+'/permission/getPermissionData',
		dataType: 'json',
		singleSelect:false,
		
		colModel : [
			{display: 'ID', name : 'id', width : "20%", sortable : false, align: 'center'},
			{display: '权限代码', name : 'code', width : "30%", sortable : false, align: 'center'},
			{display: '权限名称', name : 'name', width :"40%", sortable : false, align: 'center'}
			
			],
		buttons : [
			{name: '分配权限', bclass: 'branchIcon',id:'assignBtn',tooltip:'分配权限给角色',onpress : btnHandler}
			],
		searchitems : [
			{display: '权限代码', name : 'code'}
			],
		sortname: "id",
		sortorder: "asc",
		usepager: true,
		title: '权限列表',
		useRp: true,
		rp: 15,
		showTableToggleBtn: true,
		height: $(window).height()-135
	});   
	
});

 
	
var pid ;
var ztree;
function btnHandler(btnTest, grid){
	if(btnTest=="assignBtn"){
		var selectRows = $('.trSelected', grid);
		if(selectRows.length!=1){
			alert("请选择一个权限进行操作.");
			return false;
		}
		pid = $(selectRows.get(0)).attr("id").substring(3);
		showRoleTree(pid);
		   $("#roleTree").dialog({
			   title:'分配权限给角色',
			   modal:true,
			   buttons:[{
				   text:'确定',
				   click:function(){
					   var ids="",checkStatus="" ;
						$.each(ztree.getChangeCheckedNodes(),function(index,e){
							ids+="ids="+e.id+"&";
							checkStatus+="checkStatus="+e.checked+"&";
						});
						alert(ids+checkStatus+"rid="+ztree.getSelectedNodes()[0].id);
						$.post(contextPath+"/role/assignMenu",ids+checkStatus+"pid="+pid,function(result){
							alert("成功");
						});
				   }
			   },{
				   text:'取消',
				   click:function(){
					   ztree.selectNode(ztree.getNodeByParam("uid",2));
					   //$(this).dialog("close");
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
			otherParam:{pid:pid},
			dataFilter: filter
		}
}; 


	
	
var znodes = [{name:'角色管理',id:-1,isParent:true}];

function filter(treeId, parentNode, childNodes) {
	if (!childNodes) 
		{
		return null;
		}
	for (var i=0, l=childNodes.length; i<l; i++) {
		childNodes[i].name = childNodes[i].roleName.replace(/\.n/g, '.');
		childNodes[i].isParent = true;
		childNodes[i].uid = childNodes[i].id;
	}
	return childNodes;
}
 


 
	$.fn.zTree.init($("#roleTree"), setting,znodes);
	 ztree = $.fn.zTree.getZTreeObj("roleTree");
 
}
