package com.superhao.base.cache.util;

import com.superhao.app.entity.dto.ChatData;
import com.superhao.app.handle.RoomChatHandle;
import com.superhao.base.cache.CacheUtil;
import com.superhao.base.cache.core.RedisLock;
import com.superhao.base.util.Md5Util;
import com.superhao.base.util.SpringContexUtil;

import javax.xml.transform.Source;
import java.util.ArrayList;
import java.util.List;

/**
 * @Auther: super
 * @Date: 2019/11/20 22:08
 * @email:
 */
public class AppChatMsgLoseCacheUtil {
    private static RedisLock redisLock = SpringContexUtil.getBean(RedisLock.class);

    public  final static String MSG_LOSE_KEY = "MSGLOSE_";

    public static boolean existMsgLose(String key) {
        return CacheUtil.existsKey(MSG_LOSE_KEY+key);
    }
    public static List<String> getMsgLose(String key) {
        return  CacheUtil.get(MSG_LOSE_KEY+key,ArrayList.class);
    }
    public static boolean  opsMsgLoseLock(String key) {
        return  redisLock.lock(MSG_LOSE_KEY+key);
    }
    public static void  opsMsgLoseUnLock(String key) {
        redisLock.delete(MSG_LOSE_KEY+key);
    }



    public static void syncPush(RoomChatHandle handle, String cd) {
        String onlyKey = generateMsgLoseKey(handle);
        opsMsgLoseLock(MSG_LOSE_KEY+onlyKey);
        if(!existMsgLose(onlyKey)){
            CacheUtil.put(MSG_LOSE_KEY+onlyKey,new ArrayList<String>());
        }else{
            List<String> list = CacheUtil.get(MSG_LOSE_KEY+onlyKey,ArrayList.class);
            list.add(cd);
            CacheUtil.put(MSG_LOSE_KEY+onlyKey,list);
        }
        opsMsgLoseUnLock(MSG_LOSE_KEY+onlyKey);
    }



    public static String generateMsgLoseKey(RoomChatHandle handle) {
        Object sourceId = handle.getAttribute().getRoomId();
        if(sourceId==null){
            sourceId = handle.getAttribute().getRoomCode();
        }
        String onlyKey = handle.getUserToken().getUserId().toString();
        return Md5Util.getMD5(onlyKey+sourceId.toString());
    }
}
