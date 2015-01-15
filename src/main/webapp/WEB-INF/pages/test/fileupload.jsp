<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="m" uri="/moon"%>
 <%@ include file="../common/header.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<m:require src="jquery,bootstrap,font,webuploader,common"/>
<title>文件上传</title>
<script type="text/javascript">
$(function(){
	var template =  '<div class="imgContainer" data-id="{0}"><div class="content">'
					+'<i class="fa fa-minus-circle"></i><img src="{1}">'
					+'<div class="progress">'
					+'<div class="progress-bar progress-bar-info"></div>'
					+'</div></div>'
					+'</div>' ;
	var uploader = moon.webuploader({
		 fileQueued:function(f){
			 this.makeThumb( f, function( error, src ) {
	                if ( error ) {
	                    console.log( '不能预览' );
	                    return;
	                }
	                $(".uploader-list").append(moon.format(template,f.id,src));
	            }, 200,200 );
			 $("#btns").removeClass("origin").addClass("selected");
		 },
		 uploadButton:"#upload",
		 pick:"#picker",
		 uploadProgress:function(file,percentage){
			 var $bar = $("[data-id='"+file.id+"'] .progress-bar").css({
				 width:percentage*100+"%"
			 });
             if(percentage == 1){
                 $bar.removeClass("progress-bar-info").addClass("progress-bar-success");
             }
		 },
		 uploadAccept:function(o,ret){
			 var id = o.file.id;
			 if(ret.failure.length==1){//上传失败
				 $("[data-id='"+id+"'] .progress-bar").removeClass("progress-bar-success")
				                             .addClass("progress-bar-danger")
				                             .html(ret.failure[0].errorMsg);
			 }
		 }
	});
	
	$(document).on("click","i",function(e){
		var $div = $(e.target).closest(".imgContainer");
		uploader.removeFile($div.attr("data-id"));
		$div.remove();
	});
});
</script>
<style>
	.uploader-list{
		width:96%;
		margin: 20px 2%;
		border: 1px solid #ccc;
		min-height: 200px;
	}
	
	.uploader-list img{
	}
	
	.origin #picker{
		margin-left: 45%;
		margin-top: -145px;
	}
	
	.origin #upload{
		display:none;
	}
	
	.selected {
		text-align: right;
	}
	
	.selected #picker{
		float:right;
	}
	
	.selected #upload{
		float:right;
		margin-left: 10px;
		margin-right: 2%;
	}
	
	.imgContainer{
		position: relative;
		display: inline-block;
		margin: 5px;
	}
	
	.imgContainer i{
		display: none;
	}
	
	.imgContainer:hover i{
		position: absolute;
		display: inline-block;
		z-index: 99;
		right: 15px;
		top: 15px;
		color: rgb(223, 166, 177);
		font-size: 22px;
		cursor: pointer;
	}
	
	.progress{
		position: absolute;
		bottom: 0;
		margin-bottom: 0;
		width: 100%;
	}
</style>
</head>
<body>
<div>
	<h3>图片上传</h3>
	<div id="fileContainer" class="uploader-list">
	</div>
	<div class="origin" id="btns">
		<button id="upload" class="btn btn-default">上传文件</button>
		<div id="picker">点击选择文件</div>
	</div>
</div>
</body>
</html>