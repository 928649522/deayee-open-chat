package com.superhao.app.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.superhao.app.constant.AppChatConstant;
import com.superhao.app.entity.*;
import com.superhao.app.entity.dto.ChatData;
import com.superhao.app.entity.token.AppUserToken;
import com.superhao.app.handle.RoomChatHandle;
import com.superhao.app.handle.RoomChatHelper;
import com.superhao.app.handle.listener.RoomMessageListener;
import com.superhao.app.mapper.*;
import com.superhao.app.service.IAppChatMessageService;
import com.superhao.app.service.IAppRobotRoomService;
import com.superhao.app.service.IAppRoomService;
import com.superhao.base.cache.util.AppChatCacheUtil;
import com.superhao.base.cache.util.AppRoomRobotCacheUtil;
import com.superhao.base.cache.util.AppTokenCacheUtil;
import com.superhao.base.common.constant.SysConstantSet;
import com.superhao.base.common.constant.SysServiceConfigSet;
import com.superhao.base.common.dto.SysTips;
import com.superhao.base.common.mapper.SysFileMapper;
import com.superhao.base.common.service.ISysFileService;
import com.superhao.base.common.service.impl.BaseServiceImpl;
import com.superhao.base.entity.HttpRequestData;
import com.superhao.base.entity.Pages;
import com.superhao.base.log.util.SysLogRecordUtil;
import com.superhao.base.util.Md5Util;
import com.superhao.base.util.MybatisUtil;
import com.superhao.base.util.UUIDUtils;
import javafx.beans.binding.BooleanExpression;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.util.*;


@Service("appRobotRoomService")
public class AppRobotRoomServiceImpl extends BaseServiceImpl<AppRobotRoomMapper, AppRobotRoom> implements IAppRobotRoomService {

    @Autowired
    private AppRobotRoomMapper appRobotRoomMapper;

    @Autowired
    private AppRoomMapper appRoomMapper;

    @Autowired
    private AppUserMapper appUserMapper;
    @Autowired
    private RoomChatHelper roomChatHelper;
    @Autowired
    private SysServiceConfigSet serviceConfigSet;

    @Override
    public boolean robotAttributeValidate(AppRobotRoom appRobotRoom, HttpRequestData requestData) {
        if(appRobotRoom.getRoomId()==null){
            requestData.createErrorResponse(SysTips.PARAM_ERROR);
            return false;
        }
        if(appRoomMapper.selectCount(MybatisUtil.conditionT().eq("room_id",appRobotRoom.getRoomId()))==0){
            requestData.createErrorResponse("RoomID不存在");
            return false;
        }
        return true;
    }

    @Autowired
    private AppRoomRoleMapper appRoomRoleMapper;

    @Transactional
    @Override
    public void addRobot(AppRobotRoom appRobotRoom, HttpRequestData requestData) {

        AppUser robot = new AppUser();
        super.insertDefaultVal(appRobotRoom);
        BeanUtils.copyProperties(appRobotRoom,appRobotRoom);
        String userName = UUIDUtils.generateShortUuid();
        while(appUserMapper.selectCount(MybatisUtil.conditionT().eq("user_name",userName))!=0){
            userName = UUIDUtils.generateShortUuid();
        }

        robot.setPassword(Md5Util.getMD5(UUIDUtils.generateShortUuid()));
        robot.setUserName(userName);
        robot.setUserType(AppChatConstant.USER_TYPE_3);
        BeanUtils.copyProperties(appRobotRoom,robot);
        appUserMapper.insert(robot);
        appRobotRoom.setRobotId(robot.getUserId());
        appRobotRoomMapper.insert(appRobotRoom);

        //设置群组角色
        AppRoomRole role = new AppRoomRole();
        super.insertDefaultValForApp(role,false);
        role.setUserNickname(appRobotRoom.getNickName());
        role.setRoleType(AppRoomRole.TYPE_PEOPLE);
        role.setUserId(appRobotRoom.getRobotId());
        role.setCreator(appRobotRoom.getRobotId());
        role.setRoomId(appRobotRoom.getRoomId());
        appRoomRoleMapper.insert(role);




        this.initRobot(appRobotRoom);
/*        Map condition = new HashMap();
        condition.put("httpPath",serviceConfigSet.getHttpServerPath());
        condition.put("userId",appRobotRoom.getRobotId());
         robot = appUserMapper.selectByIdJoinFile(condition);

        //加入房间 设置 AppToken ROOMAttribute  机器人

        AppRoomRobotCacheUtil.pushRoomRobot(appRobotRoom.getRobotId().toString(),appRobotRoom);
        AppUserToken robotToken =AppUserToken.create(robot);
        AppTokenCacheUtil.pushRobot(robotToken);

        RoomChatHandle roomChatHandle = new RoomChatHandle();
        AppRoom attribute = appRoomMapper.selectById(appRobotRoom.getRoomId());
        attribute.setRoomType(AppChatConstant.SOCKET_TYPE_GROUP);
        roomChatHandle.setUserToken(robotToken);
        roomChatHandle.setAttribute(attribute);

        roomChatHelper.joinAppChatRoom(attribute,roomChatHandle);

        RoomMessageListener messageListener =  new RoomMessageListener(roomChatHandle);
        roomChatHelper.addUserMsgListenner(messageListener,appRobotRoom.getRoomId().toString());
        //通知群员，该用户上线
        roomChatHelper.noticeThisUserOnline(roomChatHandle);*/
        requestData.fillResponseData(appRobotRoom);
    }

