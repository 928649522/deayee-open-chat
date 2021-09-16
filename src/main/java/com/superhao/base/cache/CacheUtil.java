package com.superhao.base.cache;

import com.superhao.app.entity.ChatRoomCacheModel;
import com.superhao.app.entity.token.AppUserToken;
import com.superhao.base.cache.core.GuavaCacheUtil;
import com.superhao.base.cache.impl.GuavaCacheServiceImpl;
import com.superhao.base.cache.impl.RedisCacheServiceImpl;
import com.superhao.base.entity.PictureVerifyCode;
import com.superhao.base.entity.token.SysUserToken;
import com.superhao.base.log.util.SysLogRecordUtil;
import com.superhao.base.util.SpringContexUtil;
import lombok.extern.log4j.Log4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

/**
 *
 * 缓存工具，可自由切换redis GuavaCache
 * @Auther: zehao
 * @Date: 2019/4/22 22:00
 * @email:
 * @Description:
 */
@Log4j
public class CacheUtil {
/*    *//**
     * 创建一个聊天室缓存集合
     *//*
    public static final String SYS_APP_ROOM_CHAT_CACHE_KEY= "SYS_APP_ROOM_CHAT_CACHE_KEY";
    public static final String SYS_APP_USER_CHAT_CACHE_KEY= "SYS_APP_USER_CHAT_CACHE_KEY";
    *//**
     * 创建一个权限缓存集合
     *//*
    public static final String SYS_AUTHZ_CACHE_KEY= "SYS_AUTHZ_CACHE";
    *//**
     * 创建一个h5app token缓存集合
     *//*
    public static final String SYS_APPUSER_TOKEN_CACHE_KEY= "SYS_APPUSER_TOKEN_CACHE_KEY";
    *//**
     * 创建一个验证码缓存集合
     *//*
    public static final String SYS_VERIFY_CODE_CACHE_KEY= "SYS_VERIFY_CODE_CACHE_KEY";

    private static CacheService cacheService;

   static {
       CacheUtil.cacheService = SpringContexUtil.getBean(RedisCacheServiceImpl.class);
       loadNativeCach();
     //  new Thread(new LoadDefaultCache()).start();
   }

    public static Object get(String key) {
        return cacheService.get(key);
    }

    public static void put(String key, Object val) {
        cacheService.put(key,val);
    }

   static void  loadNativeCach(){
        *//**
         * 创建一个权限缓存集合
         *//*
        GuavaCacheUtil.put(SYS_AUTHZ_CACHE_KEY,new HashMap<String,SysUserToken>());

        *//**
         * 创建一个h5app token缓存集合
         *//*
       GuavaCacheUtil.put(SYS_APPUSER_TOKEN_CACHE_KEY,new ConcurrentHashMap<String,AppUserToken>());

        *//**
         * 创建一个验证码缓存集合
         *//*
       GuavaCacheUtil.put(SYS_VERIFY_CODE_CACHE_KEY,new ConcurrentHashMap<String,PictureVerifyCode>());

       *//**
        * 创建两个聊天室缓存集合 一个群组，一个一对一
        *
        *//*
       GuavaCacheUtil.put(SYS_APP_ROOM_CHAT_CACHE_KEY,new ConcurrentHashMap<String,ChatRoomCacheModel>());
       GuavaCacheUtil.put(SYS_APP_USER_CHAT_CACHE_KEY,new ConcurrentHashMap<String,AppUserToken>());

   }

    public static Object getDatabase(String sysAppuserTokenCacheKey) {
      //  cacheService.getDatabase
        return  null;
    }

    public static void remove(String key) {
    }*/
    /**
     * 创建一个聊天室缓存集合
     */
    public static final String SYS_APP_ROOM_CHAT_CACHE_KEY= "SYS_APP_ROOM_CHAT_CACHE_KEY";
    public static final String SYS_APP_USER_CHAT_CACHE_KEY= "SYS_APP_USER_CHAT_CACHE_KEY";
    /**
     * 创建一个权限缓存集合
     */
    public static final String SYS_AUTHZ_CACHE_KEY= "SYS_AUTHZ_CACHE";
    /**
     * 创建一个h5app token缓存集合
     */
    public static final String SYS_APPUSER_TOKEN_CACHE_KEY= "SYS_APPUSER_TOKEN_CACHE_KEY";
    /**
     * 创建一个验证码缓存集合
     */
    public static final String SYS_VERIFY_CODE_CACHE_KEY= "SYS_VERIFY_CODE_CACHE_KEY";

    private static CacheService cacheService;

    static {
        CacheUtil.cacheService = SpringContexUtil.getBean(RedisCacheServiceImpl.class);
        //  new Thread(new LoadDefaultCache()).start();
    }

    public static  <T> T  get(String key,Class<T> elementType) {
        return cacheService.get(key,elementType);
    }

    public static void put(String key, Object val) {
        cacheService.put(key,val);
    }



    public static void remove(String key) {
        cacheService.remove(key);
    }


    public static <T>  Set<T> getSetByPrefix(String key,Class<T> elementType) {
        return   cacheService.getSetByPrefix(key,elementType);
    }

    public static boolean existsKey(String key) {
        return   cacheService.existsKey(key);
    }

    public static void pushExpire(String s, Object target, int i, TimeUnit ut) {
           cacheService.pushExpire(s,target,i,ut);
    }
}
