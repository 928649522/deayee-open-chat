package com.superhao.base.authz.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.superhao.base.authz.entity.SysUser;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Map;

/**
 * 
 * 
 * @author superhao
 * @email 928649522@qq.com
 * @date 2019-04-22 14:52:41
 */
@Mapper
public interface SysUserMapper extends BaseMapper<SysUser> {

    SysUser selectJoinFile(SysUser sysUser);

    void updateLoginNumber(@Param("userId") Long userId);
}
