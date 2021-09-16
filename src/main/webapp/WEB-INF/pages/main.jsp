<%@ page language="java" import="java.util.*" pageEncoding="UTF-8" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%
    String path = request.getContextPath();
    String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";

%>

<!DOCTYPE html>
<html>
<head>
   <%-- <base href="<%=basePath%>">--%>
    <meta charset="utf-8">
    <title>主页</title>
    <link rel="stylesheet" href="/resources/layui/css/layui.css">
  <%--  <link rel="stylesheet" href="resources/layui/css/theme.css">--%>
    <link rel="stylesheet" href="/resources/layui/font-awesome/css/font-awesome.min.css" media="all" />
    <style type="text/css">
        .rightmenu {
            background: #0096;
            color: white;
            font-size: 10px;
            text-align: center;
            width: 100px;
            border-radius: 1px 20px 20px 47px;
            height: 43px;
            margin-left: 30px;
        }
        .rightmenu li{
            font-size: 1px;
            cursor: pointer;

        }
        .layui-side-scroll .kit-side-fold {
            height: 35px;
            background-color: #4A5064;
            color: #aeb9c2;
            line-height: 35px;
            text-align: center;
            cursor: pointer
        }

        .hide-side{
            transition: width 0.3s;
            -moz-transition: width 0.3s;	/* Firefox 4 */
            -webkit-transition: width 0.3s;	/* Safari 和 Chrome */
            -o-transition: width 0.3s;	/* Opera */
            width: 35px !important;
            text-align: left;
        }
        .hide-side .layui-nav .layui-nav-item a{
            color: rgba(0,0,0,0) !important;
            /*#393D49 */
        }
        .hide-side .kit-side-fold{
            text-align: left;
            padding-left: 10px;
        }

        .hide-side .layui-nav .layui-nav-item .layui-icon {
            font-size: 20px !important;
            width: 20px;
            margin-left: -15px;
            color: #fff9ec;
        }

        .big-content{
            margin-left: 35px !important;
        }
        .big-footer{
            left:35px !important;
        }



    </style>
</head>
<body class="layui-layout-body">
<div class="layui-layout layui-layout-admin">
    <div class="layui-header">
        <div class="layui-logo">飞鸽-SYS</div>
        <!-- 头部区域（可配合layui已有的水平导航） -->
        <ul class="layui-nav layui-layout-left">
           <%-- <li class="layui-nav-item"><a href="">控制台</a></li>
            <li class="layui-nav-item"><a href="">商品管理</a></li>
            <li class="layui-nav-item"><a href="">用户</a></li>
            <li class="layui-nav-item">
                <a href="javascript:;">其它系统</a>
                <dl class="layui-nav-child">
                    <dd><a href="">邮件管理</a></dd>
                    <dd><a href="">消息管理</a></dd>
                    <dd><a href="">授权管理</a></dd>
                </dl>
            </li>--%>
        </ul>
        <ul class="layui-nav layui-layout-right">
            <li class="layui-nav-item">
                <a href="javascript:;">

                    <img src="" id="userIco"  class="layui-nav-img" />
                    <%--<c:out value="${user.username}"/>--%>
                    <span id="username"></span>
                </a>
                <dl class="layui-nav-child">

                    <dd><a href="#" onclick="return false;" id="info">个人信息</a></dd>
                </dl>
            </li>
            <li class="layui-nav-item"><a href="#" onclick="return false;" id="logout">注销</a></li>
        </ul>
    </div>

    <div class="layui-side layui-bg-black hide-side">
        <div class="layui-side-scroll">
            <div class="kit-side-fold"><i class="fa fa-navicon" aria-hidden="true"></i></div>
            <!-- 左侧导航区域（可配合layui已有的垂直导航） -->
            <ul class="layui-nav layui-nav-tree"  lay-filter="test">
              <%--  <li class="layui-nav-item layui-nav-itemed">
                    <a class="" href="javascript:;">所有商品</a>
                    <dl class="layui-nav-child">
                        <dd><a href="javascript:;">列表一</a></dd>
                        <dd><a href="javascript:;">列表二</a></dd>
                        <dd><a href="javascript:;">列表三</a></dd>
                        <dd><a href="">超链接</a></dd>
                    </dl>
                </li>
                <li class="layui-nav-item">
                    <a href="javascript:;">解决方案</a>
                    <dl class="layui-nav-child">
                        <dd><a href="javascript:;" class="site-demo-active">列表一</a></dd>
                        <dd><a href="javascript:;">列表二</a></dd>
                        <dd><a href="">超链接</a></dd>
                    </dl>
                </li>
                <li class="layui-nav-item"><a href="http://www.baidu.com">云市场</a></li>
                <li class="layui-nav-item"><a href="">发布商品</a></li>--%>
            </ul>
        </div>
    </div>
        <div class="layui-tab big-content" lay-filter="pageTabs" lay-allowclose="true" style="margin-left: 200px;">
            <ul class="layui-tab-title">
            </ul>
            <ul class="rightmenu" style="display: none;position: absolute;">
                <li data-type="closethis">关闭当前</li>
                <li data-type="closeall">关闭所有</li>
            </ul>
            <div class="layui-tab-content">
                <!-- 内容主体区域 -->
            </div>
        </div>

    <div class="layui-footer big-footer">
        <!-- 底部固定区域 -->
        @Author：SuperHaoa
<%--        &nbsp;&nbsp;@博客:<a href="" target="_blank">http://www.superhaoge.cn</a>
        &nbsp;&nbsp;@邮箱：--%>
    </div>

</div>


<script type="text/javascript"  src="/resources/layui/js/layui.all.js"></script>
<script type="text/javascript" src="/resources/jquery/jquery-3.2.1.min.js"></script>
<script type="text/javascript" src="/resources/base/common/js/superhao.js"></script>
<script type="text/javascript"  src="/resources/base/main/js/main.js"></script>

<script>

</script>
</body>
</html>