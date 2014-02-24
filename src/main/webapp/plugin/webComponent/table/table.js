/**
 * 表格组件,支持ajax获取数据
 * 待完成:1、api文档
 * 	    2、pageSize动态设置
 * 		3、表格风格优化(done)
 * 		4、表格行选择支持(done)
 * 		5、拖拽式选择(done)
 * @author Gavin Cook
 * @Date 2012-08-25
 */
(function(){
	var defaults = {
		url:"",
		columns:[],
	    pageIndex:1,
		pageSize:10,
		params:{},
		formatData:'',
		showSelectBox:false,//是否显示单选框或者复选框
		multiSelect:true,//是否允许多选
		showNumber:true,//是否显示序号(从1开始计数)
		buttons:[],
		emptyParent:true//是否清空容器
	};

	var methods  = {
		renderData:function(opts){
			var $container = this;
			var getData = methods.getData.call(this,opts);
			var dfd = $.Deferred();
			getData.done(function(data){
				opts.currentDataSize = data.length;
				var dataHtml="<tbody>";
				tableDataCache[$container.selector]=data;
				$.each(data,function(index,columnData){
					dataHtml+="<tr data-number=\""+(index)+"\" data-id=\"tr_"+columnData[(opts.rowId||{})]+"\"";//data-number表示当前行的序号,用户获取选择行使用,从0开始计数
					if(columnData.checked){//是否选中
						dataHtml+=" class=\"selected\" ";
					}
					dataHtml+=">";
					if(opts.showNumber){//处理序号列
						dataHtml+="<td class=\"number\">"+(index+1)+"</td>";
					}
					if(opts.showSelectBox){//处理单选框或复选框
						dataHtml+="<td><input name=\"selectBox\" type=\""+(opts.multiSelect?"checkbox":"radio")+"\"/></td>";
					}
					$.each(opts.columns,function(index,columnDefinition){
						if($.isFunction(columnDefinition.render)){
							dataHtml+="<td  style=\"text-align:"+(columnDefinition.align||"left")+"\">"+columnDefinition.render(columnData)+"</td>";
						}else{
						    dataHtml+="<td  style=\"text-align:"+(columnDefinition.align||"left")+"\">"+columnData[columnDefinition.name]+"</td>";
						}
					});
					dataHtml+="</tr>";
				});
				 dataHtml+="</tbody>";
				dfd.resolve(dataHtml);
			});

			return dfd.promise();
		},
		renderHeader:function(opts){
			var header = "<thead><tr>";
			if(opts.showNumber){
			    header+="<th class=\"number\"></th>";
			}
			if(opts.showSelectBox){
				header+="<th class=\"select-box\">"+(opts.multiSelect?"<input type=\"checkbox\"/>":"")+"</th>";
			}
			$.each(opts.columns,function(index,column){
				if(column.width){
					if(typeof column.width=="number"){
						column.width +="px";
					}
				}
				header+="<th data-name=\""+(column.sortName||column.name)+"\" class=\""+(column.sort?"sort-column":"")+"\" style=\"text-align:"+(column.align||"left")+"; width:"+(column.width||"auto")+";\">"+(column.display||column.name);
				if(column.sort){
					header+="<i class=\"icon-angle-down  transparent\"></i> <i class=\"icon-angle-up hide\"></i> ";
				}
				header+="</th>";
			});
			header+="</tr></thead>";
			return header;
		},
		renderTable:function(opts){
			var $container = this;
			table.autoLayout = ($container.height()==0);//是否自适应布局
			table.opts = opts;
			table.selector = $container.selector;
			
			var $tableDiv = $(document.createElement("div"));
			opts.table = $tableDiv;
			$tableDiv.addClass("datagrid").append(methods.renderTitle(opts));
			if(!table.autoLayout){
				$tableDiv.addClass("fixed-layout");
			}
			var tableHtml = "";
			tableHtml+="<div class=\"table-container\"><table class=\"table table-bordered table-hover\">";
			tableHtml+=methods.renderHeader(opts);
			var renderTableDfd = $.Deferred();
			methods.renderData.call($container,opts).done(function(tbody){
				tableHtml+=tbody;
				renderTableDfd.resolve();
			});
			
			$.when(renderTableDfd).done(function(){
				tableHtml+="</table></div>";
				tableHtml+=methods.renderPagination(opts);
				tableHtml+=methods.renderModal();
				tableHtml+=methods.renderSelection();
				tableHtml+="</div>";
				if(opts.emptyParent){
					$container.html($tableDiv.append($(tableHtml)));
				}else{
					$container.append($tableDiv.append($(tableHtml)));
				}
				tableCache[$container.selector] = table;
				methods.bindEvents.call($container,opts);
			});
			table.methods = methods;
			return table;
		},
		renderPagination:function(opts){
			var startIndex = (opts.pageIndex-1)*opts.pageSize+1;
			var endIndex = startIndex+opts.currentDataSize-1;
			var pageCount = Math.ceil(opts.total/opts.pageSize);
			opts.pageCount = pageCount||1;

			var paginationHtml = "<div class=\"grid-pagination\">"
		   +"<span class=\"pagination-btn\" action=\"first\"> <i class=\"icon-step-backward\"></i></span>"
		   +"<span class=\"pagination-btn\" action=\"prev\"> <i class=\"icon-play icon-prev\"></i></span>"
		   +"<input type=\"text\" name=\"currentPage\" class=\"input-small\" value=\"" 
		   +(opts.pageIndex||1)
		   +"\"/>/"
		   +"<span class=\"pagecount\">"
		   +pageCount
		   +"</span>"
		   +"<span class=\"pagination-btn\" action=\"next\"> <i class=\"icon-play\"></i></span>"
		   +"<span class=\"pagination-btn\" action=\"last\"> <i class=\"icon-step-forward\"></i></span>"
		   +"<span class=\"split\"></span>"
		   +"<span class=\"pagination-btn\" action=\"refresh\"> <i class=\"icon-refresh\"></i></span><!-- icon-spin-->"
		   +"<div class=\"data-info\">"
		   +"<span>当前显示</span>"
		   +"<span class=\"current-data-info\">"+startIndex
		   +"~"
		   +endIndex
		   +"</span>"
		   +"<span>条,共</span>"
		   +"<span clss=\"total\">" 
		   +opts.total
		   +"条记录</span>"
		   +"</div>"
		   +"</div>";
		   return paginationHtml;
		},
		refreshPagination:function(opts){
			var $pagination = $(".grid-pagination",this);
			var startIndex = (opts.pageIndex-1)*opts.pageSize+1;
			var endIndex = startIndex+opts.currentDataSize-1;
			var pageCount = Math.ceil(opts.total/opts.pageSize);
			opts.pageCount = pageCount||1;
			$pagination.find(":text[name='currentPage']").val(opts.pageIndex||1);
			$pagination.find(".pagecount").html(pageCount);
			$pagination.find(".current-data-info").html(startIndex+"~"+endIndex);
			$pagination.find(".total").html(opts.total);
		},
		renderModal:function(){
			return "<div class=\"modal-backdrop fade hide\"><span> <i class=\"icon-spinner icon-spin\"></i>Loading...</span></div>";
		},
	    renderSelection:function(){
			return "<div style=\"position: absolute;border: 1px dashed #91B4F1;background:rgba(185, 213, 241, 0.7);\" id=\"area\"></div>";
	    },
		refresh:function(opts){
			var $container = this;
			var dfd = $.Deferred();
			var $refreshBtn = $(".pagination-btn .icon-refresh");
			$refreshBtn.toggleClass("icon-spin").closest(".datagrid").find(".modal-backdrop").toggleClass("hide").toggleClass("in");

			
			methods.getData.call(this,opts).done(function(data){
				tableDataCache[$container.selector]=data;
				var dataHtml="";
				$.each(data,function(index,columnData){
					dataHtml+="<tr data-number=\""+(index)+"\" data-id=\"tr_"+columnData[(opts.rowId||{})]+"\"";//data-number表示当前行的序号,用户获取选择行使用,从0开始计数
					if(columnData.checked){//是否选中
						dataHtml+=" class=\"selected\" ";
					}
					dataHtml+=">";
					if(opts.showNumber){//处理序号列
						dataHtml+="<td class=\"number\">"+(index+1)+"</td>";
					}
					if(opts.showSelectBox){//处理单选框或复选框
						dataHtml+="<td><input name=\"selectBox\" type=\""+(opts.multiSelect?"checkbox":"radio")+"\"/></td>";
					}
					$.each(opts.columns,function(index,columnDefinition){
						if($.isFunction(columnDefinition.render)){
							dataHtml+="<td  style=\"text-align:"+(columnDefinition.align||"left")+"\">"+columnDefinition.render(columnData)+"</td>";
						}else{
						    dataHtml+="<td  style=\"text-align:"+(columnDefinition.align||"left")+"\">"+columnData[columnDefinition.name]+"</td>";
						}
					});
					dataHtml+="</tr>";
				});
				$("table tbody",$container).html(dataHtml);
				methods.bindEventsForTr.call($container,opts);
				$refreshBtn.toggleClass("icon-spin").closest(".datagrid").find(".modal-backdrop").toggleClass("hide").toggleClass("in");
				dfd.resolve(dataHtml);
				methods.refreshPagination.call($container,opts);
			});
			return dfd.promise();
		},
		getSelect:function(opts){
			var $container = $(table.selector);
			var selectedData = [],tableData = tableDataCache[$container.selector];
			 $("tbody tr",$container).each(function(index,tr){
				 var $tr=$(tr); 
				 if($tr.hasClass("selected")){
					selectedData.push(tableData[$tr.attr("data-number")]);
				 }
			 });
			return selectedData;
		},
		getChangedRows:function(opts){
			var $container = $(table.selector);
			var changedRows = [],tableData = tableDataCache[$container.selector];
			 $("tbody tr",$container).each(function(index,tr){
				 var $tr=$(tr); 
				 if(tableData[$tr.attr("data-number")].checked!=$tr.hasClass("selected")){
					 changedRows.push($.extend({}, tableData[$tr
									.attr("data-number")], {
								checked : $tr.hasClass("selected")
							}));
				 }
			 });
			return changedRows;
		},
		getData:function(opts){
			var dfd = $.Deferred();
			if(opts.data){
				opts.total = opts.data.total||opts.data.length;
				if(typeof(opts.formatData)=="function"){
					dfd.resolve(opts.formatData.call(this,opts.data));
				}else{
					dfd.resolve(opts.data);
				}
				
			}else{
				$.ajax({
					url:opts.url,
					type:'Get',
					dataType:'json',
					data:$.extend({_random:Math.random()},{pageIndex:opts.pageIndex,pageSize:opts.pageSize},opts.params)
				}).done(function(data){
					opts.total = data.total||data.length;
					if($.isFunction(opts.formatData)){
						data = opts.formatData.call(this,data);
					}
					dfd.resolve(data);
				}).fail(function(jqXHR, textStatus, errorThrown){
					dfd.reject(jqXHR);
				});
			}
			return dfd.promise();
		},
		bindEvents:function(opts){
			var $container = $(this);
			/**
			**分页按钮点击事件
			**/
			$(".pagination-btn",$container).click(function(event){
				switch($(this).attr("action")){
					case "refresh": methods.refresh.call($container,opts);break;
					case "first"  : opts.pageIndex = 1;
								    methods.refresh.call($container,opts);break;
					case "prev"   : if(opts.pageIndex-1>0){
										opts.pageIndex=opts.pageIndex-1;
									}else{
										opts.pageIndex = 1;
									}
					 				methods.refresh.call($container,opts);break;
					case "next": 	if(opts.pageIndex+1>opts.pageCount){
										opts.pageIndex=opts.pageCount;
									}else{
										opts.pageIndex = opts.pageIndex+1;
									}
									methods.refresh.call($container,opts);break;
					case "last":    opts.pageIndex=opts.pageCount;
						            methods.refresh.call($container,opts);break;
				}
			});
			
			/**
			**排序事件
			**/
			$("thead th.sort-column",$container).click(function(){
				var $th = $(this),
				$downIcon = $th.find(".icon-angle-down"),
				$upIcon = $th.find(".icon-angle-up");
				$("thead th.sort-column",$container).not($th).find(".icon-angle-down").addClass("transparent").next().addClass("hide");

				if($downIcon.hasClass("hide")&&$upIcon.hasClass("hide")){
					$upIcon.removeClass("hide");
				}else{
					$downIcon.toggleClass("hide").removeClass("transparent");
					$upIcon.toggleClass("hide"); 
				}

				opts.params.sortName = $th.attr("data-name");
				opts.params.sortType = $upIcon.hasClass("hide")?"desc":"asc";
				methods.refresh.call($container,opts);
			});
			/**********************拖动事件处理***********************************/
			var topY,bottomY;//拖动区域的上下边界
			var mouseDown = false;
			var _TitleWidth = 37;
			if(opts.multiSelect){//允许多选时才绑定拖动事件
				$($container).mousedown(function(event){
					mouseDown = true;
					var x = event.pageX,y=event.pageY;
					 $(this).mousemove(function(e){
						 if(mouseDown){
							$("#area").removeClass("hide");
							var ex = e.pageX,ey=e.pageY;
							topY = (ey>y?y:ey)- $container.find("table").offset().top+_TitleWidth;
							bottomY = topY+Math.abs(ey-y);

							$("#area").css({
								top:topY,
								left:(ex>x?x:ex)-$container.find("table").offset().left,
								width:Math.abs(ex-x),
								height:Math.abs(ey-y)
							});
							
							select();
							e.preventDefault();
						 }
					 });
					 $("#area").mousemove(function(e1){
						 if(mouseDown){
							 $("#area").removeClass("hide");
							var ex1 = e1.pageX,ey1=e1.pageY;

							topY = (ey1>y?y:ey1)- $container.find("table").offset().top;
							bottomY = topY+Math.abs(ey1-y);

							$("#area").css({
								top:topY,
								left: (ex1>x?x:ex1)-$container.find("table").offset().left,
								width:Math.abs(ex1-x),
								height:Math.abs(ey1-y)
							});
							select();
							e1.preventDefault();
						 }
						 });
					 event.preventDefault();
				});
				 $("#area").mouseup(function(){
					$("#area").css({width:0,height:0}).addClass("hide");
					 mouseDown = false;
				 });

				$($container).mouseup(function(){
					$(this).unbind("mousemove");
					$("#area").css({width:0,height:0}).addClass("hide");
					mouseDown = false;
				});
			}
			function select(){
			   $("tbody tr",$container).each(function(index,e){
				if($(e).offset().top-$container.find("table").offset().top+$(e).height()+_TitleWidth>=topY&&$(e).offset().top-$container.find("table").offset().top+_TitleWidth<=bottomY){
						$(e).trigger("select");
					}else{
						$(e).trigger("unselect");
					}
			   });
		    }
			/**********************  /拖动事件处理 ***********************************/
			
			methods.bindEventsForTr.call($container,opts);
		},
		bindEventsForTr:function(opts){//给tbody Tr绑定事件,这样在刷新的时候就可以只给刷新的数据修改事件
			var $container = $(this);
			/**
			**tr行选中事件
			**/
			$("tbody tr",$container).bind("select",function(){
			     if(opts.multiSelect){
					$(this).addClass("selected").find(":checkbox").prop("checked",true);
				}else{
					$(this).addClass("selected").siblings().removeClass("selected");
					$(this).find(":radio").prop("checked",true);
				}
			});

			/**
			**tr行取消选中事件
			**/
			$("tbody tr",$container).bind("unselect",function(){
			     if(opts.multiSelect){
					$(this).removeClass("selected").find(":checkbox").prop("checked",false);
				}else{
					$(this).removeClass("selected").siblings().removeClass("selected");
					$(this).find(":radio").prop("checked",false);
				}
			});

			/**
			**tr点击事件，用于选择行或取消选择行
			**/
			$("tbody tr",$container).click(function(){
				if($(this).hasClass("selected")){
					$(this).trigger("unselect");
				}else{
					$(this).trigger("select");
				}
			});
 
		},
		renderTitle:function(opts){
			var $div = $(document.createElement("div"));
			var $span = $(document.createElement("span"));
			$span.html(opts.title||'');
			$div.addClass("table-title").append($span).append(methods.renderButtonGroup(opts));
			return $div;
		},
		renderButtonGroup:function(opts){
			var $btnDiv = $(document.createElement("div"));
			$btnDiv.addClass("button-group");
			$.each(opts.buttons,function(index,button){
				var $btn = $(document.createElement("button"));
				$btn.addClass("btn btn-small btn-link").html(button.text);
				$btn.bind("click",function(){
					button.click.call(table,button);
				});
				$btnDiv.append($btn);
			});
			return $btnDiv;
		}
	};

	/**
	 * 返回给外部调用
	 */
	var table = {
			refresh:function(){
				methods.refresh.call($(table.selector),table.opts);
			},
			getSelect:function(){
				return methods.getSelect.call(table.selector);
			},
			getChangedRows:function(){
				return methods.getChangedRows.call(table);
			}
	};
	var tableCache = {};//用于存储表对象
	var tableDataCache = {};//用于存储表对象数据
	$.fn.table=function(opts){
		if(typeof(opts)=="string"){
			if(opts=="getSelect"){
				return methods.getSelect.call($(this));
			}else if(opts=="refresh"){
				methods.refresh.call($(this),tableCache[$(this).selector].opts);
			}
		}else{
			opts=$.extend({},defaults,opts);
			return methods.renderTable.call($(this),opts);
		}
	};
})();