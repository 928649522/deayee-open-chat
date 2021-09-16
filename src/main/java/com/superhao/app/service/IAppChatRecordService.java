package com.superhao.app.service;

import com.superhao.app.entity.AppChatRecord;
import com.superhao.app.entity.AppRoom;
import com.superhao.app.entity.dto.ChatData;
import com.superhao.base.common.service.IBaseService;


import java.util.List;

/**
 * 
 *
 * @author
 * @email
 * @date 2019-10-24 00:38:53
 */
public interface IAppChatRecordService extends IBaseService<AppChatRecord> {


    List<AppChatRecord> selectListByRoomParam(AppRoom attribute);

    List<ChatData> searchChatDataByRoom(AppRoom appRoom);

    List<ChatData> searchChatRoomDataByPage(AppRoom appRoom, Long msgid);
}

