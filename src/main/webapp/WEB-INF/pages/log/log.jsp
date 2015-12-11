<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8" %>
<%@ include file="../common/header.jsp" %>
<m:require src="jquery,zt,js/ztree.extend.js,dp,handlebars,common,bootstrap,table,font,dialog,js/pages/log/log.js"/>

<form class="form-horizontal">
    <div class="form-group">
        <label class="control-label col-md-2">操作人</label>
        <div class="col-md-3">
            <input type="text" class="form-control" name="operator" placeholder="操作人">
        </div>
        <label class="control-label col-md-2">类型</label>
        <div class="col-md-3">
            <input type="text" class="form-control" name="type" placeholder="类型">
        </div>
    </div>
    <div class="form-group">
        <label class="control-label col-md-2">时间</label>
        <div class="col-md-3">
            <input type="text" class="form-control" name="startTime" onclick="WdatePicker()">
        </div>
        <label class="control-label col-md-2">至</label>
        <div class="col-md-3">
            <input type="text" class="form-control" name="endTime" onclick="WdatePicker()">
        </div>
        <div class="col-md-2">
            <button type="button" class="btn btn-primary search btn-block">搜索</button>
        </div>
    </div>
</form>
<div id="logTable"></div>
<div id="logDetail" style="display: none;">
    <div id="content">
        <div><label>操作人：</label><span>{{user.realName}}</span></div>
        <div><label>操作时间：</label><span>{{time}}</span></div>
        <div><label>详情：</label><span>{{detail}}</span></div>
    </div>
</div>