package com.superhao.base.init;

import com.superhao.app.service.IAppRobotRoomService;
import com.superhao.base.cache.util.AppRoomRobotCacheUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @Auther: super
 * @Date: 2019/11/26 23:22
 * @email:
 */

@Component
public class ChatRoomRobotInit implements ApplicationListener<ContextRefreshedEvent> {

    @Autowired
    private IAppRobotRoomService appRobotRoomService;

    private final String ROBOT_DICTIONARY_LOCK = "ROBOT_DICTIONARY";

    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
        try {
            if( AppRoomRobotCacheUtil.opsRoomRobotLock(ROBOT_DICTIONARY_LOCK,1*1000)){
                int rows =  appRobotRoomService.selectCount(null);
                if(rows != AppRoomRobotCacheUtil.getRoomRobotCount()){
                    AppRoomRobotCacheUtil.initRobotUnLock();
                }
                if(AppRoomRobotCacheUtil.initRobotLock()){
                    appRobotRoomService.initAllRobot();
                }
                AppRoomRobotCacheUtil.opsRoomRobotLock(ROBOT_DICTIONARY_LOCK);
            }
        }catch (Exception e){
            AppRoomRobotCacheUtil.initRobotUnLock();
            AppRoomRobotCacheUtil.opsRoomRobotLock(ROBOT_DICTIONARY_LOCK);
        }


    }
}
