<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="../common/header.jsp" %>
     
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<m:require src="jquery,bootstrap,switch,noty,common"></m:require>
<title>系统设置</title>
 <script type="text/javascript">
     $(function(){


         $(":checkbox").bootstrapSwitch();
         $(".cache-checkbox").on('switchChange.bootstrapSwitch', function(event, state) {
            if(state){
                $(".cache-setting-details").removeClass("hide").show("slow");
            }else{
                $(".cache-setting-details").hide("slow");
            }
         });

         $.getJsonData(contextPath+"/setting/list").done(function(data){
             if(data &&  (data = data.result)){
                 if(typeof data["memcached.open"] == "string"){
                     data["memcached.open"] = data["memcached.open"] =="true";
                 }
                 $(".cache-checkbox").bootstrapSwitch('state', data["memcached.open"]);
                 $(".host").val(data["memcached.host"]||"");
                 $(".port").val(data["memcached.port"]||"");
                 if(data["memcached.open"]){
                     $(".cache-setting-details").removeClass("hide").show("slow");
                 }

             }
         });

         $(".save").click(function(){
            // $(".cache-checkbox").bootstrapSwitch('state',"false");
             $.getJsonData(contextPath+"/setting/update",{
                 "memcached.open":$(".cache-checkbox").bootstrapSwitch("state"),
                 "memcached.host":$(".host").val(),
                 "memcached.port":$(".port").val()

             } ,{type:"Post"}).done(function(data){
                 moon.info("配置更新成功");
             });
         });
     });
 </script>
 <style type="text/css">
     .margin-md{
         margin: 5px;
     }

     .margin-md-vertical{
         margin-top: 5px;
         margin-bottom: 5px;
     }

     .margin-md-horizontal{
         margin-left: 5px;
         margin-left: 5px;
     }

     .margin-lg{
         margin: 10px;
     }

     .margin-lg-vertical{
         margin-top: 10px;
         margin-bottom: 10px;
     }

     .margin-lg-horizontal{
         margin-left: 10px;
         margin-left: 10px;
     }

     .cache-setting-flag{
         margin-top: 18px;;
     }
 </style>
</head>
<body>
   <div class="container">
       <div class="cache-setting">
           <div class="row">
                <span class="col-md-5 h3">Memcached缓存</span>
                <div class="switch cache-setting-flag">
                   <input type="checkbox" class="cache-checkbox" checked data-on-text="开启" data-off-text="关闭"/>
               </div>
           </div>
           <div class="cache-setting-details hide">
               <div class="row margin-md-vertical">
                   <span class="col-md-4">缓存服务器地址：</span>
                   <div class="col-md-8">
                       <input type="text" class="form-control host" placeholder="缓存服务器地址,默认127.0.0.1"/>
                   </div>
               </div>
               <div class="row">
                   <span class="col-md-4">缓存服务器端口：</span>
                   <div class="col-md-8">
                       <input type="text" class="form-control port" placeholder="缓存服务器端口,默认11211"/>
                   </div>
               </div>
           </div>
       </div>

       <div class="margin-lg-vertical">
           <button type="button" class="btn btn-primary btn-lg btn-block save">保存</button>
       </div>
   </div>
</body>
</html>