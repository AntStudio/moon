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
							//subMenus+="<li data-url='"+e.url+"'><a href='#'>"+e.menuName+"</a></li>";
							subMenus+="<li ><a href='"+contextPath+e.url+"' target='main'>"+e.menuName+"</a></li>";
						});
						$(".dropdown-menu",$li).html(subMenus);
					}
			 });
		 }
	 });
 });

  