var moon = moon||{};
(function(w){

    $(document).ready(function () {
        moon.tabs = $(".tab").KandyTabs({
            trigger:"click",
            scroll:true
        });

        $(document).on("click",".tabbtn i",function(e){
            var $menu = $(e.target).parent();
            moon.tabs.del($menu);
            moon.deleteTab($menu.find(".title").html())
        });
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
                    $(".dropdown-menu",$li).html(subMenus||"<span class=\"no-menu\">没有菜单</span>");
                });
            }
        });
        $(document).on("click",".dropdown-menu a:not(.no-tab)",function(e){
            var $menu = $(e.target);
            moon.addTab($menu.html(),$menu.attr("href"));
            e.preventDefault();
            window.event.returnValue = false;
        });
    });
})(window);
