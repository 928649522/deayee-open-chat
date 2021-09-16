<%@ page language="java" import="java.util.*" pageEncoding="UTF-8" %>
<%
    String path = request.getContextPath();
    String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
  <%--  <base href="<%=basePath%>">--%>

    <title>chat room</title>
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

</head>

<body>
<div class="x-body">
    <form class="layui-form layui-form-pane" lay-verify="chatRoomForm" id="chatRoomForm" onsubmit="return false;" style="margin-left: 20px;">
        <input name="roomId" type="hidden" id="roomId"/>
        <input name="fileId" type="hidden" id="fileId"/>
        <input name="versionNumber" type="hidden" id="versionNumber"/>
        <div style="width:100%;height:400px;overflow: auto;">
            <div class="layui-form-item">
                <fieldset class="layui-elem-field layui-field-title" style="margin-top: 10px;">
                    <legend style="font-size:16px;">群组头像</legend>
                </fieldset>
                <div class="layui-input-inline">
                    <div class="layui-upload-drag" style="margin-left:10%;" id="uploadEle">
                        <i style="font-size:30px;" class="layui-icon"></i>
                        <p style="font-size: 10px">点击替换头像</p>
                    </div>
                </div>
                <div class="layui-input-inline">

                    <div  id="demo2" style="margin-top: 20px;margin-left: 50px">
                        <img src="/resources/img/moreChat.png" height="100px" class="layui-upload-img layui-circle">
                    </div>

                </div>
            </div>



            <div class="layui-form-item">
                <fieldset class="layui-elem-field layui-field-title" style="margin-top: 10px;">
                    <legend style="font-size:16px;">群设置</legend>
                </fieldset>
            </div>

            <div class="layui-form-item">
                <div class="layui-inline">
                    <label for="roomName" class="layui-form-label">
                        <span class="x-red">*</span>群组名称
                    </label>
                    <div class="layui-input-inline">
                        <input type="text" id="roomName" name="roomName" lay-verify="roomName"  autocomplete="off" class="layui-input">
                    </div>
                </div>
            </div>

            <div class="layui-form-item">
                <label for="findChatRecordNumber" class="layui-form-label">
                    <span class="x-red">*</span>历史记录
                </label>
                <div class="layui-input-inline">
                    <div class="layui-input-inline">
                        <input type="number" id="findChatRecordNumber" value="10" name="findChatRecordNumber" lay-verify="findChatRecordNumber"  autocomplete="off" class="layui-input">
                    </div>
                </div>
                <div id="ms" class="layui-form-mid layui-word-aux">
                    <span class="x-red">*</span><span id="ums">允许查看的历史聊天记录条数</span>
                </div>
            </div>

            <div class="layui-form-item">
                <div class="layui-inline" id="passwordDiv">
                    <label for="isAnonymous" class="layui-form-label">
                        <span class="x-red"></span>允许匿名
                    </label>
                    <div class="layui-input-inline">
                        <input type="checkbox"  name="isAnonymous" id="isAnonymous" lay-filter="isAnonymous" value="1"  lay-skin="switch" lay-text="允许|拒绝" checked />
                    </div>
                </div>
                <div class="layui-inline">
                    <label for="isPicture" class="layui-form-label">
                        <span class="x-red"></span>图片上传
                    </label>
                    <div class="layui-input-inline">
                        <input type="checkbox" name="isPicture"  id="isPicture"  lay-filter="isPicture" value="1"  lay-skin="switch" lay-text="开启|关闭" checked />
                        <%-- <input type="text" id="isPicture" readonly="readonly" name="isPicture"  lay-verify="isPicture"
                                autocomplete="off" class="layui-input">--%>
                    </div>
                </div>
            </div>

            <div class="layui-form-item">
                <label for="basePeople" class="layui-form-label">
                    人员总基数
                </label>
                <div class="layui-input-inline">
                    <div class="layui-input-inline">
                        <input type="number" id="basePeople" value="50" name="basePeople" lay-verify="basePeople"  autocomplete="off" class="layui-input">
                    </div>
                </div>
                <div  class="layui-form-mid layui-word-aux">
                    <span class="x-red">*</span><span>展示群组总人数基数</span>
                </div>
            </div>

            <div class="layui-form-item">
                <label for="onlineBasePeople" class="layui-form-label">
                    在线基数
                </label>
                <div class="layui-input-inline">
                    <div class="layui-input-inline">
                        <input type="number" id="onlineBasePeople" value="50" name="onlineBasePeople" lay-verify="onlineBasePeople"  autocomplete="off" class="layui-input">
                    </div>
                </div>
                <div  class="layui-form-mid layui-word-aux">
                    <span class="x-red">*</span><span>展示在线的人数，1~设置的范围，随机安排在线基数</span>
                </div>
            </div>
            <div class="layui-form-item">
                <label for="findChatRecordNumber" class="layui-form-label">
                    发言时长
                </label>
                <div class="layui-input-inline">
                    <div class="layui-input-inline">
                        <input type="number" id="anonymousSayTime" value="-1" name="anonymousSayTime" lay-verify="anonymousSayTime"  autocomplete="off" class="layui-input">
                    </div>
                </div>
                <div  class="layui-form-mid layui-word-aux">
                    <span class="x-red">*</span><span>限制匿名用户发言的时长{单位:分钟;-1:不限制}</span>
                </div>
            </div>
            <div class="layui-form-item">
                <label for="findChatRecordNumber" class="layui-form-label">
                    发言条数
                </label>
                <div class="layui-input-inline">
                    <div class="layui-input-inline">
                        <input type="number" id="anonymousSayNumber" value="-1" name="anonymousSayNumber" lay-verify="anonymousSayNumber"  autocomplete="off" class="layui-input">
                    </div>
                </div>
                <div class="layui-form-mid layui-word-aux">
                    <span class="x-red">*</span><span>限制匿名用户发言的条数{-1:不限制}</span>
                </div>
            </div>
            <div class="layui-form-item">
                <label for="findChatRecordNumber" class="layui-form-label">
                    广告链接
                </label>
                <div class="layui-input-inline">
                    <div class="layui-input-inline">
                        <input type="text" id="ad" placeholder="" value="" name="ad" lay-verify="ad"  autocomplete="off" class="layui-input">
                    </div>
                </div>
                <div class="layui-form-mid layui-word-aux">
                    <span class="x-red">*</span><span>建议使用https链接(例：https://xxx.xxx.xxx)</span>
                </div>
            </div>


            <div class="layui-form-item">
                <div class="layui-inline" >
                    <label for="roomName" class="layui-form-label" >
                        <span class="x-red"></span>置顶消息
                    </label>
                    <div class="layui-input-inline" style="margin-left: 20px">
                        <textarea rows="5" cols="55" name="remark" id="remark" maxlength="200"  lay-verify="remark"></textarea>
                    </div>
                </div>
            </div>


        </div>
        <div style="width: 100%;height: 55px;background-color: white;border-top:1px solid #e6e6e6;
  position: fixed;bottom: 1px;margin-left:-20px;">
            <div class="layui-form-item" style=" float: right;margin-right: 30px;margin-top: 8px">

                <button  class="layui-btn layui-btn-normal" lay-filter="save" lay-submit>
                    保存
                </button>
                <button  class="layui-btn layui-btn-primary" id="close">
                    取消
                </button>
            </div>
        </div>
    </form>
</div>

<script type="text/javascript" src="/resources/layui/treelayui/layui.js"></script>
<script type="text/javascript" src="/resources/jquery/jquery-3.2.1.min.js"></script>
<script type="text/javascript" src="/resources/base/common/js/superhao.js"></script>


<script type="text/javascript" src="/resources/base/app/room/chatRoomForm.js"></script>

</body>
</html>
