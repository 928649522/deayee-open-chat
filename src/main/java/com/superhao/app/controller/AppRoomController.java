package com.superhao.app.controller;

import java.util.Date;
import java.util.Map;


import com.sun.deploy.association.utility.AppConstants;
import com.superhao.app.constant.AppChatConstant;
import com.superhao.app.entity.AppDisreport;
import com.superhao.app.entity.AppRedPoint;
import com.superhao.app.entity.dto.ChatData;
import com.superhao.app.service.IAppChatMessageService;
import com.superhao.app.service.IAppRoomService;
import com.superhao.base.cache.util.AppChatCacheUtil;
import com.superhao.base.entity.HttpRequestData;
import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.stereotype.Controller;



/**
 *
 *
 *
 * @author
 * @email
 * @date 2019-10-22 14:52:29
 *
 */
@RestController
@RequestMapping("/app/room")
public class AppRoomController extends AppBaseController {
    @Autowired
    private IAppRoomService appRoomService;

    @Autowired
    private IAppChatMessageService appChatMessageService;

    @RequestMapping(value = "/send",method = RequestMethod.POST)
    public Map send(ChatData data, HttpRequestData requestData){
        data.setCreationTime(new Date());
        appChatMessageService.sendDataToRoom(data,requestData);
        return requestData.response();
    }

    /**
     * 举报
     * @param requestData
     * @return
     */
    @RequestMapping(value = "/disreport",method = RequestMethod.POST)
    public Map disReport(AppDisreport appDisreport, HttpRequestData requestData){
        appRoomService.disReport(appDisreport,requestData);
        return requestData.response();
    }

    /**
     * 积分红包
     * @param appDisreport
     * @param requestData
     * @return
     */
    @RequestMapping(value = "/sendRedPoint",method = RequestMethod.POST)
    public Map sendRedPoint(AppRedPoint appDisreport, HttpRequestData requestData){
        appRoomService.sendRedPoint(appDisreport,requestData);
        return requestData.response();
    }
    /**
     * 抢红包
     * @param appDisreport
     * @param requestData
     * @return
     */
    @RequestMapping(value = "/getRedPoint",method = RequestMethod.POST)
    public Map getRedPoint( HttpRequestData requestData){
        appRoomService.getRedPoint(requestData);
        return requestData.response();
    }

    /**
     * 发起群聊
     * @param requestData
     * @return
     */
    @RequestMapping(value = "/createChatGroup",method = RequestMethod.POST)
    public Map createChatGroup( HttpRequestData requestData){
        appRoomService.createChatGroup(requestData);
        return requestData.response();
    }

    /**
     * 分享群
     * @param requestData
     * @return
     */
    @RequestMapping(value = "/shareChatGroup",method = RequestMethod.POST)
    public Map shareChatGroup( HttpRequestData requestData){
        appRoomService.shareChatGroup(requestData);
        return requestData.response();
    }

    /**
     * 禁言
     * @param appUser
     * @param requestData
     * @return
     */
    @RequestMapping(value = "/disableSay",method = RequestMethod.POST)
    public Map disableChat(HttpRequestData requestData){
        appRoomService.disableSay(requestData);
        return requestData.response();
    }

    /**
     * 禁言所有人
     * @param requestData
     * @return
     */
    @RequestMapping(value = "/disableAllSay",method = RequestMethod.POST)
    public Map disableAllSay( HttpRequestData requestData){
        appRoomService.disableAllSay(requestData);
        return requestData.response();
    }

    /**
     * 消息撤回
     * @param requestData
     * @return
     */
    @RequestMapping(value = "/msgWithdraw",method = RequestMethod.POST)
    public Map msgWithdraw( HttpRequestData requestData){
        appRoomService.msgWithdraw(requestData);
        return requestData.response();
    }

    /**
     * 根据用户Id roomId 撤回全部
     * @param requestData
     * @return
     */
    @RequestMapping(value = "/msgWithdrawAll",method = RequestMethod.POST)
    public Map msgWithdrawAll( HttpRequestData requestData){
        appRoomService.msgWithdrawAll(requestData);
        return requestData.response();
    }
    /**
     * 踢除成员
     * @param requestData
     * @return
     */
    @RequestMapping(value = "/disablePeole",method = RequestMethod.POST)
    public Map disablePeole( HttpRequestData requestData){
        appRoomService.disablePeole(requestData);
        return requestData.response();
    }
    @RequestMapping(value = "/getMaster",method = RequestMethod.POST)
    public Map getMaster( HttpRequestData requestData){
        appRoomService.getMaster(requestData);
        return requestData.response();
    }

    /**
     * 群组广告
     * @param requestData
     * @return
     */
    @RequestMapping(value = "/groupad",method = RequestMethod.POST)
    public Map groupad(HttpRequestData requestData){

        String roomId = requestData.getString("roomId");
        if(!StringUtils.isEmpty(roomId)){
            requestData.fillResponseData(AppChatCacheUtil.getRoom(roomId).getAppRoom().getAd());
        }
        return requestData.response();
    }


}
