var moon = moon||{};
(function(m){
	//详细文档: http://gmuteam.github.io/webuploader/doc/index.html
	var _default = {
			pick: '#fileContainer',
			server:contextPath+"/file/upload",
			fileVal:"files",
			swf:"Uploader.swf"
	};
	m.webuploader = function(opts){
		var uploader = WebUploader.create($.extend(_default,opts));
		/******************** 绑定事件 ********************/
		!opts.beforeFileQueued||uploader.on("beforeFileQueued",opts.beforeFileQueued);
		!opts.fileQueued||uploader.on("fileQueued",opts.fileQueued);
		!opts.filesQueued||uploader.on("filesQueued",opts.filesQueued);
		!opts.fileDequeued||uploader.on("fileDequeued",opts.fileDequeued);
		!opts.startUpload||uploader.on("startUpload",opts.startUpload);
		!opts.stopUpload||uploader.on("stopUpload",opts.stopUpload);
		!opts.uploadFinished||uploader.on("uploadFinished",opts.uploadFinished);
		!opts.uploadStart||uploader.on("uploadStart",opts.uploadStart);
		!opts.uploadBeforeSend||uploader.on("uploadBeforeSend",opts.uploadBeforeSend);
		!opts.uploadAccept||uploader.on("uploadAccept",opts.uploadAccept);
		!opts.uploadProgress||uploader.on("uploadProgress",opts.uploadProgress);
		!opts.uploadError||uploader.on("uploadError",opts.uploadError);
		!opts.uploadSuccess||uploader.on("uploadSuccess",opts.uploadSuccess);
		!opts.uploadComplete||uploader.on("uploadComplete",opts.uploadComplete);
		!opts.error||uploader.on("error",opts.error);
		/******************** /绑定事件 ********************/
		/**
		 * 上传按钮
		 */
		if(opts.uploadButton){
			$(opts.uploadButton).click(function(){
				uploader.upload();
			});
		}
		return uploader;
	} ;
})(moon);