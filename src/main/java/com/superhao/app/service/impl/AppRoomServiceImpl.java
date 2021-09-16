package com.superhao.app.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.superhao.app.constant.AppChatConstant;
import com.superhao.app.entity.*;
import com.superhao.app.entity.dto.CallListDto;
import com.superhao.app.entity.dto.ChatData;
import com.superhao.app.entity.token.AppUserToken;
import com.superhao.app.handle.RoomChatHelper;
import com.superhao.app.mapper.*;
import com.superhao.app.service.IAppActivityIntegralService;
import com.superhao.app.service.IAppChatRecordService;
import com.superhao.app.service.IAppRoomService;
import com.superhao.base.cache.util.*;
import com.superhao.base.common.constant.SysConstantSet;
import com.superhao.base.common.constant.SysServiceConfigSet;
import com.superhao.base.common.dto.SysTips;
import com.superhao.base.common.service.impl.BaseServiceImpl;
import com.superhao.base.entity.HttpRequestData;
import com.superhao.base.entity.Pages;
import com.superhao.base.entity.token.SysUserToken;
import com.superhao.base.log.util.SysLogRecordUtil;
import com.superhao.base.util.MybatisUtil;
import com.superhao.base.util.UUIDUtils;
import org.apache.commons.lang.StringEscapeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.text.DecimalFormat;
import java.util.*;


@Service("appRoomService")
public class AppRoomServiceImpl extends BaseServiceImpl<AppRoomMapper, AppRoom> implements IAppRoomService, Serializable {

    @Autowired
    private AppRoomMapper appRoomMapper;

    @Autowired
    private AppRoomRoleMapper appRoomRoleMapper;

    @Autowired
    private AppUserMapper appUserMapper;


    @Autowired
    private SysServiceConfigSet serviceConfigSet;

    @Autowired
    private AppChatMessageServiceImpl appChatMessageService;


    @Autowired
    private RoomChatHelper roomChatHelper;

    @Autowired
    private AppDisreportMapper disreportMapper;

    @Autowired
    private IAppActivityIntegralService appActivityIntegralService;
    @Autowired
    private AppRedPointMapper appRedPointMapper;



    @Override
    public void searchSysRoomByPage(HttpRequestData requestData) {
        Wrapper<AppRoom> condition = MybatisUtil.conditionT();
        condition.eq("enable", SysConstantSet.COLUMN_ENABLE_Y);

        if (!StringUtils.isEmpty(requestData.getString("roomName"))) {
            condition.and().like("room_name", requestData.getString("roomName"));

        }
        SysUserToken token =  SysAuthzUtil.currentSysUser();
        if(!serviceConfigSet.getRoot().equals(token.getAccountName())){
            condition.andNew()
                    .eq("creator",token.getUserId())
                    .or()
                    .eq("status","0");
        }

        condition.orderBy("creation_time", false);
        Page<AppRoom> page = Pages.create(requestData.getInteger("page"));
        List<AppRoom> data = appRoomMapper.selectPage(page, condition);
        int count = appRoomMapper.selectCount(condition);
        requestData.generatePageData(data, count);
    }

    @Transactional
    @Override
    public void addSysRoom(AppRoom appRoom, HttpRequestData requestData) {
        this.insertDefaultVal(appRoom);
        appRoom.setRoomCode(UUIDUtils.generateShortUuid());

        appRoom.setDisablePeople("");
        appRoomMapper.insert(appRoom);
        requestData.fillResponseData(appRoom);
    }

    @Override
    public boolean sysRoomUpdateValidate(AppRoom appRoom, HttpRequestData requestData, boolean isUpdate) {
        if (isUpdate) {
            if (StringUtils.isEmpty(appRoom.getRoomId())
                    || StringUtils.isEmpty(appRoom.getRoomName())) {
                requestData.createErrorResponse(SysTips.PARAM_ERROR);
                return false;
            }
        } else {
            if (StringUtils.isEmpty(appRoom.getRoomName())) {
                requestData.createErrorResponse(SysTips.PARAM_ERROR);
                return false;
            }
        }
        return true;
    }

    @Transactional
    @Override
    public void updateSysRoom(AppRoom appRoom, HttpRequestData requestData) {
        this.updateDefaultVal(appRoom);
        appRoom.setRoomCode(null);
        appRoomMapper.updateById(appRoom);
        appRoom = appRoomMapper.selectJoinFileById(appRoom.getRoomId());
        /**
         *
         *
         * 修改缓存部分
         *
         *
         */
        if (AppChatCacheUtil.existRoom(appRoom.getRoomId().toString())) {
            appRoom.setRoomType(AppChatConstant.SOCKET_TYPE_GROUP);
            AppChatCacheUtil.updateRoomInfo(appRoom.getRoomId().toString(), appRoom);
            ChatData cd = new ChatData();
            cd.setContent(appRoom);
            cd.setType(AppChatConstant.TYPE_GROUP_ANNOUNCEMENT);
            appChatMessageService.notifyUserRoomUpdate(appRoom, cd);
        }

    }


