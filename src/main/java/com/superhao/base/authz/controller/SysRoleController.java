package com.superhao.base.authz.controller;

import java.util.Arrays;
import java.util.Map;


import com.superhao.base.annotation.Log;
import com.superhao.base.authz.entity.SysRole;
import com.superhao.base.authz.service.ISysRoleService;
import com.superhao.base.common.controller.BaseController;
import com.superhao.base.common.dto.BaseJsonResult;
import com.superhao.base.common.dto.SysTips;
import com.superhao.base.entity.HttpRequestData;
import com.superhao.base.util.ValidationUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.stereotype.Controller;





/**
 *
 *
 *
 * @author superhao
 * @email 928649522@qq.com
 * @date 2019-05-07 11:27:34
 *
 */
@RestController
@RequestMapping("sys/sysRole")
public class SysRoleController extends BaseController {
    @Autowired
    private ISysRoleService sysRoleService;


    @Log(type = Log.LOG_TYPE.SELECT,text="查询系统角色")
    @RequestMapping(value = "searchSysRole",method = RequestMethod.POST)
    public Map searchSysRole(SysRole sysRole,HttpRequestData requestData){
        sysRoleService.searchSysRole(sysRole,requestData);
        return requestData.response();
    }

    @Log(type = Log.LOG_TYPE.SELECT,text="分页查询系统角色")
    @RequestMapping(value = "searchSysRoleByPage",method = RequestMethod.POST)
    public Map searchSysRoleByPage(HttpRequestData requestData){
        if(requestData.hasPageParam()){
            sysRoleService.searchSysRoleByPage(requestData);
        }else{
            requestData.createErrorResponse("分页参数缺失", SysTips.PARAM_ERROR);
        }
        return requestData.response();
    }

    @Log(type = Log.LOG_TYPE.SELECT,text="查询角色权限树列表")
    @RequestMapping(value = "searchSysRolePerTree",method = RequestMethod.POST)
    public Map searchSysRolePerTree(HttpRequestData requestData){
            sysRoleService.searchSysRolePermission(requestData);
        return requestData.response();
    }



    @Log(type = Log.LOG_TYPE.UPDATE,text="修改系统角色")
    @RequestMapping(value = "updateSysRole",method = RequestMethod.POST)
    public Map updateSysRole(SysRole sysRole, HttpRequestData requestData){
        ValidationUtils.validate(sysRole);
        if(sysRoleService.sysRoleUpdateValidate(sysRole,requestData,true)){
            sysRoleService.updateSysRole(sysRole);
        }
        return requestData.response();
    }

    @Log(type = Log.LOG_TYPE.ADD,text="添加系统角色")
    @RequestMapping(value = "addSysRole",method = RequestMethod.POST)
    public Map addSysRole(SysRole sysRole, HttpRequestData requestData){
        ValidationUtils.validate(sysRole);
        if(sysRoleService.sysRoleUpdateValidate(sysRole,requestData, false)){
            sysRoleService.addSysRole(sysRole,requestData);
        }
        return requestData.response();
    }
    @Log(type = Log.LOG_TYPE.ADD,text="删除系统角色",after = Log.AFTER_OPERATE.DELETE_BACKUP)
    @RequestMapping(value = "removeSysRole",method = RequestMethod.POST)
    public Map removeSysRole(HttpRequestData requestData){
        if(requestData.hasMustParam("roleId,removeType")){
            sysRoleService.logicRemoveSysRole(requestData);
        }else{
            requestData.createErrorResponse("缺少必要参数", SysTips.PARAM_ERROR);
        }
        return requestData.response();
    }
}
