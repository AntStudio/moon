$(function() {
	$("form").validate({
		theme : "darkblue",
		errorMsg : {
			'required' : '该项为必填项,请填写...<br/>'
		}
	});
	$("#confirm").click(function() {
		$("form").validate("validate");
		alert($("form").validate("getResult"));
		$.postData(contextPath + "/user/doChangePassword", {
			password : $("#newPassword").val()
		}, function() {
			alert("密码修改成功.");
			$("form").reset();
		}, function() {
			alert("密码修改出错.");
			$("form").reset();
		});
	});
});

function checkOldPassword() {
	var msg = "";
	$.postData(contextPath + "/user/matchOldPassword", {
		password : $("#oldPassword").val()
	}, function() {

	}, function() {
		msg = "原密码不正确，请核对.<br/>";
	}, function() {

	}, false);
	return msg;
}