    @Override
    public void searchChatRoomParam(HttpRequestData requestData) {
        String roomId = requestData.getString("roomId");
        if (StringUtils.isEmpty(roomId)) {
            requestData.createErrorResponse(SysTips.PARAM_ERROR);
            return;
        }
        AppRoom appRoom = appRoomMapper.selectById(roomId);
        if (appRoom == null) {
            requestData.createErrorResponse(SysTips.PARAM_ERROR);
            return;
        }
        Map condition = new HashMap<>();
        Map data = new HashMap<>();
        condition.put("roomId", roomId);
        condition.put("roleType", AppRoomRole.TYPE_ANONYMOUS);
        condition.put("httpPath", serviceConfigSet.getHttpServerPath() + "AppUser" + File.separator);
        List<AppUser> appUsers = appRoomRoleMapper.selectAppUsersByRoomId(condition);

        // ChatRoomCacheModel chatRoomCacheModel = AppChatCacheUtil.getRoom(appRoom.getRoomId().toString());
        // List<ChatData> chatRecord =chatRoomCacheModel.getHistoryChatRecords(appRoom.getFindChatRecordNumber());
        // data.put("roomUser",appUsers);
        // data.put("chatRecord",chatRecord);
        requestData.fillResponseData(appUsers);
    }


    @Transactional
    @Override
    public void updateChatRoomUserRole(HttpRequestData requestData) {
        String jsonData = requestData.getString("jsonData");
        String roomId = requestData.getString("roomId");

        if (StringUtils.isEmpty(jsonData) || StringUtils.isEmpty(roomId)) {
            requestData.createErrorResponse(SysTips.PARAM_ERROR);
            return;
        }

        List<AppRoomRole> set = JSONObject.parseArray(jsonData, AppRoomRole.class);

        AppRoomRole newMaster = null;
        AppRoomRole conditon = new AppRoomRole();
        conditon.setRoleType(AppRoomRole.TYPE_MASTER);
        conditon.setRoomId(new Long(roomId));
        AppRoomRole oldMaster = appRoomRoleMapper.selectOne(conditon);
        int masterCount = 0;
        List<AppRoomRole> otherRoles = new ArrayList<>();
        /**
         * 处理群主
         */
        for (AppRoomRole item : set) {
            if (AppRoomRole.TYPE_MASTER.equals(item.getRoleType())) {
                masterCount++;
                if (StringUtils.isEmpty(item.getRoomRoleId())) {
                    requestData.createErrorResponse(SysTips.PARAM_ERROR);
                    return;
                }
                newMaster = item;
            } else {
                otherRoles.add(item);
            }

        }
        if (masterCount > 1) {
            requestData.createErrorResponse(SysTips.PARAM_ERROR);
            requestData.createErrorResponse("已经存在群主", SysTips.PARAM_ERROR);
            return;
        }
        if (masterCount == 1) {
            if (oldMaster == null) {
                otherRoles.add(newMaster);
            }
        }
        /**
         *
         */
        if (oldMaster != null && !newMaster.getRoomRoleId().equals(oldMaster.getRoomRoleId())
                && newMaster.getRoleType().equals(oldMaster.getRoleType())) {
            oldMaster.setRoleType(AppRoomRole.TYPE_PEOPLE);
            appRoomRoleMapper.updateById(oldMaster);
            appRoomRoleMapper.updateById(newMaster);
        }


        for (AppRoomRole item : otherRoles) {
            if (item.getRoomRoleId() != null
                    && item.getRoleType() != null
                    && item.getUserId() != null) {
                super.updateDefaultVal(item);
                item.setRoomId(null);
                item.setUserId(null);
                appRoomRoleMapper.updateById(item);
            }
        }
    }


    @Override
    public void searchSysRoom(HttpRequestData requestData) {
        String roomId = requestData.getString("roomId");
        if (StringUtils.isEmpty(roomId)) {
            requestData.createErrorResponse(SysTips.PARAM_ERROR);
            return;
        }
        AppRoom appRoom = appRoomMapper.selectJoinFileById(new Long(roomId));
        appRoom.setFilePath(serviceConfigSet.getHttpServerPath() + appRoom.getFilePath());
        requestData.fillResponseData(appRoom);
    }

    @Override
    public void removeSysRoom(HttpRequestData requestData) {
        String roomId = requestData.getString("roomId");
        String removeType = requestData.getString("removeType");
        if (StringUtils.isEmpty(roomId) || StringUtils.isEmpty(removeType)) {
            requestData.createErrorResponse(SysTips.PARAM_ERROR);
            return;
        }

        AppRoom ap = new AppRoom();
        ap.setRoomId(new Long(roomId));
        super.removeOneRow(ap, removeType);
        roomChatHelper.removeRoomById(roomId);
        appRoomRoleMapper.delete(MybatisUtil.conditionT().eq("room_id", roomId));

    }

