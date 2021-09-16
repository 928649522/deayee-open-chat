package com.superhao.app.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.superhao.app.entity.AppRoomRole;
import com.superhao.app.entity.AppUser;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 
 * 
 * @author
 * @email
 * @date 2019-10-25 12:58:11
 */
@Mapper
public interface AppRoomRoleMapper extends BaseMapper<AppRoomRole> {

    List<AppUser> selectAppUsersByRoomId(Map condition);
}
