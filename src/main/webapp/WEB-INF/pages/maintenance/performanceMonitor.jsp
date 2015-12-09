<%@ page language="java" contentType="text/html; charset=utf-8"
         pageEncoding="utf-8" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <%@ include file="../common/header.jsp" %>
    <m:require src="jquery,bootstrap,font,handlebars,noty,common"></m:require>
    <title>${currentMenu.menuName}</title>
    <script type="text/javascript">
        $(function () {
            $.getJsonData(contextPath + "/performance/data").done(function (data) {
                $("#monitorData").renderTemplate(data.result, {container: ".monitor-data"});
                $("#DBStatusTemplate").renderTemplate(data.result.dbStatus, {container: ".db-status"});
            });

        });
    </script>
    <style type="text/css">
        .monitor-item .title {
            background-color: #3598DB;
            height: 44px;
            font-size: 22px;
            line-height: 44px;
            padding: 0 20px;
            color: #FFFFFF;
        }

        .item {
            padding: 5px;
        }

        .monitor-item {
            border: solid 1px #3598DB;
            margin: 0 20px 20px;
        }

        .item .value {
            font-size: 26px;
            color: #ED5821;
            text-align: center;
        }

        .item .value.create-date {
            font-size: 20px;
        }

        .item .label {
            text-align: center;
            color: #444444;
            font-size: 14px;
            font-weight: normal;
            line-height: 37px;
            padding: 0;
            display: block;
        }
    </style>
</head>
<body style="">
<div class="wrapper">
    <%@ include file="../common/nav.jsp" %>
    <section class="content-section">
        <h3 class="header-title">${currentMenu.menuName}</h3>
        <%--<i class="fa fa-spinner fa-spin"></i>正在加载--%>
        <div class="monitor-item">
            <div class="title">接口响应效率监控</div>
            <table class="table table-bordered table-striped" id="monitorTable">
                <thead>
                <tr class="th">
                    <th>接口</th>
                    <th>访问次数</th>
                    <th>最大响应时间(ms)</th>
                    <th>平均响应时间(ms)</th>
                    <th>99%(ms)</th>
                    <th>95%(ms)</th>
                    <th>75%(ms)</th>
                </tr>
                </thead>
                <tbody class="monitor-data">

                </tbody>
            </table>
        </div>

        <div class="monitor-item">
            <div class="title">数据库状态</div>
            <div class="db-status">

            </div>
        </div>

        <script id="monitorData" type="text/html">
            {{#each interfaceInfo}}
            <tr>
                <td>{{interface}}</td>
                <td>{{count}}</td>
                <td>{{max}}</td>
                <td>{{mean}}</td>
                <td>{{99th}}</td>
                <td>{{95th}}</td>
                <td>{{75th}}</td>
            </tr>
            {{/each}}
        </script>

        <script type="text/html" id="DBStatusTemplate">
            <div class="item">
                <div class="col-sm-3">
                    <div class="label">当前活动连接数</div>
                    <div class="value">{{activeCount}}</div>
                </div>
                <div class="col-sm-3">
                    <div class="label">空闲连接数</div>
                    <div class="value">{{availableCount}}</div>
                </div>
                <div class="col-sm-3">
                    <div class="label">总连接数</div>
                    <div class="value">{{sumCount}}</div>
                </div>
                <div class="col-sm-3">
                    <div class="label">已服务的连接数</div>
                    <div class="value">{{servedCount}}</div>
                </div>
                <div class="clearfix"></div>
            </div>

            <div class="item">
                <div class="col-sm-3">
                    <div class="label">已拒绝的连接数</div>
                    <div class="value">{{refusedCount}}</div>
                </div>
                <div class="col-sm-3">
                    <div class="label">连接池创建时间</div>
                    <div class="value create-date">{{date}}</div>
                </div>
                <div class="clearfix"></div>
            </div>
        </script>
    </section>
</div>
</body>
</html>