    @Override
    public void disReport(AppDisreport appDisreport, HttpRequestData requestData) {
        super.insertDefaultValForApp(appDisreport, true);
        appDisreport.setCreator(AppTokenCacheUtil.currentUser().getUserId());
        disreportMapper.insert(appDisreport);
    }


    @Transactional
    @Override
    public void sendRedPoint(AppRedPoint appRedPoint, HttpRequestData requestData) {
        if (StringUtils.isEmpty(appRedPoint.getRoomId())
                || StringUtils.isEmpty(appRedPoint.getRptype())
                || appRedPoint.getRpsize() == null || appRedPoint.getRpsize() == 0
                || appRedPoint.getRpcount() > AppChatConstant.RP_RED_POING_COUNT_MAX) {
            requestData.createErrorResponse(SysTips.PARAM_ERROR);
            return;
        }
        AppUserToken token = AppTokenCacheUtil.currentUser();
        ChatRoomCacheModel model = AppChatCacheUtil.getRoom(appRedPoint.getRoomId());
        AppUserToken role = model.getUser(token.getUserId().toString());
        //排除匿名
        if (role == null
                || AppChatConstant.USER_TYPE_0.equals(token.getUserType())) {
            requestData.createErrorResponse(SysTips.PARAM_ERROR);
            return;
        }
        //验证用户积分是否足够
        AppUser ap = appUserMapper.selectById(token.getUserId());
        if (ap.getIntegral() < appRedPoint.getRpcount()) {
            requestData.createErrorResponse("您的积分余额不足");
            return;
        }

        try {
            super.insertDefaultValForApp(appRedPoint, true);
            appRedPointMapper.insert(appRedPoint);
            appRedPoint.setUserIds(new ArrayList<Map<String, Object>>());
            appRedPoint.setBeforeRpcount(appRedPoint.getRpcount());
            AppRedPointCacheUtil.pushRedPoint(appRedPoint.getRedPointId().toString(), appRedPoint);

            AppUser updateAp = new AppUser();
            updateAp.setUserId(ap.getUserId());
            updateAp.setIntegral(ap.getIntegral() - appRedPoint.getRpcount());
            appUserMapper.updateById(updateAp);
            appActivityIntegralService.addRedPointIntegralRecord(-appRedPoint.getRpcount(), appRedPoint.getRedPointId(), AppChatConstant.INTEGRAL_SUBTRACT);
            requestData.fillResponseData(appRedPoint.getRedPointId().toString());
        } catch (Exception e) {
            requestData.createErrorResponse(SysTips.SYS_SERVER_BUSY);
            AppUser updateAp = new AppUser();
            updateAp.setUserId(ap.getUserId());
            updateAp.setIntegral(ap.getIntegral());
            appUserMapper.updateById(updateAp);
            appRedPointMapper.deleteById(appRedPoint.getRedPointId());
            AppRedPointCacheUtil.removeRedPoint(appRedPoint.getRedPointId().toString());

        }

    }


    @Transactional
    @Override
    public void getRedPoint(HttpRequestData requestData) {
        String roomId = requestData.getString("roomId");
        String redPointId = requestData.getString("redPointId");
        if (StringUtils.isEmpty(roomId)) {
            requestData.createErrorResponse(SysTips.APP_ROOM_NOT_EXIST);
            return;
        }

        ChatRoomCacheModel model = AppChatCacheUtil.getRoom(roomId);
        AppUserToken token = AppTokenCacheUtil.currentUser();
        AppUserToken role = model.getUser(token.getUserId().toString());
        //排除匿名
        if (role == null
                || AppChatConstant.USER_TYPE_0.equals(token.getUserType())) {
            requestData.createErrorResponse(SysTips.PARAM_ERROR);
            return;
        }
       /* if(!AppRedPointCacheUtil.existRedPoint(redPointId)){
            requestData.createErrorResponse(SysTips.APP_RED_POINT_EXPIRED);
        }*/


        try {
            AppRedPointCacheUtil.opsRedPointLock(redPointId);
            AppRedPoint appRedPoint = AppRedPointCacheUtil.getRedPoint(redPointId);
/*            if(appRedPoint==null){
                appRedPoint = appRedPointMapper.selectById(redPointId);
            }*/
            if (appRedPoint == null || !appRedPoint.getRoomId().equals(roomId)) {
                AppRedPointCacheUtil.opsRedPointUnLock(redPointId);
                requestData.createErrorResponse(SysTips.APP_RED_POINT_EXPIRED);
                return;
            }
            //已经领取过
            if (AppRedPoint.contains(appRedPoint, token.getUserId().toString())) {
                AppRedPointCacheUtil.opsRedPointUnLock(redPointId);
                requestData.fillResponseData(appRedPoint);
                return;
            }
            //红包被领完
            if (appRedPoint.getRpcount() == 0) {
                AppRedPointCacheUtil.opsRedPointUnLock(redPointId);
                requestData.fillResponseData(appRedPoint);

                return;
            }

            double val = this.contestRedPoint(appRedPoint);

            appRedPoint.setRpcount(appRedPoint.getRpcount() - val);
            Map param = new HashMap();
            param.put("userId", token.getUserId().toString());
            param.put("val", val);
            appRedPoint.getUserIds().add(param);
            //送回更新 缓存
            AppRedPointCacheUtil.pushRedPoint(appRedPoint.getRedPointId().toString(), appRedPoint);
            AppRedPointCacheUtil.opsRedPointUnLock(redPointId);
            //更新数据库
            AppUser ap = appUserMapper.selectById(token.getUserId());
            AppUser appUser = new AppUser();
            appUser.setIntegral(ap.getIntegral() + val);
            appUser.setUserId(ap.getUserId());
            //在更新用户积分
            appUserMapper.updateById(appUser);
            appActivityIntegralService.addRedPointIntegralRecord(val, appRedPoint.getRedPointId(), AppChatConstant.INTEGRAL_ADD);
            requestData.fillResponseData(appRedPoint);
        } catch (Exception e) {
            AppRedPointCacheUtil.opsRedPointUnLock(redPointId);
            SysLogRecordUtil.record("用户{" + token.getUserId() + "}{" + token.getAccount() + "}抢红包异常", e);
        }
    }


