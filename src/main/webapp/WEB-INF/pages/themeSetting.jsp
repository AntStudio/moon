<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<html>
    <head>
        <%@ include file="common/header.jsp"%>
        <m:require src="jquery,noty,common,bootstrap"/>
        <title>主题设置</title>
        <script type="text/javascript">
            $(function(){

                $(".save").click(function(){
                    var theme = $("[name='theme']:checked").val();
                    $.getJsonData(fullServerPath+"/theme",{theme:theme},{type:"Post"}).done(function(data){
                        if(data.success){
                            closePopup();
                        }else{
                            moon.error("主题设置发生错误");
                        }
                    });
                });
            });
            var layer = parent.layer;
            function closePopup(){
                if(layer){
                    layer.msg("主题设置成功,请刷新页面",{time:1500});
                    layer.close(parent.layer.getFrameIndex(window.name));
                }
            }
        </script>

        <style>
            .theme-item{
                border-top: solid 1px #ccc;
                line-height: 38px;
                margin-bottom:0;
                padding-left:20px;
            }
            .theme-container{
                border-bottom: solid 1px #ccc;
            }

            .btns{
                padding:20px;
                text-align: center;
            }
        </style>
    </head>
    <body class="theme-setting">
        <div class="theme-container">
            <c:forEach items="${themes}" var="theme">
                <div class="form-group theme-item">
                    <label class="control-label">
                        <input type="radio" name="theme" value="${theme.name}" <c:if test="${theme.name == currentTheme.name}">checked</c:if>/>
                        ${theme.description}
                    </label>
                </div>
            </c:forEach>
        </div>
        <div class="btns">
            <button type="button" class="btn btn-sm btn-primary save">保存</button>
            <button type="button" class="btn btn-sm btn-default">取消</button>
        </div>
    </body>
</html>