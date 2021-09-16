package com.superhao.base.cache.util;

import com.superhao.app.entity.dto.ChatData;
import com.superhao.base.cache.CacheUtil;
import com.superhao.base.cache.core.RedisLock;
import com.superhao.base.util.SpringContexUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * @Auther: super
 * @Date: 2019/11/12 22:28
 * @email:
 */
public class AppNotifyCacheUtil {
    private static RedisLock redisLock = SpringContexUtil.getBean(RedisLock.class);

    public  final static String NOTIYFY_KEY_PREFIX = "NOTIFY_";

    public static boolean existNotify(String key) {

        return CacheUtil.existsKey(NOTIYFY_KEY_PREFIX+key);
    }
    public static List<ChatData> getNotify(String key) {
        return  CacheUtil.get(NOTIYFY_KEY_PREFIX+key,ArrayList.class);
    }
    public static boolean  opsNotifyLock(String key) {
        return  redisLock.lock(NOTIYFY_KEY_PREFIX+key);
    }
    public static void  opsNotifyUnLock(String key) {
        redisLock.delete(NOTIYFY_KEY_PREFIX+key);
    }
    public static boolean  existsByReciverType(String sender, String recieverId, String chatDataType) {
        List<ChatData> tempList = getNotify(sender);
        if(tempList==null||tempList.size()==0){
            return false;
        }
        for(ChatData item:tempList){
            if(item.getType().equals(chatDataType)&&item.getSender().equals(recieverId)){
                return true;
            }
        }
        return false;
    }
  /*  public static boolean  existsNotifyByRT(String sender, String recieverId, String chatDataType) {
        List<ChatData> tempList = getNotify(sender);
        if(tempList==null||tempList.size()==0){
            return false;
        }
        for(ChatData item:tempList){
            if(item.getType().equals(chatDataType)&&item.getSender().equals(recieverId)){
                return true;
            }
        }
        return false;
    }*/




    public static void syncPushNotify(String key,ChatData chatData){
        if(redisLock.lock(NOTIYFY_KEY_PREFIX+key)){
            if(!existNotify(key)){
                List<ChatData> cd = new ArrayList<ChatData>();
                cd.add(chatData);
                CacheUtil.put(NOTIYFY_KEY_PREFIX+key,cd);
            }else{
                List<ChatData> model =  getNotify(key);
                model.add(chatData);
                CacheUtil.put(NOTIYFY_KEY_PREFIX+key,model);
            }
            redisLock.delete(NOTIYFY_KEY_PREFIX+key);
        }
    }


    /**
     * 过滤相同的通知类型 以免重复添加
     * @param key
     * @param chatData
     */
    public static boolean syncPushNotifyByType(String key,ChatData chatData){
        boolean has = false;
        if(redisLock.lock(NOTIYFY_KEY_PREFIX+key)){
            if(!existNotify(key)){
                List<ChatData> cd = new ArrayList<ChatData>();
                cd.add(chatData);
                CacheUtil.put(NOTIYFY_KEY_PREFIX+key,cd);
            }else{
                List<ChatData> model =  getNotify(key);

                for(ChatData item:model){
                    if(item.getSender().equals(chatData.getSender())&&item.getType().equals(chatData.getType())){
                        has = true;
                        break;
                    }
                }
                if(!has){
                    model.add(chatData);
                }
                CacheUtil.put(NOTIYFY_KEY_PREFIX+key,model);
            }
            redisLock.delete(NOTIYFY_KEY_PREFIX+key);
        }
        return !has;
    }



    /**
     * 根据 发送者 、 接受者、数据处理类型 来移除通知
     */
    public static void syncRemoveNotify(String sender,String reciver, String type){
        if(redisLock.lock(NOTIYFY_KEY_PREFIX+sender)){
            if(existNotify(sender)){
                List<ChatData> friendModel = getNotify(sender);
                ChatData target =null;
                for(ChatData cd : friendModel){
                    if(cd.getReciever().equals(reciver)&&cd.getType().equals(type)){
                        target =cd;
                    }
                }
                if(target!=null){
                    friendModel.remove(target);
                }
                CacheUtil.put(NOTIYFY_KEY_PREFIX+sender,friendModel);
            }
            redisLock.delete(NOTIYFY_KEY_PREFIX+sender);
        }
    }


    public static void syncRemoveNotify2(String sender,String reciver, String type){
        if(redisLock.lock(NOTIYFY_KEY_PREFIX+sender)){
            if(existNotify(sender)){
                List<ChatData> friendModel = getNotify(sender);
                List<ChatData> temp = new ArrayList<>(friendModel);
                for(ChatData cd : temp){
                    if(cd.getSender().equals(reciver)&&cd.getType().equals(type)){
                        friendModel.remove(cd);
                    }
                }
                CacheUtil.put(NOTIYFY_KEY_PREFIX+sender,friendModel);
            }
            redisLock.delete(NOTIYFY_KEY_PREFIX+sender);
        }
    }



    public static void removeNotifyList(String key) {
        if(redisLock.lock(NOTIYFY_KEY_PREFIX+key)){
            CacheUtil.remove(NOTIYFY_KEY_PREFIX+key);
            redisLock.delete(NOTIYFY_KEY_PREFIX+key);
        }
    }





}
