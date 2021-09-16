package com.superhao.app.service.impl;

import com.superhao.app.constant.AppChatConstant;
import com.superhao.app.entity.AppChatRecord;
import com.superhao.app.entity.AppRoom;
import com.superhao.app.entity.dto.ChatData;
import com.superhao.app.mapper.AppChatRecordMapper;
import com.superhao.app.service.IAppChatRecordService;
import com.superhao.base.common.service.impl.BaseServiceImpl;
import com.superhao.base.util.MybatisUtil;
import org.apache.commons.lang.StringEscapeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Service("appChatRecordService")
public class AppChatRecordServiceImpl extends BaseServiceImpl<AppChatRecordMapper, AppChatRecord> implements IAppChatRecordService,Serializable {

    private static final long serialVersionUID = -5441403815083521901L;
    @Autowired
    private AppChatRecordMapper appChatRecordMapper;

    @Override
    public List<AppChatRecord> selectListByRoomParam(AppRoom attribute) {
        int count = appChatRecordMapper.selectCount(MybatisUtil.conditionT().eq("room_id",attribute.getRoomId()));
        List < AppChatRecord > res;
        Map condition = new HashMap<>();

        if(count == 0){
            return null;
        }
        if(count<=attribute.getFindChatRecordNumber()){
            condition.put("begin",0);

        }else{
            condition.put("begin",count-attribute.getFindChatRecordNumber());
        }
        condition.put("roomId",attribute.getRoomId());
        condition.put("findChatRecordNumber",attribute.getFindChatRecordNumber());
        res  = appChatRecordMapper.selectListByRoomParam(condition);
        return res;
    }

    @Override
    public List<ChatData> searchChatDataByRoom(AppRoom attribute) {
        Object sourceId = attribute.getRoomId();
        if(sourceId==null){
            sourceId = attribute.getRoomCode();
        }
        int count = appChatRecordMapper.selectCount(MybatisUtil.conditionT().eq("room_id",sourceId.toString()));
        Map condition = new HashMap<>();
        if(count == 0){
            return null;
        }
        if(count<=AppChatConstant.GROUP_PAGE_SIZE){
            condition.put("begin",0);

        }else{
            condition.put("begin",count-AppChatConstant.GROUP_PAGE_SIZE);
        }
        condition.put("roomId",sourceId.toString());
        condition.put("findChatRecordNumber",AppChatConstant.GROUP_PAGE_SIZE);
        List < AppChatRecord >   listSource  = appChatRecordMapper.selectListByRoomParam(condition);
        List<ChatData> res = new ArrayList<>();
        if (listSource != null&&listSource.size()>0) {
            for (AppChatRecord item : listSource) {
                item.setContent(StringEscapeUtils.unescapeHtml(item.getContent()));
                ChatData historyChatRecord = ChatData.create(item.getUuid()
                        , item.getType()
                        , item.getCreator().toString()
                        , item.getRoomId().toString()
                        , item.getContent()
                        , item.getCreationTime());
                res.add(historyChatRecord);
            }
        }
        return res;
    }

    @Override
    public List<ChatData> searchChatRoomDataByPage(AppRoom attribute, Long msgid) {
        Object sourceId = attribute.getRoomId();
        if (sourceId == null) {
            sourceId = attribute.getRoomCode();
        }
        int count = appChatRecordMapper.selectCount(MybatisUtil.conditionT().eq("room_id", sourceId.toString()));
        Map condition = new HashMap<>();
        if (count == 0) {
            return null;
        }


        condition.put("msgid",msgid);
        condition.put("roomId", sourceId.toString());
        condition.put("pageSize", AppChatConstant.GROUP_PAGE_SIZE);
        List<AppChatRecord> listSource = appChatRecordMapper.searchChatRoomDataByPage(condition);

        List<ChatData> res = new ArrayList<>();
        if (listSource != null && listSource.size() > 0) {
            for (AppChatRecord item : listSource) {
                item.setContent(StringEscapeUtils.unescapeHtml(item.getContent()));
                ChatData historyChatRecord = ChatData.create(item.getUuid()
                        , item.getType()
                        , item.getCreator().toString()
                        , item.getRoomId().toString()
                        , item.getContent()
                        , item.getCreationTime());
                res.add(historyChatRecord);
            }
        }
        return res;
    }
}