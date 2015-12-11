<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8" %>
<%@ include file="../common/header.jsp" %>
<m:require src="jquery,noty,common,bootstrap,webuploader,dialog,font,table,{maintenance/DBMaintenance}"/>

<div class="DBMaintence">
    <div class="db-info">
        <div class="item">
            数据库类型：
            <span class="db-type">${dbInfo.name}</span>
        </div>
        <div class="item">
            版本：
            <span class="db-version">${dbInfo.version}</span>
        </div>
        <div class="item db-init">
            <input type="button" class="btn btn-danger init-db" value="初始化数据库" title="注意:此操作会清空已有数据">
            <i class="loading hide"></i>
        </div>
        <div class="item">
            <input type="button" class="btn btn-info clear-cache" value="清空缓存">
            <i class="loading hide"></i>
        </div>
    </div>
    <div class="table-content">
        <div class="table-name-list">
            <span class="table-name-title">数据表</span>
            <ul class="nav nav-tabs nav-stacked">
                <li class="loading"><i class="fa fa-spinner fa-spin"></i></li>
            </ul>
        </div>
        <div class="columns">

        </div>
    </div>
</div>