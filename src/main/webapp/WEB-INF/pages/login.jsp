<%@ page language="java" import="java.util.*" pageEncoding="UTF-8" %>
<%
    String path = request.getContextPath();
    String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";
    String wsPrefix = request.getServerName() + ":" + request.getServerPort() + path + "/";

%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
 <%--   <base href="<%=basePath%>">--%>

    <title>飞鸽 - Administrator</title>

    <meta http-equiv="pragma" content="no-cache">
    <meta http-equiv="cache-control" content="no-cache">
    <meta http-equiv="expires" content="0">
    <meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
    <meta http-equiv="description" content="login">

    <title>Base Project Login</title>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width,initial-scale=1.0">
    <meta name="description" content="">
    <meta name="author" content="">

    <!-- CSS -->
    <link rel="stylesheet" href="/resources/base/login/css/reset.css">
    <link rel="stylesheet" href="/resources/base/login/css/supersized.css">
    <link rel="stylesheet" href="/resources/base/login/css/style.css">


</head>
<body oncontextmenu="return false">
<div class="page-container">
    <h1><%--Base-Project-Login--%>飞鸽 - Administrator</h1>
    <form  method="post">
        <div>
            <input type="text" name="username" class="username" placeholder="Account" autocomplete="off"/>
        </div>
        <div>
            <input type="password" name="password" class="password" placeholder="Password" oncontextmenu="return false" onpaste="return false" />
        </div>
        <button id="submit" type="button">登录</button>
    </form>
    <div class="connect">
        <p>May all beings be happy and secure ; may their mind be contented!</p>
        <p style="margin-top:20px;">愿所有的人都幸福安宁，内心满足！</p>
    </div>
</div>
<div class="alert" style="display:none;">
    <h2>消息</h2>
    <div class="alert_con">
        <p id="ts"><%=request.getAttribute("res")%></p>
        <p style="line-height:30px"><a class="btn">确定</a></p>
    </div>
</div>

<form name="href" action="/sys/index" method="post" style="display: none;">
<input id="Authorization" type="hidden" name="Authorization" value="">
</form>

<!-- Javascript -->
<%--<script src="http://apps.bdimg.com/libs/jquery/1.6.4/jquery.min.js" type="text/javascript"></script>--%>
<script src="/resources/jquery/jquery-2.2.3.min.js"></script>
<script src="/resources/base/login/js/supersized.3.2.7.min.js"></script>
<script src="/resources/base/login/js/supersized-init.js"></script>
<script src="/resources/base/common/js/md5.js"></script>
<script src="/resources/base/common/js/superhao.js"></script>
<script src="/resources/base/login/js/login.js"></script>


</body>
</html>
