package com.superhao.base.cache.util;

import com.superhao.app.entity.AppRedPoint;
import com.superhao.base.cache.CacheUtil;
import com.superhao.base.cache.core.RedisLock;
import com.superhao.base.util.SpringContexUtil;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @Auther: super
 * @Date: 2019/12/12 14:51
 * @email:
 * 
 * 二维码过期时间缓存
 */
public class AppGroupQRCodeCacheUtil {
    private static RedisLock redisLock = SpringContexUtil.getBean(RedisLock.class);
    public static long GROUPQRCODE_EFFICTIVE_TIME = 24*60*60*1000;//一天
    public  final static String QR_CODE_KEY_PREFIX = "GROUPQRCODER_";

    public static boolean existQRCode(String key) {

        return CacheUtil.existsKey(QR_CODE_KEY_PREFIX+key);
    }
    public static void pushQRCode(String key, Map entity) {
        CacheUtil.put(QR_CODE_KEY_PREFIX+key,entity);
    }
    public static Map getQRCode(String key) {
        return  CacheUtil.get(QR_CODE_KEY_PREFIX+key,Map.class);
    }
    public static boolean  opsQRCodeLock(String key) {
        return  redisLock.lock(QR_CODE_KEY_PREFIX+key);
    }
    public static void  opsQRCodeUnLock(String key) {
        redisLock.delete(QR_CODE_KEY_PREFIX+key);
    }

    public static Map cacheEntity(){
        return new HashMap();
    }


}
