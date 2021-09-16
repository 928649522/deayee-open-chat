package com.superhao.base.chat.controller;

import com.superhao.app.controller.AppBaseController;
import com.superhao.app.entity.AppRoom;
import com.superhao.app.service.IAppRoomService;
import com.superhao.app.util.ChatResponseDataUtil;
import com.superhao.base.annotation.Log;
import com.superhao.base.common.constant.SysServiceConfigSet;
import com.superhao.base.common.controller.BaseController;
import com.superhao.base.common.dto.SysTips;
import com.superhao.base.entity.HttpRequestData;
import com.superhao.base.util.UUIDUtils;
import com.superhao.base.util.ValidationUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * @Auther: super
 * @Date: 2019/10/24 22:11
 * @email:
 */
@RestController
@RequestMapping("sys/sysRoom")
public class SysRoomController extends BaseController {
    @Autowired
    private IAppRoomService appRoomService;

    @Autowired
    private SysServiceConfigSet serviceConfigSet;


    @RequestMapping(value = "searchRoomHttpPath",method = RequestMethod.POST)
    public Map searchRoomHttpPath(HttpRequestData requestData){
        appRoomService.searchRoomHttpPath(requestData);
        return requestData.response();
    }
    @RequestMapping(value = "updateQrcodeExpire",method = RequestMethod.POST)
    public Map updateQrcodeExpire(HttpRequestData requestData){
        appRoomService.updateQrcodeExpire(requestData);
        return requestData.response();
    }



    @Log(type = Log.LOG_TYPE.SELECT,text="分页查询聊天室")
    @RequestMapping(value = "searchSysRoomByPage",method = RequestMethod.POST)
    public Map searchSysRoomByPage(HttpRequestData requestData){
        if(requestData.hasPageParam()){
            appRoomService.searchSysRoomByPage(requestData);
        }else{
            requestData.createErrorResponse("分页参数缺失", SysTips.PARAM_ERROR);
        }
        return requestData.response();
    }





    @Log(type = Log.LOG_TYPE.SELECT,text="分页查询聊天室")
    @RequestMapping(value = "searchSysRoom",method = RequestMethod.POST)
    public Map searchSysRoom(HttpRequestData requestData){
       appRoomService.searchSysRoom(requestData);
        return requestData.response();
    }


    /**
     * 获取聊天室用户信息和消息记录
     * @param requestData
     * @return
     */
    @RequestMapping(value = "searchChatRoomUser",method = RequestMethod.POST)
    public Map searchChatRoomUser(HttpRequestData requestData){
            appRoomService.searchChatRoomParam(requestData);
        return requestData.response();
    }


    @RequestMapping(value = "updateChatRoomUserRole",method = RequestMethod.POST)
    public Map updateChatRoomUserRole(HttpRequestData requestData){
        appRoomService.updateChatRoomUserRole(requestData);
        return requestData.response();
    }


    @Log(type = Log.LOG_TYPE.SELECT,text="新增群组")
    @RequestMapping(value = "addChatRoom",method = RequestMethod.POST)
    public Map addChatRoom(AppRoom appRoom,HttpRequestData requestData){
        //ValidationUtils.validate(appRoom);
        if(appRoomService.sysRoomUpdateValidate(appRoom,requestData, false)){
            appRoomService.addSysRoom(appRoom,requestData);
        }
        return requestData.response();
    }

    @Log(type = Log.LOG_TYPE.SELECT,text="更新群组")
    @RequestMapping(value = "updateChatRoom",method = RequestMethod.POST)
    public Map updateChatRoom(AppRoom appRoom,HttpRequestData requestData){
        if(appRoomService.sysRoomUpdateValidate(appRoom,requestData, true)){
            appRoomService.updateSysRoom(appRoom,requestData);
        }
        return requestData.response();
    }
    @Log(type = Log.LOG_TYPE.OTHER,text="删除群组")
    @RequestMapping(value = "removeSysRoom",method = RequestMethod.POST)
    public Map removeSysRoom(HttpRequestData requestData){
            appRoomService.removeSysRoom(requestData);
        return requestData.response();
    }

    /**
     * 黑名单
     * @param requestData
     * @return
     */
    @RequestMapping(value = "groupBlacklistTable",method = RequestMethod.POST)
    public Map groupBlacklistTable(HttpRequestData requestData){
         appRoomService.groupBlacklistTable(requestData);
        return requestData.response();
    }
    /**
     * 解除黑名单
     * @param requestData
     * @return
     */
    @RequestMapping(value = "relieveBlacklist",method = RequestMethod.POST)
    public Map relieveBlacklist(HttpRequestData requestData){
        appRoomService.relieveBlacklist(requestData);
        return requestData.response();
    }



}

