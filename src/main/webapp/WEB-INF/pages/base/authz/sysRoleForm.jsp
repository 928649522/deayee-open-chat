<%@ page language="java" import="java.util.*" pageEncoding="UTF-8" %>
<%
    String path = request.getContextPath();
    String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
   <%-- <base href="<%=basePath%>">--%>

    <title>角色表单</title>
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
        element{

        }
    </style>
    <link rel="stylesheet" href="/resources/layui/css/layui.css"/>
    <link rel="stylesheet" href="/resources/jquery/ztree/css/metroStyle/metroStyle.css">

</head>

<body>

<div class="x-body">
    <form class="layui-form layui-form-pane" lay-verify="sysRoleForm" id="sysRoleForm" onsubmit="return false;" style="margin-left: 20px;">
        <input name="roleId" type="hidden" id="roleId"/>
        <input name="versionNumber" type="hidden" id="versionNumber"/>
        <div style="width:100%;height:400px;overflow: auto;">
         
            <div class="layui-form-item">
                <fieldset class="layui-elem-field layui-field-title" style="margin-top: 10px;">
                    <legend style="font-size:16px;">基础信息</legend>
                </fieldset>
            </div>
            <div class="layui-form-item">
                <label for="roleName" class="layui-form-label">
                    <span class="x-red">*</span>角色名
                </label>
                <div class="layui-input-inline">
                    <input type="text"  id="roleName" name="roleName"  lay-verify="roleName"
                           autocomplete="off" class="layui-input">
                </div>
                <div id="ms" class="layui-form-mid layui-word-aux">
                    <span class="x-red">*</span><span id="ums">角色名必填</span>
                </div>
            </div>
            <div class="layui-form-item">
                <div class="layui-inline">
                    <label for="description" class="layui-form-label">
                        <span class="x-red">*</span>备注
                    </label>
                    <div class="layui-input-inline">
                        <input type="text" id="description" name="description" lay-verify="description"  autocomplete="off" class="layui-input">
                    </div>
                </div>
            </div>
            <div class="layui-form-item">
                <fieldset class="layui-elem-field layui-field-title" style="margin-top: 10px;">
                    <legend style="font-size:16px;">权限</legend>
                </fieldset>
            </div>
            <div class="layui-form-item">
                <div class="layui-inline">
                    <label class="layui-form-label">
                        <span class="x-red">*</span>权限选择
                    </label>
                    <div class="layui-input-inline">
                        <input type="hidden" name="menus">
                        <ul id="permissionTree" class="ztree"></ul>
                    </div>
                </div>
            </div>


            <div style="height: 60px"></div>
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




<script type="text/javascript" src="/resources/jquery/jquery-3.2.1.min.js"></script>
<script type="text/javascript" src="/resources/layui/treelayui/layui.js" charset="utf-8"></script>
<script type="text/javascript" src="/resources/jquery/ztree/js/jquery.ztree.core.js" charset="utf-8"></script>
<script type="text/javascript" src="/resources/jquery/ztree/js/jquery.ztree.excheck.js" charset="utf-8"></script>

<script type="text/javascript" src="/resources/base/common/js/superhao.js"></script>
<script type="text/javascript" src="/resources/base/authz/js/sysRoleForm.js"></script>

</body>
</html>