    private Random random = new Random();
    private DecimalFormat df = new DecimalFormat("######0.00");

    public double contestRedPoint(AppRedPoint appRedPoint) {
        double count = appRedPoint.getRpcount();
        int size = appRedPoint.getRpsize();
        double val = 0;
        if (AppChatConstant.RP_RED_POING_CONSTAN.equals(appRedPoint.getRptype())) {

            if (appRedPoint.getUserIds().size() == (size - 1)) {
                val = count;
            } else {
                val = count / (size - appRedPoint.getUserIds().size());
            }
        } else if (AppChatConstant.RP_RED_POING_RANDOM.equals(appRedPoint.getRptype())) {
            if (appRedPoint.getUserIds().size() == (size - 1)) {
                val = count;
            } else {
                double coreNumber = random.nextInt(size) + 1.5;
                val = count / coreNumber;
            }
        }

        return new Double(df.format(val));
    }


    @Autowired
    private AppCallListMapper appCallListMapper;

    @Autowired
    private IAppChatRecordService appChatRecordService;


    @Transactional
    @Override
    public void createChatGroup(HttpRequestData requestData) {

        if (StringUtils.isEmpty(requestData.getString("ids"))
                || StringUtils.isEmpty(requestData.getString("roomName"))) {
            requestData.createErrorResponse(SysTips.PARAM_ERROR);
            return;
        }

        Long[] ids = strToLongArray(requestData.getString("ids"));
        if (ids.length < 2) {
            requestData.createErrorResponse(SysTips.PARAM_ERROR);
            return;
        }
        AppUserToken token = AppTokenCacheUtil.currentUser();
        int rows = appRoomRoleMapper.selectCount(MybatisUtil.conditionT().eq("role_type", AppRoomRole.TYPE_MASTER)
                .and()
                .eq("user_id", token.getUserId()));
        if (rows > serviceConfigSet.getAppUserCreateGroupMax()) {
            requestData.createErrorResponse("最多只能管理" + serviceConfigSet.getAppUserCreateGroupMax() + "个群");
            return;
        }
        AppRoom appRoom = new AppRoom();
        appRoom.setRoomName(requestData.getString("roomName"));
        appRoom.setFindChatRecordNumber(50);
        appRoom.setBasePeople(50);
        appRoom.setOnlineBasePeople(50);
        appRoom.setIsAnonymous("0");
        appRoom.setStatus("0");
        appRoom.setIsPicture("1");

        insertDefaultValForApp(appRoom, false);
        addSysRoom(appRoom, requestData);


        List<Long> userList = Arrays.asList(ids);
        List<AppUser> appUserList = appUserMapper.selectBatchIds(userList);
        appUserList.add(token);
        for (AppUser user : appUserList) {
            AppRoomRole roomRole = new AppRoomRole();
            AppCallList appCallList = new AppCallList();
            if (user.getUserId().toString().equals(token.getUserId().toString())) {
                roomRole.setRoleType(AppRoomRole.TYPE_MASTER);
            } else {
                roomRole.setRoleType(AppRoomRole.TYPE_PEOPLE);
            }
            roomRole.setRoomId(appRoom.getRoomId());
            roomRole.setUserId(user.getUserId());
            roomRole.setUserNickname(user.getNickName());
            insertDefaultValForApp(roomRole, false);

            appCallList.setCreator(user.getUserId());
            appCallList.setModifier(user.getUserId());
            appCallList.setRoomId(appRoom.getRoomId());
            appCallList.setRemark(appRoom.getRoomName());
            insertDefaultValForApp(roomRole, false);

            appRoomRoleMapper.insert(roomRole);
            appCallListMapper.insert(appCallList);
        }
        AppChatRecord record = AppChatRecord.createRoomRecord(
                StringEscapeUtils.escapeHtml("大家好，我创建了一个新群")
                , "text"
                , UUIDUtils.generateUuid()
                , new Date()
                , appRoom.getRoomId().toString()
                , token.getUserId());
        appChatRecordService.insert(record);
        appRoom.setRoomType(AppChatConstant.SOCKET_TYPE_GROUP);
        roomChatHelper.joinAppChatRoom(appRoom, null);

        CallListDto callListDto = new CallListDto(appRoom.getRemark()
                , appRoom.getRoomName()
                , null
                , appRoom.getRoomId().toString()
                , appRoom.getRoomId().toString()
                , null);
        ChatData cd = ChatData.create(null, null, token.getUserId().toString(), null, callListDto, new Date());
        appChatMessageService.notifyCreateNewGroup(cd);
        //appRoomRoleMapper.insert()；

    }

