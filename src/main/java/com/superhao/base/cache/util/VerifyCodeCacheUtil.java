package com.superhao.base.cache.util;

import com.superhao.base.cache.CacheUtil;
import com.superhao.base.entity.PictureVerifyCode;

import java.io.Serializable;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class VerifyCodeCacheUtil  {


/*    public static PictureVerifyCode get(String key){
        return CacheUtil.get(key);
    }
    public static boolean hasObj(String key){

        PictureVerifyCode target = CacheUtil.get(key);
        return  target!= null && !target.isTimeout();
    }
    public static void remove(String key){
        CacheUtil.remove(key);
    }
    public static void push(PictureVerifyCode target){
        CacheUtil.put(target.getUuid(),target);
    }




    private static Map<String,PictureVerifyCode> CacheUtil{
        return (Map<String,PictureVerifyCode>) CacheUtil.get(CacheUtil.SYS_VERIFY_CODE_CACHE_KEY);
    }*/
   public  final static String KEY_PREFIX = "VCC_";

    public static PictureVerifyCode get(String key){
            return  CacheUtil.get(KEY_PREFIX+key,PictureVerifyCode.class);
    }
    public static boolean hasObj(String key){

        return  CacheUtil.existsKey(KEY_PREFIX+key);
    }
    public static void remove(String key){
        CacheUtil.remove(KEY_PREFIX+key);
    }
    public static void push(PictureVerifyCode target){
        CacheUtil.pushExpire(KEY_PREFIX+target.getUuid(),target,12, TimeUnit.MINUTES);
    }

    public static void pushExpire(PictureVerifyCode target,int minute){
        CacheUtil.pushExpire(KEY_PREFIX+target.getUuid(),target,minute+1, TimeUnit.MINUTES);
    }




}
