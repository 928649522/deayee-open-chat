<%@ page language="java" import="java.util.*" pageEncoding="UTF-8" %>
<%
    String path = request.getContextPath();
    String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
   <%-- <base href="<%=basePath%>">--%>

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
        .x-red{color: red;}
        .layui-form-pane .layui-form-text .layui-form-label{
            width:24%;
        }
    </style>
</head>

<body>
<div class="x-body">
    <form class="layui-form layui-form-pane" style="margin-left: 20px;" lay-filter="permissionForm">
        <div style="width:100%;height:500px;overflow: auto;">
            <div class="layui-form-item">
                <fieldset class="layui-elem-field layui-field-title" style="margin-top: 10px;">
                    <legend style="font-size:16px;">基础信息</legend>
                </fieldset>
            </div>




            <div style="margin-left:25%">

                <input name="permissionId" type="hidden" id="permissionId"/>
                <input name="versionNumber" type="hidden" id="versionNumber"/>
                <div class="layui-form-item">
                    <label for="icon" class="layui-form-label">
                        <span class="x-red">*</span>图标
                    </label>
                    <div class="layui-input-inline">
                        <div style="margin-left: 20px;margin-top:5px">
                            <ul>
                                <li style="display: inline-block;width: 50px;" id="menu-icon">
                                    <i class="layui-icon"   style="font-size: 25px;"></i>
                                    <input name="icon" type="hidden" id="icon" lay-verify="icon"/>
                                </li>
                                <li style="display: inline-block;"><i class="layui-btn layui-btn-primary layui-btn-sm" id="select_icon">选择图标</i></li>
                            </ul>
                        </div>
                    </div>
                </div>
                <div class="layui-form-item">
                    <label for="name" class="layui-form-label">
                        <span class="x-red">*</span>权限名称
                    </label>
                    <div class="layui-input-inline">
                        <input type="text"  id="name" name="name"  lay-verify="name"
                               autocomplete="off" class="layui-input">
                    </div>
                    <div id="ms" class="layui-form-mid layui-word-aux">
                        <span class="x-red">*</span><span id="ums">必须填写</span>
                    </div>
                </div>
                <div class="layui-form-item">
                    <label for="name" class="layui-form-label">
                        附加参数
                    </label>
                    <div class="layui-input-inline">
                        <input type="text"  id="param" name="param"  placeholder="按钮:方法名"  lay-verify="param"
                               autocomplete="off" class="layui-input">
                    </div>
                </div>

                <div class="layui-form-item">
                    <label for="status" class="layui-form-label">
                        显示状态
                    </label>
                    <div class="layui-input-inline">
                        <select name="status" id="status" >
                            <option value="display">显示</option>
                            <option value="hidden">隐藏</option>
                        </select>
                    </div>
                </div>

                <div class="layui-form-item">
                    <label for="name" class="layui-form-label">
                        <span class="x-red">*</span>布局位置
                    </label>
                    <div class="layui-input-inline">
                        <select name="position" id="position" >
                            <option value="left">主页面-左侧</option>
                            <option value="top">主页面-顶部</option>
                        </select>
                    </div>
                </div>
                <div class="layui-form-item" id="pDiv">
                    <label for="parentId" class="layui-form-label">
                       <span class="x-red">*</span>父级权限
                    </label>
                    <div class="layui-input-inline">
                        <select id="parentId" name="parentId" lay-verify="parentId" lay-search>
                            <option value="-1">根节菜单</option>
                         <%--   <option value="010">layer</option>
                            <option value="021">form</option>
                            <option value="0571" selected>layim</option>--%>
                        </select>
                    </div>
                    <div id="treeNode"  style="display:none; position: absolute;z-index:1000;background-color: white;">
                        <div  id="tree"></div>
                    </div>
                </div>


                <div class="layui-form-item">
                    <label for="type" class="layui-form-label">
                        <span class="x-red">*</span>权限类型
                    </label>
                    <div class="layui-input-inline" style="width:190px;">
                        <select name="type" id="type" >
                            <option value="1">菜单</option>
                            <option value="2">按钮</option>
                            <option value="0">菜单集合</option>
                            <option value="3">其它</option>
                        </select>
                    </div>
                </div>


                <div class="layui-form-item">
                    <label for="url" class="layui-form-label">
                            <span class="x-red">*</span>url
                    </label>
                    <div class="layui-input-inline">
                        <input type="text" id="url" name="url" value="/common/makeFuture.page" lay-verify="url"  autocomplete="off" class="layui-input">
                    </div>
                </div>


                <div class="layui-form-item">
                    <label for="orderNumber" class="layui-form-label">
                        <span class="x-red">*</span>排序
                    </label>
                    <div class="layui-input-inline">
                        <input type="orderNumber" id="orderNumber" name="orderNumber" value="9" placeholder="升序排列"  lay-verify="orderNumber"
                               autocomplete="off" class="layui-input">
                    </div>
                </div>

                <div class="layui-form-item layui-form-text">
                    <label for="description" >
                        <span class="x-red">*</span>简单描述：
                    </label>
                    <div class="layui-input-block textarea" >
                        <textarea id="description" name="description" placeholder="请输入内容" type="text/plain" style="width:60%;height:150px"></textarea>
                    </div>
                </div>


                <div style="height: 60px"></div>
            </div>
        </div>
        <div style="width: 100%;height: 55px;background-color: white;border-top:1px solid #e6e6e6;
  position: fixed;bottom: 1px;margin-left:-20px;">
            <div class="layui-form-item" style=" float: right;margin-right: 30px;margin-top: 8px">
                <button  class="layui-btn layui-btn-normal" lay-filter="save" lay-submit="">
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

<script type="text/javascript" src="/resources/base/authz/js/permissionForm.js"></script>

<script type="text/javascript">

</script>
</body>
</html>