    @Override
    public void shareChatGroup(HttpRequestData requestData) {
        try {
            if (StringUtils.isEmpty(requestData.getString("ids"))
                    || StringUtils.isEmpty(requestData.getString("roomId"))) {
                requestData.createErrorResponse(SysTips.PARAM_ERROR);
                return;
            }

            String roomId = requestData.getString("roomId");
            Long[] ids = strToLongArray(requestData.getString("ids"));
            if (ids.length < 1) {
                requestData.createErrorResponse(SysTips.PARAM_ERROR);
                return;
            }

            AppUserToken token = AppTokenCacheUtil.currentUser();

/*        int rows = appRoomRoleMapper.selectCount(MybatisUtil.conditionT().eq("role_type",AppRoomRole.TYPE_MASTER)
                .and()
                .eq("user_id",token.getUserId()));*/
            AppRoom appRoom = appRoomMapper.selectJoinFileById(new Long(roomId));

            if(appRoom == null){
                requestData.createErrorResponse(SysTips.PARAM_ERROR);
                return;
            }
            List<Long> userList = Arrays.asList(ids);
            ChatData chatData = new ChatData();
            chatData.setSender(token.getUserId().toString());
            chatData.setContent(JSONObject.toJSON(appRoom).toString());
            chatData.setCreationTime(new Date());
            chatData.setType(ChatData.TYPE_SHARE);
            for (Long userId : userList) {
                String roomFinalCode = roomChatHelper.generateFinalContactRoomId(token.getUserId(), userId);
                chatData.setUuid(UUIDUtils.generateUuid());
                chatData.setReciever(userId.toString());
                appChatMessageService.saveRoomChatRecord(chatData, roomFinalCode, token.getUserId());
                appChatMessageService.sendDataToRoomByHandleType(roomFinalCode, chatData, AppChatConstant.HANDLE_TYPE_TEXT, true, false);
            }
        } catch (IOException e) {
            SysLogRecordUtil.record("处理分享群组出错", e);
        }

    }


    @Override
    public void disableSay(HttpRequestData requestData) {
        String roomId = requestData.getString("roomId");
        String targetId = requestData.getString("userId");
        Long time = requestData.getLong("time");
        AppUserToken token = AppTokenCacheUtil.currentUser();
        if(StringUtils.isEmpty(roomId)||StringUtils.isEmpty(targetId)){
            requestData.createErrorResponse(SysTips.PARAM_ERROR);
            return;
        }


        if(!AppChatCacheUtil.existRoom(roomId)){
            requestData.createErrorResponse(SysTips.APP_ROOM_NOT_EXIST);
            return;
        }
        AppRoomRole appRoomRole = new AppRoomRole();
        appRoomRole.setRoomId(new Long(roomId));
        appRoomRole.setUserId(token.getUserId());
        appRoomRole = appRoomRoleMapper.selectOne(appRoomRole);
        if(AppRoomRole.TYPE_MASTER.equals(appRoomRole.getRoleType())||
                AppRoomRole.TYPE_MANAGER.equals(appRoomRole.getRoleType())){
            try{
                if(AppChatCacheUtil.opsRoomLock(roomId)){
                    ChatRoomCacheModel cacheModel = AppChatCacheUtil.getRoom(roomId);
                    cacheModel.getBannedTalks().put(targetId,new Long[]{System.currentTimeMillis(),time*6});
                    AppChatCacheUtil.putRoom(roomId,cacheModel);
                    AppChatCacheUtil.opsRoomUnLock(roomId);
                }
            }catch (Exception e){
                SysLogRecordUtil.record("房间ID{"+roomId+"}禁言失败",e);
                AppChatCacheUtil.opsRoomUnLock(roomId);
            }

        }else {
            requestData.createErrorResponse(SysTips.APP_ROOM_NOT_AUTH);
            return;
        }
    }

