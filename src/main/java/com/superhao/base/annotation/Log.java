package com.superhao.base.annotation;

import java.lang.annotation.*;

/**
 * @Auther: zehao
 * @Date: 2019/4/20 16:44
 * @email: 928649522@qq.com
 * @Description: 日志记录
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
@Documented
@Inherited
public @interface Log {
     enum LOG_TYPE{ADD,UPDATE,DELETE,SELECT,OTHER};

    /**
     * 操作类型
     */
    enum  AFTER_OPERATE{
        DELETE_BACKUP,
        NORMAL};
    /**
     * 详情描述
     */
    String text();
    AFTER_OPERATE after() default AFTER_OPERATE.NORMAL;

    /**
     * 类型 curd 其他
     */
    LOG_TYPE type() default LOG_TYPE.OTHER;


}
