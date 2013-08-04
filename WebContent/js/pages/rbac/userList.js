
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
 var grid;
$(function(){
	grid = $("#userTable").flexigrid({
		url: contextPath+'/user/getUsersData',
		dataType: 'json',
		singleSelect:false,
		colModel : [
			{display: 'ID', name : 'id', width : "20%", sortable : true, align: 'center'},
			{display: '用户名', name : 'user_name', width : "30%", sortable : true, align: 'center'},
			{display: '角色名称', name : 'role_name', width :"40%", sortable : false, align: 'center'}
			
			],
		buttons : [
			{name: '添加用户', bclass: 'addIcon',id:'addBtn',tooltip:'添加一个新的用户',onpress : btnHandler},
			{name: '编辑用户', bclass: 'editIcon',id:'editBtn', onpress : btnHandler},
			{name: '删除用户', bclass: 'deleteIcon',id:'deleteBtn', onpress : btnHandler},
			{separator: true},
			{name: '分配角色', bclass: 'branchIcon',id:'assignBtn', onpress : btnHandler}
			],
		searchitems : [
			{display: 'ID', name : 'id'},
			{display: '用户名', name : 'user_name', isdefault: true}
			],
		sortname: "id",
		sortorder: "asc",
		usepager: true,
		title: '用户列表',
		useRp: true,
		rp: 15,
		showTableToggleBtn: true,
		height:  $(window).height()-123
	});   
	
});
function pageHeight(){ 
	return $(window).height();
	};
	
	function pageWidth(){ 
		return $(window).width();
		};
		
		
		var temp = [];
function btnHandler(btnTest, grid){
	if(btnTest=='addBtn')
		$("#userForm").dialog({
			modal:true,
			title:'添加用户',
			buttonAlign:'center',
			show:'flod',
			hide: "explode",
			buttons:[
			         {text:'添加',click:function(){
			        	 $("#userForm").ajaxSubmitForm(contextPath+"/user/addUser","",
			        	function(){
			        		 $("#userForm").dialog("close");
			        		 $("#userTable").flexReload();
			        		 $("#userForm").reset();
			        		 },
			        	function(){alert("失败");}
			        	 );
			         }},
			         {text:'取消',click:function(){
			        	 $("#userForm").reset();
			        	 $(this).dialog("close");	 
			         }}
			         ]
		});
	
	else if(btnTest=='editBtn'){
		var selectRows = $('.trSelected', grid);
		if(selectRows.length!=1){
			alert("请选中一条数据进行编辑.");
			return false;
		}
		var id = $(selectRows.get(0)).attr("id").substring(3);
		$("#userForm").autoCompleteForm(contextPath+"/user/getUser",{id:id});
	  	$("#userForm").dialog({
			modal:true,
			title:'编辑用户',
			buttonAlign:'center',
			show:'flod',
			hide: "explode",
			buttons:[
			         {text:'保存',click:function(){
			        	 $("#userForm").ajaxSubmitForm(contextPath+"/user/updateUser",
			        			 {"user.id":id},
			        	function(){
			        		 $("#userForm").dialog("close");
			        		 $("#userTable").flexReload();
			        		 $("#userForm").reset();
			        		 },
			        	function(){alert("失败");}
			        	 );
			         }},
			         {text:'取消',click:function(){
			        	 $("#userForm").reset();
			        	 $(this).dialog("close");	 
			         }}
			         ]
		}); 
	}
	else if(btnTest=='deleteBtn'){
		var selectRows = $('.trSelected', grid);
		if(selectRows.length<1){
			alert("请选择要删除的数据");
			return false;
		}
		if(confirm("确认删除这"+selectRows.length+"条数据?")){
			var ids="";
		   $.each(selectRows,function(index,e){
			   ids+="&ids="+$(e).attr("id").substring(3);
		   });
		   ids = ids.substring(1);
		    $.post(contextPath+"/user/logicDeleteUser",ids,function(result){
			   $("#userTable").flexReload();
			  });
		}
	}
	else{
		var selectRows = $('.trSelected', grid);
		if(selectRows.length!=1){
			alert("请选择一个用户进行角色分配.");
			return false;
		}
		var uid = $(selectRows.get(0)).attr("id").substring(3);
 $.postData(contextPath+"/user/getRolePath",{uid:uid},function(result){
	
	 ztree.expandNode(ztree.getNodes()[0],true);
	// alert(result.path);
	 if(result.path)
	 {
	 temp = result.path.split(",");
	 if(temp.length!=1)
		 ztree.asyncOrExpandNode(ztree.getNodeByParam("uid",temp[0]),true);
	 else{
		 ztree.selectNode(ztree.getNodeByParam("uid",temp[0]));
		 i = 1;
	 }
	 }
	 else{
		 temp=[];
		 return false;
	 }
 });
		   $("#roleTree").dialog({
			   title:'分配角色',
			   modal:true,
			   close:function(){
				   ztree.cancelSelectedNode(ztree.getSelectedNodes[0]);
				   ztree.expandAll(false);
			   },
			   buttons:[{
				   text:'确定',
				   click:function(){
					   if(ztree.getSelectedNodes().length!=1){
						   alert("请选择一个角色进行分配");
						   return false;
					   }
					  $.post(contextPath+"/role/assignRoleToUser",
							  {uid:uid,
						       rid:ztree.getSelectedNodes()[0].id},
						       function(result){
						    	   alert("角色分配成功");
						    	   $("#userTable").flexReload();
						    	   $("#roleTree").dialog("close");
						       }
					  );
				   }
			   },{
				   text:'取消',
				   click:function(){
					   $(this).dialog("close");
				   }
			   }]
		   });
		   
		 
	}
};

$.fn.autoCompleteForm = function(url,data,exclude){
	var inputs = $(':input',this) .not(':button, :submit, :reset,[name="repassword"]');
	$.ajax({
		url:url,
		data:data,
		dataType:'json',
		type:'post',
		success:function(response){
			$.each(inputs,function(index,e){
				var temp;
				var name = $(e).attr("name");
				var position = name.indexOf(".");
				if(position!=-1){
					temp = response[name.substring(0,position)][name.substring(position+1)];
				}else{
					temp =response[name];
				}
				if(typeof(temp)!="undefined"&&temp!=null&&temp!="null")
					$(e).val(temp);
			});
			
			
		}
	});
	
};

var i= 1;

var setting = {
        data: {  
            simpleData: {  
                enable: true  
            }  
        },
		async: {
			enable: true,
			url:contextPath+"/role/getRoleData",
			autoParam:["id"],
			dataType:'json',
			dataFilter: filter
		},
		callback:{
			onAsyncSuccess :function(){
				i = ztree.selectNodebyTreepath("uid",temp,i);
			},
			onExpand : function(){
				i = ztree.selectNodebyTreepath("uid",temp,i);
			}
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
 

 var ztree;
$(document).ready(function(){
	$.fn.zTree.init($("#roleTree"), setting,znodes);
	 ztree = $.fn.zTree.getZTreeObj("roleTree");
	ztree.reAsyncChildNodes(ztree.getNodes()[0]);
});
 
 