    @Override
    public void disableAllSay(HttpRequestData requestData) {
        String roomId = requestData.getString("roomId");
        String open = requestData.getString("open");
        AppUserToken token = AppTokenCacheUtil.currentUser();
        if(StringUtils.isEmpty(roomId)||StringUtils.isEmpty(open)){
            requestData.createErrorResponse(SysTips.PARAM_ERROR);
            return;
        }

        if(!AppChatCacheUtil.existRoom(roomId)){
            requestData.createErrorResponse(SysTips.APP_ROOM_NOT_EXIST);
            return;
        }
        AppRoomRole appRoomRole = new AppRoomRole();
        appRoomRole.setRoomId(new Long(roomId));
        appRoomRole.setUserId(token.getUserId());
        appRoomRole = appRoomRoleMapper.selectOne(appRoomRole);
        if(appRoomRole==null){
            requestData.createErrorResponse(SysTips.APP_ROOM_NOT_AUTH);
            return;
        }

        if(AppRoomRole.TYPE_MASTER.equals(appRoomRole.getRoleType())||
                AppRoomRole.TYPE_MANAGER.equals(appRoomRole.getRoleType())){
            try{
                if(AppChatCacheUtil.opsRoomLock(roomId)){
                    ChatRoomCacheModel cacheModel = AppChatCacheUtil.getRoom(roomId);
                    if(AppChatConstant.CONSTANT_NO.equals(open)){
                        cacheModel.getAppRoom().setDisableSay(false);
                    }else{
                        cacheModel.getAppRoom().setDisableSay(true);
                    }
                    AppChatCacheUtil.putRoom(roomId,cacheModel);
                    AppChatCacheUtil.opsRoomUnLock(roomId);
                }
            }catch (Exception e){
                SysLogRecordUtil.record("房间ID{"+roomId+"}禁言失败",e);
                AppChatCacheUtil.opsRoomUnLock(roomId);
            }

        }else {
            requestData.createErrorResponse(SysTips.APP_ROOM_NOT_AUTH);
            return;
        }
    }



    @Transactional
    @Override
    public void msgWithdraw(HttpRequestData requestData) {
        Long roomId = requestData.getLong("roomId");
        String uuid = requestData.getString("uuid");
        if(StringUtils.isEmpty(roomId)||StringUtils.isEmpty(uuid)){
            requestData.createErrorResponse(SysTips.PARAM_ERROR);
            return;
        }
        AppRoom appRoom = AppChatCacheUtil.getRoom(roomId.toString()).getAppRoom();

        AppUserToken token = AppTokenCacheUtil.currentUser();
        AppRoomRole selfRole = new AppRoomRole();
        selfRole.setRoomId(roomId);
        selfRole.setUserId(token.getUserId());
        selfRole = appRoomRoleMapper.selectOne(selfRole);

        if(selfRole==null){
            requestData.createErrorResponse(SysTips.PARAM_ERROR);
            return;
        }
        AppChatRecord appChatRecord = appChatRecordService.selectOne(MybatisUtil.conditionT().eq("uuid",uuid));
        if(appChatRecord==null){
            return;
        }
        AppRoomRole delRole = new AppRoomRole();
        delRole.setRoomId(roomId);
        delRole.setUserId(appChatRecord.getCreator());
        delRole = appRoomRoleMapper.selectOne(delRole);

        //验证身份
        if(AppRoomRole.TYPE_MASTER.equals(selfRole.getRoleType())){

        }else if(AppRoomRole.TYPE_MANAGER.equals(selfRole.getRoleType())){
            if(AppRoomRole.TYPE_MASTER.equals(delRole.getRoleType())){
                requestData.createErrorResponse("您无权限撤回群主消息");
                return;
            }

        }else {
            if(!delRole.getUserId().toString().equals(token.getUserId().toString())){
                requestData.createErrorResponse("您无权限撤回别人的消息");
                return;
            }
            if((System.currentTimeMillis()-appChatRecord.getCreationTime().getTime())>60*1000){
                requestData.createErrorResponse("无法撤回1分钟后的消息");
                return;
            }

        }
        appChatRecordService.deleteById(appChatRecord.getChatRecordId());
        ChatData cd = new ChatData();
        cd.setSender(token.getUserId().toString());
        cd.setContent(uuid);
        cd.setType(AppChatConstant.TYPE_GROUP_MESSAGE_REMOVE);
        appChatMessageService.notifyUserRoomUpdate(appRoom, cd);
    }


