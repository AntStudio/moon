<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8" %>
<%@ include file="../common/header.jsp" %>
<m:require src="jquery,common,ev,zt,handlebars,js/ztree.extend.js,bootstrap,table,dialog,noty,font,{rbac/userList}"/>
<!-- 用户列表 -->
<div id="userTable"></div>

<!-- Modal -->
<form id="userForm" class="hide form-horizontal">
    <div class="form-group">
        <label class="col-sm-2 control-label">用户名:</label>

        <div class="col-sm-10">
            <input type="text" class="form-control" name="user.userName"
                   validate="validate[required,call(isUserNameExists)]"/>
        </div>
    </div>

    <div class="form-group">
        <label class="col-sm-2 control-label">密&nbsp;&nbsp;码:</label>

        <div class="col-sm-10">
            <input type="password" class="form-control" name="user.password" id="password"
                   validate="validate[minsize(6)]" errMsg="密码须6位以上">
        </div>
    </div>
    <div class="form-group">
        <label class="col-sm-2 control-label">重复密码:</label>

        <div class="col-sm-10">
            <input type="password" class="form-control" name="repassword"
                   validate="validate[minsize(6),eq(#password)]">
        </div>
    </div>
    <div class="form-group">
        <label class="col-sm-2 control-label">真实姓名:</label>

        <div class="col-sm-10">
            <input type="text" class="form-control" name="user.realName" validate="validate[required]"/>
        </div>
    </div>
</form>

<!-- 角色分配 -->
<div id="roleTree" class="ztree" style="display:none;"></div>

<%--用户角色设置--%>
<script type="text/x-handlebars-template" id="settingFormTemplate">
    <form class="setting-form form-horizontal">
        {{#listUserType this}}

        {{/listUserType}}
    </form>
</script>