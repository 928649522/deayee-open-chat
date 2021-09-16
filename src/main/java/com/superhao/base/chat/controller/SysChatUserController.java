package com.superhao.base.chat.controller;

import com.superhao.app.service.IAppActivityIntegralService;
import com.superhao.app.service.IAppUserService;
import com.superhao.base.annotation.Log;
import com.superhao.base.authz.handler.AuthCoreHandler;
import com.superhao.base.authz.service.impl.SysPermissionServiceImpl;
import com.superhao.base.cache.util.SysAuthzUtil;
import com.superhao.base.common.constant.SysServiceConfigSet;
import com.superhao.base.common.controller.BaseController;
import com.superhao.base.common.dto.SysTips;
import com.superhao.base.config.filter.AuthzFilter;
import com.superhao.base.entity.HttpRequestData;
import com.superhao.base.entity.token.SysUserToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * @Auther: super
 * @Date: 2019/11/24 21:38
 * @email:
 */
@RestController
@RequestMapping("sys/chatUser")
public class SysChatUserController extends BaseController {

    @Autowired
    private IAppUserService appUserService;

    @Autowired
    private IAppActivityIntegralService appActivityIntegralService;



    @Log(type = Log.LOG_TYPE.SELECT,text="查询App用户")
    @RequestMapping(value = "searchChatUserByPage",method = RequestMethod.POST)
    public Map searchChatUserByPage(HttpRequestData requestData){
        if(requestData.hasPageParam()){
            appUserService.searchChatUserByPage(requestData);
        }else{
            requestData.createErrorResponse("分页参数缺失", SysTips.PARAM_ERROR);
        }
        return requestData.response();
    }
    @Log(type = Log.LOG_TYPE.SELECT,text="查询App用户")
    @RequestMapping(value = "searchChatUserNoneAccountByPage",method = RequestMethod.POST)
    public Map searchChatUserNoneAccountByPage(HttpRequestData requestData){
        if(requestData.hasPageParam()){
            appUserService.searchChatUserNoneAccountByPage(requestData);
        }else{
            requestData.createErrorResponse("分页参数缺失", SysTips.PARAM_ERROR);
        }
        return requestData.response();
    }
    @Autowired
    private AuthCoreHandler authCoreHandler;
    @Autowired
    private SysServiceConfigSet sysServiceConfigSet;
    @Autowired
    private SysPermissionServiceImpl permissionService;
    @RequestMapping(value = "tablePermission",method = RequestMethod.POST)
    public Map tablePermission(HttpRequestData requestData){
        SysUserToken token = SysAuthzUtil.currentSysUser();
        if(authCoreHandler.authenticate("/sys/chatUser/searchChatUserByPage",token)||permissionService.isRootUser()){
            requestData.fillResponseData("/sys/chatUser/searchChatUserByPage");
        }else if(authCoreHandler.authenticate("/sys/chatUser/searchChatUserNoneAccountByPage", token)){
            requestData.fillResponseData("/sys/chatUser/searchChatUserNoneAccountByPage");
        }
        else{

        }

        return requestData.response();
    }



    @Log(type = Log.LOG_TYPE.SELECT,text="后台为App用户积分充值")
    @RequestMapping(value = "rechargeIntegral",method = RequestMethod.POST)
    public Map rechargeIntegral(HttpRequestData requestData){

            appUserService.rechargeIntegral(requestData);

        return requestData.response();
    }

    /**
     *
     * @param requestData
     * @return
     */
    @Log(type = Log.LOG_TYPE.SELECT,text="查询App用户积分")
    @RequestMapping(value = "searchChatUserIntegralByPage",method = RequestMethod.POST)
    public Map searchChatUserIntegralByPage(HttpRequestData requestData){
        if(requestData.hasPageParam()){
            appActivityIntegralService.searchChatUserIntegralByPage(requestData);
        }else{
            requestData.createErrorResponse("分页参数缺失", SysTips.PARAM_ERROR);
        }
        return requestData.response();
    }

    /**
     * 处理秘钥
     * @param requestData
     * @return
     */
    @Log(type = Log.LOG_TYPE.UPDATE,text="")
    @RequestMapping(value = "handleSecret",method = RequestMethod.POST)
    public Map handleSecret(HttpRequestData requestData){
        appActivityIntegralService.handleSecret(requestData);
        return requestData.response();
    }

    /**
     * 撤回充值
     * @param requestData
     * @return
     */
    @Log(type = Log.LOG_TYPE.SELECT,text="")
    @RequestMapping(value = "withdrawRecharge",method = RequestMethod.POST)
    public Map withdrawRecharge(HttpRequestData requestData){
        appActivityIntegralService.withdrawRecharge(requestData);
        return requestData.response();
    }


    /**
     * 注册邀请码
     */
    @RequestMapping(value = "reigisterCode",method = RequestMethod.POST)
    public Map reigisterCode(HttpRequestData requestData){
        appActivityIntegralService.generateReigisterCode(requestData);
        return requestData.response();
    }





}
