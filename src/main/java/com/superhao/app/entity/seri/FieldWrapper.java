package com.superhao.app.entity.seri;

/**
 * @Auther: super
 * @Date: 2019/11/6 23:17
 * @email:
 */
import lombok.AllArgsConstructor;
import lombok.Data;

import java.lang.reflect.Field;
@Data
@AllArgsConstructor
public class FieldWrapper {
    /**
     * 上下行数据属性
     */
    private Field field;
    /**
     * 上下行数据属性上的注解
     */
    private CodecProprety codecProprety;
}