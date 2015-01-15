(function($) {
	/**
	 * 重置表单
	 */
	$.fn.reset = function() {
		$(":text,:password", this).val("");
        $(":input:not(:button,:submit,:reset,select)").val("");
		$(":radio", this).prop("selected", false);
		$(":checkbox", this).prop("checked", false);
	};

	/**
	 * 获取表单数据,如果属性名相同,那么表单属性覆盖初始化属性
	 */
	$.fn.formData = function(initData) {
		var $inputs = $(':input', this).not(
				':button, :submit, :reset,[name="repassword"]');
		var data = initData || {};
		$.each($inputs, function(index, input) {
			var $input = $(input);
			data[$input.attr("name")] = $input.val();
		});

		return data;
	};

    /**
     * 封装ajax数据交互
     */
    var _defaults = {
        type : "Get",
        dataType : "json",
        checkPermission : false,
        traditional:false
    };

	/**
	 * ajax submit form
     **/
	$.fn.ajaxSubmitForm = function(url, data,opts) {
		var $dfd = $.Deferred();
        opts = $.extend({},{type : "Post",dataType : "json"},opts);
		$.ajax({
			url : url,
			data : $(this).serialize() + $.serializeToUrl(data, "&"),
			type : opts.type,
			dataType : opts.dataType
		}).done(function(result){
			$dfd.resolve(result);
		}).fail(function(jqXHR, textStatus, errorThrown){
			$dfd.reject(jqXHR, textStatus, errorThrown);
		});
		return $dfd.promise();
	};

	/**
	 * to turn a new url by type
	 */
	$.href = function(url, type) {
		if (typeof (type) == "undefined" || type == 'current')
			window.location.href = url;
		else {
			if (type == "new") {
				window.open(url);
			}
		}
	};

	$.serializeToUrl = function(o, prefix) {
		if (typeof (prefix) == 'undefined') {
			prefix = "";
		}
		for ( var i in o) {
            if(o[i] instanceof Array){
                for(var ii in o[i]){
                    prefix += i + "=" + o[i][ii] + "&";
                }
            }else {
                prefix += i + "=" + o[i] + "&";
            }
		}

		return prefix.substring(0, prefix.length - 1);
	};

	$.fn.fillForm = function(data) {
		var $inputs = $(':input', this).not(
				':button, :submit, :reset,:password,:file');
		$.each($inputs, function(index, e) {
			var temp;
			var name = $(e).attr("name");
			var position = name.indexOf(".");
			if (position != -1) {
				temp = data[name.substring(position + 1)];
			} else {
				temp = data[name];
			}
			if (typeof (temp) != "undefined" && temp != null && temp != "null") {
                $(e).val(temp);
            }
		});
	};
	
	$.fn.autoCompleteForm = function(url,params,opts,initData,formatData,callback){
		var $form = $(this);
        if($.isFunction(opts)){
            callback = opts;
            opts = {};
        }
		$.getJsonData(url, params, opts).done(function(data){
			if($.isFunction(formatData)){
				data = formatData.call(data);
			}else{
				data = data.result;
			}
			data = $.extend({},initData,data);
            callback&&callback.call($form,data);
			$form.fillForm(data);
		});
	};
	

	$.getJsonData = function(url, params, opts) {
		var dfd = $.Deferred();
		opts = $.extend({}, _defaults, opts);
        params = $.extend({},params,{_random:Math.random()});
		$.ajax({
			url : url,
			type : opts.type,
			dataType : opts.dataType,
			data : params,
            traditional:opts.traditional
		}).done(function(data, textStatus, jqXHR) {
            if(data.throwable != null){
                moon.error("服务器出错:"+data.throwable.cause.localizedMessage);
                dfd.reject(data, textStatus, jqXHR);
            }
			if (opts.checkPermission) {
				if (!data.permission) {
					moon.warn("对不起，无权限执行此操作");
					dfd.reject(data);
				}
				dfd.resolve(data);
			} else {
				dfd.resolve(data, textStatus, jqXHR);
			}
		}).fail(function(jqXHR, textStatus, errorThrown) {
			dfd.reject(jqXHR, textStatus, errorThrown);
		});

		return dfd.promise();
	};

    /**
     * 渲染模板
     * @param data
     * @param opts
     * @returns {*}
     */
    $.fn.renderTemplate = function(data,opts){
        var html = Handlebars.compile($(this).html())(data);
        if(opts && opts.container){
            var $container = $(opts.container);
            if(opts.emptyParent === true){
                $container.empty();
            }
            $container.append(html);
        }
        return html;
    };

})(jQuery);

var moon = moon || {};

