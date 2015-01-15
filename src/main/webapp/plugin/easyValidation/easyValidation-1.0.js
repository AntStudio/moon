/**
 * 表单验证插件
 * 
 * @date 2012-8-14
 * @version: 1.0
 * @author gavincook
 * @requires jQuery v1.3 or later
 */

(function($) {

	var formCache={};//表单缓存
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
					msg=method.getErrorMsg((val.length == 0), msgType, opts);
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

				var regex = new RegExp("^[-,+]?(([1-9]\\d*)|0)\\.\\d{2,}$");
			 
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
						.checkRegex(val, /^\w+@\w+(\.[a-zA-Z]+){1,2}$/), msgType, opts);

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
				var gdfd = $.Deferred();
				var dfds = new Array();
				var callMsg="";
				$.each(params, function(index, param) {
					var dfd = $.Deferred();
					if(window[param](field, type, opts) && jQuery.isFunction(window[param](field, type, opts).promise)){
						
						window[param](field, type, opts).done(function(data){
							callMsg+=data;
							
							dfd.resolve(data);
						});
						
					}else{
						callMsg += window[param](field, type, opts);
					    dfd.resolve(msg);
					}
					dfds.push(dfd);
				});
				method.when(dfds).done(function(){
					gdfd.resolve(callMsg);	
				});
				msg=gdfd.promise();
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

			if(msg&& $(field).attr("errMsg")){
				msg =  $(field).attr("errMsg")+"</br>";
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
			var dfds1 = new Array();
			$.each(types, function(index, value) {
				if (method.checkRegex(value, /^callback\(\w+\)$/)) {
					callback = value.match(/^callback\((.*)\)$/);
					callback = callback ? callback : "";
				}				 
				var dfd = $.Deferred()
				var doValidate=method.validateField($(field), value, options1);
				if(doValidate&&jQuery.isFunction(doValidate.promise)){
				doValidate.done(function(data){
						msg+=data;
						dfd.resolve(data);

					});
				}else{
					msg+=doValidate;
					dfd.resolve(doValidate);
				}
				
				dfds1.push(dfd.promise());
				
			});
			var gdfd = $.Deferred();
			method.when(dfds1).done(function(){
				options1.align = field.attr("msgAlign")||$.fn.defaults.align;
				options1.msg = msg;
				options1.field = field;
				result = method.showOrHideMsg(options1);
				if (typeof (callback[1]) != 'undefined') {
					gdfd.resolve(window[callback[1]](result));
				} else {
					gdfd.resolve(result);
				}
			});
			return gdfd.promise();
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
				$(".easyValidation").fadeOut(1000, function() {
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
			var regexString = /[\w\d]+(\([\w\d_#\.,]*\))?/g;
			return typeString.match(regexString);
		},
		/**
		 * 控制提示信息显示或隐藏
		 * 
		 * @param opts
		 * @returns {Boolean}
		 */
		showOrHideMsg : function(opts) {
			var result = false;

			/**
			**设置msg初始位置
			**/
			function setMsgPostion($msgDiv,align){
				var top,left;
				switch(align){
					case "left" : top=$(opts.field).offset().top-$msgDiv.height() / 2 + $(opts.field).height() / 2;
								  left=($(opts.field).offset().left - 50 -$(opts.field).width());
								  break;
					case "right": top=$(opts.field).offset().top-$msgDiv.height() / 2 + $(opts.field).height() / 2;
								  left=($(opts.field).offset().left + 50 +$(opts.field).width());
								  break;
					case "top"  : top=$(opts.field).offset().top- $msgDiv.height()-50;
								  left=($(opts.field).offset().left+$(opts.field).width()/2-50);
								  break;
					case "bottom" : top=$(opts.field).offset().top+ $(opts.field).height()+50;
								  left=$(opts.field).offset().left+$(opts.field).width()/2-50;
								  break;
				}
				$msgDiv.css("top",top).css("left",left);
			}

			/**
			**动画显示提示信息
			**/
			function show($msgDiv,align){
				var top,left;
				switch(align){
					case "left" : left=($(opts.field).offset().left -10 -$msgDiv.width());
								  break;
					case "right": left=($(opts.field).offset().left + 20 +$(opts.field).width());
								  break;
					case "top"  : top=$(opts.field).offset().top- $msgDiv.height()-10;
								  break;
					case "bottom" : top=$(opts.field).offset().top+ $(opts.field).height()+20;
								  break;
				}
				$("#msg_index_" + opts.index).animate({
					left : left	+ "px",
					opacity : '1',
					top:top+"px"
				});
			}

			if (opts.msg.replace(/<br\/>/g, "") != '') {
					var $msgDiv;
				var timeout = ($("#msg_index_" + opts.index).length==0)?0:1000;
				if ($("#msg_index_" + opts.index).length == 0||($("#msg_index_" + opts.index).css("opacity")<1&&$("#msg_index_" + opts.index).css("opacity")>0)) {// 未创建提示信息
					opts.msg = opts.msg.substring(0, opts.msg.length - 5);//取消</br>
				    $msgDiv =  $(method.getMsgDiv(opts));
					$('body').append($msgDiv);
					setMsgPostion($msgDiv,opts.align);
				} else {
					$("#msg_index_" + opts.index + " .tip-inner").html(opts.msg);
				}
				setTimeout(function(){
					show($msgDiv,opts.align);
				},timeout);
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
			var theme = $.fn.defaults.theme;
			function getByAlign(align){
				return  "<div class=\" easyValidation transparent "
						+theme
						+"\" id=\"msg_index_"
						+ opts.index+"\" >"
						+"<div class=\"tooltip "
						+align
						+"\">"
					    +"<div class=\"tooltip-inner\">"
						+"<span>"
						+opts.msg
						+"</span>"
				        +"</div>"
					    +"<div class=\"tooltip-arrow\"></div>"
					    +"</div></div>";
			}
			return getByAlign(opts.align);
		},
		when: function (dfds) {
	             var args = dfds,
	             i = 0,
	             length = args.length,
	             count = length,
	             deferred = length <= 1 && dfds[0] && jQuery.isFunction(dfds[0].promise) ?
	                 dfds[0] :
	                 jQuery.Deferred();
				 var result = true; 
	              // 构造成功（resolve）回调函数
	             function resolveFunc(i) {
	                 return function (value) {
						result=result&&value;
	                     // 如果传入的参数大于一个，则将传入的参数转换为真正的数组 sliceDeferred=[].slice
	                     //args[i] = arguments.length > 1 ? sliceDeferred.call(arguments, 0) : value;
	                     //直到count为0的时候
	                     if (!(--count)) {
	                         // Strange bug in FF4:
	                         // Values changed onto the arguments object sometimes end up as undefined values
	                         // outside the $.when method. Cloning the object into a fresh array solves the issue
	                         //resolve deferred 响应这个deferred对象，上面这句话好像是解决一个奇怪的bug
	                         deferred.resolve(result);
	                     }
	                 };
	             }
	             if (length > 1) {
	                 for (; i < length; i++) {
	                     //存在agrs[i]并且是args[i]是deferred对象，那这样的话作者怎么不直接jQuery.isFunction(args[i].promise)，感觉多判断了，作者也蒙了吧
	                     if (args[i] && jQuery.isFunction(args[i].promise)) {
	                         //执行一次resolveFunc(i)count就减少一个
	                         args[i].promise().then(resolveFunc(i), deferred.reject);
	                     } else {
	                       // 计数器，表示发现不是Deferred对象，而是普通JavaScript象 ,反正最后只要count==0才能resovle deferred
	                         --count;
	                     }
	                 }
	                 if (!count) {
	                     deferred.resolve(result);
	                 }
	             } else if (deferred !== dfds[0]) {  //如果只传了一个参数，而这个参数又不是deferred对象，则立即resolve
					
	                 deferred.resolve(length ? [dfds[0]] : []);
	             }
	             return deferred.promise();  //返回deferred只读视图
	         }
	};

	
	/**
	 * 默认值
	 */
	$.fn.defaults = {
		theme : 'darkgray',
		align : 'top',
		errorMsg : {
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
			'maxcheckbox' : '*最多选中${0}项'
	    }
	};

var resultMap = {};
	/**
	 * 验证插件方法入口
	 */
	$.fn.validate = function(options) {
		if (options == 'hide') {
			method.hide();
			return;
		}
		var $form = $(this);
		var formId = $form.attr("id")+"-"+$form.attr("name");
		if(options=="validate"){
			options = formCache[$form.selector].options; 
			return doValidate();
		}
		
		 
		$.fn.defaults.align =options.align|| $.fn.defaults.align;
		$.fn.defaults.theme =options.theme|| $.fn.defaults.theme;
		var fields = new Array();
		function getFields(){
			fields = new Array();
			$form.find("[validate]:not(:hidden)").each(function(index, value) {
				var options1 = $.extend({}, $.fn.defaults, options);
				options1.index = index;
				options1.errorMsg = $.extend({}, $.fn.defaults.errorMsg, options.errorMsg);
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
				fields.push({"field":$(field),"types":types,"options1":options1});
			});
		}
		getFields();
		if(isCached($form.selector)){
			console.log(options);
			formCache[$form.selector].options = options;
			return;
		}
		$.each(fields,function(index,e){
			/**
			 * 各个表单项的blur或click事件绑定
			 */
			if (e.options1.type == 'radio' || e.options1.type == 'checkbox') {
				$(e.field).bind('click', function() {
					method.validate($(e.field), e.types, e.options1);

				});
			} else {
				$(e.field).bind('blur', function() {
					method.validate($(e.field), e.types, e.options1);

				});
			}
		});
        var result = false;
		$form.find(":input").keyup(function(){
			result = false;
		});

		$form.bind("submit", function(e) {// 表单提交事件绑定
			doValidate().done(function(result){
				if(result){
					$form.submit();
				}
			});
		 	return result;
		});

		function doValidate(){
			getFields();
			var dfd  = $.Deferred();
			var $resultDfds = new Array();
			$.each(fields,function(index,data){
				$resultDfds.push(method.validate(data.field, data.types, data.options1));
			});
			method.when($resultDfds).done(function(data){
				 result = data;
				 resultMap[formId]=data;
				 dfd.resolve(result);
			 });

			return dfd.promise();
		}
		//点击msg，msg消失并使相应表单获取焦点
		$(document).on("click",".easyValidation",function() {
			method.hide(this);
			var index = $(this).attr("id").replace("msg_index_","");
			if (/^\d+$/.test(index)){
				$("form :input:eq(" + index + ")").focus();
			}else {
				$("form :input[name='" + index + "']").focus();
			}
		});

		if(options == "getResult"){
			return resultMap[formId]||false;
		}
		
		formCache[$form.selector]={"form":$form,"options":options};
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
		if(start!=-1){
			start = start + parseInt(len) + 1;
		}
		if (start!=-1&&s1.substr(start, 1) >= 5)
			add = 1;
		var temp = Math.pow(10, len);
		s = Math.floor(this * temp) + add;
		return (s / temp).toFixed(len);
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

	/**
	 * 表单是否被缓存
	 */
	function isCached(selector){
		for(var s in formCache){
			if(selector==s){
				return true;
			}
		}	
		return false;
	}
})(jQuery);