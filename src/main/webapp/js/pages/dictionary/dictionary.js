(function() {
	$(function() {
		var table = $("#dictionaryTable").table({
			url : contextPath + "/dictionary/list",
			columns : [{
                            name : "name",
                            display : "字典名称",
                            render:function(rowData){
                                if(rowData.isFinal){
                                    return "<span class=\"final-dic\">"+rowData.name+"</span>";
                                }else{
                                    return rowData.name;
                                }
                            }
                        },
                        {
                            name : "code",
                            display : "字典代码"
                        }
            ],
			formatData : function(data) {
				return data.result;
			},
            childrenData:function($tr,columnData){
                var $dfd = $.Deferred();
                $.getJsonData(contextPath+"/dictionary/list",{parentId:columnData.id}).done(function(data){
                    $dfd.resolve(data.result.items);
                }).fail(function(){
                    $dfd.reject();
                });
                return $dfd.promise();
            },
			title : "字典",
			rowId : "id",
            treeColumn:"name",
			buttons : [ {
				text : "增加字典项",
				name : 'addBtn',
				click : addDictionary,
				action : 'add'
			}, {
				text : "增加字典参数",
				name : 'addDicParamBtn',
				click : addDicParam,
				action : 'addDicParam'
			}, {
				text : "删除字典",
				name : 'deleteBtn',
				click : removeDictionary,
				action : 'delete'
			}, {
				text : "编辑字典",
				name : 'editBtn',
				click : editDictionary
			} ]
		});

	});

	// 添加字典项弹出窗口
	var addDictionary = function() {
		$("#dictionaryForm").dialog({
			title : '增加字典',
			afterShown : function() {
				$("#dictionaryForm").validate({
					align : 'right',
					theme : "darkblue"
				});
			},
			beforeClose : function() {
				$("#dictionaryForm").validate("hide");
			},
			buttons : [ {
				text : '保存',
				css : 'btn btn-primary',
				click : doSave,
				formId : 'dictionaryForm'
			}, {
				text : '取消',
				css : 'btn btn-default',
				click : function(opts) {
					$(this).dialog("close");
				}
			} ]
		});
	};

	// 添加字典项后台交互
	var doSave = function(btn) {
		var formId = btn.formId;
		var data = {};
		if (btn.parent_id) {
			data["dictionary.parentId"] = btn.parent_id;
		}
		$("#" + formId).validate("validate").done(
				function(result) {
					if (result) {
						$.getJsonData(contextPath + "/dictionary/add",
								$("#" + formId).formData(data), {
									type : "Post"
								}).done(function(data) {
							if (data.success) {
								$("#" + formId).dialog("close");
								$("#dictionaryTable").table("refresh");
								$("#" + formId).reset();
								moon.success("新增字典成功");
							}
						});
					}
				}).fail(function() {
			moon.error("新增字典失败");
		});
	};

	var addDicParam = function() {
		var selectRows = $("#dictionaryTable").table("getSelect");
		if (selectRows.length != 1) {
			moon.error("请选中一项进行字典参数添加.");
			return;
		}
		$("#dictionaryParamForm").dialog({
			title : '增加字典参数到 ' + selectRows[0].name,
			afterShown : function() {
				$("#dictionaryParamForm").validate({
					align : 'right',
					theme : "darkblue"
				});
			},
			beforeClose : function() {
				$("#dictionaryParamForm").validate("hide");
			},
			buttons : [ {
				text : '保存',
				css : 'btn btn-primary',
				click : doSave,
				formId : 'dictionaryParamForm',
				parent_id : selectRows[0].id
			}, {
				text : '取消',
				css : 'btn btn-default',
				click : function(opts) {
					$(this).dialog("close");
				}
			} ]
		});

	};

	var removeDictionary = function() {
		var selectRows = $("#dictionaryTable").table("getSelect");
		if (selectRows.length == 0) {
			moon.error("请选择要删除的字典.");
			return;
		}
		var ids = new Array();
        var containsFinal = false;//是否包含系统字典
		$.each(selectRows, function(index, dic) {
			ids.push(dic.id);
            if(dic.isFinal){
                containsFinal = true;
            }
		});
        if(containsFinal){
            moon.error("不能完成操作,删除记录中包含了系统字典");
            return;
        }
		jQuery.ajaxSettings.traditional = true;
		$.getJsonData(contextPath + "/dictionary/delete", {
			ids : ids
		}, {
			type : 'Post'
		}).done(function() {
			$("#dictionaryTable").table("refresh");
			moon.success("字典项成功删除");
		});
	};

	var editDictionary = function() {
		var selectRows = $("#dictionaryTable").table("getSelect");
		if (selectRows.length == 0) {
			moon.error("请选择要编辑的字典.");
			return;
		}
		var editRow = selectRows[0];
        if(editRow.isFinal){
            moon.error("系统字典不能编辑");
            return;
        }
		$("#dictionaryForm").dialog(
				{
					title : '编辑',
					afterShown : function() {
						$("[name='dictionary.code']", "#dictionaryForm").val(editRow.code);
						$("[name='dictionary.name']", "#dictionaryForm").val(editRow.name);
						$("#dictionaryForm").validate({
							align : 'right',
							theme : "darkblue"
						});
					},
					beforeClose : function() {
						$("[name='dictionary.code']", "#dictionaryForm").val("");
						$("[name='dictionary.name']", "#dictionaryForm").val("");
						$("#dictionaryForm").validate("hide");
					},
					buttons : [ {
						text : '保存',
						click : doUpdate,
						css : 'btn btn-primary',
						data : editRow
					}, {
						text : '取消',
						css : 'btn btn-default',
						click : function() {
							this.close();
						}
					} ]
				});
	};

	var doUpdate = function(btn) {
		var data = {};
		data["dictionary.id"] = btn.data.id;
		$("#dictionaryForm").validate("validate").done(
				function(result) {
					if (result) {
						$.getJsonData(contextPath + "/dictionary/update",
								$("#dictionaryForm").formData(data), {
									type : "Post"
								}).done(function(data) {
							if (data.success) {
								$("#dictionaryForm").dialog("close");
								$("#dictionaryTable").table("refresh");
								$("#dictionaryForm").reset();
								moon.success("更新字典成功");
							}
						});
					}
				});
	}
})();