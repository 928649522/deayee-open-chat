package com.superhao.base.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

/**
 * spring mvc 额外的java配置
 * @Auther: zehao
 * @Date: 2019/5/16 11:32
 * @email: 928649522@qq.com
 */
public class WebExtraConfig extends WebMvcConfigurationSupport {

/*    @Bean
    public SwaggerInterceptor swaggerInterceptor(){
        return new SwaggerInterceptor();
    }
    *//**
     * 拦截器注册
     * @param registry
     *//*
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(swaggerInterceptor())
                //.addPathPatterns("/**");
                .addPathPatterns("/v2/api-docs", "/configuration/ui", "/swagger-resources", "/configuration/security", "/swagger-ui.html", "/webjars/**","/swagger-resources/configuration/ui","/swagge‌​r-ui.html");
    }*/


}
