package com.superhao.base.config.sms;

import com.alibaba.fastjson.JSONObject;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

/**
 * @Auther: zehao
 * @Date: 2019/5/19 14:32
 * @email: 928649522@qq.com
 */
@Component
@Slf4j
public class SmsService extends SmsJavaConfig implements ISmsService {
    private final static int phoneNumberLength = 11;
    /**
     * 处理发送后回执过来的参数信息流
     */
    public void disposeSendDetails(String phoneNumbers) {
        super.querySendDetailsInit(phoneNumbers);
        super.beginQuerySendDetails();
    }


    @Override
    public boolean sendSms(Object templateParam, String phoneNumbers) {
        String templateParamJson = JSONObject.toJSONString(templateParam);
        log.debug(templateParamJson.toString());
        //发送配置初始化
        super.sendInit(templateParamJson.toString(), phoneNumbers);
        //开始发送信息
        SendSmsResponse sendSmsResponse = super.beginSend();
        if (sendSmsResponse.getCode() != null && sendSmsResponse.getCode().equals("OK")) {
            this.disposeSendDetails(phoneNumbers);
            return true;
        }
        return  false;
    }

    @Override
    public void sendSmsBySmsTemplate(Object templateParamContent, String phoneNumbers, String smsTemplateName) {
        if (phoneNumbers == null || phoneNumbers.length() != phoneNumberLength) {
            System.err.println("发送失败 phoneNumbers有误");
            return;
        }
        String templateParamJson = JSONObject.toJSONString(templateParamContent);
        //发送配置初始化
        super.sendInit(templateParamJson.toString(), phoneNumbers);
        super.setSmsTemplate(smsTemplateName);
        //开始发送信息
        SendSmsResponse sendSmsResponse = super.beginSend();
        if (sendSmsResponse.getCode() != null && sendSmsResponse.getCode().equals("OK")) {
            this.disposeSendDetails(phoneNumbers);
        }
    }

    @Override
    public void sendSmsListBySmsTemplate(com.alibaba.fastjson.JSONObject obj, String phone) {
        if (StringUtils.isEmpty(phone) || phone.length() != phoneNumberLength) {
            this.setSendFlag("电话格式有错误！");
            return;//过滤格式有误电话
        }
        
        log.debug(obj.toString());
        super.sendInit(obj.toString(), phone);
        SendSmsResponse sendSmsResponse = super.beginSend();//开始发送信息
        if (sendSmsResponse.getCode() != null && sendSmsResponse.getCode().equals("OK")) {
            this.disposeSendDetails(phone);
        }
    }
}
