package com.superhao.base.util;



import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

/**
 * @Auther: zehao
 * @Date: 2019/10/2 17:01
 * @email: 928649522@qq.com
 */
public class Base64Utils {
    public static String decodeBase64(String target){
        return  new String(Base64.getDecoder().decode(target));
    }
    public static String encodeBase64(String target){
        return  new String(Base64.getEncoder().encode(target.getBytes()));
    }


}
