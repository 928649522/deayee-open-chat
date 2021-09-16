package com.superhao.base.cache.util;

import com.superhao.app.entity.AppRobotRoom;
import com.superhao.base.cache.CacheUtil;
import com.superhao.base.cache.core.RedisLock;
import com.superhao.base.util.SpringContexUtil;

import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * @Auther: super
 * @Date: 2019/11/26 16:00
 * @email:
 */
public class AppRoomRobotCacheUtil {
    private static RedisLock redisLock = SpringContexUtil.getBean(RedisLock.class);

    public  final static String ROOM_ROBOT_KEY_PREFIX = "ROOMROBOT_";

    public  final static String ROBOT_INIT_LOCK = "ROBOT_INIT"; //机器人初始化KEY

    public static boolean existRoomRobot(String key) {

        return CacheUtil.existsKey(ROOM_ROBOT_KEY_PREFIX+key);
    }
    public static AppRobotRoom getRoomRobot(String key) {
        return  CacheUtil.get(ROOM_ROBOT_KEY_PREFIX+key,AppRobotRoom.class);
    }
    public static boolean  opsRoomRobotLock(String key) {
        return  redisLock.lock(ROOM_ROBOT_KEY_PREFIX+key);
    }
    public static void  opsRoomRobotUnLock(String key) {
        redisLock.delete(ROOM_ROBOT_KEY_PREFIX+key);
    }

    /**
     * 机器人初始化专用  lock  unlock
     * @return
     */
    public static boolean  initRobotLock() {
        return  redisLock.setExprireLock(ROBOT_INIT_LOCK,-1);
    }
    public static void  initRobotUnLock() {
        redisLock.delete(ROBOT_INIT_LOCK);
    }





    public static void removeRoomRobot(String key) {
        if(redisLock.lock(ROOM_ROBOT_KEY_PREFIX+key)){
            CacheUtil.remove(ROOM_ROBOT_KEY_PREFIX+key);
            redisLock.delete(ROOM_ROBOT_KEY_PREFIX+key);
        }
    }


    public static void pushRoomRobot(String key, AppRobotRoom appRoomRobot) {
        CacheUtil.put(ROOM_ROBOT_KEY_PREFIX+key,appRoomRobot);
    }

    public static boolean opsRoomRobotLock(String key,int time) {
        return  redisLock.setExprireLock(ROOM_ROBOT_KEY_PREFIX+key,time);
    }

    public static int getRoomRobotCount() {
        Set<String> set =  CacheUtil.getSetByPrefix(ROOM_ROBOT_KEY_PREFIX,String.class);
        return set==null?0:set.size();
    }
}
