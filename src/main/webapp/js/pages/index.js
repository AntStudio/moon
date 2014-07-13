$(document).ready(function () {
	
	/**
	 * 给iframe绑定load事件,load触发后给iframe的内容绑定点击事件,关闭bootstrap的dropdown菜单
	 */
	$("iframe#main").bind("load",function(){
		$(this.contentDocument).click(function(){
			$("[id^='menu_']").removeClass('open');
		});
	});
	
	 /**
	  * 异步加载菜单
	  */
	 $("[id^='menu_']").click(function(){
		 var $li = $(this);
		 var id = $(this).attr("id").replace("menu_","");
		 if($(".dropdown-menu",$li).find(".loading").length>0){
			 $.getJsonData(contextPath+"/menu/getSubMenus",{pid:id}).done(function(data){
				 var subMenus="";
				 $.each(data.result,function(index,menu){
					 subMenus+="<li ><a href='"+contextPath+menu.url+"' target='main'>"+menu.menuName+"</a></li>";
				 });
				 $(".dropdown-menu",$li).html(subMenus);
			 });
		 }
	 });
 });

  