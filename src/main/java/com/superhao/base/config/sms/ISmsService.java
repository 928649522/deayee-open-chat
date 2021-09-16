package com.superhao.base.config.sms;

import com.alibaba.fastjson.JSONObject;

/**
 * @Auther: zehao
 * @Date: 2019/5/19 14:32
 * @email: 928649522@qq.com
 */
public interface ISmsService {
    /**
     * @param templateParamContent   短信参数内容  注：跟模板对应
     * @param phoneNumber   接受短信的手机号
     *
     * 发送短信
     */
    boolean sendSms(Object templateParamContent, String phoneNumber);

    /**
     * @param templateParamContent   短信参数内容实体  注：实体属性跟模板参数key对应
     * @param phoneNumber   接受短信的手机号
     * @param smsTemplateName  短信模板名称
     * 选定模板发送短信
     */
    void sendSmsBySmsTemplate(Object templateParamContent, String phoneNumber, String smsTemplateName);

    /**
     * 根据信息模型内容
     * @param obj	发送短信内容
     * @param phone	电话号码
     */
    void sendSmsListBySmsTemplate(JSONObject obj, String phone);
}
