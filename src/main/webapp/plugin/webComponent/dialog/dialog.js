/**
 * 对话框组件
 * @author Gavin Cook
 * @Date 2014-01-06
 */

(function(){
	var dialogCache = {};
	
	var defaults = {
		title:"",
		buttons:[],
		width:560,
		callBack:{}
	};

	var methods  = {
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
					btn.click.call(dialog);
				});
				$btnGroup.append($btn);
			});

			$dialogDiv.append($dialogHeader).append($dialogContent).append($btnGroup);

			methods.bindEvents.call($dialogDiv,opts);
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
			if(isCached(opts.selector)){
				$dialog.unbind();
			}else{
				$(".close",$dialog).bind("click",function(){
					$dialog.modal("hide");
				});
			}
			$dialog.on('show.bs.moda', function (e) {
				if(opts.beforeShow){
					opts.beforeShow.call($dialog);
				}
			});
			$dialog.on('shown.bs.modal', function (e) {
				if(opts.afterShown){
					opts.afterShown.call($dialog);
				}
			});
			$dialog.on('hide.bs.modal', function (e) {
				if(opts.beforeClose){
					opts.beforeClose.call($dialog);
				}
			});
			$dialog.on('hidden.bs.modal', function (e) {
				if(opts.afterClosed){
					opts.afterClosed.call($dialog);
				}
			});
		}
	};

	/**
	 * 对话框是否被缓存
	 */
	function isCached(selector){
		for(var s in dialogCache){
			if(selector==s){
				return true;
			}
		}	
		return false;
	}
	var dialog = {
			renderDialog:function(){
				methods.renderDialog.call($(this),dialog.opts);
			},
			close:function(){
				dialogCache[dialog.selector].modal("hide");
			},
			reBindEvents:function(){
				methods.bindEvents.call($(this),dialog.opts);
			}
	};
	$.fn.dialog=function(opts){
		if(typeof(opts)=="string"){
			if(opts=="close"){
				dialog.close();
				dialogCache[$(this).selector].modal("hide");
			}
		}else{
			opts=$.extend({},defaults,opts);
			for(var selector in dialogCache){
				if($(this).selector==selector){
					dialog.opts = opts;
					dialog.reBindEvents.call(dialogCache[dialog.selector]);
					dialogCache[dialog.selector].modal("show");
					return;
				}
			}	
				
			dialog.opts = opts;
			dialog.selector=$(this).selector;
			dialog.renderDialog.call($(this),opts);
		}
	};
})();