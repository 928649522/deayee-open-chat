<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
  
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
  <link rel="stylesheet" href="/resources/layui/css/layui.css"/>
  </head>
  
  <body>
  <div class="gridBody">
      <table class="layui-hidden" id="groupBlacklistTable" lay-filter="groupBlacklistTable"></table>
  </div>
  <script type="text/html" id="toolbar">
      <a class="layui-btn layui-btn-xs layui-btn-normal" lay-event="relieve">解除</a>
  </script>
  <script type="text/javascript" src="/resources/layui/treelayui/layui.js"></script>
<script type="text/javascript" src="/resources/jquery/jquery-3.2.1.min.js"></script>
<script type="text/javascript" src="/resources/base/common/js/superhao.js"></script>
  <script type="text/javascript">
      layui.config({
          base: '/resources/base/module/js/',
          version: '1.0.2'
      }).use(['table', 'layer'], function () {
          var table = layui.table;
          var clos = [[
               {type: 'numbers', title: '序号'}
              , {field: 'nickName', title: '昵称'}
              , {field: 'userName', title: '飞鸽号', sort: true, templet: function (d) {
                      if(d.userName){
                          return d.userName;
                      }else{
                          return '未绑定';
                      }
                  }}
              , {field: 'phone', title: '手机', sort: true, templet: function (d) {
                      if(d.phone){
                          return d.phone;
                      }else{
                          return '未绑定';
                      }
                  }}
              , {field: 'email', title: '邮箱', sort: true, templet: function (d) {
                      if(d.email){
                          return d.email;
                      }else{
                          return '未绑定';
                      }
                  }}
          ]];
          var roomId = queryRequestParam("roomId");

          var targetTab = table.render({
              id: 'groupBlacklistTable'
              ,elem: '#groupBlacklistTable'
              ,method:'post'
              , height: '100%'
              , url: '/sys/sysRoom/groupBlacklistTable'
              , cols: clos
              , page: false
              ,height: '700'
              ,where: {
                  roomId: roomId
              }
              ,headers:baseService.token.get()
          });
          
          
          table.on('tool(groupBlacklistTable)', function (obj) {
              var row = obj.data //获得当前行数据
                  , layEvent = obj.event; //获得 lay-event 对应的值
              if(layEvent=='relieve'){
                  baseService.simpleAjax('/sys/sysRoom/relieveBlacklist'
                      , {roomId: roomId,userId:row.userId}, function (d) {
                          targetTab.reload();
                          layer.alert('解除成功');
                      },true);
              }
          });
      });
      
  </script>
  </body>
</html>
