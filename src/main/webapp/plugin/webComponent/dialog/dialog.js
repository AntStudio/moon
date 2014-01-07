/**
 * 对话框组件
 * @author Gavin Cook
 * @Date 2014-01-06
 */

var dialogCache = {};
(function(){
	var defaults = {
		title:"",
		buttons:[],
		width:560
	};

	var dialog  = {
		renderDialog:function(opts){
			var $contentHtml = this;
			var selector = this.selector;
			var $dialogDiv = $(document.createElement("div"));
			$dialogDiv.addClass("modal hide fade");

			var $dialogHeader = $(document.createElement("div"));
			$dialogHeader.addClass("modal-header");

			var $closeBtn =  $(document.createElement("button"));
			$closeBtn.addClass("close").html("&times;");
			 
			var $title = $(document.createElement("h3"));
            $title.html(opts.title);

            $dialogHeader.append($closeBtn).append($title);

			var $dialogContent = $(document.createElement("div"));
			$dialogContent.addClass("modal-body").append($contentHtml.show());

			var $btnGroup = $(document.createElement("div"));
			$btnGroup.addClass("modal-footer");

			$.each(opts.buttons,function(index,btn){
				var $btn = $(document.createElement("button"));
				$btn.addClass(btn.css).html(btn.text);
				$btn.bind("click",function(){
					btn.click.call($dialogDiv);
				});
				$btnGroup.append($btn);
			});

			$dialogDiv.append($dialogHeader).append($dialogContent).append($btnGroup);

			dialog.bindEvents.call($dialogDiv,opts);
			$dialogDiv.css({"width":opts.width,
							 "margin-left":-opts.width/2});
			dialogCache[selector] = $dialogDiv;
			$dialogDiv.modal("show");

		},
		/**
		** 绑定事件，如.close的关闭对话框
		**/
		bindEvents:function(opts){
			var $dialog = this;
			$(".close",$dialog).bind("click",function(){
				$dialog.modal("hide");
			});
		}
	};

	$.fn.dialog=function(opts){
		if(typeof(opts)=="string"){
			if(opts=="close"){
				dialogCache[$(this).selector].modal("hide");
			}
		}else{
			opts=$.extend({},defaults,opts);
			dialog.renderDialog.call($(this),opts);
		}
	};
})();