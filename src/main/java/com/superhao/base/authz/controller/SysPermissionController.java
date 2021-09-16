package com.superhao.base.authz.controller;

import java.security.Permission;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


import com.superhao.base.annotation.Log;
import com.superhao.base.authz.entity.SysPermission;
import com.superhao.base.authz.service.ISysPermissionService;
import com.superhao.base.common.controller.BaseController;
import com.superhao.base.common.dto.BaseJsonResult;
import com.superhao.base.common.dto.SysTips;
import com.superhao.base.entity.HttpRequestData;
import com.superhao.base.util.ValidationUtils;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.stereotype.Controller;

import javax.xml.ws.RequestWrapper;


/**
 *
 *
 *
 * @author superhao
 * @email 928649522@qq.com
 * @date 2019-04-25 11:37:14
 *
 */
@RestController
@RequestMapping("/sys/sysPermission")
@Api()
public class SysPermissionController extends BaseController {
    @Autowired
    private ISysPermissionService sysPermissionService;

    @Log(type = Log.LOG_TYPE.SELECT,text="查询权限列表")
    @RequestMapping(value = "searchList")
    public Map searchList(HttpRequestData requestData){
      sysPermissionService.searchList(requestData);
        return  requestData.response();
    }

    @Log(type = Log.LOG_TYPE.SELECT,text="查询权限列表")
    @RequestMapping(value = "searchUserPermission")
    public Map searchUserPermission(HttpRequestData requestData){
        sysPermissionService.searchUserPermission(requestData);
        return  requestData.response();
    }

    /**
     * 获取当前的按钮
     * @param requestData
     */
    @Log(type = Log.LOG_TYPE.SELECT,text="加载按钮")
    @RequestMapping(value = "searchButtons",method = RequestMethod.POST)
    public Map searchButtons(HttpRequestData requestData){
        if(!requestData.hasParam("permissionId")){
              return renderError();
        }
        sysPermissionService.searchButtons(requestData);
        return requestData.response();
    }

    /**
     * 找出所有“菜单集合”“菜单”节点
     * @param requestData
     * @return
     */
    @RequestMapping(value = "searchPermissionItem",method = RequestMethod.POST)
    public Map searchPermissionItem(HttpRequestData requestData){

        sysPermissionService.searchPermissionItem(requestData);
        return requestData.response();
    }

    @RequestMapping(value = "searchPermission",method = RequestMethod.POST)
    public Map searchPermission(HttpRequestData requestData){
        sysPermissionService.searchPermission(requestData);
        return requestData.response();
    }

    @Log(type = Log.LOG_TYPE.ADD,text="新增权限")
    @RequestMapping(value = "addSysPermission",method = RequestMethod.POST)
    public Map addSysPermission(SysPermission sysPermission,HttpRequestData requestData){
        ValidationUtils.validate(sysPermission);
        if(sysPermissionService.permissionUpdateValidate(sysPermission,requestData)){
            sysPermissionService.addSysPermission(sysPermission);
        }
        return requestData.response();
    }

    @Log(type = Log.LOG_TYPE.UPDATE,text="修改权限")
    @RequestMapping(value = "updateSysPermission",method = RequestMethod.POST)
    public Map updateSysPermission(SysPermission sysPermission,HttpRequestData requestData){
        ValidationUtils.validate(sysPermission);
        if(sysPermissionService.permissionUpdateValidate(sysPermission,requestData)){
            sysPermissionService.updateSysPermission(sysPermission);
        }
        return requestData.response();
    }

    @RequestMapping(value = "removeSysPermission",method = RequestMethod.POST)
    public Map removeSysPermission(HttpRequestData requestData){

        if(!requestData.hasMustParam("permissionId,removeType")){
             requestData.createErrorResponse("缺少必要参数", SysTips.PARAM_ERROR);
        }else{
            sysPermissionService.removeSysPermission(requestData);
        }
        return requestData.response();
    }



}
