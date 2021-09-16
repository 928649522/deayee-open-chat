package com.superhao.base.config.sms;

import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.dysmsapi.model.v20170525.QuerySendDetailsRequest;
import com.aliyuncs.dysmsapi.model.v20170525.QuerySendDetailsResponse;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsRequest;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsResponse;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.exceptions.ServerException;
import com.aliyuncs.profile.DefaultProfile;
import com.aliyuncs.profile.IClientProfile;

import java.text.SimpleDateFormat;
import java.util.Date;


/**
 * @Auther: zehao
 * @Date: 2019/5/19 14:14
 * @email: 928649522@qq.com
 */
@Slf4j
public class SmsJavaConfig{

        private static final String product = "";// 产品名称:云通信短信API产品,开发者无需替换
        private static final String domain = ""; // 产品域名,开发者无需替换

        private static final String accessKeyId = ""; // TODO 必填:授权id
        private static final String accessKeySecret = "";// TODO 必填:授权密码

        private String SignName = ""; // TODO 必填:短信签名-可在短信控制台中找到
        private String SmsTemplate = "";// TODO 必填:短信模板-可在短信控制台中找到SMS_137671147
        private String outId = ""; // 可选:outId为提供给业务方扩展字段,最终在短信回执消息中将此值带回给调用者

        private static IClientProfile profile;
        private static IAcsClient acsClient;

        private SendSmsRequest sendSmsRequest;
        private SendSmsResponse sendSmsResponse;
        // 组装请求对象
        private QuerySendDetailsRequest queryRequest;
        private QuerySendDetailsResponse queryResponse;

        private String sendFlag;
        private String sendMessage;

        /**
         * 初始化必要参数
         * */
        public SmsJavaConfig() {
            try {
                // 可自助调整超时时间
                System.setProperty("sun.net.client.defaultConnectTimeout", "10000");
                System.setProperty("sun.net.client.defaultReadTimeout", "10000");
                profile = DefaultProfile.getProfile("cn-hangzhou", accessKeyId,accessKeySecret);
                DefaultProfile.addEndpoint("cn-hangzhou", "cn-hangzhou", product,domain);
                acsClient = new DefaultAcsClient(profile);
            } catch (ClientException e) {
                e.printStackTrace();
            }
        }

        protected void sendInit(String templateParam, String phoneNumbers) {
            sendSmsRequest = new SendSmsRequest();// 创建短信发送请求
            sendSmsRequest.setSignName(SignName);
            sendSmsRequest.setTemplateCode(SmsTemplate);
            sendSmsRequest.setOutId(outId);
            sendSmsRequest.setTemplateParam(templateParam);
            sendSmsRequest.setPhoneNumbers(phoneNumbers);
        }

        /**
         * 开始发送
         * */
        public SendSmsResponse beginSend() {
            try {
                this.sendSmsResponse = acsClient.getAcsResponse(sendSmsRequest);// 获取短信请求
                if (this.sendSmsResponse != null) {
                    this.printSendSmsParamInfo();
                }
                return this.sendSmsResponse;
            } catch (ServerException e) {
                e.printStackTrace();
            } catch (ClientException e) {
                e.printStackTrace();
            }
            return null;
        }

        private void printSendSmsParamInfo() {
            log.debug("短信接口返回的数据----------------");
            log.debug("Code=" + sendSmsResponse.getCode());
            log.debug("Message=" + sendSmsResponse.getMessage());
            log.debug("RequestId=" + sendSmsResponse.getRequestId());
            log.debug("BizId=" + sendSmsResponse.getBizId());
        }

        public boolean querySendDetailsInit(String phoneNumbers) {
            queryRequest = new QuerySendDetailsRequest();
            // 必填-号码
            queryRequest.setPhoneNumber(phoneNumbers);
            // 可选-流水号
            queryRequest.setBizId(sendSmsResponse.getBizId());
            // 必填-发送日期 支持30天内记录查询，格式yyyyMMdd
            SimpleDateFormat ft = new SimpleDateFormat("yyyy-MM-dd");
            queryRequest.setSendDate(ft.format(new Date()));
            // 必填-页大小
            queryRequest.setPageSize(10L);
            // 必填-当前页码从1开始计数
            queryRequest.setCurrentPage(1L);
            return true;
        }

        public QuerySendDetailsResponse beginQuerySendDetails() {
            try {
                this.queryResponse = acsClient.getAcsResponse(queryRequest);
                if (this.queryResponse != null) {
                    this.printQueryDetailInfo(this.queryResponse);
                    return this.queryResponse;
                }
            } catch (ServerException e) {
                e.printStackTrace();
            } catch (ClientException e) {
                e.printStackTrace();
            }
            return null;
        }

        /**
         * @param querySendDetailsResponse
         *            查询response 打印短信明细
         */
        private void printQueryDetailInfo(QuerySendDetailsResponse querySendDetailsResponse) {
            this.sendFlag = querySendDetailsResponse.getCode();
            this.sendMessage = querySendDetailsResponse.getMessage();
		log.debug("短信明细查询接口返回数据----------------");
		log.debug("Code=" + querySendDetailsResponse.getCode());
		log.debug("Message=" + querySendDetailsResponse.getMessage());
		int i = 0;
		for (QuerySendDetailsResponse.SmsSendDetailDTO smsSendDetailDTO : querySendDetailsResponse .getSmsSendDetailDTOs()) {
			log.debug("SmsSendDetailDTO[" + i + "]:");
			log.debug("Content=" + smsSendDetailDTO.getContent());
			log.debug("ErrCode=" + smsSendDetailDTO.getErrCode());
			log.debug("OutId=" + smsSendDetailDTO.getOutId());
			log.debug("PhoneNum=" + smsSendDetailDTO.getPhoneNum());
			log.debug("ReceiveDate=" + smsSendDetailDTO.getReceiveDate());
			log.debug("SendDate=" + smsSendDetailDTO.getSendDate());
			log.debug("SendStatus=" + smsSendDetailDTO.getSendStatus());
			log.debug("Template=" + smsSendDetailDTO.getTemplateCode());
		}
		log.debug("TotalCount=" + querySendDetailsResponse.getTotalCount());
		log.debug("RequestId=" + querySendDetailsResponse.getRequestId());
        }

        public String getSendFlag() {
            return sendFlag;
        }

        public void setSendFlag(String sendFlag) {
            this.sendFlag = sendFlag;
        }

        public void setSignName(String signName) {
            SignName = signName;
        }

        public void setSmsTemplate(String smsTemplate) {
            if(!StringUtils.isEmpty(smsTemplate)){
                SmsTemplate = smsTemplate;
            }
        }

        public String getSendMessage() {
            return sendMessage;
        }
}
