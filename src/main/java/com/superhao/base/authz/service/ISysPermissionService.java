package com.superhao.base.authz.service;

import com.superhao.base.authz.entity.SysPermission;
import com.superhao.base.common.service.IBaseService;
import com.superhao.base.entity.HttpRequestData;

/**
 * 
 *
 * @author
 * @email
 * @date 2019-04-25 11:37:14
 */
public interface ISysPermissionService extends IBaseService<SysPermission> {

    void searchList(HttpRequestData requestData);

    void addSysPermission(SysPermission sysPermission);

    void searchButtons(HttpRequestData requestData);

    void searchPermissionItem(HttpRequestData requestData);

    void searchPermission(HttpRequestData requestData);

    boolean permissionUpdateValidate(SysPermission sysPermission, HttpRequestData requestData);

    void updateSysPermission(SysPermission sysPermission);

    void removeSysPermission(HttpRequestData requestData);

    void searchUserPermission(HttpRequestData requestData);
}