    private void initRobot(AppRobotRoom appRobotRoom){
        Map condition = new HashMap();
        condition.put("httpPath",serviceConfigSet.getHttpServerPath());
        condition.put("userId",appRobotRoom.getRobotId());
        AppUser robot = appUserMapper.selectByIdJoinFile(condition);

        //加入房间 设置 AppToken ROOMAttribute  机器人

        AppRoomRobotCacheUtil.pushRoomRobot(appRobotRoom.getRobotId().toString(),appRobotRoom);
        AppUserToken robotToken =AppUserToken.create(robot);
        AppTokenCacheUtil.pushRobot(robotToken);

        RoomChatHandle roomChatHandle = new RoomChatHandle();
        AppRoom attribute = appRoomMapper.selectById(appRobotRoom.getRoomId());
        attribute.setRoomType(AppChatConstant.SOCKET_TYPE_GROUP);
        roomChatHandle.setUserToken(robotToken);
        roomChatHandle.setAttribute(attribute);

        roomChatHelper.joinAppChatRoom(attribute,roomChatHandle);

        RoomMessageListener messageListener =  new RoomMessageListener(roomChatHandle);
        roomChatHelper.addUserMsgListenner(messageListener,appRobotRoom.getRoomId().toString());
        //通知群员，该用户上线
        roomChatHelper.noticeThisUserOnline(roomChatHandle);
    }

    @Override
    public void initAllRobot() {
        List<AppRobotRoom> robotList = appRobotRoomMapper.selectList(null);
        if (robotList.size()>0){
            for (AppRobotRoom robot:robotList){
                this.initRobot(robot);
            }
        }
    }

    @Transactional
    @Override
    public void updateRobot(AppRobotRoom appRobotRoom, HttpRequestData requestData) {
        if(appRobotRoom.getRobotRoomId()==null||appRobotRoom.getRobotId()==null){
            requestData.createErrorResponse(SysTips.PARAM_ERROR);
            return ;
        }
        updateDefaultVal(appRobotRoom);
        AppUser appUser = new AppUser();
        appUser.setUserType(AppChatConstant.USER_TYPE_3);
        BeanUtils.copyProperties(appRobotRoom,appUser);
        appUser.setUserId(appRobotRoom.getRobotId());
        appUserMapper.updateById(appUser);

        appRobotRoomMapper.updateById(appRobotRoom);
        //
        Map condition = new HashMap();
        condition.put("httpPath",serviceConfigSet.getHttpServerPath());
        condition.put("userId",appRobotRoom.getRobotId());
        AppUser robot = appUserMapper.selectByIdJoinFile(condition);

        AppUserToken robotToken =AppTokenCacheUtil.get(robot.getUserId().toString());
        BeanUtils.copyProperties(robot,robotToken);
        AppTokenCacheUtil.pushRobot(robotToken);
        AppRoomRobotCacheUtil.pushRoomRobot(appRobotRoom.getRobotId().toString(),appRobotRoom);

        requestData.fillResponseData(appRobotRoom);
    }



    @Autowired
    private SysFileMapper sysFileMapper;

