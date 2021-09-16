package com.superhao.app.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.superhao.app.entity.AppRobotRoom;
import org.apache.ibatis.annotations.Mapper;

import java.util.Map;

/**
 * 
 * 
 * @author superhao
 * @email 928649522@qq.com
 * @date 2019-11-26 15:03:27
 */
@Mapper
public interface AppRobotRoomMapper extends BaseMapper<AppRobotRoom> {

    AppRobotRoom selectByIdJoinInfo(Map condition);
}
