/**
 * reset all the field of form,call like $("#loginForm").reset();
 */
$.fn.reset = function(){
	$(':input',this)  
	 .not(':button, :submit, :reset, :hidden')  
	 .val('')  
	 .removeAttr('checked')  
	 .removeAttr('selected'); 
};
 
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
				if(result.permission=='noPermission')
				{
					alert("您没有权限执行此操作.");
					return false;
				}
				if(result.success){
					successFun(result);
				}
				else{
					failureFun(result);
				}
			},
			failure:function(XMLHttpRequest, textStatus, errorThrown){
				errorFun(XMLHttpRequest, textStatus, errorThrown);
			}
		});
 };
 
 /**
  * 异步获取数据
  * 以post--json的方式获取，已处理权限判断
  * @param url 请求路径
  * @param data 传递的参数
  * @param successFun  success=true或其他不等于false,0的值，则回调此方法
  * @param failureFun success=false回调方法
  * @param errorFun  异步获取出错回调
  * @param async 是否采用异步方式,默认为true
  */
 $.postData = function(url,data,successFun,failureFun,errorFun,async){
		$.ajax({
			url:url,
			data:data,
			type:'post',
			async:async!==false,
			dataType:'json',
			beforeSend:function(){
				  	   $(".loading").show();
								},
			success:function(result){
				if(result.permission=='noPermission')
				{
					alert("您没有权限执行此操作.");
					return false;
				}
				$(".loading").hide();
				if(result.success){
					successFun(result);
				}
				else{
					if(failureFun)
					failureFun(result);
				}
			},
			failure:function(XMLHttpRequest, textStatus, errorThrown){
				$(".loading").hide();
				if(errorFun)
				errorFun(XMLHttpRequest, textStatus, errorThrown);
			}
		});
};

 /**
  * to turn a new url by type
  */
 $.href=function(url,type){
	 if(typeof(type)=="undefined"||type=='current')
	  window.location.href=url;
	 else
		 if(type=="new")
			 window.open(url);
 };
 
 $.serializeToUrl=function(o,prefix){
		if(typeof(prefix)=='undefined'){
			prefix = "";
		}
	    for(var i in o){
	    	prefix+=i+"="+o[i]+"&";
	    }
		
	    return prefix.substring(0,prefix.length-1);
	};
	
	
/**
 * 自动填充表单
 */
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
	
	$.returnValue=function(val){
		return val;
	};
	
	/**
	 * 封装ajax数据交互
	 */
	var _defaults = {type:"Get",dataType:"json"};
	$.getJsonData = function(url,params,opts){
		var dfd = $.Deferred();
		opts = $.extend({},_defaults,opts);
		
		$.ajax({
			   url : url,
			  type : opts.type,
		  dataType : opts.dataType,
		      data : params
		}).done(function(data,textStatus, jqXHR ){
			dfd.resolve(data,textStatus, jqXHR );
		}).fail(function( jqXHR, textStatus, errorThrown ){
			dfd.reject( jqXHR, textStatus, errorThrown );
		});
		
		return dfd.promise();
	};
	
	var moon = {};
	
(function(m){
	m.alert = function(options){
		_noty(formatParam(options),{type:'alert'});
	};
	
	m.notify = function(options){
		var _n = _noty(formatParam(options),{type:'information'});
		setTimeout(function(){
			$.noty.close(_n.options.id);
		},3000);
	};
	
	m.error = function(options){
		var _n = _noty(formatParam(options),{type:'error'});
		setTimeout(function(){
			$.noty.close(_n.options.id);
		},3000);
	};
	
	function formatParam(options){
		if(typeof(options)=='string'){
			options={text:options};
		}
		return options;
	}
	
	function _noty(op1,op2){
		return noty($.extend(op1,op2));
	}
})(moon);