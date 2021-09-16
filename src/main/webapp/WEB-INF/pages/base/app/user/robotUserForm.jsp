<%@ page language="java" import="java.util.*" pageEncoding="UTF-8" %>
<%
    String path = request.getContextPath();
    String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
    <title>机器人</title>
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

    </style>
</head>

<body>



<%--<div id="controlBtn" class="layui-btn-group">
</div>--%>
<div class="x-body">
    <form class="layui-form layui-form-pane" lay-verify="appRobotUserForm" id="appRobotUserForm" onsubmit="return false;" style="margin-left: 20px;">
        <input name="fileId" type="hidden" id="fileId"/>
        <input name="robotId" type="hidden" id="robotId"/>
        <input name="robotRoomId" type="hidden" id="robotRoomId"/>
        <div style="width:100%;height:400px;overflow: auto;">
            <div class="layui-form-item">
                <fieldset class="layui-elem-field layui-field-title" style="margin-top: 10px;">
                    <legend style="font-size:16px;">所属群</legend>
                </fieldset>
                <label for="nickName" class="layui-form-label">
                    <span class="x-red">*</span>RoomID
                </label>
                <div class="layui-input-inline">
                    <input type="text" id="roomId" name="roomId" lay-verify="roomId"  autocomplete="off" class="layui-input">
                </div>
                <div  class="layui-form-mid layui-word-aux">
                    <span class="x-red">*</span><span>注：群的房间ID 可以在群组管理查看</span>
                </div>
            </div>
            <div class="layui-form-item">
                <fieldset class="layui-elem-field layui-field-title" style="margin-top: 10px;">
                    <legend style="font-size:16px;">基本属性</legend>
                </fieldset>
                <div class="layui-input-inline">
                    <div class="layui-upload-drag" style="margin-left:10%;" id="uploadEle">
                        <i style="font-size:30px;" class="layui-icon"></i>
                        <p style="font-size: 10px">点击替换头像</p>
                    </div>
                </div>
                <div class="layui-input-inline">
                    <div  id="demo2" style="margin-top: 20px;margin-left: 50px">
                        <img src="/resources/img/moreChat.png" height="100px" class="layui-upload-img layui-circle">
                    </div>
                </div>
            </div>
            <div class="layui-form-item">
                <div class="layui-inline">
                    <label for="nickName" class="layui-form-label">
                        <span class="x-red">*</span>昵称
                    </label>
                    <div class="layui-input-inline">
                        <input type="text" id="nickName" name="nickName" lay-verify="nickName"  autocomplete="off" class="layui-input">
                    </div>
                </div>
            </div>
            <div class="layui-form-item">
                <div class="layui-inline">
                    <label for="sex" class="layui-form-label">
                        <span class="x-red">*</span>性别
                    </label>
                    <div class="layui-input-block">
                        <input type="radio" name="sex" value="男" title="男" >
                        <input type="radio" name="sex" value="女" title="女" checked="true">
                    </div>
                </div>
            </div>
            <div class="layui-form-item">
                <div class="layui-inline">
                    <label for="signature" class="layui-form-label">
                        个人签名
                    </label>
                    <div class="layui-input-block">
                        <input type="text" id="signature" name="signature" lay-verify="signature"  autocomplete="off" class="layui-input">
                    </div>
                </div>
            </div>
            <div class="layui-form-item">
                <div class="layui-inline">
                    <label for="birthday" class="layui-form-label">生日</label>
                    <div class="layui-input-block">
                        <input type="text" name="birthday" id="birthday" value="1995-1-20" autocomplete="off" class="layui-input">
                    </div>
                </div>
            </div>
            <div class="layui-form-item">
                <fieldset class="layui-elem-field layui-field-title" style="margin-top: 10px;">
                    <legend style="font-size:16px;">参数设定</legend>
                </fieldset>
            </div>
            <div class="layui-form-item">
                <label for="sayRule" class="layui-form-label">
                    匹配规则
                </label>
                <div class="layui-input-inline">
                    <div class="layui-input-inline">
                        <select id="sayRule" name="sayRule" lay-filter="aihao">
                            <option value="0" selected="">关键字</option>
                            <option value="1" >整句</option>
                        </select>
                    </div>
                </div>
                <div  class="layui-form-mid layui-word-aux">
                    <span class="x-red">*</span><span>注：机器人回复消息的规则</span>
                </div>
            </div>
            <div class="layui-form-item">
                <label for="dictionaryType" class="layui-form-label">
                    字典类型
                </label>
                <div class="layui-input-inline">
                    <div class="layui-input-inline">
                        <select id="dictionaryType" name="dictionaryType" lay-filter="dictionaryType">
                            <option value="0" selected="">默认</option>
                            <option value="1" >自定义</option>
                        </select>
                    </div>
                </div>
                <div  class="layui-form-mid layui-word-aux">
                    <span class="x-red">*</span><span>注：消息的类型</span>
                </div>
            </div>
            <div class="layui-form-item">
                <label for="sleepTime" class="layui-form-label">
                    间歇时长
                </label>
                <div class="layui-input-inline">
                    <div class="layui-input-inline">
                        <input type="number" id="sleepTime" value="5" name="sleepTime"  lay-verify="sleepTime"  autocomplete="off" class="layui-input">
                    </div>
                </div>
                <div  class="layui-form-mid layui-word-aux">
                    <span class="x-red">*</span><span>注：间歇回复消息的最大时长 {单位：秒}</span>
                </div>
            </div>

            <div class="layui-form-item">
                <label for="sayTime" class="layui-form-label">
                    在线时间
                </label>
                <div class="layui-input-inline">
                    <input type="text" class="layui-input" id="sayTime" name="sayTime"  lay-verify="sayTime"  placeholder=" - ">
                </div>
                <div  class="layui-form-mid layui-word-aux">
                    <span class="x-red">*</span><span>注：机器人的在线发言时间</span>
                </div>
            </div>
            <div class="layui-form-item">
                <div class="layui-inline">
                    <label for="autoSay" class="layui-form-label">
                        <span class="x-red"></span>自动发言
                    </label>
                    <div class="layui-input-inline">
                        <input type="checkbox"   name="autoSay" id="autoSay" lay-filter="autoSay" value="0"  lay-skin="switch" lay-text="开启|关闭"  />
                    </div>
                    <div  class="layui-form-mid layui-word-aux">
                        <span class="x-red">*</span><span>注：机器人可以增加吵群效果</span>
                    </div>
                </div>
            </div>
            <div class="layui-form-item">
                <div class="layui-inline" >
                    <label for="remark" class="layui-form-label" >
                        <span class="x-red"></span>备注
                    </label>
                    <div class="layui-input-inline" style="margin-left: 20px">
                        <textarea rows="5"  placeholder=" 介绍此机器人的用途"   cols="55" name="remark" id="remark" maxlength="50"  lay-verify="remark"></textarea>
                    </div>
                </div>
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
        </div>
    </form>

</div>


<script type="text/html" id="toolbar">
    <a class="layui-btn layui-btn-xs" lay-event="recharge">充值积分</a>
    <%--<a class="layui-btn layui-btn-danger layui-btn-xs" lay-event="del">群主</a>--%>
</script>

<script type="text/javascript" src="/resources/layui/treelayui/layui.js"></script>
<script type="text/javascript" src="/resources/jquery/jquery-3.2.1.min.js"></script>
<script type="text/javascript" src="/resources/base/common/js/superhao.js"></script>
<script type="text/javascript" src="/resources/base/app/user/robotUserForm.js"></script>
</body>


</html>
