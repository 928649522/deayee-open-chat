package com.superhao.app.handle;

import com.superhao.app.constant.AppChatConstant;
import com.superhao.app.entity.*;
import com.superhao.app.entity.dto.ChatData;
import com.superhao.app.entity.token.AppUserToken;
import com.superhao.app.handle.listener.RoomMessageListener;
import com.superhao.app.mapper.AppRoomRoleMapper;
import com.superhao.app.service.IAppChatMessageService;
import com.superhao.app.service.IAppChatRecordService;
import com.superhao.base.cache.util.AppChatCacheUtil;
import com.superhao.base.common.constant.SysServiceConfigSet;
import com.superhao.base.common.dto.SysTips;
import com.superhao.base.entity.HttpRequestData;
import com.superhao.base.util.Md5Util;
import org.apache.commons.lang.StringEscapeUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @Auther: super
 * @Date: 2019/10/29 15:27
 * @email:
 */
@Component
public class RoomChatHelper {

    @Autowired
    private IAppChatRecordService appChatRecordService;
    @Autowired
    private IAppChatMessageService appChatMessageService;

    @Autowired
    private RedisMessageListenerContainer listenerContainer;

    @Autowired
     private   SysServiceConfigSet  serviceConfigSet;

    public SysServiceConfigSet getServiceConfigSet() {
        return serviceConfigSet;
    }

    public void addUserMsgListenner(RoomMessageListener topicListener, String channelId) {
        listenerContainer.addMessageListener(topicListener, new ChannelTopic(channelId));
    }

    /**
     * 创建一个新的聊天组
     */
    public synchronized ChatRoomCacheModel joinAppChatRoom(AppRoom appRoom, RoomChatHandle currentSocket) {

        try {
            Object id = appRoom.getRoomId();
            if(id==null){
                id = appRoom.getRoomCode();
            }
            String roomId = id.toString();
            if (AppChatCacheUtil.existRoom(roomId)) {
                if(currentSocket!=null){
                    AppChatCacheUtil.addRoomChatHandle(roomId, currentSocket);

                }
                return AppChatCacheUtil.getRoom(roomId);
            }
            ChatRoomCacheModel model = new ChatRoomCacheModel(appRoom);
            // model.getSockets().add(this);
            /**
             * 初始化 历史聊天记录
             */
            List<AppChatRecord> res = appChatRecordService.selectListByRoomParam(appRoom);
            if (res != null&&res.size()>0) {
                for (AppChatRecord item : res) {
                    item.setContent(StringEscapeUtils.unescapeHtml(item.getContent()));
                    ChatData historyChatRecord = ChatData.create(item.getUuid()
                            , item.getType()
                            , item.getCreator().toString()
                            , item.getRoomId().toString()
                            , item.getContent()
                            , item.getCreationTime());
                    model.getChatRecord().add(historyChatRecord);
                }
            }
            if (currentSocket != null) {
                model.getSockets().add(currentSocket);
            }
            model.setAppRoom(appRoom);
            AppChatCacheUtil.syncPutRoom(roomId, model);
            return model;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;

    }

    public void removeRoomUser(RoomChatHandle attribute) {
        if(attribute.getMessageListener()!=null){
            AppRoom room =attribute.getAttribute();
            //通知退出群组
            if(room!=null&&AppChatConstant.SOCKET_TYPE_GROUP.equals(room.getRoomType())){
                AppUserToken info = attribute.getUserToken();
                ChatData cd = new ChatData();
                cd.setSender(info.getUserId().toString());
                cd.setContent(info.getUserId().toString());
                cd.setType(AppChatConstant.TYPE_GROUP_EXIT_PEOPLE);
                appChatMessageService.notifyUserRoomUpdate(room,cd);
            }

            listenerContainer.removeMessageListener(attribute.getMessageListener());
        }
        AppRoom targetRoom = attribute.getAttribute();
        if (targetRoom == null) {
            return;
        }
        Object userOrGroupId = targetRoom.getRoomId();
        if(userOrGroupId==null){
            userOrGroupId = targetRoom.getRoomCode();
        }
        if (!AppChatCacheUtil.existRoom(userOrGroupId.toString())) {
            return;
        }
        AppChatCacheUtil.removeRoomSocket(userOrGroupId.toString(), attribute.getUserToken().getUserId().toString());
    }

    public void noticeThisUserOnline(RoomChatHandle roomChatHandle) {
        //ChatRoomCacheModel cacheModel,ChatData data,String handleType
        ChatRoomCacheModel roomModel = AppChatCacheUtil.getRoom(roomChatHandle.getAttribute().getRoomId().toString());
        ChatData data = new ChatData();
        AppUserToken userToken = roomChatHandle.getUserToken();
        AppUser appUser =  new AppUser();
        BeanUtils.copyProperties(userToken,appUser);
        data.setContent(appUser);
        data.setSender(appUser.getUserId().toString());
        data.setType(AppChatConstant.TYPE_ONLINE);
        appChatMessageService.notifyRoomUser(roomModel.getAppRoom(), data);
    }

    public void removeRoomById(String roomId) {
        if (!AppChatCacheUtil.existRoom(roomId)) {
            return;
        }
        ChatData data = new ChatData();
        data.setSender("-1");
        data.setContent("该群已被解散");
        data.setType(AppChatConstant.TYPE_REMOVE_ROOM);
        ChatRoomCacheModel roomModel = AppChatCacheUtil.getRoom(roomId);
        appChatMessageService.notifyRoomUserRemove(roomModel, data);
        AppChatCacheUtil.removeRoom(roomId);

    }



    public String generateFinalContactRoomId(Long userId1, Long userId2) {
        if (userId1 > userId2) {
            return Md5Util.getMD5(userId1 + "" + userId2);
        } else {
            return Md5Util.getMD5(userId2 + "" + userId1);
        }
    }


    /**
     * 验证用户进入房间是否满足条件
     *
     * @param attribute
     * @param token
     * @param requestData
     * @return
     */
    public boolean isJoinGroupRule(AppRoom attribute, AppUserToken token, HttpRequestData requestData) {
        if(AppChatConstant.USER_TYPE_0.equals(token.getUserType())){ //匿名
            if (AppChatConstant.CONSTANT_NO.equals(attribute.getIsAnonymous())) { //不允许匿名用户进入
                return false;
            }
        }
        //验证是否被踢除
        if(attribute.getDisablePeople().contains(token.getUserId().toString())){
            requestData.createErrorResponse(SysTips.APP_ROOM_DISABLE_PEOPLE);
            return false;
        }
        return true;
    }

    public boolean isJoinContactRule(AppUserToken currentUser, HttpRequestData requestData) {
        if (AppChatConstant.USER_TYPE_0.equals(currentUser.getUserType())) {  //匿名用户无法发起
            return false;
        }
        //TODO 设置只有管理员才可以向别人发起对话

        return true;
    }

    public boolean isJoinCallListRule(AppUserToken currentUser) {
        if (AppChatConstant.USER_TYPE_0.equals(currentUser.getUserType())) {  //匿名用户无法发起
            return false;
        }


        return true;
    }

    public AppRoomRole getDBRoomHasUser(String group, Long userId) {
        AppRoomRole appRoomRole = new AppRoomRole();
        appRoomRole.setRoomId(new Long(group));
        appRoomRole.setUserId(userId);
        return appRoomRoleMapper.selectOne(appRoomRole);
    }

    @Autowired
    private transient AppRoomRoleMapper appRoomRoleMapper;
}
