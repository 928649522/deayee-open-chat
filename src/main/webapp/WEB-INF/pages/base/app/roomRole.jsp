<%@ page language="java" import="java.util.*" pageEncoding="UTF-8" %>
<%
    String path = request.getContextPath();
    String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
  <%--  <base href="<%=basePath%>">--%>

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
    <style type="text/css">
        .layui-btn {
            margin-top: -10px;
        }

        .mui-table-view.mui-grid-view {
            font-size: 0;
            display: block;
            width: 100%;
            padding: 0 10px 10px 0;
            white-space: normal;
        }

        .mui-table-view {
            list-style: none;
            margin-left: 50px;
        }

        .mui-table-view li {
            display: inline;
            line-height: 40px;
            float: left;
            margin-left: 20px;
            width: 16%;
        }

        .mui-media-body {
            overflow: hidden;
            text-overflow:ellipsis;
            white-space: nowrap;
            text-align: center;
        }

        .mui-table-view span {
            visibility: hidden ;
        }

        .mui-table-view img {
            max-height: 50px;
            max-width: 50px;
            margin-left: 31%;
            border-radius: 50%;
        }
        #addGroupManager{
            background-color: #808080;
        }
        .mui-table-view-cell:hover span {
            visibility: visible;
            top: -20px;
            font-size: 20px;
            color: red;
            font-weight: bold;
            cursor: pointer;
            position: relative;
        }

    </style>
    <%--   <link rel="stylesheet" href="resources/appchat/mui/css/mui.css"/>--%>
</head>

<body>
<div class="x-body">
    <form class="layui-form layui-form-pane" lay-verify="chatRoomForm" id="chatRoomForm" onsubmit="return false;"
          style="margin-left: 20px;">
        <input name="roomId" type="hidden" id="roomId"/>
        <input name="fileId" type="hidden" id="fileId"/>
        <input name="versionNumber" type="hidden" id="versionNumber"/>
        <div style="width:100%;height:400px;overflow: auto;">
            <div class="layui-form-item">
                <fieldset class="layui-elem-field layui-field-title" style="margin-top: 10px;">
                    <legend style="font-size:16px;">群主</legend>
                </fieldset>
                <div class="layui-form-item">
                    <label for="groupMaster" class="layui-form-label">
                        <span class="x-red"></span>设置群主
                    </label>
                    <div class="layui-input-inline">
                        <div class="layui-input-inline">
                            <select name="groupMaster" id="groupMaster" lay-verify="groupMaster" lay-search>
                                <option value=""></option>
                            </select>
                        </div>
                    </div>
                    <div id="ms" class="layui-form-mid layui-word-aux">
                        <span class="x-red"></span><span>注：从群组成员中选择一个群主master</span>
                    </div>
                </div>
                <div class="layui-form-item">
                    <fieldset class="layui-elem-field layui-field-title" style="margin-top: 10px;">
                        <legend style="font-size:16px;">管理员</legend>
                    </fieldset>
                    <div class="layui-form-item">
                        <label for="groupManager" class="layui-form-label">
                            <span class="x-red"></span>设置管理
                        </label>
                        <div class="layui-input-inline">
                            <div class="layui-input-inline">
                                <select name="groupManager" id="groupManager" lay-verify="groupManager" lay-search>
                                    <option value=""></option>
                                </select>
                            </div>
                        </div>
                        <div class="layui-form-mid layui-word-aux">
                            <button class="layui-btn layui-btn-normal" id="addGroupManager" lay-filter="addGroupManager"
                                    lay-submit>确定
                            </button>
                        </div>
                        <div class="layui-form-mid layui-word-aux">
                            <span class="x-red"></span><span id="ums">注：    从群组成员中添加多个管理员</span>
                        </div>
                    </div>
                    <ul id="groupManagerList" class="mui-table-view">
                    </ul>
                </div>


                <%--<div class="layui-form-item">
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
                </div>--%>

                <div style="width: 100%;height: 55px;background-color: white;border-top:1px solid #e6e6e6;
  position: fixed;bottom: 1px;margin-left:-20px;">
                    <div class="layui-form-item" style=" float: right;margin-right: 30px;margin-top: 8px">

                        <button class="layui-btn layui-btn-normal" lay-filter="save" lay-submit>
                            保存
                        </button>
                        <button class="layui-btn layui-btn-primary" id="close">
                            取消
                        </button>
                    </div>
                </div>

            </div>
    </form>
</div>


<script type="text/javascript" src="/resources/layui/treelayui/layui.js"></script>
<script type="text/javascript" src="/resources/jquery/jquery-3.2.1.min.js"></script>
<script type="text/javascript" src="/resources/base/common/js/superhao.js"></script>
<script type="text/javascript" src="/resources/base/app/room/roomRole.js"></script>

</body>
</html>
