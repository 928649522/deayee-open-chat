<%@ page language="java" import="java.util.*" pageEncoding="UTF-8" %>
<%
    String path = request.getContextPath();
    String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
    <%--<base href="<%=basePath%>">--%>

    <title>My JSP 'MyJsp.jsp' starting page</title>
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
 <%--   <script src="http://cdn.sockjs.org/sockjs-0.3.min.js"></script>--%>
    <link rel="stylesheet" href="//cdn.bootcss.com/bootstrap/3.3.5/css/bootstrap.min.css">
    <link rel="stylesheet" href="//cdn.bootcss.com/bootstrap/3.3.5/css/bootstrap-theme.min.css">
    <script src="//cdn.bootcss.com/jquery/1.11.3/jquery.min.js"></script>
    <script src="//cdn.bootcss.com/bootstrap/3.3.5/js/bootstrap.min.js"></script>
    <script type="text/javascript">
        $(function() {
            var websocket;
            if('WebSocket' in window) {
                console.log("此浏览器支持websocket");
                websocket = new WebSocket("ws://127.0.0.1:8080/chat/12345?id=999&&token=dsddsdsd");
            } else if('MozWebSocket' in window) {
                alert("此浏览器只支持MozWebSocket");
            } else {
                alert("此浏览器只支持SockJS");
            }
            websocket.onopen = function(evnt) {
                $("#tou").html("链接服务器成功!")
            };
            websocket.onmessage = function(evnt) {
                $("#msg").html($("#msg").html() + "<br/>" + evnt.data);
            };
            websocket.onerror = function(evnt) {};
            websocket.onclose = function(evnt) {
                $("#tou").html("与服务器断开了链接!")
            }




            $('#send').click(function(){
                send();
            });

            function send() {
                if(websocket != null) {
                    var message = document.getElementById('message').value;
                    websocket.send(message);
                } else {
                    alert('未与服务器链接.');
                }
            }
        });
    </script>
</head>

<body>
<div class="page-header" id="tou">
    webSocket多终端聊天测试
</div>
<div class="well" id="msg"></div>
<div class="col-lg">
    <div class="input-group">
        <input type="text" class="form-control" placeholder="发送信息..." id="message">

        <span class="input-group-btn">
                    <button class="btn btn-default" type="button" id="send" >发送</button>
                </span>
    </div>
</div>
</body>

</html>
