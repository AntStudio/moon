<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8" %>
<%@ include file="../common/header.jsp" %>
<m:require src="jquery,bootstrap,table,font"/>
<script type="text/javascript">
    var topY, bottomY;
    $(function () {
        $("#userTable").table({url: contextPath + "/permission/getPermissionData",
            columns: [
                {name: "id"},
                {name: "name"},
                {name: "code"}
            ],
            formatData: function (data) {
                return {items: data.result.items, totalItemsCount: data.result.totalItemsCount};
            },
            title: "测试表格",
            showSelectBox: true
        });
    });
</script>
<div id="userTable"></div>