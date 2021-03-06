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
            log.warn("userId:{"+tokenKey+"}????????????");
            requestData.createErrorResponse(SysTips.APP_ROOM_SEND_BUSY);
            return;
        }


        AppUserToken currentUser = cacheModel.getUser(tokenKey);
        AppRoom currentRoom = cacheModel.getAppRoom();
        Map<String, Long[]> bannedTalks = cacheModel.getBannedTalks();
        if(currentUser == null){
            //AppChatCacheUtil.opsRoomUnLock(roomId);
            log.warn("userId:{"+tokenKey+"}????????????");
            requestData.createErrorResponse(SysTips.PARAM_ERROR);
            return;
        }
        try {
            //??????
        if(AppChatConstant.SOCKET_TYPE_GROUP.equals(currentRoom.getRoomType())){
            //if(roomChatHelper.isJoinGroupRule(currentRoom,currentUser))

            //???????????? ????????????????????????
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
            //?????????????????????
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
            //??????????????????
            currentUser = AppTokenCacheUtil.currentUser();
            currentUser.plusOneSayNumber(roomId);
            AppTokenCacheUtil.pushExpire(currentUser);
            //?????????????????????:?????? ???????????????????????????
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

        //TODO ??????redis?????? ??????????????????
        /*   cacheModel = AppChatCacheUtil.getRoomForLock(roomId);
        if(cacheModel == null){
            //AppChatCacheUtil.opsRoomUnLock(roomId);
            log.warn("userId:{"+tokenKey+"}????????????");
            requestData.createErrorResponse(SysTips.SYS_SERVER_BUSY);
            return;
        }
     currentUser = cacheModel.getUser(tokenKey);
        currentUser.setSayNumber(currentUser.getSayNumber()+1);
        cacheModel.addChatRecord(data);
        AppChatCacheUtil.putRoom(roomId,cacheModel);
        AppChatCacheUtil.opsRoomUnLock(roomId); //??????*/

        log.info("???????????????????????????"+(System.currentTimeMillis()-beC)+"ms");
        } catch (IOException e) {
            String msg = "{user:"+currentUser.getAccount()+"} -->{roomCode:"+currentRoom.getRoomCode()+"}??????";
            log.warn(msg);
            SysLogRecordUtil.record(msg,e);

        }

    }


    @Override
    public void notifyRoomUser(AppRoom roomModel, ChatData data) {
        try {
            this.sendDataToRoomByHandleType(roomModel.getRoomId().toString(),data,AppChatConstant.HANDLE_TYPE_ONLINE,true,true);
        } catch (IOException e) {
            SysLogRecordUtil.record("??????????????????????????????",e);
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
            SysLogRecordUtil.record("?????????????????????",e);
        }
    }

    @Override
    public void notifyRoomUserRemove(ChatRoomCacheModel roomModel, ChatData data) {
        try {
            this.sendDataToRoomByHandleType(roomModel.getAppRoom().getRoomId().toString(),data,AppChatConstant.HANDLE_TYPE_REMOVE_ROOM,false,true);
        } catch (IOException e) {
            SysLogRecordUtil.record("????????????????????????",e);
        }
    }

    /**
     * ????????????????????????
     * @param chatData
     * @param requestData
     */
    @Override
    public void notifyFriendRequest(ChatData chatData, HttpRequestData requestData) {

        try {
            chatData.setType(AppChatConstant.TYPE_ADD_FRIEND);
            /**
             * ?????????????????????????????????
             */
            if(AppNotifyCacheUtil.existsByReciverType(chatData.getSender(),chatData.getReciever(),chatData.getType())){
                return;
            }
            /**
             * ????????????????????????????????????
             */
            if(AppNotifyCacheUtil.existsByReciverType(chatData.getReciever(),chatData.getSender(),chatData.getType())){
                return;
            }
            AppNotifyCacheUtil.syncPushNotify(chatData.getReciever(),chatData);
            this.sendDataToRoomByHandleType(AppChatConstant.TOPIC_SYS_NOTIFY,chatData,AppChatConstant.HANDLE_TYPE_CALL_NOTIFY,false,false);
        } catch (IOException e) {
            SysLogRecordUtil.record("???????????????????????????",e);
        }
    }

    /**
     * ????????????
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
             * ????????????????????????????????????
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
            SysLogRecordUtil.record("????????????",e);
        }
    }

    @Override
    public void notifyFriendAgreeRequest(ChatData chatData, HttpRequestData requestData) {
        try {
            chatData.setType(AppChatConstant.TYPE_AGREE_FRIEND);
            //??????????????????????????????
            AppNotifyCacheUtil.syncRemoveNotify(chatData.getReciever(),chatData.getReciever(),AppChatConstant.TYPE_ADD_FRIEND);
            //??????????????????????????????
            AppNotifyCacheUtil.syncRemoveNotify(chatData.getSender(),chatData.getSender(),AppChatConstant.TYPE_ADD_FRIEND);
            this.sendDataToRoomByHandleType(AppChatConstant.TOPIC_SYS_NOTIFY,chatData,AppChatConstant.HANDLE_TYPE_CALL_NOTIFY,false,false);
        } catch (IOException e) {
            SysLogRecordUtil.record("?????????????????????????????????",e);
        }
    }
    @Override
    public void notifyCreateNewGroup(ChatData chatData) {
        try {
            chatData.setType(AppChatConstant.TYPE_CREATE_NEW_GROUP);
            this.sendDataToRoomByHandleType(AppChatConstant.TOPIC_SYS_NOTIFY,chatData,AppChatConstant.HANDLE_TYPE_CALL_NOTIFY,true,true);
        } catch (IOException e) {
            SysLogRecordUtil.record("?????????????????????",e);
        }
    }

    /*    private void  sendDataToRoomByHandleType(ChatRoomCacheModel cacheModel,ChatData data,String handleType,boolean sendSelf) throws IOException {
            Set<RoomChatHandle> targetRoom = cacheModel.getSockets();
            String sender = data.getSender();
            if(targetRoom!=null&&targetRoom.size()>0){
                //??????????????????
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
//TODO ???redis????????????

    /**
     *
     * @param roomId ????????????
     * @param data ????????????
     * @param handleType ??????????????????
     * @param sendSelf ?????????????????????
     * @param massInfo ?????????????????????
     * @throws IOException
     */
    public void  sendDataToRoomByHandleType(String roomId,ChatData data,String handleType,boolean sendSelf,boolean massInfo) throws IOException {
        String  msg = ChatResponseDataUtil.toJsonText(data,handleType,sendSelf,massInfo);
        redisTemplate.convertAndSend(roomId,msg);
    }



    /**
     * ???????????????
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
