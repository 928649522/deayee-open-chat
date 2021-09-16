package com.superhao.app.service.impl;

import com.superhao.app.constant.AppChatConstant;
import com.superhao.app.entity.*;
import com.superhao.app.entity.dto.ChatData;
import com.superhao.app.entity.token.AppUserToken;
import com.superhao.app.handle.RoomChatHelper;
import com.superhao.app.mapper.AppChatRecordMapper;
import com.superhao.app.mapper.AppRoomRoleMapper;
import com.superhao.app.service.IAppChatMessageService;
import com.superhao.app.util.ChatResponseDataUtil;
import com.superhao.base.cache.util.AppChatCacheUtil;
import com.superhao.base.cache.util.AppNotifyCacheUtil;
import com.superhao.base.cache.util.AppTokenCacheUtil;
import com.superhao.base.common.dto.SysTips;
import com.superhao.base.entity.HttpRequestData;
import com.superhao.base.log.util.SysLogRecordUtil;
import com.superhao.base.util.MybatisUtil;
import lombok.extern.log4j.Log4j;
import org.apache.commons.lang.StringEscapeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.util.Map;

/**
 * @Auther: super
 * @Date: 2019/10/29 17:03
 * @email:
 */

@Log4j
@Service("appChatMessageService")
public class AppChatMessageServiceImpl implements IAppChatMessageService {

    @Autowired
    private AppChatRecordMapper appChatRecordMapper;

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private AppRoomRoleMapper appRoomRoleMapper;

    @Autowired
    private RoomChatHelper roomChatHelper;




