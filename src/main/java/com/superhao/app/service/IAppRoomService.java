package com.superhao.app.service;

import com.superhao.app.entity.AppDisreport;
import com.superhao.app.entity.AppRedPoint;
import com.superhao.app.entity.AppRoom;
import com.superhao.app.entity.dto.ChatData;
import com.superhao.base.common.service.IBaseService;
import com.superhao.base.entity.HttpRequestData;

/**
 * 
 *
 * @author superhao
 * @email
 * @date 2019-10-22 14:52:29
 */
public interface IAppRoomService extends IBaseService<AppRoom> {

    /**
     * 分页查找聊天室信息
     * @param requestData
     */
    void searchSysRoomByPage(HttpRequestData requestData);

    void addSysRoom(AppRoom appRoom, HttpRequestData requestData);

    boolean sysRoomUpdateValidate(AppRoom appRoom, HttpRequestData requestData, boolean isUpdate);

    void updateSysRoom(AppRoom appRoom, HttpRequestData requestData);

    /**
     * 获取聊天室用户信息和消息记录
     * @param requestData
     * @return
     */
    void searchChatRoomParam(HttpRequestData requestData);

    void updateChatRoomUserRole(HttpRequestData requestData);

    void searchSysRoom(HttpRequestData requestData);

    void removeSysRoom(HttpRequestData requestData);

    void disReport(AppDisreport appDisreport, HttpRequestData requestData);

    void sendRedPoint(AppRedPoint appDisreport, HttpRequestData requestData);

    void getRedPoint(HttpRequestData requestData);

    void createChatGroup(HttpRequestData requestData);

    void shareChatGroup(HttpRequestData requestData);

    void disableSay(HttpRequestData requestData);

    void disableAllSay(HttpRequestData requestData);

    void msgWithdraw(HttpRequestData requestData);

    void msgWithdrawAll(HttpRequestData requestData);

    void disablePeole(HttpRequestData requestData);

    void groupBlacklistTable(HttpRequestData requestData);

    void relieveBlacklist(HttpRequestData requestData);

    void getMaster(HttpRequestData requestData);

    void searchRoomHttpPath(HttpRequestData requestData);

    void updateQrcodeExpire(HttpRequestData requestData);
}

