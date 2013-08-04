/**
 * 表单验证插件
 * 
 * @date 2012-8-14
 * @version: 1.0
 * @author swby
 * @requires jQuery v1.3 or later
 */

(function($) {

	var method = {
		/**
		 * 验证表单里的每一个值
		 * 
		 * @param field
		 *            表单的字段对象
		 * @param type
		 *            验证类型,如数字、邮箱等等
		 */
		validateField : function(field, type, opts) {
			var val = $(field).val();
			var typePrefix = /(.*)\((.*)\)/.exec(type);
			var msg = "";// 错误提示信息,如无错误则为空字符串
			var msgType;// 校验提示信息名字
			type = typePrefix ? typePrefix[1] : type;// 得到验证类型
			var params = typePrefix ? typePrefix[2].split(',') : "";// 验证类型所在的参数
			opts.params = params;
			switch (type) {
			/**
			 * 必填
			 */
			case 'required':
				msgType = typeof (params[0]) != 'undefined' ? params[0] : type;
				if (opts.type == 'text' || opts.type == 'password')
					msg = method.getErrorMsg((val.length == 0), msgType, opts);
				else if (opts.type == 'radio') {// radio必填处理
					msg = method.getErrorMsg(!method.validateRadio(field, type,
							opts), msgType, opts);
				} else {// checkbox 则为至少选中一个
					msgType = typeof (params[0]) != 'undefined' ? params[0]
							: 'mincheckbox';
					opts.minselect = 1;
					opts.params = [ 1 ];
					msg = method.getErrorMsg(!method.validateCheckBox(field,
							type, opts), msgType, opts);
				}
				break;
			/**
			 * 数字验证
			 */
			case 'number':
				msgType = typeof (params[0]) != 'undefined' ? params[0] : type;
				if (!method.checkRegex(val, /^[-,+]?(([1-9]\d*)|0)(.\d+)?$/))
					msg = method.getErrorMsg(true, msgType, opts);
				else {
					if (method.checkRegex(val, /^\+(.*)/))
						$(field).attr("value", val.substring(1, val.length));
					msg = "";
				}
				break;
			/**
			 * 精度处理
			 */
			case 'precision':
				var bit = (params[0] || params[0] != '') ? params[0] : 2;// 默认两位小数

				var regex = new RegExp("^[-,+]?(([1-9]\\d*)|0).\\d{" + bit
						+ ",}")

				if (!method.checkRegex(val, regex)) {
					msgType = typeof (params[2]) != 'undefined' ? params[0]
							: type;
					msg = method.getErrorMsg(true, msgType, opts);
				} else {
					if (method.checkRegex(val, /^\+(.*)/))
						$(field).attr("value", val.substring(1, val.length));
					if (val.length != 0) {
						if (params[1] == 'false')
							$(field).attr("value", parseFloat(val).hold(bit));// 不四舍五入
						else
							$(field).attr("value", parseFloat(val).round(bit));// 四舍五入
					}
					msg = "";
				}

				break;
			/**
			 * 邮箱格式
			 */
			case 'email':
				msgType = typeof (params[0]) != 'undefined' ? params[0] : type;
				msg = method.getErrorMsg(!method
						.checkRegex(val, /^\w+@\w+.\w+/), msgType, opts);

				break;
			case 'eq':
				msgType = typeof (params[1]) != 'undefined' ? params[1] : type;
				msg = method.getErrorMsg($(params[0]).val() != val, msgType,
						opts);

				break;
			case 'gt':
				msgType = typeof (params[1]) != 'undefined' ? params[1] : type;
				msg = method.getErrorMsg(!method.compare(val, $(params[0])
						.val(), 'gt'), msgType, opts);
				break;
			case 'ge':
				msgType = typeof (params[1]) != 'undefined' ? params[1] : type;
				msg = method.getErrorMsg(!method.compare(val, $(params[0])
						.val(), 'ge'), msgType, opts);
				break;
			case 'lt':
				msgType = typeof (params[1]) != 'undefined' ? params[1] : type;
				msg = method.getErrorMsg(!method.compare(val, $(params[0])
						.val(), 'lt'), msgType, opts);
				break;
			case 'le':
				msgType = typeof (params[1]) != 'undefined' ? params[1] : type;
				msg = method.getErrorMsg(!method.compare(val, $(params[0])
						.val(), 'le'), msgType, opts);
				break;
			/**
			 * 输入长度 minsize(v1,msg)
			 */
			case 'minsize':
				msgType = typeof (params[1]) != 'undefined' ? params[1] : type;
				msg = method.getErrorMsg(val.length < params[0], msgType, opts);
				break;
			/**
			 * 输入长度 maxsize(v1,msg)
			 */
			case 'maxsize':
				msgType = typeof (params[1]) != 'undefined' ? params[1] : type;
				msg = method.getErrorMsg(val.length > params[0], msgType, opts);
				break;
			/**
			 * 输入长度 size(v1,v2,msg)
			 */
			case 'size':
				msgType = typeof (params[2]) != 'undefined' ? params[2] : type;
				msg = method.getErrorMsg((val.length < opts.params[0])
						|| (val.length > opts.params[1]), msgType, opts);
				break;
			/**
			 * 时间有效性验证
			 */
			case 'date':
				msgType = typeof (params[0]) != 'undefined' ? params[0] : type;
				msg = method
						.getErrorMsg(
								!method
										.checkRegex(
												val,
												/^(?:(?!0000)[0-9]{4}-(?:(?:0[1-9]|1[0-2])-(?:0[1-9]|1[0-9]|2[0-8])|(?:0[13-9]|1[0-2])-(?:29|30)|(?:0[13578]|1[02])-31)|(?:[0-9]{2}(?:0[48]|[2468][048]|[13579][26])|(?:0[48]|[2468][048]|[13579][26])00)-02-29)$/),
								msgType, opts);
				break;
			/**
			 * 用户自定义正则验证
			 */
			case 'custom':
				msgType = typeof (params[1]) != 'undefined' ? params[1] : type;
				msg = method.getErrorMsg(!method.checkRegex(val, new RegExp(
						params[0].substring(1, params[0].length - 1))),
						msgType, opts);
				break;
			/**
			 * 调用用户自定义方法验证 返回错误信息
			 */
			case 'call':

				$.each(params, function(index, param) {
					var callMsg = window[param](field, type, opts);
					if(callMsg&&callMsg!==true)
					msg += window[param](field, type, opts);
					else
						if(callMsg===false)
							msg+=method.getErrorMsg(true,
									type, opts);
				});
				break;
			/**
			 * 复选框 选择判断
			 */
			case 'minselect':
				msgType = typeof (params[1]) != 'undefined' ? params[1]
						: 'mincheckbox';
				opts.minselect = params[0];
				msg = method.getErrorMsg(!method.validateCheckBox(field, type,
						opts), msgType, opts);
				break;
			case 'maxselect':
				msgType = typeof (params[1]) != 'undefined' ? params[1]
						: 'maxcheckbox';
				opts.maxselect = params[0];
				msg = method.getErrorMsg(!method.validateCheckBox(field, type,
						opts), msgType, opts);
				break;
			default:
				msg = "";
			}
			return msg;
		},
		/**
		 * 验证单选框
		 * 
		 * @param field
		 * @param type
		 * @param opts
		 * @returns {Boolean}
		 */
		validateRadio : function(field, type, opts) {
			var result = false;
			$(":input[name='" + $(field).attr('name') + "'][type='radio']")
					.each(function() {
						if ($(this).attr('checked') == 'checked') {
							result = true;
							return;
						}
					});
			return result;
		},
		/**
		 * 验证复选框
		 * 
		 * @param field
		 * @param type
		 * @param opts
		 * @returns {Boolean}
		 */
		validateCheckBox : function(field, type, opts) {
			var result = true;
			var selectNum = 0;// 选中个数
			var minSelect = opts.minselect;
			var maxSelect = opts.maxselect;
			$(":input[name='" + $(field).attr('name') + "'][type='checkbox']")
					.each(function() {
						if ($(this).attr('checked') == 'checked') {
							selectNum++;
						}
					});

			if (typeof (minSelect) != 'undefined') {
				if (selectNum >= minSelect)
					result = true;
				else
					result = false;

			}

			if (typeof (maxSelect) != 'undefined') {
				if (selectNum <= maxSelect)
					result = result && true;
				else
					result = result && false;
			}

			return result;
		},
		/**
		 * 比较v1和v2的大小关系
		 * 
		 * @param v1
		 * @param v2
		 * @param type
		 * @returns {Boolean}
		 */
		compare : function(v1, v2, type) {
			var result = true;
			switch (type) {
			case 'gt':
				result = (v1 > v2);
				break;
			case 'ge':
				result = (v1 >= v2);
				break;
			case 'lt':
				result = (v1 < v2);
				break;
			case 'le':
				result = (v1 <= v2);
				break;
			default:
				result = true;
			}

			return result;
		},
		/**
		 * 验证表单里的每一个值
		 * 
		 * @param field
		 *            表单的字段对象
		 * @param type
		 *            验证类型字符串,可能包括多个类型如：number,email...
		 */
		validate : function(field, types, options1) {

			var msg = "";
			var callback = "";
			var result = true;
			$.each(types, function(index, value) {
				if (method.checkRegex(value, /^callback\(\w+\)$/)) {

					callback = value.match(/^callback\((.*)\)$/);
					callback = callback ? callback : "";

				}

				msg += method.validateField($(field), value, options1);

			});

			options1.align = 'right';
			options1.msg = msg;
			options1.field = field;
			result = method.showOrHideMsg(options1);
			if (typeof (callback[1]) != 'undefined') {
				window[callback[1]](result);
			} else {
				return result;
			}
		},
		/**
		 * 正则匹配
		 */
		checkRegex : function(value, regex) {
			if (value == '')
				return true;
			var matchArray = value.match(regex);
			if (matchArray == null)
				return false;
			else
				return true;
		},
		/**
		 * 隐藏错误信息显示
		 * 
		 * @param msg
		 */
		hide : function(msg) {
			if (msg) {
				$(msg).fadeOut(1000, function() {
					$(this).remove();
				});
			} else {
				$(".formvalidation_msg_tip").fadeOut(1000, function() {
					$(this).remove();
				});
			}
		},
		/**
		 * 填充形式为${number}参数值
		 * 
		 * @param msg
		 * @param opts
		 * @returns
		 */
		fillValue : function(msg, opts) {
			var params = new Array();
			var $start = msg.indexOf("${");
			while ($start != -1) {
				var $end = msg.indexOf('}', $start + 2);
				params.push(msg.substring($start + 2, $end));
				$start = msg.indexOf("${", $end + 1);
			}
			$.each(params, function(index, value) {
				msg = msg.replace("${" + value + "}", opts[value]);
			});
			return msg;

		},
		/**
		 * 得到错误信息
		 * 
		 * @param flag
		 *            有错误信息
		 * @param type
		 * @returns
		 */
		getErrorMsg : function(flag, type, opts) {
			if (flag) {
				var msg = opts.errorMsg[type];
				var params = new Array();
				var $start = msg.indexOf("${");
				while ($start != -1) {
					var $end = msg.indexOf('}', $start + 2);
					params.push(msg.substring($start + 2, $end));
					$start = msg.indexOf("${", $end + 1);
				}
				$.each(params, function(index, value) {
					msg = msg.replace("${" + value + "}", opts.params[value]);
				});
				return msg + "<br/>";
			} else
				return "";
		},
		/**
		 * 得到需要校验的类型,每个校验类型可能带有多个参数,因此不能用split(',')处理
		 * 
		 * @param typeString
		 * @returns {Array}
		 */
		getTypes : function(typeString) {

			var types = new Array();
			var start = 0;
			var $start = typeString.indexOf("(");

			while (start != -1 && start != typeString.length) {
				if (start == 0) {
					start = -1;
				}
				var end = typeString.indexOf(",", start + 1);
				var $end = typeString.indexOf(')', $start + 1);
				if (end == -1) {
					types.push(typeString.substring(start + 1));
					break;
				}
				if (end > $start && $start != -1) {// 逗号进入括号了
					types.push(typeString.substring(start + 1, $end + 1));
					$start = $end;
					end = (end >= $end) ? end : ($end + 1);
				} else {
					types.push(typeString.substring(start + 1, end));
				}
				start = end;

			}

			return types;
		},
		/**
		 * 控制提示信息显示或隐藏
		 * 
		 * @param opts
		 * @returns {Boolean}
		 */
		showOrHideMsg : function(opts) {
			var result = false;
			if (opts.msg.replace(/<br\/>/g, "") != '') {
				if ($("#msg_index_" + opts.index).length == 0) {// 未创建提示信息
					opts.msg = opts.msg.substring(0, opts.msg.length - 5);

					$('body').append(method.getMsgDiv(opts));
					$(".formvalidation_msg_tip").click(
							function() {
								method.hide(this);
								if (typeof (opts.index) == 'number')
									$("form :input:eq(" + opts.index + ")")
											.focus();
								else {
									$("form :input[name='" + opts.index + "']")
											.focus();
								}
							});
					switch (opts.align) {
					case 'left':
						$("#msg_index_" + opts.index)
								.attr(
										"style",
										"position:absolute;filter:alpha(opacity=0); -moz-opacity:0; -khtml-opacity: 0; opacity: 0;top:"
												+ ($(opts.field).offset().top
														- $(
																"#msg_index_"
																		+ opts.index)
																.height() / 2 + $(
														opts.field).height() / 2)
												+ "px;left:"
												+ ($(opts.field).offset().left - 50 - $(
														"#msg_index_"
																+ opts.index)
														.width()) + "px");
						break;
					case 'top':
						$("#msg_index_" + opts.index)
								.attr(
										"style",
										"position:absolute;filter:alpha(opacity=0); -moz-opacity:0; -khtml-opacity: 0; opacity: 0;left:"
												+ $(opts.field).offset().left
												+ "px;top:"
												+ ($(opts.field).offset().top - 50 - $(
														"#msg_index_"
																+ opts.index)
														.height()) + "px");
						break;
					case 'right':
						$("#msg_index_" + opts.index)
								.attr(
										"style",
										"position:absolute;filter:alpha(opacity=0); -moz-opacity:0; -khtml-opacity: 0; opacity: 0;top:"
												+ ($(opts.field).offset().top
														- $(
																"#msg_index_"
																		+ opts.index)
																.height() / 2 + $(
														opts.field).height() / 2)
												+ "px;left:"
												+ ($(opts.field).offset().left + 50 + $(
														opts.field).width())
												+ "px");
						break;
					case 'bottom':
						$("#msg_index_" + opts.index)
								.attr(
										"style",
										"position:absolute;filter:alpha(opacity=0); -moz-opacity:0; -khtml-opacity: 0; opacity: 0;left:"
												+ $(opts.field).offset().left
												+ "px;top:"
												+ ($(opts.field).offset().top + 50 + $(
														opts.field).height())
												+ "px");
						break;

					}

				} else {
					$("#msg_index_" + opts.index + " .tip-inner")
							.html(opts.msg);
				}

				switch (opts.align) {
				case 'left':
					$("#msg_index_" + opts.index).animate(
							{
								left : ($(opts.field).offset().left - $(
										"#msg_index_" + opts.index).width())
										+ "px",
								opacity : '1'
							});
					break;

				case 'top':
					$("#msg_index_" + opts.index).animate(
							{
								top : ($(opts.field).offset().top - $(
										"#msg_index_" + opts.index).height())
										+ "px",
								opacity : '1'
							});
					break;
				case 'right':
					$("#msg_index_" + opts.index).animate(
							{
								left : ($(opts.field).offset().left + $(
										opts.field).width())
										+ "px",
								opacity : '1'
							});
					break;
				case 'bottom':
					$("#msg_index_" + opts.index).animate(
							{
								top : ($(opts.field).offset().top + $(
										opts.field).height())
										+ "px",
								opacity : '1'
							});
					break;
				}

				result = false;

			} else {

				$("#msg_index_" + opts.index).animate({
					top : $(opts.field).offset().top + "px",
					opacity : '0'
				}, function() {
					$(this).remove();
				});
				result = true;
			}
			return result;
		},
		/**
		 * 得到提示信息的html代码
		 * 
		 * @param opts
		 * @returns {String}
		 */
		getMsgDiv : function(opts) {
			var msgDiv = "";
			switch (opts.align) {
			case 'top':
				msgDiv = ' <div  class="formvalidation_msg_tip tip-darkgray"  id="msg_index_'
						+ opts.index
						+ '" style="position:absolute;filter:alpha(opacity=0); -moz-opacity:0; -khtml-opacity: 0; opacity: 0;">'
						+ '<table border="0" cellpadding="0" cellspacing="0" >'
						+ '<tr>'
						+ '<td class="tip_bg"  style=" height:10px;"></td>'
						+ '<td class="tip_bg" style="background-image: url(css/tip-darkgray.png); height:10px;width:10px;"></td>'
						+ '</tr>'
						+ '<tr >'
						+ '<td class="tip-inner" style="padding-left:12px;">'
						+ opts.msg
						+ '</td>'
						+ '<td class="tip_bg" style="background-position:100% 10%; height:10px;width:10px;"></td></tr>'
						+ '<tr>'
						+ '<td style="background-position:0 100%;background-image: url(css/tip-darkgray.png); height:10px;"></td>'
						+ ' <td class="tip_bg" style="background-position:100% 100%; height:10px;width:10px;"></td>'
						+ ' </tr>'
						+ '</table>'
						+ '<div class=" tip-arrow-bottom"></div>' + '</div>';
				break;
			case 'left':
				msgDiv = ' <div  class="formvalidation_msg_tip tip-darkgray"  id="msg_index_'
						+ opts.index
						+ '" style="position:absolute;filter:alpha(opacity=0); -moz-opacity:0; -khtml-opacity: 0; opacity: 0;">'
						+ '<table border="0" cellpadding="0" cellspacing="0" >'
						+ '<tr>'
						+ '<td class="tip_bg" style=" height:10px;"></td>'
						+ '<td class="tip_bg" style="background-position:100% 0;height:10px;width:10px;"></td>'
						+ '<td style="width:11px;"></td></tr>'
						+ '<tr >'
						+ '<td class="tip-inner" style="padding-left:12px;">'
						+ opts.msg
						+ '</td>'
						+ '<td class="tip_bg" style="background-position:100% 10%; height:10px;width:10px;"></td>'
						+ '<td><div class="tip-arrow-right" style="width:11px;height:21px"></div></td></tr><tr>'
						+ '<td class="tip_bg" style="background-position:0 100%; height:10px;"></td>'
						+ ' <td class="tip_bg" style="background-position:100% 100%; height:10px;width:10px;"></td>'
						+ ' <td></td></tr>' + '</table>' + '</div>';
			case 'right':
				msgDiv = ' <div  class="formvalidation_msg_tip tip-darkgray"  id="msg_index_'
						+ opts.index
						+ '" style="position:absolute;filter:alpha(opacity=0); -moz-opacity:0; -khtml-opacity: 0; opacity: 0;">'
						+ '<table border="0" cellpadding="0" cellspacing="0" >'
						+ '<tr><td style="width:11px;"></td>'
						+ '<td class="tip_bg" style="height:10px;"></td>'
						+ '<td class="tip_bg" style="background-position:100% 0; height:10px;width:10px;"></td>'
						+ '</tr>'
						+ '<tr ><td><div class="tip-arrow-left"></div></td>'
						+ '<td class="tip-inner" style="padding-left:12px;">'
						+ opts.msg
						+ '</td>'
						+ '<td class="tip_bg" style="background-position:100% 10%; height:10px;width:10px;"></td>'
						+ '</tr><tr><td></td>'
						+ '<td class="tip_bg" style="background-position:0 100%; height:10px;"></td>'
						+ ' <td class="tip_bg" style="background-position:100% 100%; height:10px;width:10px;"></td>'
						+ ' </tr></table></div>';
				break;
			case 'bottom':
				msgDiv = ' <div  class="formvalidation_msg_tip tip-darkgray"  id="msg_index_'
						+ opts.index
						+ '" style="position:absolute;filter:alpha(opacity=0); -moz-opacity:0; -khtml-opacity: 0; opacity: 0;">'
						+ '<div class=" tip-arrow-top"></div><table border="0" cellpadding="0" cellspacing="0" >'
						+ '<tr>'
						+ '<td style="background-image: url(css/tip-darkgray.png); height:10px;"></td>'
						+ '<td style="background-position:100% 0;background-image: url(css/tip-darkgray.png); height:10px;width:10px;"></td>'
						+ '</tr>'
						+ '<tr >'
						+ '<td class="tip-inner" style="padding-left:12px;">'
						+ opts.msg
						+ '</td>'
						+ '<td style="background-position:100% 10%;background-image: url(css/tip-darkgray.png); height:10px;width:10px;"></td></tr>'
						+ '<tr>'
						+ '<td style="background-position:0 100%;background-image: url(css/tip-darkgray.png); height:10px;"></td>'
						+ ' <td style="background-position:100% 100%;background-image: url(css/tip-darkgray.png); height:10px;width:10px;"></td>'
						+ ' </tr>' + '</table>' + '</div>';
				break;

			default:
				msgDiv = '没有指定错误信息显示方向';

			}
			return msgDiv;
		}

	};

	/**
	 * 默认的提示信息
	 */
	$.fn.errorMsg = {
		'required' : '*该输入项为必填项',
		'number' : '*该输入项应为数字',
		'email' : '*邮箱格式不正确',
		'phoneNumber' : '*电话号码格式不正确',
		'eq' : '*两次输入不能匹配',
		'gt' : '*大小有问题gt',
		'ge' : '*大小有问题ge',
		'lt' : '*大小有问题lt',
		'le' : '*大小有问题le',
		'minsize' : '*至少输入${0}个字符',
		'maxsize' : '*最多输入${0}个字符',
		'size' : '*该输入项长度应在${0}和${1}之间',
		'date' : '*日期不正确',
		'precision' : "*请输入${0}位小数",
		'custom' : '*自定义正则表达式验证失败',
		'mincheckbox' : '*至少选中${0}项',
		'maxcheckbox' : '*最多选中${0}项',
		'call':'*用户自定义验证未通过(call:function)'
	};
	/**
	 * 默认值
	 */
	$.fn.defaults = {
		css : 'darkgray',
		align : 'top'
	};

	/**
	 * 验证插件方法入口
	 */
	$.fn.validate = function(options) {
		if (options == 'hide') {
			method.hide();
			return;
		}
		var $form = $(this);
		$form.find("[validate]").each(function(index, value) {
			var options1 = $.extend({}, $.fn.defaults, options);
			options1.index = index;
			options1.errorMsg = $.extend({}, $.fn.errorMsg, options.errorMsg);
			var opts = $(value).attr("validate");
			var field = $(this);
			opts = /validate\[(.*)\]/.exec(opts);

			if (!opts)
				return false;

			var types = method.getTypes(opts[1]);
			options1.type = $(field).attr("type");
			if (options1.type == 'radio' || options1.type == 'checkbox') {
				options1.index = $(field).attr('name');
			}

			$($form).bind("submit", function() {// 表单提交事件绑定
				return method.validate($(field), types, options1);
			});
			/**
			 * 各个表单项的blur或click事件绑定
			 */
			if (options1.type == 'radio' || options1.type == 'checkbox') {
				$(field).bind('click', function() {
					method.validate($(field), types, options1);

				});
			} else {
				$(field).bind('blur', function() {

					method.validate($(field), types, options1);

				});
			}
		});
	};
	/**
	 * 四舍五入
	 * 
	 * @param len
	 * @returns {Number}
	 */
	Number.prototype.round = function(len) {
		var add = 0;
		var s;
		var s1 = this + "";
		var start = s1.indexOf(".");
		start = start + parseInt(len) + 1;
		if (s1.substr(start, 1) >= 5)
			add = 1;
		var temp = Math.pow(10, len);
		s = Math.floor(this * temp) + add;

		return s / temp;
	};

	/**
	 * 直接保留位数 不四舍五入
	 * 
	 * @param len
	 * @returns {Number}
	 */
	Number.prototype.hold = function(len) {
		var s;
		var temp = Math.pow(10, len);
		s = Math.floor(this * temp);
		return s / temp;
	};

})(jQuery);