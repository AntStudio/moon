<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
 <script type="text/javascript" src="js/jquery-1.7.1.js"></script>
 <script type="text/javascript" src="om/operamasks-ui.min.js"></script>
 <link rel="stylesheet" type="text/css" href="om/themes/apusic/operamasks-ui.css"/>
 <style>
	html{ height:100%;}
	body{ height:100%;}
	.errorMsg {
	    background:  url(om/themes/apusic/images/alert.png) center no-repeat white;
		background-position: 5px 50%;
		text-align: left;
		padding: 2px 20px 2px 25px;
		border: 1px solid red;
		display: none;
		width: 75px;
		margin-left: 10px;
		position: absolute;
		border-radius: 4px;
	}
	.errorImg{
	    background: url("om/themes/apusic/images/invalid.png") no-repeat scroll 0 0 transparent;
	    height: 16px;
	    width: 16px;
	    cursor: pointer;
	    vertical-align: inherit;
	}
	.x-form-invalid{
    background-color: #FFFFFF;
    background : url(om/themes/apusic/images/invalid_line.gif) repeat-x scroll center bottom transparent;
}
</style>
    
    
<title>Ext Demo</title>
</head>
<body>
<script>
	$(function() {
		$( "#dialog-modal").omDialog({
			autoOpen: true,
			height: 200,
			width:400,
			modal: true,
			resizable:false,
			buttons : [{
			     text : "登录", 
			     width:80,
			     click : function () {alert("ss");}
			 }, {
			     text : "重置",
			     width:80,
			     style:'padding-right:80px',
			     click : function () {alert("ss");}
			 }]
		});
		 $('.btn').omButton({width:85,
			 onClick:function(){alert("ss");},
			 label:"我去"
		 });

		 var test = $("#loginForm").validate({
             rules : {
            	 userName : "required",
                 password : {
                     required : true,
                     minlength : 5,
                     maxlength : 10
                 }
             },
             messages : {
            	 userName : {
                     required : "请输入用户名"
                 },
                 password : {
                     required : "请输入密码",
                     minlength : "密码长度不够",
                     maxlength : "密码长度不能超过10"
                 }
             },
             submitHandler : function(){
                 alert('提交成功！');
                 $(this)[0].currentForm.reset()
                 return false;
             },
           //显示校验信息的容器，本示例使用<span class="errorMsg" />做为容器，建议使用容器来避免和其它组件的dom元素交叉的问题
     	    //比如使用omCombo的时候如果不使用容器将会导致样式错乱，根本原因是combo是在input外面包裹一层span再添加样式组成，而校验
     	    //框架默认会再input后面加label标签从而导致combo组件样式混乱。
     	     errorPlacement : function(error, element) { 
                 if(error.html()){
                     $(element).parents().map(function(){
                         var attentionElement = $(this).children().eq(2);
                         if(this.tagName.toLowerCase()=='p'){ 
                             attentionElement.html(error);
                             attentionElement.css('display','none'); //覆盖默认显示方法，先隐藏消息，等鼠标移动上去再显示
                             attentionElement.prev().prev().children("input").addClass("x-form-invalid");
                             if(attentionElement.prev().prev().children().length <= 0)
                                 attentionElement.prev().prev().addClass("x-form-invalid");
                         }
                     });
                 }
             }, 
             //控制错误显示隐藏的方法，当自定义了显示方式之后一定要在这里做处理。
             showErrors: function(errorMap, errorList) {
                 if(errorList && errorList.length > 0){
                     $.each(errorList,function(index,obj){
                         var msg = this.message;
                         $(obj.element).parents().map(function(){
                             if(this.tagName.toLowerCase()=='p'){
                                 var attentionElement =  $(this).children().eq(2);
                                 attentionElement.prev().css("display","inline-block");
         	                    attentionElement.html(msg);
                             }
                         });
                     });
                 }else{
                     $(this.currentElements).parents().map(function(){
                         $(this).children("input").removeClass("x-form-invalid");
                         //获取errorImg图标，如果不是则不执行hide操作
                         var errorImg = $(this).children().eq(1);
                         if(errorImg.hasClass("errorImg")){
                             errorImg.hide();
                         }
                     });
                 }
                 this.defaultShowErrors();
             }


         });

		 
		 
		 $('.errorImg').bind('mouseover',function(e){
		        //要有错误才显示
		        var errorMsg = $(this).next();
		            errorMsg.css('display','inline');//.css({'top':e.pageY, 'left':e.pageX});
		    }).bind('mouseout',function(){
		        $(this).next().css('display','none');
		    });

	});
	 


	 
	</script>
 
    <div id="dialog-modal" title="登录" style="display:none">
    <form id="loginForm">
       <p> 用户名：<input type="text" name="userName"/> <span class="errorImg"></span><span
						class="errorMsg"></span></p>
     <p>  密&nbsp;&nbsp;&nbsp;码：<input type="text" name="password"/> <span class="errorImg"></span><span
						class="errorMsg"></span>  </p>
     </form>
    </div>

</body>
</html>