package com.superhao.base.authz.service;

import com.superhao.base.authz.entity.SysPermission;
import com.superhao.base.authz.entity.SysUser;
import com.superhao.base.authz.entity.SysUserRole;
import com.superhao.base.common.service.IBaseService;
import com.superhao.base.entity.HttpRequestData;

import java.util.List;
import java.util.Map;

/**
 * 用户角色关联表
 *
 * @author superhao
 * @email 928649522@qq.com
 * @date 2019-05-11 12:31:16
 */
public interface ISysUserRoleService extends IBaseService<SysUserRole> {

    void searchSysUserRole(HttpRequestData requestData);

    void updateSysUserRole(SysUser sysUser);

    /**
     * 根据用户条件查询对应的用户权限
     * @param user
     * @return
     */
    List<SysPermission> searchUserRolePermission(Map map);
}

