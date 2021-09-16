package com.superhao.base.chat.controller;

import java.util.Arrays;
import java.util.Map;


import com.superhao.app.entity.AppRobotRoom;
import com.superhao.app.service.IAppRobotRoomService;
import com.superhao.base.annotation.Log;
import com.superhao.base.authz.entity.SysPermission;
import com.superhao.base.common.controller.BaseController;
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
 * @date 2019-11-26 15:03:27
 *
 */
@RestController
@RequestMapping("sys/roomRobot")
public class AppRobotRoomController extends BaseController {
    @Autowired
    private IAppRobotRoomService appRobotRoomService;


    @Log(type = Log.LOG_TYPE.ADD,text="增加房间机器人")
    @RequestMapping(value = "addRobot",method = RequestMethod.POST)
    public Map addRobot(AppRobotRoom appRobotRoom, HttpRequestData requestData){
        if(appRobotRoomService.robotAttributeValidate(appRobotRoom,requestData)){
            appRobotRoomService.addRobot(appRobotRoom,requestData);
        }
        return requestData.response();
    }
    @Log(type = Log.LOG_TYPE.UPDATE,text="修改房间机器人")
    @RequestMapping(value = "updateRobot",method = RequestMethod.POST)
    public Map updateRobot(AppRobotRoom appRobotRoom, HttpRequestData requestData){
        if(appRobotRoomService.robotAttributeValidate(appRobotRoom,requestData)){
            appRobotRoomService.updateRobot(appRobotRoom,requestData);
        }
        return requestData.response();
    }

    @RequestMapping(value = "searchRobot",method = RequestMethod.POST)
    public Map updateRobot(HttpRequestData requestData){
            appRobotRoomService.searchRobot(requestData);
        return requestData.response();
    }
    @RequestMapping(value = "removeRobot",method = RequestMethod.POST)
    public Map removeRobot(HttpRequestData requestData){
        appRobotRoomService.removeRobot(requestData);
        return requestData.response();
    }
    @RequestMapping(value = "searchRobotByPage",method = RequestMethod.POST)
    public Map searchRobotByPage(HttpRequestData requestData){
        if(requestData.hasPageParam()){
            appRobotRoomService.searchRobotByPage(requestData);
        }else{
            requestData.createErrorResponse("分页参数缺失", SysTips.PARAM_ERROR);
        }
        return requestData.response();
    }

}
