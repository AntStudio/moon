<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="../common/header.jsp" %>
<!DOCTYPE html>
<html>
<head>
	<title>数据字典</title>
	<m:require src="jquery,common,noty,font,bootstrap,table,ev,dialog,{dictionary/dictionary}"></m:require>
</head>

<body>
	<div id="dictionaryTable"></div>
	
	<!-- 字典项表单 -->
	<form id="dictionaryForm" class="form-horizontal hide">
		<div class="form-group">
            <label class="col-sm-2 control-label">字典代码:</label>
            <div class="col-sm-10">
			    <input type="text" class="form-control" name="dictionary.code" validate="validate[required]"/>
            </div>
		</div>
		<div class="form-group">
            <label class="col-sm-2 control-label">字典名称:</label>
            <div class="col-sm-10">
			    <input type="text" name="dictionary.name" class="form-control" validate="validate[required]"/>
            </div>
		</div>
	</form>
	
	<!-- 字典参数表单 -->
	<form id="dictionaryParamForm" class="form-horizontal hide">
		<div class="form-group">
            <label class="col-sm-3 control-label">字典参数名称:</label>
            <div class="col-sm-9">
			    <input type="text"  class="form-control" name="dictionary.code" validate="validate[required]"/>
            </div>
		</div>
		<div class="form-group">
            <label class="col-sm-3 control-label">字典参数值:</label>
            <div class="col-sm-9">
			    <input type="text"  class="form-control" name="dictionary.name" validate="validate[required]"/>
            </div>
		</div>
	</form>
</body>
</html>