package com.superhao.base.config.email;

import java.util.List;

/**
 * @Auther: zehao
 * @Date: 2019/5/19 11:51
 * @email: 928649522@qq.com
 */
public class EmailDto {
    private String sendTo;
    private String sendFrom;
    private String subject;
    private String text;
    private List<String> attachmentFileName;

    public String getSendTo() {
        return sendTo;
    }

    public void setSendTo(String sendTo) {
        this.sendTo = sendTo;
    }

    public String getSendFrom() {
        return sendFrom;
    }

    public void setSendFrom(String sendFrom) {
        this.sendFrom = sendFrom;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public List<String> getAttachmentFileName() {
        return attachmentFileName;
    }

    public void setAttachmentFileName(List<String> attachmentFileName) {
        this.attachmentFileName = attachmentFileName;
    }
}
