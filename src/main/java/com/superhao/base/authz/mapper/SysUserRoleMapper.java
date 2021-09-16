package com.superhao.base.authz.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.superhao.base.authz.entity.SysPermission;
import com.superhao.base.authz.entity.SysUser;
import com.superhao.base.authz.entity.SysUserRole;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

/**
 * 用户角色关联表
 * 
 * @author superhao
 * @email 928649522@qq.com
 * @date 2019-05-11 12:31:16
 */
@Mapper
public interface SysUserRoleMapper extends BaseMapper<SysUserRole> {

    List<SysPermission> selectRolePermissionByUser(Map user);
}
