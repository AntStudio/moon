<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8" %>
<%@ include file="../common/header.jsp" %>
<m:require src="jquery,bootstrap,common,ev,{rbac/changePassword}"/>
<c:if test="${info!=null}">
    ${info }

</c:if>
<c:if test="${info==null}">
    <form class="form">
        <div>
            <span class="label-text">原密码：</span>
            <input name="oldPassword" id="oldPassword" type="password"
                   validate="validate[call(checkOldPassword)]"
                   msgalign="right"/>
        </div>
        <div>
            <span class="label-text">新密码：</span>
            <input name="newPassword" id="newPassword" type="password"
                   validate="validate[required]"
                   msgalign="right"/>
        </div>
        <div>
            <span class="label-text">确认密码：</span>
            <input name="rePassword" type="password"
                   validate="validate[required,eq(#newPassword)]"
                   msgalign="right"/>
        </div>
        <input type="button" value="确认" id="confirm" class="btn btn-primary"/>
    </form>
</c:if>