    @Transactional
    @Override
    public void msgWithdrawAll(HttpRequestData requestData) {
        Long roomId = requestData.getLong("roomId");
        Long targetId = requestData.getLong("id");
        if(StringUtils.isEmpty(roomId)||StringUtils.isEmpty(targetId)){
            requestData.createErrorResponse(SysTips.PARAM_ERROR);
            return;
        }
        AppRoom appRoom = AppChatCacheUtil.getRoom(roomId.toString()).getAppRoom();

        AppUserToken token = AppTokenCacheUtil.currentUser();
        AppRoomRole selfRole = new AppRoomRole();
        selfRole.setRoomId(roomId);
        selfRole.setUserId(token.getUserId());
        selfRole = appRoomRoleMapper.selectOne(selfRole);
        if(selfRole==null){
            requestData.createErrorResponse(SysTips.PARAM_ERROR);
            return;
        }
        AppRoomRole delRole = new AppRoomRole();
        delRole.setRoomId(roomId);
        delRole.setUserId(targetId);
        delRole = appRoomRoleMapper.selectOne(delRole);

        //验证身份
        if(AppRoomRole.TYPE_MASTER.equals(selfRole.getRoleType())){

        }else if(AppRoomRole.TYPE_MANAGER.equals(selfRole.getRoleType())){
            if(AppRoomRole.TYPE_MASTER.equals(delRole.getRoleType())){
                requestData.createErrorResponse("您无权限撤回群主消息");
                return;
            }

        }else {
            requestData.createErrorResponse("您无权限撤回别人的消息");
            return;
        }
        appChatRecordService.delete(MybatisUtil.conditionT().eq("room_id",roomId.toString())
                .and()
                .eq("creator",targetId));
        ChatData cd = new ChatData();
        cd.setSender(token.getUserId().toString());
        cd.setContent(targetId);
        cd.setType(AppChatConstant.TYPE_GROUP_MESSAGE_REMOVE_ALL);
        appChatMessageService.notifyUserRoomUpdate(appRoom, cd);

    }

    @Transactional
    @Override
    public void disablePeole(HttpRequestData requestData) {
        Long roomId = requestData.getLong("roomId");
        Long targetId = requestData.getLong("id");
        if(StringUtils.isEmpty(roomId)||StringUtils.isEmpty(targetId)){
            requestData.createErrorResponse(SysTips.PARAM_ERROR);
            return;
        }
        AppUserToken token = AppTokenCacheUtil.currentUser();
        AppRoomRole selfRole =  appRoomRoleMapper.selectOne(new AppRoomRole(token.getUserId(),roomId));
        if(selfRole==null){
            requestData.createErrorResponse(SysTips.PARAM_ERROR);
            return;
        }
        AppRoomRole  disableRole = appRoomRoleMapper.selectOne(new AppRoomRole(targetId,roomId));

        //验证身份
        if(AppRoomRole.TYPE_MASTER.equals(selfRole.getRoleType())){

        }else if(AppRoomRole.TYPE_MANAGER.equals(selfRole.getRoleType())){
            if(AppRoomRole.TYPE_MASTER.equals(disableRole.getRoleType())){
                requestData.createErrorResponse("您无权限踢除群主");
                return;
            }

        }else {
            requestData.createErrorResponse("您无权限踢除别人");
            return;
        }
        AppRoom room = appRoomMapper.selectJoinFileById(roomId);
        room.setRoomType(AppChatConstant.SOCKET_TYPE_GROUP);
        String disablePeopleList = room.getDisablePeople();
        if(disablePeopleList==null){
            disablePeopleList="";
        }
        disablePeopleList+=(targetId.toString()+",");
        appRoomMapper.updateDisablePeople(disablePeopleList,roomId);
        appRoomRoleMapper.delete(MybatisUtil.conditionT().eq("room_id",roomId).and().eq("user_id",targetId));
        appCallListMapper.delete(MybatisUtil.conditionT().eq("creator",targetId).and().eq("room_id",roomId));
        room.setDisablePeople(disablePeopleList);
        AppChatCacheUtil.updateRoomInfo(roomId.toString(),room);
        //AppChatCacheUtil.removeRoomSocket(roomId.toString(),targetId.toString());
        ChatData cd = new ChatData();
        cd.setSender(token.getUserId().toString());
        cd.setContent(targetId);
        cd.setType(AppChatConstant.TYPE_GROUP_DISABLE_PEOPLE);
        appChatMessageService.notifyUserRoomUpdate(AppChatCacheUtil.getRoom(roomId.toString()).getAppRoom(), cd);

    }

    @Override
    public void groupBlacklistTable(HttpRequestData requestData) {
        String roomId = requestData.getString("roomId");
        if(StringUtils.isEmpty(roomId)){
            requestData.createErrorResponse(SysTips.PARAM_ERROR);
            return;
        }
        AppRoom room = AppChatCacheUtil.getRoom(roomId.toString()).getAppRoom();
        if(room==null){
            room =appRoomMapper.selectById(roomId);
        }
        Long[] user = strToLongArray( room.getDisablePeople());
        List<AppUser> appUserList = new ArrayList<>();
        if(user!=null&&user.length>0){
            appUserList = appUserMapper.selectBatchIds(Arrays.asList(user));
        }
        requestData.generatePageData(appUserList,user.length);



    }

