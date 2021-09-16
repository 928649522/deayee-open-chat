package com.superhao.app.entity.seri;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.google.common.collect.Lists;
import lombok.Data;

import java.lang.reflect.Field;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * @Auther: super
 * @Date: 2019/11/6 23:18
 * @email:
 */
@Data
public abstract class Codecable {

    public static List<FieldWrapper> resolveFileldWrapperList(Class clazz){
        Field[] fields = clazz.getDeclaredFields();
        List<FieldWrapper> fieldWrapperList = Lists.newArrayList();
        for (Field field : fields) {
            CodecProprety codecProprety = field.getAnnotation(CodecProprety.class);
            if (codecProprety == null) {
                continue;
            }
            FieldWrapper fw = new FieldWrapper(field, codecProprety);
            fieldWrapperList.add(fw);
        }

        Collections.sort(fieldWrapperList, new Comparator<FieldWrapper>() {
            @Override
            public int compare(FieldWrapper o1, FieldWrapper o2) {
                return o1.getCodecProprety().order() - o2.getCodecProprety().order();
            }
        });

        return fieldWrapperList;
    }

    @JsonIgnore
    public abstract List<FieldWrapper> getFieldWrapperList();
}
