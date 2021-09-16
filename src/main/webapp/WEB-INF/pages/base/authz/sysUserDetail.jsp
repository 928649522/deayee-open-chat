<%@ page language="java" import="java.util.*" pageEncoding="UTF-8" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
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
    <form class="layui-form layui-form-pane" lay-verify="sysUserForm" id="sysUserForm" onsubmit="return false;" style="width: 650px; position: relative; left:25%;">
        <input name="userId" type="hidden" id="userId" value="" value="${user.userId}"/>
        <input name="fileId" type="hidden" id="fileId" value="${user.fileId}"/>
        <input name="filePath" type="hidden" id="filePath" value="${user.filePath}"/>
        <input  type="hidden" id="oldPwd" value="${user.password}"/>
        <input name="versionNumber" type="hidden" id="versionNumber"/>
        <div style="width:100%;">
            <div class="layui-form-item">
                <fieldset class="layui-elem-field layui-field-title" style="margin-top: 10px;">
                    <legend style="font-size:16px;">更新头像</legend>
                </fieldset>
                <div class="layui-input-inline">
                    <div class="layui-upload-drag" style="margin-left:10%;" id="uploadEle">
                        <i style="font-size:30px;" class="layui-icon"></i>
                        <p style="font-size: 10px">点击上传，或将文件拖拽到此处</p>
                    </div>
                </div>
                <div class="layui-input-inline">

                    <div  id="demo2" style="margin-top: 20px;margin-left: 50px">
                        <img src="${re.contextPath}/plugin/x-admin/images/bg.png" width="100px" height="100px" class="layui-upload-img layui-circle">
                    </div>

                </div>
            </div>
            <div class="layui-form-item">
                <fieldset class="layui-elem-field layui-field-title" style="margin-top: 10px;">
                    <legend style="font-size:16px;">个人资料</legend>
                </fieldset>
            </div>

            <div class="layui-form-item">
                <div class="layui-inline">
                    <label for="username" class="layui-form-label">
                        <span class="x-red">*</span>昵称
                    </label>
                    <div class="layui-input-inline">
                        <input type="text" id="username" value="${user.username}"  name="username" lay-verify="username"  autocomplete="off" class="layui-input">
                    </div>
                </div>
                <div class="layui-inline">
                    <label for="birthday" class="layui-form-label">
                        <span class="x-red">*</span>出生日期
                    </label>
                    <div class="layui-input-inline">
                        <input type="text" id="birthday" readonly="readonly" value="<fmt:formatDate value="${user.birthday}" pattern="yyyy-MM-dd" />"  name="birthday"  lay-verify="birthday"
                               autocomplete="off" class="layui-input">
                    </div>
                </div>
            </div>
            <div class="layui-form-item">
                <div class="layui-inline" id="passwordDiv">
                    <label for="password" class="layui-form-label">
                        <span class="x-red">*</span>密码
                    </label>
                    <div class="layui-input-inline">
                        <input type="password" id="password" name="password" value="${user.password}"   lay-verify="password"
                               autocomplete="off" class="layui-input">
                    </div>
                </div>
                <div class="layui-inline">
                    <label for="repass" class="layui-form-label">
                        <span class="x-red">*</span>确认密码
                    </label>
                    <div class="layui-input-inline">
                        <input type="password" id="repass" name="repass"  value="${user.password}" lay-verify="repass"
                               autocomplete="off" class="layui-input">
                    </div>
                </div>
            </div>

            <div class="layui-form-item">
                <div class="layui-inline">
                    <label for="email" class="layui-form-label">
                        <span class="x-red">*</span>邮箱
                    </label>
                    <div class="layui-input-inline">
                        <input type="email" id="email" name="email" value="${user.email}"  lay-verify="email"
                               autocomplete="off" class="layui-input">
                    </div>
                </div>
                <div class="layui-inline">
                    <label for="address" class="layui-form-label">
                        <span class="x-red">*</span>住址
                    </label>
                    <div class="layui-input-inline">

                        <input type="text" id="address" value="${user.address}" style="width: 93%" name="address"  lay-verify="address"
                               autocomplete="off" class="layui-input">
                    </div>
                </div>
            </div>


            <div class="layui-form-item">
                <div class="layui-inline">
                    <label for="signature" class="layui-form-label">
                        <span class="x-red">*</span>个性签名
                    </label>
                    <div class="layui-input-inline">
                        <input type="signature" id="signature" name="signature"  value="${user.signature}"lay-verify="signature"
                               autocomplete="off" class="layui-input">
                    </div>
                </div>
                <div class="layui-inline">
                    <label for="sex" class="layui-form-label">
                        <span class="x-red">*</span>性别
                    </label>
                    <div class="layui-input-inline">
                        <select name="sex" id="sex" >
                            <option value="1" <c:if test="${user.sex eq 1 }">selected</c:if>>男</option>
                            <option value="0"  <c:if test="${user.sex eq 0 }">selected</c:if>>女</option>
                        </select>
                    </div>
                </div>
            </div>





            <%--<div class="layui-form-item">
                <label class="layui-form-label">角色选择</label>
                <div class="layui-input-block">
                    <#list boxJson as json>
                        <input type="checkbox" name="role" title="${json.name}" lay-filter="check" value="${json.id}">
                    </#list>
                </div>
            </div>--%>
            <div ><br/><br/><br/><br/><br/>
                <button  class="layui-btn layui-btn-normal" style="margin-left:50px;width: 500px;" lay-filter="save" lay-submit>
                    保存
                </button>
            </div>
        </div>

    </form>
</div>

<script type="text/javascript" src="/resources/layui/treelayui/layui.js"></script>
<script type="text/javascript" src="/resources/jquery/jquery-3.2.1.min.js"></script>
<script type="text/javascript" src="/resources/base/common/js/md5.js"></script>
<script type="text/javascript" src="/resources/base/common/js/superhao.js"></script>
<script type="text/javascript" src="/resources/base/authz/js/sysUserDetail.js"></script>

</body>
</html>
