package com.superhao.base.authz.service;

import com.baomidou.mybatisplus.service.IService;
import com.superhao.base.authz.entity.SysRole;
import com.superhao.base.common.service.IBaseService;
import com.superhao.base.entity.HttpRequestData;


import java.util.Map;

/**
 * 
 *
 * @author superhao
 * @email 928649522@qq.com
 * @date 2019-05-07 11:27:34
 */
public interface ISysRoleService extends IBaseService<SysRole> {

    void searchSysRole(SysRole sysRole, HttpRequestData requestData);

    void searchSysRoleByPage(HttpRequestData requestData);

    boolean sysRoleUpdateValidate(SysRole sysRole, HttpRequestData requestData, boolean b);

    void updateSysRole(SysRole sysRole);

    void addSysRole(SysRole sysRole, HttpRequestData requestData);

    void logicRemoveSysRole(HttpRequestData requestData);

    void searchSysRolePermission(HttpRequestData requestData);
}

