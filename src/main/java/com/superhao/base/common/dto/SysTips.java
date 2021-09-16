package com.superhao.base.common.dto;

/**
 * @Auther: zehao
 * @Date: 2019/4/23 12:25
 * @email: 928649522@qq.com
 * @Description:
 */
public enum SysTips {
    REQUEST_SUCCESS(200,"请求成功"),
    REQUEST_FAIL(500,"请求失败"),
    LOGIN_ERROR(403,"账号或密码有误"),
    PARAM_ERROR(400,"参数错误"),

    SYS_SERVER_BUSY(501,"服务正忙，稍后再试"),
    SYS_UNKNOWN_ERROR(500,"拒绝请求"),

    REQUEST_TIMEOUT(503,"请求超时"),

    APP_ROOM_DISABLE_PEOPLE(443,"您已被踢除该群组"),
    APP_ROOM_BANNED_CHAT_ALL(444,"全体禁言"),
    APP_ROOM_NOT_JOIN_ANONYMOUS(445,"该房间禁止匿名登录"),
    APP_ROOM_SEND_BUSY(446,"您发言过快，取消"),
    APP_USERNAME_REPEAT(447,"用户名已存在，请换一个"),
    APP_ROOM_BANNED_CHAT(448,"您已被禁言"),


    APP_ROOM_NOT_AUTH(449,"您不是管理，无权限"),
    APP_ROOM_NOT_EXIST(450,"房间不存在"),

    APP_ROOM_SAY_NUMBER_MAX(451,"您的发言次数已用尽，请联系管理员"),
    APP_ROOM_SAY_TIME_MAX(451,"您的发言时间已到期，请联系管理员"),
    APP_TEXT_SIZE_OUT_MAX(452,"超过最大文本长度"),
    APP_UPLOAD_FILE_SIZE_MAX(452,"上传的文件过大"),

    APP_RED_POINT_EXPIRED(454,"红包已过期"),
    APP_RED_COUNT_NOT(455,"红包已抢完"),
    APP_RED_REPEAT(456,"红包已经领取过"),
    APP_INTEGRAL_INSUFFICIENT(457,"积分不足"),


    APP_CALL_LIST_REPEATE(453,"对方已是你的联系人");

    private String text;
    private int code;

    SysTips( int code,String text) {
        this.text = text;
        this.code = code;
    }

    public String getText() {
        return text;
    }



    public int getCode() {
        return code;
    }


}
