<%@ page language="java" contentType="text/html; charset=utf-8"
         pageEncoding="utf-8" %>
<script type="text/javascript">
    $(function(){
        $('.collapse').collapse({
            toggle: false
        });
        /**
         * 异步加载菜单
         */
        $(document).on("click","[id^='menu_']",function(e){
            var $li = $(e.target).closest("li");
            var id = $li.attr("id").replace("menu_","");
            var $subNav = $(".sidebar-subnav",$li);
            if($subNav.find(".loading").length>0){
                $.getJsonData(contextPath+"/menu/getSubMenus",{pid:id}).done(function(data){
                    var subMenus="";
                    $.each(data.result,function(index,menu){
                        subMenus+="<li ><a href='"+contextPath+menu.url+"'>"+menu.menuName+"</a></li>";
                    });

                    $subNav.html(subMenus||"<span class=\"no-menu\">没有菜单</span>").collapse("show");
                });
            }else{
                $(".sidebar-subnav").collapse("hide");
                $subNav.collapse("toggle");
            }
            $(".sidebar-subnav").collapse("hide");
        });

        $(".navbar-right a").popover({trigger:"hover",placement:"auto"});
    });
</script>
<div class="top-navbar">
    <div class="navbar-header">
        <a href="/" class="logo-con">
            <i class="fa fa-heartbeat font-logo"></i>
            <span class="title">携心医疗</span>
        </a>
    </div>
    <div class="navbar-right">
        <a href="${pageContext.request.contextPath}/user/my" data-content="个人信息">
            <div class="avatar-container">
                <img src="${pageContext.request.contextPath}/file/get/${currentUser.avatar}"
                        onerror="javascript:this.src='${pageContext.request.contextPath}/css/images/logo.png'">
            </div>
            <c:if test="${currentUser.realName!=null}">${currentUser.realName}</c:if>
            <c:if test="${currentUser.realName==null}">${currentUser.userName}</c:if>
        </a>
        <a href="${pageContext.request.contextPath}/user/loginOut"  data-content="注销">
            <i class="fa fa-sign-out"></i>
        </a>
    </div>
    <div class="clearfix"></div>
</div>
<aside class="aside">
    <ul id="elements" class="nav">
        <c:forEach items="${menus}" var="s">
            <li id="menu_${s.id}">
                <a>
                    <i class="${s.iconCss}"></i>
                    <span class="menu-text"> ${s.menuName}</span>
                </a>

                <ul class="sidebar-subnav collapse <c:if test='${s.code==expandMenuCode}'>in</c:if>">
                    <c:if test="${s.code==expandMenuCode}">
                        <c:forEach items="${subMenus}" var="subMenu">
                            <%--设置当前菜单,用于显示菜单名称--%>
                            <li class="<c:if test='${currentMenu.code==subMenu.code}'>active</c:if>">
                                <a href="${pageContext.request.contextPath}${subMenu.url}">${subMenu.menuName}</a>
                            </li>
                        </c:forEach>
                    </c:if>
                    <c:if test="${s.code!=expandMenuCode}">
                        <i class="fa fa-spinner fa-spin loading"></i>
                    </c:if>
                </ul>
            </li>
        </c:forEach>
    </ul>
</aside>