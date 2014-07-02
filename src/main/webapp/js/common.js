(function($) {
	/**
	 * 重置表单
	 */
	$.fn.reset = function() {
		$(":text,:password", this).val("");
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
	 * ajax submit form
     **/
	$.fn.ajaxSubmitForm = function(url, data) {
		var $dfd = $.Deferred();
		$.ajax({
			url : url,
			data : $(this).serialize() + $.serializeToUrl(data, "&"),
			type : 'post',
			dataType : 'json'
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
			prefix += i + "=" + o[i] + "&";
		}

		return prefix.substring(0, prefix.length - 1);
	};

	$.fn.fillForm = function(data) {
		var $inputs = $(':input', this).not(
				':button, :submit, :reset,:password');
		$.each($inputs, function(index, e) {
			var temp;
			var name = $(e).attr("name");
			var position = name.indexOf(".");
			if (position != -1) {
				temp = data[name.substring(position + 1)];
			} else {
				temp = data[name];
			}
			if (typeof (temp) != "undefined" && temp != null && temp != "null")
				$(e).val(temp);
		});
	};
	
	$.fn.autoCompleteForm = function(url,params,opts,initData,formatData){
		var $form = $(this);
		$.getJsonData(url, params, opts).done(function(data){
			if($.isFunction()){
				data = formatData.call(data);
			}else{
				data = data.result;
			}
			data = $.extend({},initData,data);
			$form.fillForm(data);
		});
	};
	
	/**
	 * 封装ajax数据交互
	 */
	var _defaults = {
		type : "Get",
		dataType : "json",
		checkPermission : false
	};
	$.getJsonData = function(url, params, opts) {
		var dfd = $.Deferred();
		opts = $.extend({}, _defaults, opts);

		$.ajax({
			url : url,
			type : opts.type,
			dataType : opts.dataType,
			data : params
		}).done(function(data, textStatus, jqXHR) {
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
				onClick : function() {
					dfd.resolve(false);
					_n.close();
				}
			} ]
		});

		return dfd.promise();
	};

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