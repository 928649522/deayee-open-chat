package com.superhao.base.config.email;

import com.superhao.base.exception.SystemException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.File;
import java.util.List;

/**
 * @Auther: zehao
 * @Date: 2019/5/19 11:50
 * @email: 928649522@qq.com
 */
@Slf4j
@Component
public class MailSenderService {


    @Autowired
    private JavaMailSender mailSender;


    /**
     * 普通邮件
     */
    public void sendSimpleMail(EmailDto emailDto) {
        SimpleMailMessage mail = new SimpleMailMessage();
        mail.setTo(emailDto.getSendTo());// 收件人邮箱地址
        mail.setFrom(emailDto.getSendFrom());//
        mail.setSubject(emailDto.getSubject());// 主题
        mail.setText(emailDto.getText());// 正文
        mailSender.send(mail);
    }

    public static void main(String[] args) {
        JavaMailSender mailSender =new EmailJavaConfig().javaMailSender();
        EmailDto emailDto = new EmailDto();
        emailDto.setSendTo("641904998@qq.com");
        emailDto.setSendFrom("928649522@qq.com");
        emailDto.setText("sdds");
        emailDto.setSubject("subject");

        SimpleMailMessage mail = new SimpleMailMessage();
        mail.setTo(emailDto.getSendTo());// 收件人邮箱地址
        mail.setFrom(emailDto.getSendFrom());//
        mail.setSubject(emailDto.getSubject());// 主题
        mail.setText(emailDto.getText());// 正文
        mailSender.send(mail);
    }

    public JavaMailSender getMailSender() {
        return this.mailSender;
    }

    /**
     * 附件邮件
     */
    public void sendAttachmentMail(EmailDto emailDto) {
        try {
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);
            helper.setTo(emailDto.getSendTo());// 收件人邮箱地址
            helper.setFrom(emailDto.getSendFrom());//
            helper.setSubject(emailDto.getSubject());// 主题
            helper.setText(emailDto.getText());// 正文
            List<String> attachmentFileName = emailDto.getAttachmentFileName();
            for (String name : attachmentFileName) {
                FileSystemResource file = new FileSystemResource(new File( name));
                helper.addAttachment("附件-" + name, file);
            }
            mailSender.send(mimeMessage);
            log.debug("发送成功");
        } catch (MessagingException e) {
            e.printStackTrace();
            throw new SystemException("邮件发送失败",e);
        } finally {

        }
    }

    /**
     * 发送嵌套图片的邮件
     */
    public void sendImgMail(EmailDto emailDto) {
        String pathSuffix = "D:\\temp\\Echart\\";
        try {
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);
            helper.setTo(emailDto.getSendTo());// 收件人邮箱地址
            helper.setFrom(emailDto.getSendFrom());//
            helper.setSubject(emailDto.getSubject());// 主题
            StringBuilder text = new StringBuilder();
            text.append("<html><head><meta text/html;charset=gb2312></head><body><h1>").append("电力图" + "</h1>");
            List<String> attachmentFileName = emailDto.getAttachmentFileName();
            int i = 0;
            for (String name : attachmentFileName) {
                String str = "<img src=\"cid:" + i + "\"/><br>";
                i++;
                text.append(str);
                FileSystemResource img = new FileSystemResource(new File(pathSuffix + name));
                helper.addInline(i + "", img);
            }
            System.out.println(text.toString());
            helper.setText(text.toString(), true);

            mailSender.send(mimeMessage);
        } catch (MessagingException e) {
            e.printStackTrace();
            throw new SystemException("邮件发送失败",e);
        } finally {

        }
    }

}