(function(m) {
	m.format = function() {
		if (arguments.length == 0) {
			return;
		}
		var src = arguments[0];
		for (var i = 1, l = arguments.length; i < l; i++) {
			src = src.replace(new RegExp("\\{" + (i - 1) + "\\}", "gi"),
					arguments[i]);
		}
		return src;
	};

	//删除数组的元素
	m.removeItem = function(array,item){
		var index = 0;
		$.each(function(i,e){
			if(e === item){
				index = i;
				return false;
			}
		});
		array.splice(index,1);
		return array;
	};

	m.alert = function(options, layout) {
		_noty(formatParam(options), {
			type : 'alert',
			layout : layout || "topCenter"
		});
	};

	m.info = function(options, layout) {
		var _n = _noty(formatParam(options), {
			type : 'information',
			layout : layout || "topCenter"
		});
		setTimeout(function() {
			$.noty.close(_n.options.id);
		}, 3000);
	};

	m.error = function(options, layout) {
		var _n = _noty(formatParam(options), {
			type : 'error',
			layout : layout || "topCenter"
		});
		setTimeout(function() {
			$.noty.close(_n.options.id);
		}, 3000);
	};

	m.warn = function(options, layout) {
		var _n = _noty(formatParam(options), {
			type : 'warning',
			layout : layout || "topCenter"
		});
		setTimeout(function() {
			$.noty.close(_n.options.id);
		}, 3000);
	};

	m.success = function(options, layout) {
		var _n = _noty(formatParam(options), {
			type : 'success',
			layout : layout || "topCenter"
		});
		setTimeout(function() {
			$.noty.close(_n.options.id);
		}, 3000);
	};

	m.confirm = function(options, layout) {
		var dfd = $.Deferred();
		_noty(formatParam(options), {
			type : 'confirm',
			layout : layout || "center",
			modal : true,
			buttons : [ {
				text : "确认",
				addClass : "btn btn-small btn-primary",
				onClick : function(_n) {
					dfd.resolve(true);
					_n.close();
				}
			}, {
				text : "取消",
				addClass : "btn btn-small",
				onClick : function(_n) {
					dfd.resolve(false);
					_n.close();
				}
			} ]
		});

		return dfd.promise();
	};

    m.filePrefix = contextPath+"/file/get/";

    /**
     * 请求全屏
     */
    m.requestFullScreen = function() {
        var de = document.documentElement;
        if (de.requestFullscreen) {
            de.requestFullscreen();
        } else if (de.mozRequestFullScreen) {
            de.mozRequestFullScreen();
        } else if (de.webkitRequestFullScreen) {
            de.webkitRequestFullScreen();
        }else if (de.msRequestFullscreen) {
            de.msRequestFullscreen();
        }
    }

    /**
     * 退出全屏
     */
    m.exitFullscreen = function() {
        var de = document;
        if (de.exitFullscreen) {
            de.exitFullscreen();
        } else if (de.mozCancelFullScreen) {
            de.mozCancelFullScreen();
        } else if (de.webkitCancelFullScreen) {
            de.webkitCancelFullScreen();
        }else if (de.msExitFullscreen) {
            de.msExitFullscreen();
        }
    }

    /**
     * 用于存储目前打开了的菜单Tab
     * @type {Array}
     */
    if(self!=top) {
        var $w = self;
        while($w!=top){
            $w = $w.parent;
        }
        m.menus = $w.moon.menus;
        m.tabs = $w.moon.tabs;
        m.top = $w;
    }else{
        m.menus = new Array();
        m.top = self;
    }

    /**
     * 根据菜单名字删除菜单数组中的菜单
     * @param name
     */
    m.deleteTab = function(name){
        for(var menuIndex in m.menus){
            if(m.menus[menuIndex] == name){
                m.menus.splice(parseInt(menuIndex),1);//tab是从1开始的,并且第一个主页介绍不能关闭
                return;
            }
        }
    }

    var _tab_opts = {
                    closeAble:true,//是否可以关闭
                    refresh:false//当已经有同名的标签，再次添加时是否需要刷新
                    };
    /**
     * 添加菜单
     * @param name 菜单名字
     * @param url 菜单地址
     * @param opts tab配置，如{closeAble:true,refresh:false}
     */
    m.addTab = function(name,url,opts){
        opts = $.extend(_tab_opts,opts);
        if(typeof(m.tabs) == "undefined"){
            window.location.href = url;
        }

        for(var menuIndex in m.menus){
            if(m.menus[menuIndex] == name){
                m.tabs.tab(parseInt(menuIndex)+2);//tab是从1开始的,并且第一个主页介绍不能关闭
                if(opts.refresh) {//需要刷新
                    $(".tabcont:eq(" + (parseInt(menuIndex) + 1) + " ) iframe", m.top.window.document).attr("src", url);
                }
                return;
            }
        }
        m.menus.push(name);
        m.tabs.add("<span><span class=\"title\">"+name+"</span>"+(opts.closeAble?"<i class=\"fa fa-times pointer close-tab\"></i>":"")+"</span>",
                "<iframe src=\""+url+"\" name=\"main\" allowfullscreen=\"true\"></iframe>",
                true);
    }

    /**
     * 模板渲染
     * @param template
     * @param data
     * @param opts
     * @returns {*}
     */
    m.renderTemplate = function(template,data,opts){
        var html = Handlebars.compile(template)(data);
        if(opts && opts.container){
            var $container = $(opts.container);
            if(opts.emptyParent === false){
                $container.empty();
            }
            $container.append(html);
        }
        return html;
    };

    /**
     * 随机字符串 ascii 48到126
     * @param length
     * @returns {string}
     */
    m.randomString = function(length){
        if(typeof(length) == "undefined" || length < 0){
            return "";
        }
        var randomString = "";
        while(length-->0){
            randomString += String.fromCharCode(47+parseInt(Math.random()*79));
        }
        return randomString;
    }
    /**
     * 系统常量
     */
    m.constants = {
        "consultation.notice.unread":0,
        "consultation.notice.read":1,
        "userType.patient":0,
        "userType.doctor":1,
        "userType.admin":2,
        "article.type.news":1,
        "article.type.library":2,
        "article.domain.all":0,
        "friend.request.agree":1,
        "friend.request.refuse":2
    }
	/** ************* 私有方法 ********************** */

	/**
	 * 格式化通知noty参数,
	 */
	function formatParam(options) {
		if (typeof (options) == 'string') {
			options = {
				text : options
			};
		}
		return options;
	}

	function _noty(op1, op2) {
		return noty($.extend({
			layout : "topCenter"
		}, op1, op2));
	}

	/** ************* /私有方法 ********************** */
})(moon);