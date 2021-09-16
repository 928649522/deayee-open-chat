package com.superhao.app.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.superhao.app.entity.AppUser;
import com.superhao.base.entity.HttpRequestData;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * H5聊天app用户表
 * 
 * @author superhao
 * @email
 * @date 2019-10-16 13:57:42
 */
@Mapper
public interface AppUserMapper extends BaseMapper<AppUser> {

    AppUser selectByMoreAccount(AppUser appUser);

    List<AppUser> selectUserByRoom(Map roomCode);

    AppUser selectByIdJoinFile(Map targetUserId);

    AppUser selectByAccountJoinFile(Map condition);

    void updateColumById(AppUser appUser);

    int updateIntegralById(Map condition);
}
