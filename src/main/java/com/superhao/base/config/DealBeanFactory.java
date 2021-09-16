package com.superhao.base.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ComponentScans;
import org.springframework.context.annotation.Configuration;

/**
 * @Auther: zehao
 * @Date: 2019/4/20 16:30
 * @email: 928649522@qq.com
 * @Description: spring application 加载Bean时做前后处理
 */
@Configuration
@Slf4j
public class DealBeanFactory implements BeanPostProcessor {

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName)
            throws BeansException {
        log.info("对象---" + beanName + "---初始化开始");
        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        log.info("对象---" + beanName + "---初始化成功");
        return bean;
    }
}