package com.superhao.base.cache.util;

import com.alibaba.fastjson.JSONObject;
import com.superhao.app.entity.AppRoom;
import com.superhao.app.entity.ChatRoomCacheModel;
import com.superhao.app.entity.dto.ChatData;
import com.superhao.app.entity.token.AppUserToken;
import com.superhao.app.handle.RoomChatHandle;
import com.superhao.base.cache.CacheUtil;
import com.superhao.base.cache.core.RedisLock;
import com.superhao.base.log.util.SysLogRecordUtil;
import com.superhao.base.util.SpringContexUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Auther: super
 * @Date: 2019/10/22 22:48
 * @email:
 */
@Slf4j
public class AppChatCacheUtil  {


  @SuppressWarnings("unchecked")
  private static RedisLock redisLock = SpringContexUtil.getBean(RedisLock.class);

    public  final static String ROMM_KEY_PREFIX = "ACRC_";
    
    public static boolean existRoom(String key) {

        return CacheUtil.existsKey(ROMM_KEY_PREFIX+key);
    }
    public static ChatRoomCacheModel getRoom(String key) {
            return  CacheUtil.get(ROMM_KEY_PREFIX+key,ChatRoomCacheModel.class);
    }
    public static boolean  opsRoomLock(String key) {
        return  redisLock.lock(ROMM_KEY_PREFIX+key);
    }
    public static void  opsRoomUnLock(String key) {
          redisLock.delete(ROMM_KEY_PREFIX+key);
    }







    public static void syncPutRoom(String key, ChatRoomCacheModel chatRoomCacheModel) {
        if(redisLock.lock(ROMM_KEY_PREFIX+key)){
            if(!existRoom(key)){
                CacheUtil.put(ROMM_KEY_PREFIX+key,chatRoomCacheModel);
            }else{
                ChatRoomCacheModel model =  getRoom(key);
                model.getSockets().add((RoomChatHandle) chatRoomCacheModel.getSockets().toArray()[0]);
                CacheUtil.put(ROMM_KEY_PREFIX+key,model);
            }
            redisLock.delete(ROMM_KEY_PREFIX+key);
        }else{
            String msg= "房间网络或人数超载  Redis Key--->"+ROMM_KEY_PREFIX+key;
            log.warn(msg);
            if(chatRoomCacheModel!=null){
                msg+=("  房间Id："+key);
                msg+=("  连接人数："+chatRoomCacheModel.getSockets().size());
                msg+=("  历史记录条数："+chatRoomCacheModel.getChatRecord().size());
            }
            SysLogRecordUtil.record(msg);
        }
    }



    public static void putRoom(String key, ChatRoomCacheModel chatRoomCacheModel) {
            if(existRoom(key)){
                CacheUtil.put(ROMM_KEY_PREFIX+key,chatRoomCacheModel);
            }
    }

    public static void removeRoom(String key) {
        if(redisLock.lock(ROMM_KEY_PREFIX+key)){
            CacheUtil.remove(ROMM_KEY_PREFIX+key);
            redisLock.delete(ROMM_KEY_PREFIX+key);
        }
    }

    public static void addRoomChatHandle(String key, RoomChatHandle roomChatHandle) {
        if(redisLock.lock(ROMM_KEY_PREFIX+key)){
            ChatRoomCacheModel chatRoomCacheModel = getRoom(key);
            chatRoomCacheModel.getSockets().add(roomChatHandle);
            CacheUtil.put(ROMM_KEY_PREFIX+key,chatRoomCacheModel);
            redisLock.delete(ROMM_KEY_PREFIX+key);
        }else{
            String msg= "房间网络或人数超载  Redis Key--->"+ROMM_KEY_PREFIX+key;
            log.warn(msg);
            SysLogRecordUtil.record(msg);
        }
    }

    public static void addRoomRecord(String key, ChatData chatData) {
        if(redisLock.lock(ROMM_KEY_PREFIX+key)){
            ChatRoomCacheModel chatRoomCacheModel = getRoom(key);
            chatRoomCacheModel.getChatRecord().add(chatData);
            CacheUtil.put(ROMM_KEY_PREFIX+key,chatRoomCacheModel);
            redisLock.delete(ROMM_KEY_PREFIX+key);
        }
    }


    public static void updateRoomInfo(String key, AppRoom appRoom) {
        if(redisLock.lock(ROMM_KEY_PREFIX+key)){
            ChatRoomCacheModel chatRoomCacheModel = getRoom(key);
            chatRoomCacheModel.setAppRoom(appRoom);
            CacheUtil.put(ROMM_KEY_PREFIX+key,chatRoomCacheModel);
            redisLock.delete(ROMM_KEY_PREFIX+key);
        }

    }

    public static void removeRoomSocket(String key, String userId) {
        try {
            if(redisLock.lock(ROMM_KEY_PREFIX+key)){

                ChatRoomCacheModel chatRoomCacheModel = getRoom(key);

                Set<RoomChatHandle> tempSet = new HashSet<>(chatRoomCacheModel.getSockets());
                for(RoomChatHandle item:tempSet){
                    Long targetId = item.getUserToken().getUserId();
                    if(targetId!=null&&targetId.toString().equals(userId)){
                        chatRoomCacheModel.getSockets().remove(item);
                    }
                }
                CacheUtil.put(ROMM_KEY_PREFIX+key,chatRoomCacheModel);
                redisLock.delete(ROMM_KEY_PREFIX+key);
        }
        }catch (Exception e){
            redisLock.delete(ROMM_KEY_PREFIX+key);
        }
    }

    public static ChatRoomCacheModel getRoomForLock(String key) {
        if(redisLock.lock(ROMM_KEY_PREFIX+key)){
            return CacheUtil.get(ROMM_KEY_PREFIX+key,ChatRoomCacheModel.class);
        }
        return null;
    }
}
