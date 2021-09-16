<%@ page language="java" import="java.util.*" pageEncoding="UTF-8" %>
<%
    String path = request.getContextPath();
    String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
   <%-- <base href="<%=basePath%>">--%>

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

<div id="controlBtn" class="layui-btn-group">
</div>

<div class="gridBody">
    <table class="layui-hidden" id="chatRoomTable" lay-filter="chatRoomTable"></table>
</div>

<script type="text/html" id="toolbar">

    <a class="layui-btn layui-btn-xs layui-btn-normal" lay-event="roomRole">设置角色</a>
    <a class="layui-btn layui-btn-xs layui-btn-normal" lay-event="qrCode">二维码</a>
    <a class="layui-btn layui-btn-xs " style="background-color: #808080;" lay-event="blacklist">解除黑名单</a>
    <%--<a class="layui-btn layui-btn-danger layui-btn-xs" lay-event="del">群主</a>--%>
</script>

<script type="text/javascript" src="/resources/layui/treelayui/layui.js"></script>
<script type="text/javascript" src="/resources/jquery/jquery-3.2.1.min.js"></script>
<script type="text/javascript" src="/resources/base/common/js/superhao.js"></script>

<script type="text/javascript" src="/resources/base/app/room/chatRoom.js"></script>

</body>
</html>
