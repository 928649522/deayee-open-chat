package com.superhao.app.service;

import com.superhao.app.entity.AppRoom;
import com.superhao.app.entity.AppUser;
import com.superhao.app.entity.ChatRoomCacheModel;
import com.superhao.app.entity.dto.ChatData;
import com.superhao.base.entity.HttpRequestData; /**
 * @Auther: super
 * @Date: 2019/10/29 17:01
 * @email:
 */
public interface IAppChatMessageService {
    void sendDataToRoom(ChatData data, HttpRequestData requestData);


    void notifyRoomUser(AppRoom roomModel, ChatData data);


    void notifyUserRoomUpdate(AppRoom roomModel, ChatData data);

    void notifyRoomUserRemove(ChatRoomCacheModel roomModel, ChatData data);

    /**
     * 好友请求
     * @param chatData
     * @param requestData
     */
    void notifyFriendRequest(ChatData chatData, HttpRequestData requestData);

    /**
     * 好友同意请求
     * @param chatData
     * @param requestData
     */
    void notifyFriendAgreeRequest(ChatData chatData, HttpRequestData requestData);

    void notifyTempContact(ChatData chatData, HttpRequestData requestData, AppUser b);

    void notifyCreateNewGroup(ChatData chatData);
}
