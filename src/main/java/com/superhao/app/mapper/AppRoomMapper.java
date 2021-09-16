package com.superhao.app.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.superhao.app.entity.AppRoom;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 
 * 
 * @author
 * @email
 * @date 2019-10-22 14:52:29
 */
@Mapper
public interface AppRoomMapper extends BaseMapper<AppRoom> {

    AppRoom selectJoinFileById(@Param("roomId") Long roomId);

    void updateDisablePeople(@Param("disablePeople") String disablePeople,@Param("roomId")  Long roomId);
}
