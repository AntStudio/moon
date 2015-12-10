<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8" %>
<%@ include file="../common/header.jsp" %>
<m:require src="jquery,bootstrap,font,noty,common"/>
<script type="text/javascript">
    $(function () {
        $.getJsonData(contextPath + "/setting/list").done(function (data) {
            if (data && (data = data.result)) {
                $.each(data,function(index,v){
                    $("[name='"+index+"']").val(v);
                });
            }
        });

        $(".save").click(function () {
            // $(".cache-checkbox").bootstrapSwitch('state',"false");
            var prams = {};
            $(":text").each(function(index,e){
                var $e = $(e);
                prams[$e.attr("name")] = e.value;
            });
            $.getJsonData(contextPath + "/setting/update", prams, {type: "Post"}).done(function (data) {
                moon.info("配置更新成功");
            });
        });
    });
</script>
<style type="text/css">
    .margin-lg-vertical {
        margin-top: 10px;
        margin-bottom: 10px;
    }

    .setting-container{
        padding: 20px 35px;
    }

    .splitter{
        border-top: solid 1px #929799;
        border-bottom: solid 1px #FFFFFF;
        margin-left: 250px;
        margin-top: -14px;
        margin-bottom: 25px;
        margin-right: 20px;
    }

    .label-text{
        width: 150px;
        display: inline-block;
    }

    .input-box{
        width: 300px;
        display: inline-block;
    }

    .setting{
        margin-top:20px;
    }

    .margin-md-vertical{
        margin-bottom:10px;
        margin-left: 10px;
    }

    .database span{
        color: #F77E4A;
    }

    .envelope span{
        color: #62BDF5;
    }

    .welcome span{
        color: #08D52A;
    }

    .email span{
        color:#DD33F9;
    }

    .other span{
        color: #008BFF;
    }

    .database .splitter{
        margin-left: 268px;
    }

    .envelope .splitter{
        margin-left: 180px;
    }

    .welcome .splitter{
        margin-left: 175px;
    }

    .email .splitter{
        margin-left: 135px;
    }

    .other .splitter{
        margin-left: 135px;
    }
    .save{
        width:400px;
    }

    .description{
        margin-left: 160px;
        color: #ccc;
    }
</style>
<div class="setting-container">
    <div class="setting">
        <div class="row database">
            <span class="h3"><i class="fa fa-database"></i> 数据库备份与恢复设置</span>
            <div class="splitter"></div>
        </div>
        <div class="row margin-md-vertical">
            <span class="label-text">数据库主机：</span>

            <div class="input-box">
                <input type="text" name="db.host" class="form-control host" placeholder="数据库主机,默认127.0.0.1"/>
            </div>
        </div>
        <div class="row margin-md-vertical">
            <span class="label-text">数据库端口：</span>

            <div class="input-box">
                <input type="text" name="db.port" class="form-control port" placeholder="数据库端口,默认3306"/>
            </div>
        </div>
        <div class="row margin-md-vertical">
            <span class="label-text">数据库名称：</span>

            <div class="input-box">
                <input type="text" name="db.name" class="form-control name"/>
            </div>
        </div>
        <div class="row margin-md-vertical">
            <span class="label-text">数据库用户名：</span>

            <div class="input-box">
                <input type="text" name="db.username" class="form-control name"/>
            </div>
        </div>
        <div class="row margin-md-vertical">
            <span class="label-text">数据库密码：</span>

            <div class="input-box">
                <input type="text" name="db.password" class="form-control name"/>
            </div>
        </div>
        <div class="row margin-md-vertical">
            <span class="label-text">数据库客户端路径：</span>

            <div class="input-box">
                <input type="text" name="db.executableDir" class="form-control name"/>
            </div>
        </div>
    </div>

    <div class="setting">
        <div class="row envelope">
            <span class="h3"><i class="fa fa-envelope"></i> 短信发送设置</span>
            <div class="splitter"></div>
        </div>
        <div class="row margin-md-vertical">
            <span class="label-text">1分钟最多条数：</span>

            <div class="input-box">
                <input type="text" name="sms.minute.max" class="form-control name"/>
            </div>
        </div>

        <div class="row margin-md-vertical">
            <span class="label-text">1小时最多条数：</span>

            <div class="input-box">
                <input type="text" name="sms.hour.max" class="form-control name"/>
            </div>
        </div>

        <div class="row margin-md-vertical">
            <span class="label-text">1天最多条数：</span>

            <div class="input-box">
                <input type="text" name="sms.day.max" class="form-control name"/>
            </div>
        </div>
    </div>

    <div class="setting">
        <div class="row welcome">
            <span class="h3"><i class="fa fa-smile-o"></i> 欢迎页面设置</span>
            <div class="splitter"></div>
        </div>
        <c:forEach items="${userTypes}" var="userType">
            <div class="row margin-md-vertical">
                <span class="label-text">${userType.name}：</span>

                <div class="input-box">
                    <input type="text" name="user.index.userType${userType.code}" class="form-control name"/>
                </div>
            </div>
        </c:forEach>
    </div>

    <div class="setting">
        <div class="row email">
            <span class="h3"><i class="fa fa-envelope-o"></i> 邮箱设置</span>
            <div class="splitter"></div>
        </div>
        <div class="row margin-md-vertical">
            <span class="label-text">统计邮件接收：</span>

            <div class="input-box">
                <input type="text" name="email.statistics" class="form-control name"/>
            </div>
        </div>
        <div class="row margin-md-vertical">
            <span class="label-text">系统异常邮件接收：</span>

            <div class="input-box">
                <input type="text" name="email.exception" class="form-control name"/>
            </div>
        </div>
        <div class="row margin-md-vertical description">注：多个邮箱，使用逗号分隔</div>
    </div>
    <div class="setting">
        <div class="row other">
            <span class="h3"><i class="fa fa-list-ul"></i> 其他设置</span>
            <div class="splitter"></div>
        </div>
        <div class="row margin-md-vertical">
            <span class="label-text">医生(含公众号)角色：</span>

            <div class="input-box">
                <input type="text" name="role.doctorWithPublicMember" class="form-control name"/>
            </div>
        </div>
    </div>
    <button type="button" class="btn btn-primary btn-lg save">保存</button>
</div>