    @Override
    public void sendDataToRoom(ChatData data, HttpRequestData requestData) {
        //AppUserToken userToken = AppTokenCacheUtil.currentUser();
        String roomId  = requestData.getString("roomId");
        String tokenKey  = requestData.getString("tokenKey");
        long beC = System.currentTimeMillis();

        if(StringUtils.isEmpty(roomId)||StringUtils.isEmpty(data.getType())|| StringUtils.isEmpty(data.getUuid())||StringUtils.isEmpty(data.getContent())){
            requestData.createErrorResponse(SysTips.PARAM_ERROR);
            return;
        }
        if(data.getContent().toString().length()>AppChatConstant.DATA_TEXT_MAX_LENGTH){
            requestData.createErrorResponse(SysTips.APP_TEXT_SIZE_OUT_MAX);
            return;
        }
        ChatRoomCacheModel cacheModel = AppChatCacheUtil.getRoom(roomId);
        if(cacheModel == null){
            //AppChatCacheUtil.opsRoomUnLock(roomId);
            log.warn("userId:{"+tokenKey+"}服务器忙");
            requestData.createErrorResponse(SysTips.APP_ROOM_SEND_BUSY);
            return;
        }


        AppUserToken currentUser = cacheModel.getUser(tokenKey);
        AppRoom currentRoom = cacheModel.getAppRoom();
        Map<String, Long[]> bannedTalks = cacheModel.getBannedTalks();
        if(currentUser == null){
            //AppChatCacheUtil.opsRoomUnLock(roomId);
            log.warn("userId:{"+tokenKey+"}非法发言");
            requestData.createErrorResponse(SysTips.PARAM_ERROR);
            return;
        }
        try {
            //群发
        if(AppChatConstant.SOCKET_TYPE_GROUP.equals(currentRoom.getRoomType())){
            //if(roomChatHelper.isJoinGroupRule(currentRoom,currentUser))

            //全体禁言 不对群主管理生效
            if(cacheModel.getAppRoom().isDisableSay()){
                int rows = appRoomRoleMapper.selectCount(MybatisUtil.conditionT()
                        .eq("room_id",roomId)
                        .and()
                        .eq("user_id",tokenKey)
                        .andNew()
                        .eq("role_type", AppRoomRole.TYPE_MASTER)
                        .or()
                        .eq("role_type", AppRoomRole.TYPE_MANAGER));
                if(rows==0){
                    requestData.createErrorResponse(SysTips.APP_ROOM_BANNED_CHAT_ALL);
                    return;
                }
            }
            //验证是否被禁言
            if(bannedTalks.size()>0){
                long current = System.currentTimeMillis();
                for(Map.Entry<String,Long[]> item:bannedTalks.entrySet()){
                    if(item.getKey().equals(tokenKey)){
                        long begin = item.getValue()[0];
                        long seconds  = item.getValue()[1];
                        boolean pass = (current-begin)>(seconds*1000);
                        if(!pass){
                            requestData.createErrorResponse(SysTips.APP_ROOM_BANNED_CHAT);
                            return;
                        }
                    }
                }
            }
            //叠加发言次数
            currentUser = AppTokenCacheUtil.currentUser();
            currentUser.plusOneSayNumber(roomId);
            AppTokenCacheUtil.pushExpire(currentUser);
            //匿名用户的规则:验证 发言次数、发言时间
            if(AppChatConstant.USER_TYPE_0.equals(currentUser.getUserType())){
                if(!chatRoomRoleValidate(currentRoom,currentUser,requestData)){
                    // AppChatCacheUtil.opsRoomUnLock(roomId);
                    return;
                }
            }

            saveRoomChatRecord(data,cacheModel,currentUser.getUserId());
            this.sendDataToRoomByHandleType(roomId,data,AppChatConstant.HANDLE_TYPE_TEXT,false,true);
        }else{
            saveRoomChatRecord(data,cacheModel,currentUser.getUserId());
            this.sendDataToRoomByHandleType(cacheModel.getAppRoom().getRoomCode(),data,AppChatConstant.HANDLE_TYPE_TEXT,false,false);
        }

        //TODO 改变redis缓存 一致性的地方
        /*   cacheModel = AppChatCacheUtil.getRoomForLock(roomId);
        if(cacheModel == null){
            //AppChatCacheUtil.opsRoomUnLock(roomId);
            log.warn("userId:{"+tokenKey+"}服务器忙");
            requestData.createErrorResponse(SysTips.SYS_SERVER_BUSY);
            return;
        }
     currentUser = cacheModel.getUser(tokenKey);
        currentUser.setSayNumber(currentUser.getSayNumber()+1);
        cacheModel.addChatRecord(data);
        AppChatCacheUtil.putRoom(roomId,cacheModel);
        AppChatCacheUtil.opsRoomUnLock(roomId); //解锁*/

        log.info("此次消息处理时间："+(System.currentTimeMillis()-beC)+"ms");
        } catch (IOException e) {
            String msg = "{user:"+currentUser.getAccount()+"} -->{roomCode:"+currentRoom.getRoomCode()+"}异常";
            log.warn(msg);
            SysLogRecordUtil.record(msg,e);

        }

    }


    @Override
    public void notifyRoomUser(AppRoom roomModel, ChatData data) {
        try {
            this.sendDataToRoomByHandleType(roomModel.getRoomId().toString(),data,AppChatConstant.HANDLE_TYPE_ONLINE,true,true);
        } catch (IOException e) {
            SysLogRecordUtil.record("通知该用户上线时出错",e);
        }
    }
    @Override
    public void notifyUserRoomUpdate(AppRoom roomModel, ChatData data) {
        try {
            if(StringUtils.isEmpty(data.getSender())){
                data.setSender("-1");
            }
            this.sendDataToRoomByHandleType(roomModel.getRoomId().toString(),data,AppChatConstant.HANDLE_TYPE_UPDATE_ROOM,true,true);
        } catch (IOException e) {
            SysLogRecordUtil.record("更新群信息出错",e);
        }
    }

    @Override
    public void notifyRoomUserRemove(ChatRoomCacheModel roomModel, ChatData data) {
        try {
            this.sendDataToRoomByHandleType(roomModel.getAppRoom().getRoomId().toString(),data,AppChatConstant.HANDLE_TYPE_REMOVE_ROOM,false,true);
        } catch (IOException e) {
            SysLogRecordUtil.record("通知移除群组出错",e);
        }
    }

