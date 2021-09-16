package com.superhao.base.authz.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.superhao.base.authz.entity.SysRole;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 
 * 
 * @author superhao
 * @email 928649522@qq.com
 * @date 2019-05-07 11:27:34
 */
@Mapper
public interface SysRoleMapper extends BaseMapper<SysRole> {

    List<SysRole> selectRoleByUserId(@Param("userId") Long userId);
}
