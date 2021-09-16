package com.superhao.base.cache.util;

import com.superhao.base.cache.CacheUtil;
import com.superhao.base.common.constant.SysConstantSet;
import com.superhao.base.common.constant.SysServiceConfigSet;
import com.superhao.base.entity.token.SysUserToken;
import com.superhao.base.util.SpringContexUtil;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 *  操作系统用户权限的类
 * @Auther: zehao
 * @Date: 2019/4/26 13:47
 * @email: 928649522@qq.com
 */
public class SysAuthzUtil {
   // private static Map<String,SysUserToken>  SYS_AUTHZ_CACHE;


/*    public static SysUserToken currentSysUser(HttpServletRequest request){

        String tokenCode = (String) request.getSession().getAttribute(SysConstantSet.SYSTEM_AUTHZ_TOKEN_KEY);
        if(StringUtils.isEmpty(tokenCode)){
            return null;
        }
        return CacheUtil.get(tokenCode);
        //return new SysUserToken();
    }
    public static SysUserToken currentSysUser(){

        String tokenCode = (String) SpringContexUtil.getSession().getAttribute(SysConstantSet.SYSTEM_AUTHZ_TOKEN_KEY);
        if(StringUtils.isEmpty(tokenCode)){
            return null;
        }
        return CacheUtil.get(tokenCode);
       // return new SysUserToken();
    }

    public static void addSysUserToken(SysUserToken token){
         CacheUtil.put(token.getTokenCode(),token);
    }

    private static Map<String,SysUserToken> CacheUtil{
        return (Map<String,SysUserToken>) CacheUtil.get(CacheUtil.SYS_AUTHZ_CACHE_KEY);
    }

   *//*
   public static void main(String[] args) {
          Map<String,SysUserToken>  SYS_AUTHZ_CACHE =new HashMap<>();;
        CacheUtil.put("SYS_AUTHZ_CACHE",SYS_AUTHZ_CACHE);
        SYS_AUTHZ_CACHE.put("1",new SysUserToken());
        SYS_AUTHZ_CACHE.put("2",new SysUserToken());
        Map<String,SysUserToken> m = (Map<String,SysUserToken>) CacheUtil.get("SYS_AUTHZ_CACHE");
        System.out.println(m.size());
    }
    *//*

    public static void logout() {
        SysAuthzUtil.removeSysUserToken(SysAuthzUtil.currentSysUser());
    }
    public static void removeSysUserToken(SysUserToken token){
        if(token!=null){
            CacheUtil.remove(token.getTokenCode());
        }
    }

    public static SysUserToken getByUserId(Long userId) {
        for(Map.Entry<String,SysUserToken> item:CacheUtil.entrySet()){
            if(item.getValue().getUserId().equals(userId)){
                return item.getValue();
            }
        }
        return null;
    }*/
public  final static String KEY_PREFIX = "SAU_";
public static SysServiceConfigSet configSet = SpringContexUtil.getBean(SysServiceConfigSet.class);

    public static SysUserToken currentSysUser(HttpServletRequest request){

        String tokenCode = request.getHeader("Authorization");
        if(StringUtils.isEmpty(tokenCode)){
            tokenCode = request.getParameter("Authorization");
        }
        //String tokenCode = (String) request.getSession().getAttribute(SysConstantSet.SYSTEM_AUTHZ_TOKEN_KEY);
        if(StringUtils.isEmpty(tokenCode)||!SysAuthzUtil.exist(tokenCode)){
            return null;
        }
        return  SysAuthzUtil.get(tokenCode);
    }

    private static boolean exist(String tokenCode) {
        return CacheUtil.existsKey(KEY_PREFIX+tokenCode);
    }

    public static SysUserToken currentSysUser(){
       // String tokenCode = (String) SpringContexUtil.getSession().getAttribute(SysConstantSet.SYSTEM_AUTHZ_TOKEN_KEY);
        HttpServletRequest request  =  SpringContexUtil.getServletRequest();
        String tokenCode = request.getHeader("Authorization");
        if(StringUtils.isEmpty(tokenCode)){
            tokenCode = request.getParameter("Authorization");
        }
        if(StringUtils.isEmpty(tokenCode)||!SysAuthzUtil.exist(tokenCode)){
            return null;
        }
        return  SysAuthzUtil.get(tokenCode);

    }

    public static void addSysUserToken(SysUserToken token){
        CacheUtil.pushExpire(KEY_PREFIX+token.getTokenCode(),token,new Integer(configSet.getSysSessionTimeout()+2).intValue(), TimeUnit.SECONDS);
    }


    

    public static void logout() {
        SysAuthzUtil.removeSysUserToken(SysAuthzUtil.currentSysUser());
    }
    public static void removeSysUserToken(SysUserToken token){
        if(token!=null){
            CacheUtil.remove(KEY_PREFIX+token.getTokenCode());
        }
    }

    public static SysUserToken get(String tokenCode) {

        return  CacheUtil.get(KEY_PREFIX+tokenCode,SysUserToken.class);
    }

    public static SysUserToken getByUserId(String id) {
      Set<SysUserToken> set =  CacheUtil.getSetByPrefix(KEY_PREFIX+id,SysUserToken.class);
      if(set==null){
          return null;
      }
      for(SysUserToken item:set){
          if(id.equals(item.getUserId().toString())){
              return item;
          }
      }
        return null;
    }
}
