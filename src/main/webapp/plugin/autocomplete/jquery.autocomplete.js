;
/**
 * jQuery 自动补全插件
 * author: GavinCook
 * 功能特性：
 * 1. 本地指定数据补全 (done)
 * 2. 异步数据补全(json格式) (done)
 * 3. 补全列表尺寸设置(高度、宽度) (done)
 * 4. 动态补全事件绑定(可为还不存在的元素绑定自动补全触发事件) (done)
 * 5. 多触发方式(keydown,keyup,方向键) (done)
 * 6. 选择回调,选择条目后,可自定义处理逻辑
 **/
(function($){
    var _defaults = {
        trigger : "keyup",//触发方式
        triggerLength: 1,//触发长度,只有当内容的长度达到设置才会触发自动补全
        data:[],//列表数据 : array , function , deferred , string(url)
        css:"_auto-complete",//css
        close:"blur",//关闭触发方式，触发后在其他地方点击才会关闭
        ajaxType:"Get",//异步数据请求方式
        ajaxParam:{},//异步请求参数
        ajaxDataType:"json",//异步请求数据格式
        inputKey:"keyword",//输入框
        customFormat:undefined,//自定义的数据格式化,默认不定义
        width:"input",//补全列表的宽度, input->和输入框的宽度一样, number->具体的宽度(单位px)
        height:"auto",//补全列表的高度,默认自动
        itemRenderer:undefined,//Li条目渲染器,传回该条目的数据
        beforeSelect:undefined,//选择前处理逻辑,如果返回false那么不会执行设值的逻辑和selectCallback的回调
        selectCallback:undefined//选择后回调,如有定义,会将该条目的所有数据传回
    };

    //插件主入口
    $.fn.autoComplete = function(opts){
        opts = $.extend(_defaults,opts);

        //自动补全触发事件绑定(打开和关闭)
        $(document).on(opts.trigger+" "+opts.close,this.selector,function(e){
            if(e.type == opts.trigger){//打开事件
                switch(e.keyCode){
                    case 13: //enter
                    case 27: //esc
                    case 37: //left
                    case 38: //up
                    case 39: //right
                    case 40: break;
                    default :
                        var target = e.target;
                        if(target.value.length >= opts.triggerLength){
                            _autoComplete.hideOthers.call(target);
                            _autoComplete.show.call(target,opts);
                        }else{
                            _autoComplete.close.call(target,opts);
                        }
                }

            }else{//关闭事件
                $(document).one("click",function(){
                    _autoComplete.close.call(target,opts);
                });
            }
        });

        //输入框的键盘操作事件(方向键、ESC键、Enter键)
        $(document).on("keyup",this.selector,function(e){
            var target = e.currentTarget;
            switch(e.keyCode){
                case 13: _autoComplete.select.call(target,opts);break;//enter
                case 27: _autoComplete.close.call(target,opts);break;//esc
                case 37: _autoComplete.prev.call(target);break;//left
                case 38: _autoComplete.prev.call(target);break;//up
                case 39: _autoComplete.next.call(target);break;//right
                case 40: _autoComplete.next.call(target);break;//down
                    defult:break;
            }
        });

        //点击自动补全列表的选择事件
        $(document).on("click","."+opts.css+" li",function(e){
            var $autoCompleteList = $(e.currentTarget);
            var beforeSelect = true;
            if($autoCompleteList[0].tagName == "LI"){
                var $ul = $autoCompleteList.closest("ul");
                var data = $autoCompleteList.data("data");
                var _input = $ul[0]._input;
                if((beforeSelect = _autoComplete.beforeSelect.call(_input,opts,data))) {
                    _input.value = data.value;
                    opts.selectCallback && opts.selectCallback.call(_input, data);//回调方法
                }
            }
            if(beforeSelect) {
                _autoComplete.close.call($autoCompleteList, opts);
            }
        });
    };

    //ID生成器,用于为自动补全列表生成唯一的标识,用于隐藏自动补全列表时排除当前输入框的补全列表(用_id区分)
    var _idGenerator = function(){
        var i = 1;
        this.next=function(){
            return i++;
        }
    };

    //ID生成器实例
    var idGenerator = new _idGenerator();

    //自动补全方法列表
    var _autoComplete = {

        //获取数据
        getData:function(opts){
            var target = this;
            var inputValue = target.value;
            var data = opts.data;
            var $dfd = $.Deferred();
            if(data instanceof Array){//数组
                $dfd.resolve(data);
            }else if($.isFunction(data)){//方法获取
                $dfd.resolve(data.call(target));
            }else if(typeof(data) == "string"){//url获取
                if(opts.inputKey) {
                    opts.ajaxParam[opts.inputKey] = inputValue;
                }
                $.ajax({
                    url:data,
                    type:opts.ajaxType,
                    dataType:opts.ajaxDataType,
                    data:opts.ajaxParam
                }).done(function(resultData){
                    if(opts.customFormat){
                        $dfd.resolve(opts.customFormat.call(target,resultData));
                    }
                });
            }
            return $dfd.promise();
        },

        //格式化数据,数据格式为{text:xxx,value:xxx,img:xxx}
        formatData:function(data){
            var formatedData = new Array();
            $.each(data,function(index,d){
                if(typeof(d)=="string"){
                    d = {text:d,value:d,img:d};
                }else if(typeof(d)=="object"){
                    var text = d.text || "";
                    d.text = text;
                    d.value = d.value || d.text;
                    d.img = d.img || d.text;
                }
                formatedData.push(d);
            });
            return formatedData;
        },
        //高亮关键字
        highlight:function(d){
            var target = this;
            var value = target.value;
            var regex = new RegExp(value, "gi");
            if(regex.exec(d.text)){
                d.text = d.text.replace(regex,"<span class=\"high-light\">"+value+"</span>");
                return true;
            }
            return false;
        },
        //创建节点（-->createElement）
        ce:function(tag,opts){
            var tag = document.createElement(tag);
            tag._id = idGenerator.next();
            var $tag = $(tag);
            for(var name in opts){
                $tag.attr(name,opts[name]);
            }
            return $tag;
        },
        //创建数据列表的条目
        createListItem : function(opts,index,data){//默认选择第一条
            var content = typeof(opts.itemRenderer) == "undefined" ? data.text : opts.itemRenderer.call(this,data);
            var $li = _autoComplete.ce("li",index==0?({class:"selected"}):{}).html(content);
            $li.data("data",data);//设置数据
            return $li;
        },
        //渲染整个数据列表
        renderList:function(opts){
            var target = this;
            var $container = target._autoCompleteConatiner.empty();
            _autoComplete.getData.call(target,opts).done(function(data){
                data = _autoComplete.formatData.call(target,data);
                var index = 0;
                $.each(data,function(i,d){
                    if(_autoComplete.highlight.call(target,d)){
                        $container.append(_autoComplete.createListItem(opts,index++,d));
                    }
                });
                target._autoCompleteConatiner = $container;
            });
            return $container;
        },
        //创建列表容器,只有没有创建时才会创建，否则返回创建的实例
        createListContainer:function(opts){
            var target = this;
            if(typeof(target._autoCompleteConatiner) == 'undefined'){
                var $ul = _autoComplete.ce("ul",
                    {
                        class:opts.css,
                        style:"left:"+opts.left+"px;" +
                            "top:"+opts.top+"px;" +
                            "width:"+opts.width+";" +
                            "height:"+opts.height
                    });

                target._autoCompleteConatiner  = $ul;
                $ul[0]._input = target;
                $(document.body).append(target._autoCompleteConatiner);
            }
            return target._autoCompleteConatiner;
        },
        //显示自动补全列表(必要时创建容器，根据新的数据重新渲染列表)
        show:function(opts){
            var target = this;
            var $target = $(target);
            opts.top = $target.offset().top + target.offsetHeight+2;
            opts.left = $target.offset().left;
            if(opts.width == "input"){
                opts.width = $target.outerWidth()+"px";
            }else if(typeof(opts.width)== "number"){
                opts.width = opts.width + "px";
            }

            if(opts.height != "auto" && typeof(opts.height)!="string"){
                opts.height = opts.height + "px";
            }

            _autoComplete.createListContainer.call(target,opts).addClass("active");

            _autoComplete.renderList.call(target,opts);
        },
        //隐藏其他的补全列表
        hideOthers:function(){
            var target = this;
            $("._auto-complete.active").not(function(index,ele){
                return target._autoCompleteConatiner && ele._id == target._autoCompleteConatiner[0]._id;
            }).removeClass("active");
        },
        //关闭所有的补全列表
        close:function(opts){
            $("."+opts.css+".active").removeClass("active");
        },
        //选择下一个补全数据
        next:function(){
            var autoCompleteConatiner = this._autoCompleteConatiner;
            if(!autoCompleteConatiner) return false;

            var selectedIndex = autoCompleteConatiner.selectedIndex || 0;
            var length = $("li",autoCompleteConatiner).length;
            if(selectedIndex == length-1){
                selectedIndex = 0;
            }else{
                selectedIndex++;
            }
            $(".selected",autoCompleteConatiner).removeClass("selected");
            $("li:eq("+selectedIndex+")",autoCompleteConatiner).addClass("selected");
            autoCompleteConatiner.selectedIndex = selectedIndex;
        },
        //选择上一个补全数据
        prev:function(){
            var autoCompleteConatiner = this._autoCompleteConatiner;
            if(!autoCompleteConatiner) return false;

            var selectedIndex = autoCompleteConatiner.selectedIndex || 0;
            var length = $("li",autoCompleteConatiner).length;

            if(selectedIndex == 0){
                selectedIndex = length-1;
            }else{
                selectedIndex--;
            }
            $(".selected",autoCompleteConatiner).removeClass("selected");
            $("li:eq("+selectedIndex+")",autoCompleteConatiner).addClass("selected");
            autoCompleteConatiner.selectedIndex = selectedIndex;
        },
        beforeSelect:function(opts,data){
            var beforeSelect = true;
            if(opts.beforeSelect){
                beforeSelect = opts.beforeSelect.call(this,data) !== false;
            }
            return beforeSelect;
        },
        //根据当前选择的条目，设置输入框的值
        select:function(opts){

            var target = this;
            var autoCompleteConatiner = target._autoCompleteConatiner;
            if(!autoCompleteConatiner) return false;
            //通过selectedIndex属性来管理选择的条目，而不是直接通过选择器来获取选择的条目，因为这里是通过html内容对象来查询，没法查询到后面通过js动态改变的内容
            var selectedIndex = autoCompleteConatiner.selectedIndex || 0;
            var data = $("li:eq("+selectedIndex+")",autoCompleteConatiner).data("data");

            if(_autoComplete.beforeSelect.call(target,opts,data)){
                target.value = data.value;
                opts.selectCallback && opts.selectCallback.call(target,data);//回调方法
                $(autoCompleteConatiner).removeClass("active");
            }
        }

    };
})(jQuery);