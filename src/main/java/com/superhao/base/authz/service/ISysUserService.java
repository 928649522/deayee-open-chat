package com.superhao.base.authz.service;

import com.superhao.base.authz.entity.SysUser;
import com.superhao.base.common.service.IBaseService;
import com.superhao.base.entity.HttpRequestData;

/**
 *
 *
 * @author superhao
 * @email 928649522@qq.com
 * @date 2019-04-22 14:52:41
 */
public interface ISysUserService extends IBaseService<SysUser> {

    boolean login(SysUser sysUser, HttpRequestData requestData);


    boolean sysUserUpdateValidate(SysUser sysUser, HttpRequestData requestData, boolean isUpdate);

    void updateSysUser(SysUser sysUser);

    void addSysUser(SysUser sysUser, HttpRequestData requestData);

    void searchSysUserByPage(HttpRequestData requestData);

    void searchSysUser(SysUser sysUser,HttpRequestData requestData);

    void logicRemoveSysUser(HttpRequestData requestData);

    void updateSysUserDetail(SysUser requestData, HttpRequestData data);
}

