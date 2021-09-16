package com.superhao.app.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.superhao.app.entity.AppChatRecord;
import com.superhao.app.entity.AppRoom;
import com.superhao.app.entity.dto.ChatData;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

/**
 * 
 * 
 * @author
 * @email
 * @date 2019-10-24 00:38:53
 */
@Mapper
public interface AppChatRecordMapper extends BaseMapper<AppChatRecord> {


    List<AppChatRecord> selectListByRoomParam(Map attribute);

    List<AppChatRecord> searchChatRoomDataByPage(Map condition);
}
