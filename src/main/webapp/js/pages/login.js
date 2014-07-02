$(function(){
	//登录页面欢迎信息
	moon.info("Welcome to Moon!","top");
	
	//登录动作
	$("#submit").click(function(){
		$("#loginForm").validate("validate").done(function(result){
			if(result){
				$("#loginForm").ajaxSubmitForm(contextPath+"/user/login/validate").done(function(data){
					if(data.success){
						if(from){
							$.href(from);
						}else{
							$.href(contextPath+"/index");
						}
					}else{
						moon.error("用户名或密码错误","top");
					}
				}).fail(function(){
					moon.error("服务出错，请稍后再试","top");
				});
			}
		});
	});
	
	//页面动画
	$(".form-container").animate({
		"margin-top":"-120px"
	},2000,"linear",function(){
		$(".system-info-container").animate({
			"margin-left":0
		});
	});
	
	//添加表单验证
	$("#loginForm").validate({align:'bottom',theme:"darkblue"});
});