    @Override
    public void relieveBlacklist(HttpRequestData requestData) {
        String roomId = requestData.getString("roomId");
        String account = requestData.getString("account");
        if(StringUtils.isEmpty(roomId)||StringUtils.isEmpty(account)){
            requestData.createErrorResponse(SysTips.PARAM_ERROR);
            return;
        }
        ChatRoomCacheModel cacheModel = AppChatCacheUtil.getRoom(roomId.toString());
        AppRoom room = null;
        if(cacheModel==null||cacheModel.getAppRoom()==null){
            room =appRoomMapper.selectById(roomId);
        }else{
            room = cacheModel.getAppRoom();
        }
        Map condition = new HashMap();
        condition.put("httpPath",serviceConfigSet.getHttpServerPath());
        condition.put("account",account);
        AppUser user = appUserMapper.selectByAccountJoinFile(condition);
        if(user==null){
            requestData.createErrorResponse("该账号不存在");
            return;
        }
        room.setDisablePeople(room.getDisablePeople().replace(user.getUserId()+",",""));
        if(cacheModel!=null){
            AppChatCacheUtil.updateRoomInfo(roomId.toString(),room);
        }
        appRoomMapper.updateDisablePeople(room.getDisablePeople(),new Long(roomId));

    }

    @Override
    public void getMaster(HttpRequestData requestData) {
        String group = requestData.getString("group");

        if(StringUtils.isEmpty(group)){
            requestData.createErrorResponse(SysTips.PARAM_ERROR);
            return;
        }
        AppRoomRole master =appRoomRoleMapper.selectOne(new AppRoomRole(AppRoomRole.TYPE_MASTER,new Long(group)));
        requestData.fillResponseData(master);
    }

    @Override
    public void searchRoomHttpPath(HttpRequestData requestData) {
        String group = requestData.getString("group");

        if(StringUtils.isEmpty(group)){
            requestData.createErrorResponse(SysTips.PARAM_ERROR);
            return;
        }
        Map res = new HashMap();
        long effictiveTime = 0;
        long creationTime = 0;
        res.put("path",serviceConfigSet.getAppRoomPagePath());
        Map qrCodeCache =null;

        if(!AppGroupQRCodeCacheUtil.existQRCode(group)){
            Long defualtEffictiveTime = requestData.getLong("defualtEffictiveTime");

            qrCodeCache = AppGroupQRCodeCacheUtil.cacheEntity();
            effictiveTime = AppGroupQRCodeCacheUtil.GROUPQRCODE_EFFICTIVE_TIME;
            creationTime = System.currentTimeMillis();

            qrCodeCache.put("creationTime",creationTime);
            qrCodeCache.put("effictiveTime",effictiveTime);

            AppGroupQRCodeCacheUtil.pushQRCode(group,qrCodeCache);
        }else{
            qrCodeCache = AppGroupQRCodeCacheUtil.getQRCode(group);
             effictiveTime = (Long)qrCodeCache.get("effictiveTime");
             creationTime =  (Long)qrCodeCache.get("creationTime");

        }
        res.put("creationTime",new Long(creationTime).toString());
        res.put("effictiveTime",new Long(effictiveTime).toString());
        requestData.fillResponseData(res);
    }

    @Override
    public void updateQrcodeExpire(HttpRequestData requestData) {
        String group = requestData.getString("group");
        Long effictiveTime = requestData.getLong("effictiveTime");
        if(StringUtils.isEmpty(group)||StringUtils.isEmpty(effictiveTime)){
            requestData.createErrorResponse(SysTips.PARAM_ERROR);
            return;
        }
        if(AppGroupQRCodeCacheUtil.opsQRCodeLock(group)){
            Map qrCodeCache = AppGroupQRCodeCacheUtil.getQRCode(group);
            qrCodeCache.put("effictiveTime",effictiveTime*1000);
            qrCodeCache.put("creationTime",System.currentTimeMillis());
            AppGroupQRCodeCacheUtil.pushQRCode(group,qrCodeCache);
            AppGroupQRCodeCacheUtil.opsQRCodeUnLock(group);
        }


    }

    public static void main(String[] args) {
      /*  List<AppRoomRole> se = new ArrayList<>();
        AppRoomRole ap = new AppRoomRole();
        ap.setRoleType("hehe");
        ap.setRoomId(254L);
        se.add(ap);
        se.add(ap);
        String json = JSONObject.toJSONString(se);
        System.out.println(json);
        se=  JSONObject.parseArray(json,  AppRoomRole.class);*/

    }
}