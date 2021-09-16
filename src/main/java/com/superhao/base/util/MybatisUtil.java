package com.superhao.base.util;


import com.baomidou.mybatisplus.mapper.EntityWrapper;

import java.util.HashMap;
import java.util.Map;

/**
 * @Auther:
 * @Date: 2019/4/22 18:58
 * @email:
 * @Description:
 */
public class MybatisUtil<T> {

    public static  <T>  EntityWrapper condition(){
        return new EntityWrapper<T>();
    }
    public static    EntityWrapper conditionT(){
        return new EntityWrapper();
    }



}
