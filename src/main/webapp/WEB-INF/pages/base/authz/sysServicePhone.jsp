<%@ page language="java" import="java.util.*" pageEncoding="UTF-8" %>
<%
    String path = request.getContextPath();
    String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
   <%-- <base href="<%=basePath%>">--%>

    <title>用户表单</title>
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
        <input name="userId" type="hidden" id="userId"/>
        <div style="width:100%;height:400px;overflow: auto;">

            <div class="layui-form-item">
                <fieldset class="layui-elem-field layui-field-title" style="margin-top: 10px;">
                    <legend style="font-size:16px;">1.基础配置</legend>
                </fieldset>
            </div>
            <div class="layui-form-item">
                <label for="accountName" class="layui-form-label">
                    <span class="x-red">*</span>起始数
                </label>
                <div class="layui-input-inline">
                    <input type="text"  id="begin" name="begin"  lay-verify="accountName"
                           autocomplete="off" class="layui-input">
                </div>

                <div id="ms" class="layui-form-mid layui-word-aux">
                    <span class="x-red">*</span><span id="">数据库对应条数，将从该记录数开始往下找</span>
                </div>

            </div>

            <button  class="layui-btn layui-btn-normal" lay-filter="open" id="open">
                开始执行
            </button>

            <div class="layui-form-item">
                <fieldset class="layui-elem-field layui-field-title" style="margin-top: 10px;">
                    <legend style="font-size:16px;">2.参数反馈</legend>
                </fieldset>
            </div>
            <div class="layui-form-item">
                <label for="" class="layui-form-label">
                    <span class="x-red">*</span>结果查看 ser_kluser_t 表<span id="number"></span>
                </label>
                <div class="layui-input-inline">
                   <%-- <input type="text"  id="accountName" name="accountName"  lay-verify="accountName"
                           autocomplete="off" class="layui-input">--%>
                </div>
                <div id="" class="layui-form-mid layui-word-aux">
                    <span class="x-red">*</span><span id="ums"></span>
                </div>
            </div>

            <button  class="layui-btn layui-btn-normal" lay-filter="stop" id="stop" >
                停止执行
            </button>


</div>

<script type="text/javascript" src="/resources/layui/treelayui/layui.js"></script>
<script type="text/javascript" src="/resources/jquery/jquery-3.2.1.min.js"></script>
<script type="text/javascript" src="/resources/base/common/js/md5.js"></script>
<script type="text/javascript" src="/resources/base/common/js/superhao.js"></script>
<script type="text/javascript" src="/resources/base/authz/js/sysServicePhone.js"></script>

</body>
</html>
