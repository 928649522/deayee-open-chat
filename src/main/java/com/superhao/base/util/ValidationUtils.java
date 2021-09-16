package com.superhao.base.util;

import com.baomidou.mybatisplus.toolkit.CollectionUtils;
import com.superhao.base.authz.entity.SysPermission;
import com.superhao.base.common.dto.BaseJsonResult;
import com.superhao.base.common.dto.SysTips;
import com.superhao.base.exception.HttpParamException;
import org.hibernate.validator.HibernateValidator;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * @Auther: zehao
 * @Date: 2019/4/25 19:40
 * @email: 928649522@qq.com
 * @Description:
 */
public class ValidationUtils {
    /**
     * 使用hibernate的注解来进行验证
     */
/*
    private static Validator validator = Validation
            .byProvider(HibernateValidator.class).configure().failFast(true).buildValidatorFactory().getValidator();
*/
    private final static Object LOCK = new Object();
    private static Validator validator = null;

    private static void initValidator() {
        if (validator == null) {
            validator = SpringContexUtil.getBean("validator");
            synchronized (LOCK) {
                if (validator == null) {
                    validator = Validation
                            .byProvider(HibernateValidator.class).configure().failFast(true).buildValidatorFactory().getValidator();

                }
            }
        }
    }


    public static <T> void validate(T obj) {
        ValidationUtils.initValidator();
        Set<ConstraintViolation<T>> constraintViolations = validator.validate(obj);
        // 抛出检验异常
        if (constraintViolations.size() > 0) {
            String msg = constraintViolations.iterator().next().getMessage();
            throw new HttpParamException(new BaseJsonResult(false, msg
                    , SysTips.PARAM_ERROR));
        }
    }

    public static void main(String[] args) {
        SysPermission sysPermission = new SysPermission();
        System.out.println("11");
        ValidationUtils.validate(sysPermission);

    }


}
