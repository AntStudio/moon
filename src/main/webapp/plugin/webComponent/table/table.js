/**
 * 表格组件,支持ajax获取数据
 * 待完成:1、api文档
 * 	    2、pageSize动态设置
 * 		3、表格风格优化(done)
 * 		4、表格行选择支持(done)
 * 		5、拖拽式选择(done)
 *		6、树形表格(done)
 * @author Gavin Cook
 * @Date 2012-08-25
 */
(function(){

	var VERSION = "1.0.0";
	var defaults = {
		                url : "",
                      rowId : "id",
		            columns : [],
                  pageIndex : 1,
		           pageSize : 10,
		             params : {},
		         formatData : '',//格式化数据
		      showSelectBox : false,//是否显示单选框或者复选框
		        multiSelect : true,//是否允许多选
		         showNumber : true,//是否显示序号(从1开始计数)
		            buttons : [],//按钮组
		        emptyParent : true,//是否清空容器
		         dragSelect : false,//是否启动拖拽选择
		          treeTable : true,//是否开启树形菜单
		         treeColumn : "id",//哪一列作为树形表格的处理列
                     indent : "_TreeColumn",//当表格为树形表格时，哪些列需要缩进 可选值为：{_TreeColumn(树形表格处理列) , _All(所有列) , 具体的列名}
       childrenPropertyName : "_hasChildren",//树形表格中，子行在父行中的标示名字
		       childrenData : [],//树形表格处理行，展开时需要渲染的子数据，可为数组或Deferred对象
		           sortType : "asc",//默认排序方式
             showPagination : true,//是否显示分页栏
                  showTitle : true,//是否显示title
            showTableHeader : true//是否显示表格标题
	};

	var methods  = {
		//渲染数据
		renderData:function(){
			var tableInstance = this;
			var $container = tableInstance.$container;
			var opts = tableInstance.opts;
			var dfd = $.Deferred();

			methods.getData.call(tableInstance).done(function(data){
				opts.currentDataSize = data.length;
				var $tbody = methods.ce("tbody");
				tableDataCache[$container.selector]=data;
				$.each(data,function(index,columnData){
					var $tr = methods.renderDataRow.call(tableInstance,opts,index,"0-"+index,columnData);
					$tbody.append($tr);
				});
				dfd.resolve($tbody);
			});

			return dfd.promise();
		},
		renderDataRow:function(opts,index,level,columnData){//渲染数据行
			var tableInstance = this;
			var $tr = methods.ce("tr",{"data-number":index,"level":level,"data-id":"tr_"+columnData[opts.rowId]||index });//data-number表示当前行的序号,用户获取选择行使用,从0开始计数
			if(columnData.checked){//是否选中
				$tr.addClass("selected");
			}
			
			if(opts.showSelectBox){//处理单选框或复选框
				var $selectTd = methods.ce("td");
				var $selectBox = methods.ce("input",{"name":"selectBox","type":(opts.multiSelect?"checkbox":"radio")});
				$selectTd.append($selectBox);
				$tr.append($selectTd);
			}

			if(opts.showNumber){//处理序号列
				var $seriesTd = methods.ce("td",{"class":"number"});
				if(methods.getLevel(level) == 1){
					$seriesTd.html(index+1);
				}
				$tr.append($seriesTd);
			}
			if(opts.treeTable){//树形表格
				$.each(opts.columns,function(index,columnDefinition){//处理数据行
					var $dataTd = methods.ce("td",{"style":"text-align:"+(columnDefinition.align||"left")+";"});//文字默认左对齐
					var isTreeColumn = opts.treeColumn === columnDefinition.name;
					var $data = methods.ce("span");
					if(columnData[opts.childrenPropertyName] == true && isTreeColumn){
						var $icon =methods.ce("i",{"class":"fa fa-plus-square tree-icon"});
						$icon.click(function(event){
                            var level = $tr.attr("level");
							if($icon.prop("expand") === true){
								$icon.removeClass("fa-minus-square").addClass("fa-plus-square");
								var $next = $tr.next();
								while($next &&$next.attr("level") && $next.attr("level").length > level.length){
									$next.hide();
									$next = $next.next();
								}
								$icon.prop("expand",false);
							}else{
								$icon.removeClass("fa-plus-square").addClass("fa-minus-square");
								if($icon.prop("dataLoaded")){//如果已经加载了数据
									var $next = $tr.next();
									while(methods.startWith($next.attr("level"),level)){//这里直接判断是否以父节点的level开头的即可
										$next.show();
										$next = $next.next();
									}
								}else{
									methods.renderChildrenData.call(tableInstance,$tr,columnData);
								}
								$icon.prop("dataLoaded",true);//设置数据加载标志
								$icon.prop("expand",true);
							}
                            return false;
						});
						$data.append($icon);
					}
                    var levelNum = methods.getLevel(level);
                    var indent = opts.indent ;
                    indent = (indent == "_All") || (indent == "_TreeColumn" && isTreeColumn) ||(indent == columnDefinition.name);
					while(levelNum>1 && indent){
						$dataTd.append(methods.ce("div",{"class":"space"}));
                        levelNum--;
					}
					if($.isFunction(columnDefinition.render)){
						$data.append(columnDefinition.render.call($dataTd,columnData));
						$dataTd.append($data);//如果有自定义渲染方法，则调用自定义渲染方法
					}else{
						$data.append(columnData[columnDefinition.name]);
						$dataTd.append($data);//没有自定义渲染方法，默认通过column.name取值
					}
					$tr.append($dataTd);
				});
			}else{
				$.each(opts.columns,function(index,columnDefinition){//处理数据行
					var $dataTd = methods.ce("td",{"style":"text-align:"+(columnDefinition.align||"left")+";"});//文字默认左对齐
					if($.isFunction(columnDefinition.render)){
						$dataTd.html(columnDefinition.render.call($dataTd,columnData));//如果有自定义渲染方法，则调用自定义渲染方法
					}else{
						$dataTd.html(columnData[columnDefinition.name]);//没有自定义渲染方法，默认通过column.name取值
					}
					$tr.append($dataTd);
				});
			}
			return $tr;
		},
        //判断src是否以prefix开头
        startWith:function(src,prefix){
            return prefix && src && prefix == src.substring(0,prefix.length);
        },
        getLevel:function(src){
            var c = 0;
            var p = 0;
            while((p = src.indexOf("-",p))!=-1){
                c++;
                p++;//找到"-"后往后移动一位
            }
            return c;
        },
		//渲染树形表格的数据
		renderChildrenData:function(parentRow,parentRowData){
			var tableInstance = this;
			var $tr = methods.ce("tr");
			var opts = tableInstance.opts;
			var $dfd = $.Deferred();
            if(!opts.childrenData){
                $dfd.resolve([]);
            }else if($.isArray(opts.childrenData)){
				$dfd.resolve(opts.childrenData);
			}else if($.isFunction(opts.childrenData)){
				var result = opts.childrenData.call(tableInstance,parentRow,parentRowData)||[];
                if($.isFunction(result.done)) {
                    result.done(function (data) {
                        $dfd.resolve(data);
                    });
                }else{
                    $dfd.resolve(result);
                }
			}

			$dfd.done(function(childrenData){
                parentRowData.children = childrenData;
				$.each(childrenData,function(index,columnData){
					var level = parentRow.attr("level");//层级
					var currentLevel = level+"-"+index;
					var $tr = methods.renderDataRow.call(tableInstance,opts,index,currentLevel,columnData);
					parentRow.after($tr);
				});
			});
		},
		//渲染表头
		renderHeader:function(){
			var tableInstance = this;
			var $thead=methods.ce("thead");
			var $tr = methods.ce("tr");
			var opts = tableInstance.opts;

			if(opts.showSelectBox){//渲染选择框表头
				var $selectTh = methods.ce("th",{"class":"select-box"});
				if(opts.multiSelect){
					$selectTh.append(methods.ce("input",{"type":"checkbox"}));
				}
				$tr.append($selectTh);
			}

			if(opts.showNumber){//渲染序列号表头
				var $numberTh = methods.ce("th",{"class":"number"});
				$tr.append($numberTh);
			}

			$.each(opts.columns,function(index,column){//渲染数据列表头
				if(column.width){
					if(typeof column.width == "number"){
						column.width +="px";
					}
				}
				var $dataTh = methods.ce("th",{"data-name":(column.sortName||column.name),"class":(column.sort?"sort-column":""),"style":"text-align:"+(column.align||"left")+"; width:"+(column.width||"auto")+";"})
				$dataTh.append((column.display||column.name));				
				if(column.sort){
					$dataTh.append(methods.ce("i",{"class":"fa fa-angle-down  transparent"}))
						   .append(methods.ce("i",{"class":"fa fa-angle-up hide"}));
				}
				$tr.append($dataTh);
			});

			$thead.append($tr);
			return $thead;
		},
		//渲染表格(表头、表内容和分页)
		renderTable:function(){
			var tableInstance = this;
			var $container = tableInstance.$container;
			var autoLayout = ($container.height()==0);//是否自适应布局
			var opts = tableInstance.opts;
			var $tableDiv = methods.ce("div");//整个table（包含表格标题、按钮组、表头、内容以及分页栏）的容器
			$tableDiv.addClass("datagrid");
            if(opts.showTitle) {
                $tableDiv.append(methods.renderTitle.call(tableInstance));
            }
			if(!autoLayout){
				$tableDiv.addClass("fixed-layout");
			}
			
			var $tableContainerDiv = methods.ce("div");//表格容器
			$tableContainerDiv.addClass("table-container");

			var $table = methods.ce("table");//表格主体

            if(opts.showTableHeader) {
                $table.append(methods.renderHeader.call(tableInstance));
            }
			$table.addClass("table table-bordered table-hover");
			
			var renderTableDfd = $.Deferred();//渲染表格延时对象，ajax等异步需要使用
			methods.renderData.call(tableInstance).done(function(tbody){
				$table.append(tbody);
				renderTableDfd.resolve();
			});
			
			$.when(renderTableDfd).done(function(){
				if(opts.emptyParent){
					$container.empty();
				}
				$tableContainerDiv.append($table);
                $tableDiv.append($tableContainerDiv)
                if(opts.showPagination) {
                    $tableDiv.append(methods.renderPagination.call(tableInstance));
                }
				$tableDiv.append(methods.renderModal.call(tableInstance));
				$tableDiv.append(methods.renderSelection.call(tableInstance));
				
				$container.append($tableDiv);
				
				tableCache[$container.selector] = tableInstance;//加入缓存
				methods.bindEvents.call(tableInstance);
			});
			return tableInstance;
		},
		//渲染分页栏
		renderPagination:function(){
			var tableInstance = this;
			var opts = tableInstance.opts;
			var startIndex = (opts.currentDataSize==0)?0:((opts.pageIndex-1)*opts.pageSize+1);
			var endIndex = (opts.currentDataSize==0)?0:(startIndex+opts.currentDataSize-1);
			var pageCount = Math.ceil((opts.totalItemsCount||1)/opts.pageSize);
			opts.pageCount = pageCount||1;

			var $paginationDiv = methods.ce("div",{"class":"grid-pagination"});
			
		
			$paginationDiv.append(	methods.ce("span",{"class":"pagination-btn",action:"first"}).append(methods.ce("i",{"class":"fa fa-step-backward"})));
			$paginationDiv.append(	methods.ce("span",{"class":"pagination-btn",action:"prev"}).append(methods.ce("i",{"class":"fa fa-play fa-rotate-180"})));
			$paginationDiv.append(	methods.ce("input",{"class":"input-small",type:"text",name:"currentPage",value:(opts.pageIndex||1)}));
			$paginationDiv.append("/");
			$paginationDiv.append(methods.ce("span",{"class":"pagecount"}).html(pageCount));
			$paginationDiv.append(	methods.ce("span",{"class":"pagination-btn",action:"next"}).append(methods.ce("i",{"class":"fa fa-play"})));
			$paginationDiv.append(	methods.ce("span",{"class":"pagination-btn",action:"last"}).append(methods.ce("i",{"class":"fa fa-step-forward"})));
			$paginationDiv.append(	methods.ce("span",{"class":"split"}));
			$paginationDiv.append(	methods.ce("span",{"class":"pagination-btn",action:"refresh"}).append(methods.ce("i",{"class":"fa fa-refresh"})));
			
			var $dataInfo = methods.ce("div",{"class":"data-info"});
			$dataInfo.append(	methods.ce("span").html("当前显示"));
			$dataInfo.append(	methods.ce("span",{"class":"current-data-info"}).html(startIndex+"~"+endIndex));
			$dataInfo.append(	methods.ce("span").html("条,共"));
			$dataInfo.append(	methods.ce("span",{"class":"total"}).html((opts.totalItemsCount||0)));
			$dataInfo.append("条记录");
			
			$paginationDiv.append($dataInfo);
			return $paginationDiv;
		},
		renderModal:function(){
			return "<div class=\"modal-backdrop fade hide\"><span> <i class=\"fa fa-spinner fa-spin\"></i>Loading...</span></div>";
		},
	    renderSelection:function(){
			var tableInstance = this;
			return "<div class=\"drag-area hide\" id=\"_datagrid_area_"+tableInstance._id+"\"></div>";
	    },
		//刷新
		refresh:function(params){
			var tableInstance = this;
			var $container = tableInstance.$container;
			var dfd = $.Deferred();
			var $refreshBtn = $(".pagination-btn .fa-refresh");
			var opts = tableInstance.opts;
            $.extend(tableInstance.opts.params,params);//将新的参数添加到配置中，以供表格中刷新和翻页使用

            $refreshBtn.toggleClass("fa-spin").closest(".datagrid").find(".modal-backdrop").toggleClass("hide").toggleClass("in");

			methods.renderData.call(tableInstance).done(function(tbody){
				dfd.resolve(tbody);
				var $table = $("table",$container);
				$("tbody",$table).remove();
				$table.append(tbody);
				$refreshBtn.toggleClass("fa-spin").closest(".datagrid").find(".modal-backdrop").toggleClass("hide").toggleClass("in");//关闭loading状态
				methods.bindEventsForDragArea.call(tableInstance);
				methods.refreshPagination.call(tableInstance);//刷新分页			
			});
			
			return dfd.promise();
		},
		//刷新分页栏
		refreshPagination:function(){
			var tableInstance = this;
			var $container = tableInstance.$container;
			var opts = tableInstance.opts;
			var $pagination = $(".grid-pagination",$container);
			var startIndex = (opts.currentDataSize==0)?0:((opts.pageIndex-1)*opts.pageSize+1);
			var endIndex = (opts.currentDataSize==0)?0:(startIndex+opts.currentDataSize-1);
			var pageCount = Math.ceil((opts.totalItemsCount||1)/opts.pageSize);
			opts.pageCount = pageCount||1;
			$pagination.find(":text[name='currentPage']").val(opts.pageIndex||1);
			$pagination.find(".pagecount").html(pageCount);
			$pagination.find(".current-data-info").html(startIndex+"~"+endIndex);
			$pagination.find(".total").html(opts.totalItemsCount||0);
		},
		//获取选中的行
		getSelect:function(){
			var tableInstance = this;
			var $container = tableInstance.$container;
			var selectedData = [],tableData = tableDataCache[$container.selector];
			 $("tbody tr",$container).each(function(index,tr){
				 var $tr=$(tr); 
				 if($tr.hasClass("selected")){
                    var level = $tr.attr("level");//如0-2-3
					selectedData.push(methods.getRowDataByLevel.call(tableInstance,level));
				 }
			 });
			return selectedData;
		},
        //根据level节点获取该行的数据
        getRowDataByLevel:function(level){
            var tableInstance = this;
            var $container = tableInstance.$container;
            var tableData = tableDataCache[$container.selector];
            var levels = level.substring(2).match(/(\d)/g);//返回[2,3],第一个0-(表示第一级)需要去掉
            var tempData = tableData;
            for(var i = 0,l =levels.length;i<l;i++){
                if(i>0){
                    tempData = tempData.children[parseInt(levels[i])];//获取树节点的子节点数据
                }else {
                    tempData = tempData[parseInt(levels[i])];//获取第一级节点数据
                }

            }
            return tempData;
        },
		//获取状态改变的行，包含两种情况（1.选中变为未选中; 2.未选中变为选中）
		getChangedRows:function(){
			var tableInstance = this;
			var $container = tableInstance.$container;
			var changedRows = [],tableData = tableDataCache[$container.selector];
			 $("tbody tr",$container).each(function(index,tr){
				 var $tr=$(tr); 
				 if(tableData[$tr.attr("data-number")].checked!=$tr.hasClass("selected")){//默认提取数据中的checked字段检测
					 changedRows.push($.extend({}, tableData[$tr
									.attr("data-number")], {
								checked : $tr.hasClass("selected")
							}));
				 }
			 });
			return changedRows;
		},
		//获取数据，分两种方式: 1.直接传递数据 2.ajax异步获取数据
		//totalItemsCount 获取顺序： totalItemsCount-->formatData.totalItemsCount-->data.length-->calcTotalCount. 越靠后优先级越高
		getData:function(){
			var tableInstance = this;
			var opts = tableInstance.opts;
			var dfd = $.Deferred();
			if(opts.data){ 
				var data = opts.data;
				opts.totalItemsCount = data.totalItemsCount;//默认从totalItemsCount中获取总条数
				var tableData = data.items||[];
				if($.isFunction(opts.formatData)){//自定义格式化数据{items:[],totalItemsCount:20}
					var temp = opts.formatData.call(tableInstance,tableData);
					if(temp.items){
						tableData = temp.items;
					}
					if(temp.totalItemsCount){
						opts.totalItemsCount = temp.totalItemsCount;
					}
				}
				if(!opts.totalItemsCount){//从数据中获取条数
					opts.totalItemsCount = tableData.length;
				}

				if($.isFunction(opts.calcTotalCount)){//自定义计算总共的条数
					opts.totalItemsCount = opts.calcTotalCount.call(tableInstance,data);
				}
				dfd.resolve(tableData);
			}else{
				$.ajax({
					url:opts.url,
					type:'Get',
					dataType:'json',
					data:$.extend({_random:Math.random()},{pageIndex:opts.pageIndex,pageSize:opts.pageSize},opts.params)
				}).done(function(result){
					var data ;
					opts.totalItemsCount = result.totalItemsCount;//默认从totalItemsCount中获取总条数
					data = result.items||[];
					if($.isFunction(opts.formatData)){//自定义格式化数据{items:[],totalItemsCount:20}
						var temp = opts.formatData.call(tableInstance,result)||{};
						if(temp.items){
							data = temp.items;
						}
						if(temp.totalItemsCount){
							opts.totalItemsCount = temp.totalItemsCount;
						}
					}
					if(!opts.totalItemsCount){//从数据中获取条数
						opts.totalItemsCount = data.length;
					}

					if($.isFunction(opts.calcTotalCount)){//自定义计算总共的条数
						opts.totalItemsCount = opts.calcTotalCount.call(tableInstance,result);
					}
					dfd.resolve(data);
				}).fail(function(jqXHR, textStatus, errorThrown){
					dfd.reject(jqXHR);
				});
			}
			return dfd.promise();
		},
		sortData:function(data,sortName,sortType){
			if(!data||data.length==0){
				return [];
			}
			var temp;
			for(var j = data.length - 1; j >= 1;j--){
				for (var i = j; i >= 1;i-- ) {
					 if(data[i][sortName]&&data[i][sortName]>(data[i-1][sortName])){
					 	temp = data[i];
					 	data[i] = data[i-1];
					 	data[i-1] = temp;
					 }
				}
			}
		},
		//绑定事件
		//1.分页按钮点击事件 2.排序事件 3.拖动选择事件
		bindEvents:function(){
			var tableInstance = this;
			var $container = tableInstance.$container;
			var opts = tableInstance.opts;
			//表头复选框事件
			if(opts.multiSelect&&opts.showSelectBox){
				$("th.select-box :checkbox",$container).click(function(){
					if($(this).prop("checked")){
						$("td :checkbox[name='selectBox']",$container).trigger("select");
					}else{
						$("td :checkbox[name='selectBox']",$container).trigger("unselect");
					}
				});
			}

            if(opts.showPagination) {
                /**
                 **分页按钮点击事件
                 **/
                $(".pagination-btn", $container).click(function (event) {
                    switch ($(this).attr("action")) {
                        case "refresh":
                            methods.refresh.call(tableInstance);
                            break;
                        case "first"  :
                            opts.pageIndex = 1;
                            methods.refresh.call(tableInstance);
                            break;
                        case "prev"   :
                            if (opts.pageIndex - 1 > 0) {
                                opts.pageIndex = opts.pageIndex - 1;
                            } else {
                                opts.pageIndex = 1;
                            }
                            methods.refresh.call(tableInstance);
                            break;
                        case "next":
                            if (opts.pageIndex + 1 > opts.pageCount) {
                                opts.pageIndex = opts.pageCount;
                            } else {
                                opts.pageIndex = opts.pageIndex + 1;
                            }
                            methods.refresh.call(tableInstance);
                            break;
                        case "last":
                            opts.pageIndex = opts.pageCount;
                            methods.refresh.call(tableInstance);
                            break;
                    }
                });
            }
			/**
			**排序事件
			**/
			$("thead th.sort-column",$container).click(function(){
				var $th = $(this),
				$downIcon = $th.find(".fa-angle-down"),
				$upIcon = $th.find(".fa-angle-up");
				$("thead th.sort-column",$container).not($th).find(".fa-angle-down").addClass("transparent").next().addClass("hide");

				if($downIcon.hasClass("hide")&&$upIcon.hasClass("hide")){
					$upIcon.removeClass("hide");
				}else{
					$downIcon.toggleClass("hide").removeClass("transparent");
					$upIcon.toggleClass("hide"); 
				}

				opts.params.sortName = $th.attr("data-name");
				opts.params.sortType = $upIcon.hasClass("hide")?"desc":"asc";
				methods.refresh.call(tableInstance);
			});
			
			methods.bindEventsForDragArea.call(tableInstance);
			methods.bindEventsForTr.call(tableInstance);
		},
		bindEventsForDragArea:function(){
			var tableInstance = this;
			var opts = tableInstance.opts;
			var $container = tableInstance.$container;
		/**********************拖动事件处理***********************************/
			var areaSelector = "#_datagrid_area_"+tableInstance._id;
			var topY,bottomY;//拖动区域的上下边界
			var mouseDown = false;
			var _TitleWidth = 37+36;
			if(opts.multiSelect&&opts.dragSelect){//允许多选时才绑定拖动事件
				$("tbody",$container).mousedown(function(event){
					mouseDown = true;
					var x = event.pageX,y=event.pageY;
					 $($container).mousemove(function(e){
						 if(mouseDown){
							$(areaSelector).removeClass("hide");
							var ex = e.pageX,ey=e.pageY;
							topY = (ey>y?y:ey)- $container.find("tbody").offset().top+_TitleWidth;
							bottomY = topY+Math.abs(ey-y);
							
							$(areaSelector).css({
								top:topY,
								left:(ex>x?x:ex)-$container.find("tbody").offset().left,
								width:Math.abs(ex-x),
								height:Math.abs(ey-y)
							});
							if(Math.abs(ex-x)>10 && Math.abs(ey-y)>10){
								select();
							}
							e.preventDefault();
						 }
					 });
					 $(areaSelector).mousemove(function(e1){
						 if(mouseDown){
							 $(areaSelector).removeClass("hide");
							var ex1 = e1.pageX,ey1=e1.pageY;

							topY = (ey1>y?y:ey1)- $container.find("table").offset().top;
							bottomY = topY+Math.abs(ey1-y);

							$(areaSelector).css({
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
				 $(areaSelector).mouseup(function(){
					$(areaSelector).css({width:0,height:0}).addClass("hide");
					 mouseDown = false;
				 });

				$($container).mouseup(function(){
					$(this).unbind("mousemove");
					$(areaSelector).css({width:0,height:0}).addClass("hide");
					mouseDown = false;
				});
			}
			function select(){
			   $("tbody tr",$container).each(function(index,e){
				if($(e).offset().top-$container.find("tbody").offset().top+$(e).height()+_TitleWidth>=topY&&$(e).offset().top-$container.find("tbody").offset().top+_TitleWidth<=bottomY){
						$(e).trigger("select");
					}else{
						$(e).trigger("unselect");
					}
			   });
		    }
			/**********************  /拖动事件处理 ***********************************/
		},
		bindEventsForTr:function(){//给tbody Tr绑定事件,这样在刷新的时候就可以只给刷新的数据修改事件
			var tableInstance = this;
			var $container = tableInstance.$container;
			var opts = tableInstance.opts;
			var checkboxNumber = $("td :checkbox[name='selectBox']",$container).length;
			/**
			**tr行选中事件
			**/
			$container.on("select","tbody tr",function(e){
				 var $target = $(e.currentTarget);
				 if($target[0].tagName == "TR"){
					 if(opts.multiSelect){
						$target.addClass("selected").find(":checkbox").prop("checked",true);
						if(opts.showSelectBox&&checkboxNumber==$("td :checkbox[name='selectBox']:checked",$container).length){
							$("th.select-box :checkbox",$container).prop("checked",true);
						}
					}else{
						$target.addClass("selected").siblings().removeClass("selected");
						$target.find(":radio").prop("checked",true);
					}
				}
			});

			/**
			**tr行取消选中事件
			**/
			$container.on("unselect","tbody tr",function(e){
				 var $target = $(e.currentTarget);
				 if($target[0].tagName == "TR"){
					 if(opts.multiSelect){
						$target.removeClass("selected").find(":checkbox").prop("checked",false);
						if(opts.showSelectBox){
							$("th.select-box :checkbox",$container).prop("checked",false);
						}
					}else{
						$target.removeClass("selected").siblings().removeClass("selected");
						$target.find(":radio").prop("checked",false);
					}
				}
			});

			/**
			**tr点击事件，用于选择行或取消选择行
			**/
			$container.on("click","tbody tr",function(e){
                var target = e.target;
                if(target.tagName == "A" || target.tagName == "BUTTON" || target.tageName == "INPUT"){
                    return; //当为a,button,input标签时不执行行选中事件,不能直接返回false,需要将事件继续往上冒泡
                }
				var $currentTarget = $(e.currentTarget);
				if($currentTarget.hasClass("selected")){
                    $currentTarget.trigger("unselect");
				}else{
                    $currentTarget.trigger("select");
				}
			});
		},
		//表格标题
		renderTitle:function(){
			var tableInstance = this;
			var $div = methods.ce("div");
			var $span = methods.ce("span");
			$span.html(tableInstance.opts.title||'');
            tableInstance.$title = $span;//保存title到tableInstance中
			$div.addClass("table-title").append($span).append(methods.renderButtonGroup.call(tableInstance));
			return $div;
		},
		//渲染按钮组
		renderButtonGroup:function(){
			var tableInstance = this;
			var $btnDiv = methods.ce("div");
			$btnDiv.addClass("button-group");
			$.each(tableInstance.opts.buttons,function(index,button){
				var $btn = methods.ce("button");
				$btn.addClass("btn btn-small btn-link").html(button.text);
				$btn.bind("click",function(){
					button.click.call(tableInstance,button);
				});
				$btnDiv.append($btn);
			});
			return $btnDiv;
		},
		//创建节点（-->createElement）
		ce:function(tag,opts){
			var $tag = $(document.createElement(tag));
			for(var name in opts){
				$tag.attr(name,opts[name]);
			}
			return $tag;
		}
	};

	/**
	 * 返回给外部调用
	 */
	var _table = {
			newInstance:function(){
				var t = new Object();
				t.refresh = function(params){//刷新
					methods.refresh.call(t,params);
				};
				t.getSelect = function(){//选取选择的数据
					return methods.getSelect.call(t);
				};
				t.getChangedRows = function(){
                    return methods.getChangedRows.call(t);
                };
                /**
                 * 根据tr行获取对应的数据
                 * @param tr
                 * @returns {*}
                 */
                t.getRowData = function(tr){
                    return methods.getRowDataByLevel.call(t,$(tr).attr("level"))
                };
				t._id = idGenerator.next();

                /**
                 * 动态设置Title
                 * @param title
                 */
                t.updateTitle = function(title){
                    this.$title.html(title);
                }
				return t;
			}
			
	};
	var _idGenerator = function(){
		var i = 1;
		this.next=function(){
			return i++;
		}
	};
	var idGenerator = new _idGenerator();
	var tableCache = {};//用于存储表对象
	var tableDataCache = {};//用于存储表对象数据
	
	$.fn.table=function(opts){
		var tableInstance ;
		if(typeof(opts)=="string"){
			tableInstance = tableCache[$(this).selector];
			if(tableInstance){
				if(opts=="getSelect"){//获取选中的行
					return tableInstance.getSelect();
				}else if(opts=="refresh"){//刷新
					tableInstance.refresh();
				}
			}else{
				if(console){
					console.log("表未初始化");
				}
			}
			
		}else{
			tableInstance = _table.newInstance();
			tableInstance.opts=$.extend({},defaults,opts);
			tableInstance.$container = $(this);

			return methods.renderTable.call(tableInstance);
		}
	};
})();