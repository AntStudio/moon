<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8" %>
<%@ include file="../common/header.jsp" %>
<m:require src="jquery,noty,common,bootstrap,webuploader,dialog,font,table,{maintenance/DBBackup}"/>

<div class="DBBackup">
    <div id="backupTable"></div>
</div>
<div class="backup-edit-dialog hide">
    <span id="upload">选择要上传的数据库备份文件</span>
    <div id="fileContainer" class="alert alert-info hide">
        <span class="info"></span>
        <span class="loading"><i class="fa fa-circle-o-notch fa-spin"></i>上传中...</span>
    </div>
</div>