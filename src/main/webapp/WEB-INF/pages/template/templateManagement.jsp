<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8" %>
<%@ include file="../common/header.jsp" %>
<m:require src="jquery,common,handlebars,webuploader,noty,font,bootstrap,table,ev,dialog,{template/template}"/>
<div class="templates">
    <div class="opt-btns">
        <a href="javascript:void(0)" class="add btn btn-warning"><i class="fa fa-plus"></i> 添加模板</a>
    </div>
    <div class="content">

    </div>
</div>

<%--表单模板--%>
<script id="template" type="text/html">
    <form class="form-horizontal template-form">
        <div class="form-group">
            <span class="control-label col-md-2">模板名称</span>
            <div class="col-md-10">
                <input class="form-control" name="name" placeholder="模板名称..." value="{{name}}"/>
            </div>
        </div>
        <div class="form-group">
            <span class="control-label col-md-2">模板内容</span>
            <div class="col-md-10">
                <textarea class="form-control" name="content" placeholder="模板内容..." rows="5">{{content}}</textarea>
            </div>
        </div>
        <div class="form-group">
            <span class="control-label col-md-2">效果图</span>
            <input type="hidden" name="cover" class="cover" value="{{cover}}"/>
            <div class="col-md-10">
                <div id="fileContainer" class="uploader-list">
                    {{#nonEmpty cover}}
                    <img src="{{fullServerPath}}/file/get/{{cover}}" class="cover-image">
                    {{/nonEmpty}}
                </div>
                <div id="picker">点击添加效果图</div>
            </div>
        </div>
    </form>
</script>

<%--模板栏目--%>
<script id="templateList" type="text/html">
    {{#each items}}
        <div class="template-item" data-id="{{id}}">
            <i class="fa fa-times remove"></i>
            <img src="{{fullServerPath}}/file/get/{{cover}}" class="cover-image">
            <div class="info">{{name}}</div>
            <div class="info">创建时间：{{time}}</div>
            <div class="info">更新时间：{{lastUpdateTime}}</div>
        </div>
    {{/each}}
</script>
