<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<%@ include file="common/header.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
  <script type="text/javascript" src="${pageContext.request.contextPath}/jquery/ui/layout/jquery.layout-latest.js"></script>
 <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/jquery/ui/layout/layout-default-latest.css"/>
 <script type="text/javascript" src="${pageContext.request.contextPath}/js/pages/index.js"></script>
<title>主页</title>
<script>
//***********************************************<公用方法(提取到公用js文件中)>*******************************************************

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
 $.fn.ajaxSubmitForm = function(url,successFun,failureFun,errorFun){
		$.ajax({
			url:url,
			data:$(this).serialize(),
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
 $.href=function(url,type){
	 if(typeof(type)=="undefined"||type=='current')
	  window.location.href=url;
	 else
		 if(type=="new")
			 window.open(url);
 };
//***********************************************</公用方法(提取到公用js文件中)>*******************************************************
 

</script>
</head>
<body>
<iframe id="main" name="main" class="ui-layout-center"
	width="100%" height="600" frameborder="0" scrolling="auto"
	src="${pageContext.request.contextPath}/user"></iframe><!-- http://ui.operamasks.org/demos/grid/simple.html -->

 <div class="ui-layout-north" style="padding:3px">
 <div style="background:url('${pageContext.request.contextPath}/css/images/bg.png');width:100%;height:100%;overflow:hidden;">
 <span style="color: #e6e6e6;font-family: serif;font-size: 40px;position:relative;left:40px;top:20px;" >电力双视监控平台</span>
 <span style="float:right;margin-top: 60px;margin-right: 40px;font-size: 20px;color:#121647;">欢迎您:
 ${currentUser.userName}
 <span style="padding-left: 10px;color:#475699;">|</span>
 <a href="${pageContext.request.contextPath}/user/loginOut" style="text-decoration: none;color: #AC294E;padding-left: 10px;" >退出</a></span>
 </div>
 </div>
 <div class="ui-layout-west">

 <div id="menu" >
 <c:forEach items="${menus}" var="s">
 <h3 id="${s.id}">${s.menuName}</h3>
<ul></ul>
 </c:forEach>
    </div>
 </div>    
</body>
</html>