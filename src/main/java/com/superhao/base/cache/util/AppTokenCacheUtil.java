package com.superhao.base.cache.util;

import com.superhao.app.constant.AppChatConstant;
import com.superhao.app.entity.AppUser;
import com.superhao.app.entity.token.AppUserToken;
import com.superhao.base.cache.CacheUtil;
import com.superhao.base.util.SpringContexUtil;
import com.superhao.base.util.UUIDUtils;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

public class AppTokenCacheUtil {



/*    public static AppUserToken get(String key){
        if(StringUtils.isEmpty(key)){
            return null;
        }
        return CacheUtil.get(key);
    }
    public static boolean hasObj(String key){
        return CacheUtil.get(key) != null;
    }

    public static void remove(String key){
        CacheUtil.remove(key);
    }
    public static void push(AppUserToken target){
        CacheUtil.put(target.getUserId().toString(),target);
    }


    private static Map<String,AppUserToken> CacheUtil{
        return (Map<String,AppUserToken>) CacheUtil.getDatabase(CacheUtil.SYS_APPUSER_TOKEN_CACHE_KEY);
    }

    public static boolean auth(String tokenKey,String tokenCode){
        if(StringUtils.isEmpty(tokenKey)||StringUtils.isEmpty(tokenCode)){
            //验证失败不存在
            return false;
        }
        AppUserToken token = CacheUtil.get(tokenKey);
        if(token == null ||!tokenCode.equals(tokenCode)){
            //验证失败不存在
            return false;
        }
        return true;

    }
    public static AppUserToken currentUser(){
        HttpServletRequest request = SpringContexUtil.getServletRequest();
        String tokenKey = request.getParameter("tokenKey");
        String tokenCode = request.getParameter("tokenCode");
        if(StringUtils.isEmpty(tokenKey)||StringUtils.isEmpty(tokenCode)){
            //验证失败不存在
            return null;
        }
        return CacheUtil.get(tokenKey);
    }*/
public  final static String KEY_PREFIX = "ATC_";
    public static AppUserToken get(String key){
        if(StringUtils.isEmpty(key)){
            return null;
        }
            return CacheUtil.get(KEY_PREFIX+key,AppUserToken.class);
    }
    public static boolean hasObj(String key){
        return get(key) != null;
    }

    public static void remove(String key){
        CacheUtil.remove(KEY_PREFIX+key);
    }
    public static void pushExpire(AppUserToken target){
        if(AppChatConstant.USER_TYPE_0.equals(target.getUserType())){
            CacheUtil.pushExpire(KEY_PREFIX+target.getUserId().toString(),target,7, TimeUnit.DAYS);
        }else{
            CacheUtil.pushExpire(KEY_PREFIX+target.getUserId().toString(),target,15, TimeUnit.DAYS);
        }
    }



    public static boolean auth(String tokenKey,String tokenCode){
        if(StringUtils.isEmpty(tokenKey)||StringUtils.isEmpty(tokenCode)){
            //验证失败不存在
            return false;
        }
        AppUserToken token = AppTokenCacheUtil.get(tokenKey);
        if(token == null ||!tokenCode.equals(tokenCode)){
            //验证失败不存在
            return false;
        }
        return true;

    }
    public static AppUserToken currentUser(){
        HttpServletRequest request = SpringContexUtil.getServletRequest();
        String tokenKey = request.getParameter("tokenKey");
        String tokenCode = request.getParameter("tokenCode");

        if(StringUtils.isEmpty(tokenKey)||StringUtils.isEmpty(tokenCode)){
            tokenKey = request.getHeader("tokenKey");
            tokenCode = request.getHeader("tokenCode");
            if(StringUtils.isEmpty(tokenKey)||StringUtils.isEmpty(tokenCode)){
                //验证失败不存在
                return null;
            }
        }
        AppUserToken current = get(tokenKey);
        if(current!=null&&current.getTokenCode().equals(tokenCode)){
            return current;
        }
        return null;
    }


    public static void pushRobot(AppUserToken target) {
        CacheUtil.put(KEY_PREFIX+target.getUserId().toString(),target);
    }
}
