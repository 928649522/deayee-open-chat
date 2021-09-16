package com.superhao.app.controller;

import com.superhao.app.constant.AppChatConstant;
import com.superhao.app.entity.AppUser;
import com.superhao.app.service.IAppActivityIntegralService;
import com.superhao.app.service.IAppUserService;
import com.superhao.base.annotation.Log;
import com.superhao.base.cache.util.AppGroupQRCodeCacheUtil;
import com.superhao.base.common.constant.SysServiceConfigSet;
import com.superhao.base.common.controller.BaseController;
import com.superhao.base.entity.HttpRequestData;
import com.superhao.base.util.MybatisUtil;
import com.superhao.base.util.ValidationUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

/**
 * @Auther: super
 * @Date: 2019/10/16 18:05
 * @email:
 */
@RestController
@RequestMapping("app/user")
public class AppUserController  extends AppBaseController {

    @Resource(name = "appUserService")
    private IAppUserService appUserService;


    @Autowired
    IAppActivityIntegralService appActivityIntegralService;

    @Autowired
    SysServiceConfigSet serviceConfigSet;


    //下发验证码
    //登陆
    //注册

/* 获取验证码图片*/

/*    @RequestMapping(value = "/getVerifyCode",method = RequestMethod.GET)
    public void getVerificationCode(String jsonData,HttpServletResponse response) {
        Map params = super.encodeJsonToObj(jsonData, HashMap.class);
        appUserService.getVerificationCode(response, params);
    }*/

    @RequestMapping(value = "/register",method = RequestMethod.POST)
    public Map register(String jsonData,HttpRequestData requestData) {
        AppUser appUser = super.encodeJsonToObj(jsonData,AppUser.class);
        ValidationUtils.validate(appUser);
        if(appUserService.registerParamValidate(appUser,requestData)){
            appUserService.register(appUser,requestData);
        }
        return requestData.response();
    }

    @RequestMapping(value = "/modifyPwd",method = RequestMethod.POST)
    public Map modifyPwd(HttpRequestData requestData) {
        appUserService.modifyPwd(requestData);
        return requestData.response();
    }



    /**
     * 匿名或实名登陆
     * @param requestData
     * @return
     */
    @Log(text = "app登陆",type = Log.LOG_TYPE.OTHER)
    @RequestMapping(value = "/login",method = RequestMethod.POST)
    public Map login(String jsonData, HttpRequestData requestData){
        String type = requestData.getString("type");
        if(AppChatConstant.LOGIN_TYPE_SM.equals(type)){
            AppUser appUser = super.encodeJsonToObj(jsonData,AppUser.class);
            appUserService.realLogin(appUser,requestData);
        }else if(AppChatConstant.LOGIN_TYPE_NM.equals(type)){
            Map param = super.encodeJsonToObj(jsonData,HashMap.class);
            appUserService.anonymousLogin(param,requestData);
        }
        return requestData.response();
    }

    @RequestMapping(value = "/searchUserGroupChat",method = RequestMethod.POST)
    public Map searchUserGroupChat(HttpRequestData requestData){
            appUserService. searchUserGroupChat(requestData);
        return requestData.response();
    }

    /**
     * 分页查找群组历史记录
     * @param requestData
     * @return
     */
    @RequestMapping(value = "/searchGroupRecordByPage",method = RequestMethod.POST)
    public Map searchGroupRecordByPage(HttpRequestData requestData){
        appUserService.searchGroupRecordByPage(requestData);
        return requestData.response();
    }
    @RequestMapping(value = "/searchUserCallList",method = RequestMethod.POST)
    public Map searchUserCallList(HttpRequestData requestData){
        appUserService.searchUserCallList(requestData);
        return requestData.response();
    }

    /**
     * 初始化聊天
     * @param requestData
     * @return
     */
    @RequestMapping(value = "/searchUserContact",method = RequestMethod.POST)
    public Map searchUserContact(HttpRequestData requestData){
        appUserService.searchUserContact(requestData);
        return requestData.response();
    }

    @RequestMapping(value = "/info",method = RequestMethod.POST)
    public Map info(HttpRequestData requestData){
        appUserService.info(requestData);
        return requestData.response();
    }

    /**
     * 修改备注
     * @param requestData
     * @return
     */
    @RequestMapping(value = "/modifyRemark",method = RequestMethod.POST)
    public Map modify(HttpRequestData requestData){
        appUserService.modifyRemark(requestData);
        return requestData.response();
    }

    @RequestMapping(value = "/searchFirend",method = RequestMethod.POST)
    public Map searchFirend(HttpRequestData requestData){
        appUserService.searchFirend(requestData);
        return requestData.response();
    }
    @RequestMapping(value = "/addFirend",method = RequestMethod.POST)
    public Map addFirend(HttpRequestData requestData){
        appUserService.addFirend(requestData);
        return requestData.response();
    }

    /**
     * 同意添加好友
     * @param requestData
     * @return
     */
    @RequestMapping(value = "/agreeFriend",method = RequestMethod.POST)
    public Map agreeFriend(HttpRequestData requestData){
        appUserService.agreeFriend(requestData);
        return requestData.response();
    }

    /**
     * 删除好友
     * @param requestData
     * @return
     */
    @RequestMapping(value = "/removeFriend",method = RequestMethod.POST)
    public Map removeFriend(HttpRequestData requestData){
        appUserService.removeFriend(requestData);
        return requestData.response();
    }
    /**
     * 退出群组
     * @param requestData
     * @return
     */
    @RequestMapping(value = "/removeGroup",method = RequestMethod.POST)
    public Map removeGroup(HttpRequestData requestData){
        appUserService.removeGroup(requestData);
        return requestData.response();
    }


    /**
     * 删除好友
     * @param requestData
     * @return
     */
    @RequestMapping(value = "/modifyDetail",method = RequestMethod.POST)
    public Map modifyDetail(AppUser appUser,HttpRequestData requestData){
        appUserService.modifyDetail(appUser,requestData);
        return requestData.response();
    }

    /**
     * delete chatmsg
     * @param appUser
     * @param requestData
     * @return
     */
    @RequestMapping(value = "/removeChatMsg",method = RequestMethod.POST)
    public Map removeChatMsg(HttpRequestData requestData){
        appUserService.removeChatMsg(requestData);
        return requestData.response();
    }

    @RequestMapping(value = "/count",method = RequestMethod.POST)
    public Map count(HttpRequestData requestData){

        appUserService.redirectLogic(requestData);

        return requestData.response();
    }



    /**
     * 积分记录
     */
    @RequestMapping(value = "/integralHistory",method = RequestMethod.POST)
    public Map integralHistory(HttpRequestData requestData){
        appActivityIntegralService.integralHistory(requestData);
        return requestData.response();
    }

    /**
     * 积分兑换
     */
    @RequestMapping(value = "/redeemIntegral",method = RequestMethod.POST)
    public Map redeemIntegral(HttpRequestData requestData){
        appActivityIntegralService.redeemIntegral(requestData);
        return requestData.response();
    }






}
