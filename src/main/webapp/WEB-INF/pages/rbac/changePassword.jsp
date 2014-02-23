<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="../common/header.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<m:require src="jquery,bootstrap,common,ev,{rbac/changePassword}"></m:require>
<title>修改密码</title>
</head>
<body>
	<c:if test="${info!=null}">
${info } 

</c:if>
	<c:if test="${info==null}">
		<form class="form">
			<div>
				<span class="label-text">原密码：</span><input name="oldPassword"
					id="oldPassword" type="password"
					validate="validate[call(checkOldPassword)]"
					msgalign="right" />
			</div>
			<div>
				<span class="label-text">新密码：</span><input name="newPassword"
					id="newPassword" type="password" validate="validate[required]"
					msgalign="right" />
			</div>
			<div>
				<span class="label-text">确认密码：</span><input name="rePassword"
					type="password" validate="validate[required,eq(#newPassword)]"
					msgalign="right" />
			</div>
			<input type="button" value="确认" id="confirm" class="btn btn-primary"/>
		</form>
	</c:if>

</body>
</html>