    /**
     * 发送添加好友请求
     * @param chatData
     * @param requestData
     */
    @Override
    public void notifyFriendRequest(ChatData chatData, HttpRequestData requestData) {

        try {
            chatData.setType(AppChatConstant.TYPE_ADD_FRIEND);
            /**
             * 判断对方有没有添加自己
             */
            if(AppNotifyCacheUtil.existsByReciverType(chatData.getSender(),chatData.getReciever(),chatData.getType())){
                return;
            }
            /**
             * 判断该类型的请求是否重复
             */
            if(AppNotifyCacheUtil.existsByReciverType(chatData.getReciever(),chatData.getSender(),chatData.getType())){
                return;
            }
            AppNotifyCacheUtil.syncPushNotify(chatData.getReciever(),chatData);
            this.sendDataToRoomByHandleType(AppChatConstant.TOPIC_SYS_NOTIFY,chatData,AppChatConstant.HANDLE_TYPE_CALL_NOTIFY,false,false);
        } catch (IOException e) {
            SysLogRecordUtil.record("添加好友的通知出错",e);
        }
    }

    /**
     * 临时会话
     * @param chatData
     * @param requestData
     * @param firendInfo
     */
    @Override
    public void notifyTempContact(ChatData chatData, HttpRequestData requestData, AppUser firendInfo) {
        try {
            chatData.setType(AppChatConstant.TYPE_TEMP_CONTACT);

            if(AppNotifyCacheUtil.syncPushNotifyByType(chatData.getReciever(),chatData)){
                this.sendDataToRoomByHandleType(AppChatConstant.TOPIC_SYS_NOTIFY,chatData,AppChatConstant.HANDLE_TYPE_CALL_NOTIFY,false,false);
            }
            /**
             * 没有此联系人在建零时会话
             */
            if(firendInfo!=null){
                chatData.setSender(chatData.getReciever());
                Map map = (Map) chatData.getContent();
                map.put("user",firendInfo);
                map.put("meiqia",AppChatConstant.CONSTANT_NO);
                chatData.setContent(map);
                chatData.setReciever(AppTokenCacheUtil.currentUser().getUserId().toString());
                AppNotifyCacheUtil.syncPushNotifyByType(AppTokenCacheUtil.currentUser().getUserId().toString(),chatData);
            }
        } catch (IOException e) {
            SysLogRecordUtil.record("临时会话",e);
        }
    }

    @Override
    public void notifyFriendAgreeRequest(ChatData chatData, HttpRequestData requestData) {
        try {
            chatData.setType(AppChatConstant.TYPE_AGREE_FRIEND);
            //把添加好友的通知删除
            AppNotifyCacheUtil.syncRemoveNotify(chatData.getReciever(),chatData.getReciever(),AppChatConstant.TYPE_ADD_FRIEND);
            //把添加好友的通知删除
            AppNotifyCacheUtil.syncRemoveNotify(chatData.getSender(),chatData.getSender(),AppChatConstant.TYPE_ADD_FRIEND);
            this.sendDataToRoomByHandleType(AppChatConstant.TOPIC_SYS_NOTIFY,chatData,AppChatConstant.HANDLE_TYPE_CALL_NOTIFY,false,false);
        } catch (IOException e) {
            SysLogRecordUtil.record("同意添加好友的通知出错",e);
        }
    }
    @Override
    public void notifyCreateNewGroup(ChatData chatData) {
        try {
            chatData.setType(AppChatConstant.TYPE_CREATE_NEW_GROUP);
            this.sendDataToRoomByHandleType(AppChatConstant.TOPIC_SYS_NOTIFY,chatData,AppChatConstant.HANDLE_TYPE_CALL_NOTIFY,true,true);
        } catch (IOException e) {
            SysLogRecordUtil.record("拉好友进群出错",e);
        }
    }

