(function(w,$){
    $.getJsonData(contextPath+"/dbMaintenance/tables").done(function(data){
        data = data.result;
        var list = "";
        $.each(data,function(index,e){
            list += "<li data-name=\""+ e.tableName+"\"><a href=\"javascript:void(0)\">"+ e.tableName+"</a></li>";
        });
        $(".table-name-list ul").html(list);
    });

    $(document).on("click",".table-name-list a",function(e){
        $(".table-name-list .clicked").removeClass("clicked");
        var tableName = $(e.target).addClass("clicked").closest("li").attr("data-name");
        loadTable(tableName);
    });

    function loadTable(tableName){
        $(".columns").table({
            title:"数据表 "+tableName,
            columns:[
                {
                    name:"name",
                    display:"字段名"
                },
                {
                    name:"type",
                    display:"类型"
                },
                {
                    name:"size",
                    display:"长度"
                },
                {
                    name:"nullable",
                    display:"是否允许空"
                },
                {
                    name:"autoIncrement",
                    display:"是否自增"
                }
            ],
	        url : contextPath+"/dbMaintenance/table/"+tableName,
            showNumber : true,//是否显示行号，默认显示
            formatData : function(data){
                var result = data.result;
                return {items:result,totalItemCount:result.length};
            },
            pageSize:100
        });
    }
})(window,jQuery);