package com.superhao.base.config.email;

import com.superhao.base.common.constant.SysServiceConfigSet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import java.util.Properties;

/**
 * @Auther: zehao
 * @Date: 2019/5/19 12:39
 * @email: 928649522@qq.com
 */

@Configuration
public class EmailJavaConfig {

   @Autowired
   private SysServiceConfigSet configSet;

    @Bean
    public JavaMailSender javaMailSender(){
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        mailSender.setHost(configSet.getEmailHost());
        mailSender.setPort(Integer.parseInt(configSet.getEmailPort()));
        mailSender.setUsername(configSet.getEmailUsername());//用户名
        mailSender.setPassword(configSet.getEmailPassword());//密码
        mailSender.setDefaultEncoding("UTF-8");
        Properties pts =new Properties();
        pts.put("mail.smtp.auth", true);
        pts.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        pts.put("mail.smtp.socketFactory.port", "465");
        pts.put("mail.smtp.port", "465");
        mailSender.setJavaMailProperties(pts);
        return mailSender;
    }



}
