package com.superhao.app.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.superhao.app.entity.AppCallList;
import com.superhao.app.entity.AppRoom;
import com.superhao.app.entity.AppUser;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 
 * 
 * @author
 * @email
 * @date 2019-10-22 15:48:21
 */
@Mapper
public interface AppCallListMapper extends BaseMapper<AppCallList> {

    List<AppRoom> selectRoomById(@Param("userId")Long userId);

    List<AppUser> selectUserById(@Param("userId") Long userId);
}
