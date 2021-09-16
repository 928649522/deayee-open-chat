package com.superhao.base.cache.util;

import com.superhao.app.handle.RoomChatHandle;
import com.superhao.base.cache.CacheUtil;
import com.superhao.base.cache.core.RedisLock;
import com.superhao.base.util.Md5Util;
import com.superhao.base.util.SpringContexUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * @Auther: super
 * @Date: 2019/11/26 23:43
 * @email:
 */
public class AppRobotChatRoomCacheUtils {
    private static RedisLock redisLock = SpringContexUtil.getBean(RedisLock.class);

    public  final static String Robot_KEY = "ROBOT_";



    public static boolean exist(String key) {
        return CacheUtil.existsKey(Robot_KEY+key);
    }
    public static List<String> getRobot(String key) {
        return  CacheUtil.get(Robot_KEY+key, ArrayList.class);
    }


    
    
    public static boolean  opsRobotLock(String key) {
        return  redisLock.lock(Robot_KEY+key);
    }
    public static void  opsRobotUnLock(String key) {
        redisLock.delete(Robot_KEY+key);
    }



    public static void syncPush(RoomChatHandle handle, String cd) {
        String onlyKey = generateRobotKey(handle);
        opsRobotLock(Robot_KEY+onlyKey);
        if(!exist(onlyKey)){
            CacheUtil.put(Robot_KEY+onlyKey,new ArrayList<String>());
        }else{
            List<String> list = CacheUtil.get(Robot_KEY+onlyKey,ArrayList.class);
            list.add(cd);
            CacheUtil.put(Robot_KEY+onlyKey,list);
        }
        opsRobotUnLock(Robot_KEY+onlyKey);
    }



    public static String generateRobotKey(RoomChatHandle handle) {
        Object sourceId = handle.getAttribute().getRoomId();
        if(sourceId==null){
            sourceId = handle.getAttribute().getRoomCode();
        }
        String onlyKey = handle.getUserToken().getUserId().toString();
        return Md5Util.getMD5(onlyKey+sourceId.toString());
    }
    
}
