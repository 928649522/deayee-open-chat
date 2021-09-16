package com.superhao.base.authz.controller;

import com.superhao.base.annotation.Log;
import com.superhao.base.authz.entity.SysUser;
import com.superhao.base.authz.service.ISysUserRoleService;
import com.superhao.base.authz.service.ISysUserService;
import com.superhao.base.cache.util.SysAuthzUtil;
import com.superhao.base.common.controller.BaseController;
import com.superhao.base.common.dto.SysTips;
import com.superhao.base.entity.HttpRequestData;
import com.superhao.base.util.ValidationUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;


/**
 * 
 *
 * @author superhao
 * @email 928649522@qq.com
 * @date 2019-04-22 14:52:41
 */
@RestController
@RequestMapping("sys/sysUser")
public class SysUserController extends BaseController {
    @Autowired
    private ISysUserService sysUserService;

    @Autowired
    private ISysUserRoleService sysUserRoleService;


    @RequestMapping("/updateSysUserDetail")
    public Map updateSysUserDetail(SysUser sysUser,HttpRequestData requestData) {

            sysUserService.updateSysUserDetail(sysUser,requestData);
        return requestData.response();
    }


    @Log(text = "后台登陆",type = Log.LOG_TYPE.OTHER)
    @RequestMapping(value = "/login",method = RequestMethod.POST)
    public Map login(SysUser sysUser,HttpRequestData requestData){
        sysUserService.login(sysUser,requestData);
        return requestData.response();
    }
    @Log(text = "注销登录",type = Log.LOG_TYPE.OTHER)
    @RequestMapping(value = "/logout")
    public void logout(HttpServletResponse response) throws IOException {
        SysAuthzUtil.logout();
        response.sendRedirect("/login.page");
    }

    @Log(type = Log.LOG_TYPE.SELECT,text="查询系统用户")
    @RequestMapping(value = "searchSysUser",method = RequestMethod.POST)
    public Map searchSysUser(SysUser sysUser,HttpRequestData requestData){
        sysUserService.searchSysUser(sysUser,requestData);
        return requestData.response();
    }

    @Log(type = Log.LOG_TYPE.SELECT,text="分页查询系统用户")
    @RequestMapping(value = "searchSysUserByPage",method = RequestMethod.POST)
    public Map searchSysUserByPage(HttpRequestData requestData){
        if(requestData.hasPageParam()){
            sysUserService.searchSysUserByPage(requestData);
        }else{
            requestData.createErrorResponse("分页参数缺失", SysTips.PARAM_ERROR);
        }
        return requestData.response();
    }



    @Log(type = Log.LOG_TYPE.UPDATE,text="修改系统用户")
    @RequestMapping(value = "updateSysUser",method = RequestMethod.POST)
    public Map updateSysUser(SysUser sysUser, HttpRequestData requestData){
        ValidationUtils.validate(sysUser);
        if(sysUserService.sysUserUpdateValidate(sysUser,requestData,true)){
            sysUserService.updateSysUser(sysUser);
        }
        return requestData.response();
    }

    @Log(type = Log.LOG_TYPE.ADD,text="添加系统用户")
    @RequestMapping(value = "addSysUser",method = RequestMethod.POST)
    public Map addSysUser(SysUser sysUser, HttpRequestData requestData){
        ValidationUtils.validate(sysUser);
        if(sysUserService.sysUserUpdateValidate(sysUser,requestData, false)){
            sysUserService.addSysUser(sysUser,requestData);
        }
        return requestData.response();
    }
    @Log(type = Log.LOG_TYPE.ADD,text="删除系统用户")
    @RequestMapping(value = "removeSysUser",method = RequestMethod.POST)
    public Map removeSysUser(HttpRequestData requestData){
        if(requestData.hasMustParam("userId,removeType")){
            sysUserService.logicRemoveSysUser(requestData);
        }else{
            requestData.createErrorResponse("缺少必要参数", SysTips.PARAM_ERROR);
        }
        return requestData.response();
    }

    @Log(type = Log.LOG_TYPE.ADD,text="查询系统用户担任的角色")
    @RequestMapping(value = "searchSysUserRole",method = RequestMethod.POST)
    public Map searchSysUserRole(HttpRequestData requestData){
            sysUserRoleService.searchSysUserRole(requestData);
        return requestData.response();
    }






}