    @Transactional
    @Override
    public void removeRobot(HttpRequestData requestData) {
        Long robotRoomId = requestData.getLong("robotRoomId");
        if(robotRoomId==null){
            requestData.createErrorResponse(SysTips.PARAM_ERROR);
            return ;
        }
       AppRobotRoom deleteObj =  appRobotRoomMapper.selectById(robotRoomId);
        if(deleteObj!=null){
           // System.out.println("===========>"+AppChatCacheUtil.getRoom(deleteObj.getRoomId().toString()).getSockets().size());
            AppChatCacheUtil.removeRoomSocket(deleteObj.getRoomId().toString(), deleteObj.getRobotId().toString());
          //  System.out.println("===========>"+AppChatCacheUtil.getRoom(deleteObj.getRoomId().toString()).getSockets().size());

            AppTokenCacheUtil.remove(deleteObj.getRobotId().toString());
            AppRoomRobotCacheUtil.removeRoomRobot(deleteObj.getRobotId().toString());

            sysFileMapper.deleteById(deleteObj.getFileId());
            appUserMapper.deleteById(deleteObj.getRobotId());
            appRobotRoomMapper.deleteById(robotRoomId);
            appRoomRoleMapper.delete(MybatisUtil.conditionT()
                    .eq("creator",deleteObj.getRobotId())
                    .and()
                    .eq("room_id",deleteObj.getRoomId()));
        }

    }

    @Override
    public void searchRobot(HttpRequestData requestData) {
        Long robotRoomId = requestData.getLong("robotRoomId");
        if(robotRoomId==null){
            requestData.createErrorResponse(SysTips.PARAM_ERROR);
            return ;
        }
        Map condition = new HashMap();

        condition.put("httpPath",serviceConfigSet.getHttpServerPath());
        condition.put("robotRoomId",robotRoomId);

        AppRobotRoom appRobotRoom = appRobotRoomMapper.selectByIdJoinInfo(condition);
        requestData.fillResponseData(appRobotRoom);
    }


    @Override
    public void searchRobotByPage(HttpRequestData requestData) {
        Wrapper<AppRobotRoom> condition = MybatisUtil.conditionT();
        condition.eq("enable", SysConstantSet.COLUMN_ENABLE_Y);
        String searchText = requestData.getString("searchText");
        if(!StringUtils.isEmpty(searchText)){
            condition.and().eq("room_id",searchText);
        }
        condition.orderBy("creation_time", false);
        Page<AppRobotRoom> page = Pages.create(requestData.getInteger("page"));
        List<AppRobotRoom> data = appRobotRoomMapper.selectPage(page, condition);
        int count = appRobotRoomMapper.selectCount(condition);
        requestData.generatePageData(data, count);
    }



