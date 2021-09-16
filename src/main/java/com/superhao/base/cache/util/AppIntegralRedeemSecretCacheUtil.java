package com.superhao.base.cache.util;

import com.superhao.base.cache.CacheUtil;
import com.superhao.base.cache.core.RedisLock;
import com.superhao.base.util.SpringContexUtil;

import java.util.concurrent.TimeUnit;

/**
 * @Auther: super
 * @Date: 2019/11/24 14:03
 * @email:
 */
public class AppIntegralRedeemSecretCacheUtil {
    private static RedisLock redisLock = SpringContexUtil.getBean(RedisLock.class);

    public  final static String REDEEM_SECRET_KEY_PREFIX = "REDEEMSECRET_";

    public static boolean existRedeemSecret(String key) {
        return CacheUtil.existsKey(REDEEM_SECRET_KEY_PREFIX+key);
    }
    public static String getRedeemSecret(String key) {
        return  CacheUtil.get(REDEEM_SECRET_KEY_PREFIX+key,String.class);
    }
    public static boolean  opsRedeemSecretLock(String key) {
        return  redisLock.lock(REDEEM_SECRET_KEY_PREFIX+key);
    }
    public static void  opsRedeemSecretUnLock(String key) {
        redisLock.delete(REDEEM_SECRET_KEY_PREFIX+key);
    }

    public static void removeRedeemSecret(String key) {
        if(redisLock.lock(REDEEM_SECRET_KEY_PREFIX+key)){
            CacheUtil.remove(REDEEM_SECRET_KEY_PREFIX+key);
            redisLock.delete(REDEEM_SECRET_KEY_PREFIX+key);
        }
    }


    public static void pushRedeemSecret(String key, String val) {
        CacheUtil.pushExpire(REDEEM_SECRET_KEY_PREFIX+key,val,24, TimeUnit.SECONDS);
    }

    public static boolean opsRedeemSecretLock(String key,int time) {
        return  redisLock.setExprireLock(REDEEM_SECRET_KEY_PREFIX+key,time);
    }
    
}
