<%@ page language="java" import="java.util.*" pageEncoding="UTF-8" %>
<%
    String path = request.getContextPath();
    String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
   <%-- <base href="<%=basePath%>">--%>

    <title>出错了！</title>
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
    <style type="text/css">
        .tips{
            font-size: 40px;
            position: absolute;
            left: 15%;
            top: 36%;
        }
    </style>
</head>

<body>
${msg}
<div class="tips">∑(っ°Д°;)っ卧槽，ERROR~~~~</div>
<script type="text/javascript" src="resources/jquery/jquery-3.2.1.min.js"></script>
<%@include file="../refjsp/activity-font.jsp"%>
<script type="text/javascript">
    $(function(){
        $(".tips").flipping_text();
    });
</script>
</body>
</html>
