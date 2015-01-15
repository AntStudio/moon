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
        callBack:{},
        size:"md",//可选值：sm(小尺寸),md(中等尺寸),lg(大尺寸),full(全屏)
        document:"",
        url:"",
        data:undefined,//模板数据,当配置了该项,将使用handlebars进行模板渲染
        style:"default"//弹出框样式,可选值为：default(默认弹出框),right(右侧划出),left(左侧划出)
    };

    var methods  = {
        renderDialog:function(content,opts){
            var documentContext = opts.document || window.document;
            var dialogInstance = this;
            var $contentHtml = content;
            var selector = content.selector;
            var $dialogDiv = $(documentContext.createElement("div"));
            $dialogDiv.addClass("modal fade").addClass("modal-"+opts.style);

            var $modalDialog = $(documentContext.createElement("div")).addClass("modal-dialog")
                .addClass("modal-"+opts.size);
            var $modalContent = $(documentContext.createElement("div")).addClass("modal-content");

            var $dialogHeader = $(documentContext.createElement("div"));
            $dialogHeader.addClass("modal-header");

            var $closeBtn =  $(documentContext.createElement("button"));
            $closeBtn.addClass("close").html("&times;");

            var $title = $(documentContext.createElement("h3"));
            $title.html(opts.title);

            $dialogHeader.append($closeBtn).append($title);

            var $dialogContent = $(documentContext.createElement("div"));
            $dialogContent.addClass("modal-body");
            if(opts.url){
                $dialogContent.append("<iframe class=\"iframe-content\" src=\""+opts.url+"\">");
            }else{
                if(opts.data){//模板渲染
                    $dialogContent.append(Handlebars.compile($contentHtml.html())(opts.data));
                }else {
                    $dialogContent.append($contentHtml.show());
                    $contentHtml.removeClass("hide");
                }
            }

            var $btnGroup = $(documentContext.createElement("div"));
            $btnGroup.addClass("modal-footer");

            $.each(opts.buttons,function(index,btn){
                var $btn = $(documentContext.createElement("button"));
                $btn.addClass(btn.css||"btn btn-default").html(btn.text);
                $btn.bind("click",function(){
                    btn.click.call(dialogCache[selector],btn,opts);
                });
                $btnGroup.append($btn);
            });


            $modalContent.append($dialogHeader).append($dialogContent).append($btnGroup);
            $modalDialog.append($modalContent);
            $dialogDiv.append($modalDialog);

            methods.bindEvents.call(dialogInstance,$dialogDiv,opts);
            $(documentContext.body).append($dialogDiv);
            $dialogDiv.modal("show");

            return $dialogDiv;
        },
        /**
         ** 绑定事件，如.close的关闭对话框
         **/
        bindEvents:function($dialogDiv,opts){
            var dialogInstance = this;
            var $dialog = $dialogDiv;
            var selector  = dialogInstance.selector;
            if(isCached(dialogInstance.selector)){
                $dialog.off();
                $dialog.unbind();
            }

            $(".close",$dialog).bind("click",function(){
                $dialog.modal("hide");
            });

            $dialog.on('show.bs.modal', function (e) {
                if(opts.beforeShow){
                    opts.beforeShow.call(dialogCache[selector],e);
                }
            });
            $dialog.on('shown.bs.modal', function (e) {
                if(opts.afterShown){
                    opts.afterShown.call(dialogCache[selector],e);
                }
            });
            $dialog.on('hide.bs.modal', function (e) {
                if(opts.beforeClose){
                    opts.beforeClose.call(dialogCache[selector],e);
                }
            });
            $dialog.on('hidden.bs.modal', function (e) {
                if(opts.afterClosed){
                    opts.afterClosed.call(dialogCache[selector],e);
                }
                if(opts.data){
                    $dialog.remove();
                    delete dialogCache[selector];
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
    var dialog = function(selector,opts){
        var dialogInstance = this;
        var triggerStatusChange = function(){
            dialogCache[selector] = dialogInstance;
        };
        triggerStatusChange();//新建时，先存储到缓存，因为创建过程会使用到该对象

        this.opts = opts;
        this.selector = selector;
        this.renderDialog = function(){
            dialogInstance.$e = methods.renderDialog.call(dialogInstance,$(this),opts);
            triggerStatusChange();
            return dialogInstance;
        };
        this.close = function(){
            dialogInstance.$e.modal("hide");
            return dialogInstance;
        };
        this.reBindEvents = function(){
            methods.bindEvents.call(dialogInstance.$e,opts);
            triggerStatusChange();
            return dialogInstance;
        };
        this.show = function(){
            dialogInstance.$e.modal("show");
            return dialogInstance;
        };

        return dialogInstance;
    };

    $.fn.dialog=function(opts){
        if(typeof(opts)=="string"){
            if(opts=="close"){
                dialogCache[$(this).selector].close();
            }
        }else{
            opts=$.extend({},defaults,opts);
            var dialogSelector = $(this).selector;
            var newDialog = new dialog(dialogSelector,opts);
            newDialog.renderDialog.call($(this),opts);
        }
    };
})();