    /*    private void  sendDataToRoomByHandleType(ChatRoomCacheModel cacheModel,ChatData data,String handleType,boolean sendSelf) throws IOException {
            Set<RoomChatHandle> targetRoom = cacheModel.getSockets();
            String sender = data.getSender();
            if(targetRoom!=null&&targetRoom.size()>0){
                //遍历下发消息
                for(RoomChatHandle socket:targetRoom){
                        Session session  = socket.getChatSession().getSession();
                        String tokenKey = socket.getUserToken().getUserId().toString();
                        if(!sendSelf&&sender.equals(tokenKey)){
                            continue;
                        }
                        if(session.isOpen()){
                            session.getBasicRemote()
                                    .sendText(ChatResponseDataUtil.toJsonText(data,handleType));
                        }

                }
            }

        }*/
//TODO 改redis消息推送

    /**
     *
     * @param roomId 房间标识
     * @param data 数据信息
     * @param handleType 数据处理类型
     * @param sendSelf 是否发送给自己
     * @param massInfo 是不是群发消息
     * @throws IOException
     */
    public void  sendDataToRoomByHandleType(String roomId,ChatData data,String handleType,boolean sendSelf,boolean massInfo) throws IOException {
        String  msg = ChatResponseDataUtil.toJsonText(data,handleType,sendSelf,massInfo);
        redisTemplate.convertAndSend(roomId,msg);
    }



    /**
     * 存聊天记录
     * @param chatData
     */
 /*   public void saveRoomChatRecord( ChatData chatData,ChatRoomCacheModel cacheModel,AppUserToken userToken){
        AppRoom currentRoom = cacheModel.getAppRoom();
        AppChatRecord oneChatRecord = AppChatRecord.createRoomRecord(
                chatData.getContent().toString()
                         ,chatData.getType()
                        ,chatData.getUuid()
                ,chatData.getCreationTime()
                        ,currentRoom.getRoomId()
                        ,userToken.getUserId());
        cacheModel.getChatRecord().add(chatData);
        oneChatRecord.setContent(StringEscapeUtils.escapeHtml( oneChatRecord.getContent()));
        this.appChatRecordMapper.insert(oneChatRecord);
    }*/

    public void saveRoomChatRecord( ChatData chatData,ChatRoomCacheModel cacheModel,Long senderId){
        Object roomId = cacheModel.getAppRoom().getRoomId();
        if(roomId==null){
            roomId = cacheModel.getAppRoom().getRoomCode();
        }
        AppChatRecord oneChatRecord = AppChatRecord.createRoomRecord(
                chatData.getContent().toString()
                ,chatData.getType()
                ,chatData.getUuid()
                ,chatData.getCreationTime()
                ,roomId.toString()
                ,senderId);
        oneChatRecord.setContent(StringEscapeUtils.escapeHtml( oneChatRecord.getContent()));
        this.appChatRecordMapper.insert(oneChatRecord);
    }
    public void saveRoomChatRecord( ChatData chatData,String roomId,Long senderId){
        AppChatRecord oneChatRecord = AppChatRecord.createRoomRecord(
                chatData.getContent().toString()
                ,chatData.getType()
                ,chatData.getUuid()
                ,chatData.getCreationTime()
                ,roomId.toString()
                ,senderId);
        oneChatRecord.setContent(StringEscapeUtils.escapeHtml( oneChatRecord.getContent()));
        this.appChatRecordMapper.insert(oneChatRecord);
    }
    private boolean chatRoomRoleValidate(AppRoom appRoom,AppUserToken appUserToken,HttpRequestData requestData){
        Integer  constant = -1;
        if(!constant.equals(appRoom.getAnonymousSayNumber())&&appUserToken.getSayNumber(appRoom)>appRoom.getAnonymousSayNumber()){
            requestData.createErrorResponse(SysTips.APP_ROOM_SAY_NUMBER_MAX);
            return false;
        }
        boolean pass =(System.currentTimeMillis() -appUserToken.getCreateTime())>(appRoom.getAnonymousSayTime()*1000);
        if(!constant.equals(appRoom.getAnonymousSayTime())&&pass){
            requestData.createErrorResponse(SysTips.APP_ROOM_SAY_TIME_MAX);
            return false;
        }
        return true;
    }


    /**
     *
     */
}