    @Autowired
    private AppChatMessageServiceImpl appChatMessageService;
    private Random random = new Random();
    /**
     * 处理自动聊天
     * @param room
     * @param robotToken
     * @param msg
     */
    @Override
    public void handleChat(AppRoom room, AppUserToken robotToken, String msg) {
        try {


            Map cacheData =  JSONObject.parseObject(msg,HashMap.class);
       // ChatData receiverData = JSONObject.parseObject(cacheData.get("data"),ChatData.class);
        ChatData receiverData= JSONObject.toJavaObject((JSONObject) cacheData.get("data"),ChatData.class);
        AppRobotRoom robotOption = AppRoomRobotCacheUtil.getRoomRobot(robotToken.getUserId().toString());

        AppUserToken senderToken = AppTokenCacheUtil.get(receiverData.getSender());
        //AppUserToken sender = JSONObject.toJavaObject((JSONObject) receiverData.getContent(), AppUserToken.class);

        //自动发消息 回消息  吵群
            if(AppChatConstant.USER_TYPE_3.equals(senderToken.getUserType())){
                if(AppChatConstant.CONSTANT_NO.equals(robotOption.getAutoSay())){
                    return;
                }
            }


            //判断机器人在线时间
        Date nowDate = new Date();
        Date beginDate = robotOption.getBeginWorktime();
        Date endDate = robotOption.getEndWorktime();
        long nowTime = (nowDate.getHours()*3600)+(nowDate.getMinutes()*60)+nowDate.getSeconds();
        long beginTime = (beginDate.getHours()*3600)+(beginDate.getMinutes()*60)+beginDate.getSeconds();
        long endTime= (endDate.getHours()*3600)+(endDate.getMinutes()*60)+endDate.getSeconds();
        if(!(nowTime>=beginTime&&nowTime<=endTime)){
            return;
        }

        ChatData sendData = new ChatData();
        sendData.setUuid(UUIDUtils.generateUuid());
        sendData.setType(AppChatConstant.HANDLE_TYPE_TEXT);
        sendData.setSender(robotToken.getUserId().toString());
        sendData.setReciever(room.getRoomId().toString());
        sendData.setCreationTime(new Date());

        if(AppChatConstant.HANDLE_TYPE_TEXT.equals(receiverData.getType())){

            //发言间歇
           Integer sleep = random.nextInt(robotOption.getSleepTime().intValue()*1000);
           if(sleep<1000){
               sleep+=robotOption.getSleepTime().intValue()/2;
           }
            String res = createRobotMsg(receiverData.getContent().toString(),new Long(robotOption.getDictionaryType()));
           if(StringUtils.isEmpty(res)){
               return;
           }
           res.replace("[cqname]",senderToken.getNickName());
            res.replace("[name]",robotToken.getNickName());
            sendData.setContent(res);
           Thread.sleep(sleep.longValue());
           appChatMessageService.sendDataToRoomByHandleType(room.getRoomId().toString(),sendData,AppChatConstant.HANDLE_TYPE_TEXT,false,true);
        }

        if(AppChatConstant.TYPE_ONLINE.equals(receiverData.getType())) {
            sendData.setContent("欢迎"+senderToken.getNickName()+"~");
            appChatMessageService.sendDataToRoomByHandleType(room.getRoomId().toString(),sendData,AppChatConstant.HANDLE_TYPE_TEXT,false,true);

        }

        appChatMessageService.saveRoomChatRecord(sendData, room.getRoomId().toString(),robotToken.getUserId());

            System.out.println(receiverData.toString());
        System.out.println(msg);
        System.out.println(room.toString());
        System.out.println(robotToken.toString());
        } catch (Exception e) {
            SysLogRecordUtil.record("机器人发言出错",e);
        }
    }

    @Autowired
    private AppRobotWordsMapper appRobotWordsMapper;

    private String createRobotMsg(String senderText,Long dictionaryId){
        if (StringUtils.isEmpty(senderText)||StringUtils.isEmpty(senderText.trim())){
            return null;
        }
        EntityWrapper<AppRobotWords>  condition =  MybatisUtil.conditionT();
        condition.eq("dictionary_id",dictionaryId)
                .and()
                .eq("wkey",senderText);
        int rows = appRobotWordsMapper.selectCount(condition);
        List<AppRobotWords> res=null;
        if(rows>0){
            res = appRobotWordsMapper.selectList(condition);
            if (res.size()>0){
                return res.get(random.nextInt(res.size())).getWvalue();
            }
        }
        String tempText = senderText;
        int begin = 0;
        int cons = 2;
        int loopMaxpCount = 10;
        if(senderText.length()<cons){
            return null;

        }
        condition =  MybatisUtil.conditionT();
        condition.eq("dictionary_id",dictionaryId);

        String like = "";
        int i=0;
        EntityWrapper orQuery = MybatisUtil.conditionT();
        condition.andNew();
        while((begin+=cons)<=senderText.length()&&i<loopMaxpCount){
            like = senderText.substring(begin-cons,begin);
            if(i==0){
                condition.like("wkey",like);
            }else{
                condition.or().like("wkey",like);
            }
            i++;
        }
        //condition.and(orQuery.getSqlSegment());

         rows = appRobotWordsMapper.selectCount(condition);
        if(rows>0){
            int page = rows/Pages.ROW_SIZE;
            if(page>5){
                page = random.nextInt(page);
                if(page==0){
                    page=1;
                }
            }
            res = appRobotWordsMapper.selectPage(Pages.create(page),condition);
            if (res.size()>0){
                return res.get(random.nextInt(res.size())).getWvalue();
            }
        }
        return null;
    }

    public static void main(String[] args) {
         Random random = new Random();
       /*  while (true){
             System.out.println(random.nextInt(3));
         }*/
System.out.println("12".substring(0,2));
    }
}