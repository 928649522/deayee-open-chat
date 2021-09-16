<%@ page language="java" import="java.util.*" pageEncoding="UTF-8" %>
<%
    String path = request.getContextPath();
    String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
    <%--<base href="<%=basePath%>">--%>

    <title>QRCode</title>
    <meta name="renderer" content="webkit">
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
    <meta name="viewport"
          content="width=device-width,user-scalable=yes, minimum-scale=0.4, initial-scale=0.8,target-densitydpi=low-dpi"/>
    <meta http-equiv="pragma" content="no-cache">
    <meta http-equiv="cache-control" content="no-cache">
    <meta http-equiv="expires" content="0">
    <meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
    <meta http-equiv="description" content="This is my page">
    <!--
	<link rel="stylesheet" type="text/css" href="styles.css">
	-->
    <link rel="stylesheet" href="/resources/layui/css/layui.css"/>
    <script type="text/javascript" src="/resources/layui/treelayui/layui.js"></script>
    <script type="text/javascript" src="/resources/jquery/jquery-3.2.1.min.js"></script>
    <script type="text/javascript" src="/resources/base/common/js/qrcode.min.js"></script>
    <script type="text/javascript" src="/resources/base/common/js/superhao.js"></script>
   <style type="text/css">
       #qrcode{
           margin-left: 30%;
           margin-top: -80px;
       }
   </style>
</head>

<body>
<div class="x-body">
    <form class="layui-form layui-form-pane" lay-verify="chatRoomForm" id="chatRoomForm" onsubmit="return false;"
          style="margin-left: 20px;">
            <div class="layui-form-item">
                <fieldset class="layui-elem-field layui-field-title" style="margin-top: 10px;">
                    <legend style="font-size:16px;">群二维码</legend>
                </fieldset>
                <div class="layui-form-item">
                       <div id="qrcode"></div>
                </div>
            </div>
        <br/>
        <div class="layui-form-item">
            <div class="layui-inline">
                <label for="effictiveTime" class="layui-form-label" style="width: 200px;">
                    <span class="x-red">*</span>有效时间(单位：分钟）
                </label>
                <div class="layui-input-inline">
                    <input type="number" id="effictiveTime" name="effictiveTime" lay-verify="roomName"  autocomplete="off" class="layui-input">
                </div>
                <div id="ms" class="layui-form-mid layui-word-aux" style="padding: 0px 0!important">
                    <a class="layui-btn layui-btn-normal" id = "restExpire">重置</a>
                    <span class="x-red" style="color: #1E9FFF"> 有效时间剩余：<span id="time"></span>秒</span>
                </div>
            </div>
        </div>
        <div class="layui-form-item">
            <fieldset class="layui-elem-field layui-field-title" style="margin-top: 10px;">
                <legend style="font-size:16px;">群链接</legend>
            </fieldset>
            <div class="layui-form-item">
                <div id="qrcodeText" style="font-weight: bold"></div>
            </div>
        </div>
    </form>
        </div>
    </form>
</div>

<script type="text/javascript">
    layui.config({
        base: '/resources/base/module/js/',
        version: '1.0.2'
    }).use(['table', 'layer'], function () {
        var time=0;
        var qrcode = new QRCode('qrcode', {
            width: 150,  height: 150,
        })
        qrcode.clear();
        var roomId = queryRequestParam("roomId");
        if(!roomId){
            waringTips('缺少roomId');
            return;
        }

        baseService.simpleAjax('/sys/sysRoom/searchRoomHttpPath',{group:roomId},function (d) {
            if(!d.data){
                waringTips('RoomPagePath未配置');
                return;
            }
            var data = d.data;
            var text = data.path+'?roomId='+roomId;
            $("#qrcodeText").append(text);
            create(text);
            $("#effictiveTime").val(parseInt(data.effictiveTime/1000/60));
            console.log(new Date().getTime());
            time = (data.effictiveTime - (new Date().getTime() - data.creationTime))/1000;
            time = parseInt(time);

            var flag = setInterval(function () {
                                time-=1;
                                if(time>0){
                                    $("#time").text(time);
                                }else{
                                    $("#time").text(0);
                                  //  clearInterval(flag);
                                }
             },1000);

            //console.log(data);
            //  console.log(d.data+'?roomId='+roomId);
        });

        $("#restExpire").click(function () {
           var effictiveTime =  $("#effictiveTime").val();
            effictiveTime = parseInt(effictiveTime * 60);
            console.log(effictiveTime);
           if(effictiveTime<=0){
               waringTips('有效时间必须大于0');
               return;
           }
            baseService.simpleAjax('/sys/sysRoom/updateQrcodeExpire',{group:roomId,effictiveTime:effictiveTime},function (d) {
                if(!d.success){
                    waringTips('重置失败');
                    return;
                }
                successTips('重置成功');
                time =effictiveTime;
              });
        });

        function create(data) {
            var qrcode = new QRCode(document.getElementById("qrcode"), {
                text: data,
                width: 260,
                height: 260,
                colorDark : "#000000",
                colorLight : "#ffffff",
                correctLevel : QRCode.CorrectLevel.H
            });
        }


    });

    $(function () {

    })

</script>
</body>
</html>
