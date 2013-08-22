 $(document).ready(function () {
	 
	 /**
	  * 异步加载菜单
	  */
	 $("[id^='menu_']").click(function(){
		 var $li = $(this);
		 var id = $(this).attr("id").replace("menu_","");
		 if($(".dropdown-menu",$li).find(".loading").length>0){
			 $.ajax({
				  url:contextPath+'/menu/getSubMenus',
				  data:{
					  pid:id
				  },
					type:'post',
					dataType:'json',
					success:function(response){
						var subMenus="";
						$(eval(response)).each(function(index,e){
							subMenus+="<li data-url='"+e.url+"'><a href='#'>"+e.menuName+"</a></li>";
							
						});
						setTimeout(function(){
							$(".dropdown-menu",$li).html(subMenus);
							$(".dropdown-menu li",$li).click(function(){
								 $.ajax({
										dataType:'html',
										type:'Get',
										url:contextPath+$(this).attr("data-url")
									}).done(function(data){
										$(".mainIframeContainer.current").html(data);
										$(".mainIframeContainer").not(".current").animate({right:'100%'},500,'swing',function(){
											 $(this).toggleClass("left").toggleClass("right").toggleClass("current").removeAttr("style").empty();;
										 });
										 $(".mainIframeContainer.current").animate({opacity:1,left:0,right:0},500,"swing",function(){
											 $(this).toggleClass("left").toggleClass("right").toggleClass("current").removeAttr("style");
											 
										 });
									});
							});
						},500);
					}
			 });
		 }
	 });
 });


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
  