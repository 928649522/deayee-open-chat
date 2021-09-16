package com.superhao.app.service;

import com.superhao.app.entity.AppRobotRoom;
import com.superhao.app.entity.AppRoom;
import com.superhao.app.entity.token.AppUserToken;
import com.superhao.base.common.service.IBaseService;
import com.superhao.base.entity.HttpRequestData;

/**
 * 
 *
 * @author superhao
 * @email 928649522@qq.com
 * @date 2019-11-26 15:03:27
 */
public interface IAppRobotRoomService extends IBaseService<AppRobotRoom> {

    boolean robotAttributeValidate(AppRobotRoom appRobotRoom, HttpRequestData requestData);

    void addRobot(AppRobotRoom appRobotRoom, HttpRequestData requestData);

    void updateRobot(AppRobotRoom appRobotRoom, HttpRequestData requestData);

    void searchRobot(HttpRequestData requestData);

    void searchRobotByPage(HttpRequestData requestData);

    void removeRobot(HttpRequestData requestData);

    void handleChat(AppRoom room, AppUserToken robotToken, String msg);

    void initAllRobot();
}

