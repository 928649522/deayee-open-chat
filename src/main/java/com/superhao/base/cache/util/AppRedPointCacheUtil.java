package com.superhao.base.cache.util;

import com.superhao.app.entity.AppRedPoint;
import com.superhao.app.entity.dto.ChatData;
import com.superhao.base.cache.CacheUtil;
import com.superhao.base.cache.core.RedisLock;
import com.superhao.base.util.SpringContexUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * 红包
 * @Auther: super
 * @Date: 2019/11/12 22:28
 * @email:
 */
public class AppRedPointCacheUtil {
    private static RedisLock redisLock = SpringContexUtil.getBean(RedisLock.class);

    public  final static String RED_POINT_KEY_PREFIX = "REDPOINT_";

    public static boolean existRedPoint(String key) {

        return CacheUtil.existsKey(RED_POINT_KEY_PREFIX+key);
    }
    public static AppRedPoint  getRedPoint(String key) {
        return  CacheUtil.get(RED_POINT_KEY_PREFIX+key,AppRedPoint.class);
    }
    public static boolean  opsRedPointLock(String key) {
        return  redisLock.lock(RED_POINT_KEY_PREFIX+key);
    }
    public static void  opsRedPointUnLock(String key) {
        redisLock.delete(RED_POINT_KEY_PREFIX+key);
    }







    public static void removeRedPoint(String key) {
        if(redisLock.lock(RED_POINT_KEY_PREFIX+key)){
            CacheUtil.remove(RED_POINT_KEY_PREFIX+key);
            redisLock.delete(RED_POINT_KEY_PREFIX+key);
        }
    }


    public static void pushRedPoint(String key, AppRedPoint appRedPoint) {
        CacheUtil.pushExpire(RED_POINT_KEY_PREFIX+key,appRedPoint,24, TimeUnit.HOURS);
    }

    public static boolean opsRedPointLock(String key,int time) {
        return  redisLock.setExprireLock(RED_POINT_KEY_PREFIX+key,time);
    }
}
