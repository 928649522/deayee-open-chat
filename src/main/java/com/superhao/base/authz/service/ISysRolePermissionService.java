package com.superhao.base.authz.service;

import com.superhao.base.authz.entity.SysRole;
import com.superhao.base.authz.entity.SysRolePermission;
import com.superhao.base.common.service.IBaseService;


import java.util.List;

/**
 * 
 *
 * @author superhao
 * @email 928649522@qq.com
 * @date 2019-05-07 19:54:26
 */
public interface ISysRolePermissionService extends IBaseService<SysRolePermission> {

    void modifyRolePermissionRelation(SysRole role);

    List<SysRolePermission> searchPermissionByRole(Long roleId);
}

