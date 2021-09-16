package com.superhao.app.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
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
import com.superhao.app.service.IAppChatMessageService;
import com.superhao.app.service.IAppChatRecordService;
import com.superhao.app.service.IAppUserService;
import com.superhao.app.util.BirthdayUtils;
import com.superhao.app.util.Cn2Spell;
import com.superhao.base.cache.util.*;
import com.superhao.base.common.constant.SysConstantSet;
import com.superhao.base.common.constant.SysServiceConfigSet;
import com.superhao.base.common.dto.SysTips;
import com.superhao.base.common.service.impl.BaseServiceImpl;
import com.superhao.base.entity.HttpRequestData;
import com.superhao.base.entity.Pages;
import com.superhao.base.entity.PictureVerifyCode;
import com.superhao.base.exception.SystemException;
import com.superhao.base.log.util.SysLogRecordUtil;
import com.superhao.base.remote.service.impl.RemoteFileSFTPImpl;
import com.superhao.base.util.*;
import jdk.nashorn.internal.parser.Token;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import sun.net.util.IPAddressUtil;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.OutputStream;
import java.util.*;


@Service("appUserService")
public class AppUserServiceImpl extends BaseServiceImpl<AppUserMapper, AppUser> implements IAppUserService {

    @Autowired
    private AppUserMapper appUserMapper;

    @Autowired
    private AppRoomMapper appRoomMapper;

    @Autowired
    private AppCallListMapper appCallListMapper;

    @Autowired
    private AppRoomRoleMapper appRoomRoleMapper;

    @Autowired
    private IAppChatMessageService appChatMessageService;



    @Autowired
    SysServiceConfigSet serviceConfigSet;



    @Override
    public void realLogin(AppUser appUser, HttpRequestData requestData) {
        //参数校验
        //身份校验
        //分配token
        //获取联系人和群
        if(!realLoginParamValidate(appUser,requestData)){
            return;
        }

        appUser.setPassword(Md5Util.getMD5(appUser.getPassword()+serviceConfigSet.getUserSalt()));
        AppUser target = appUserMapper.selectByMoreAccount(appUser);
        if(target==null){
            requestData.createErrorResponse("账号或密码有误");
            return;
        }
        target.setFilePath(serviceConfigSet.getHttpServerPath()+target.getFilePath());
        target.setAccount(getAccout(target));
        //判断是否已经登陆  重复登陆替换Token 刷新appuser info
        if(AppTokenCacheUtil.hasObj(target.getUserId().toString())){
            AppTokenCacheUtil.remove(target.getUserId().toString());
        }

        //分配app token
        String tokenCode = UUID.randomUUID().toString().replace("-","");
        AppUserToken token = AppUserToken.create(target, tokenCode);
        AppTokenCacheUtil.pushExpire(token);

        String rooomId = requestData.getString("roomId");
        if(!StringUtils.isEmpty(rooomId)){
            this.joinRoomByRoomId(token,rooomId);
        }
        //TODO 记录用户登录 地点 次数
        this.updateUserLoginInfo(target);
        requestData.fillResponseData(token);

        /**
         * TODO 加载联系人
         */
       /* List<AppRoom> roomList = appCallListMapper.selectRoomById(token.getUserId());
        List<AppUser> userList = appCallListMapper.selectUserById(token.getUserId());

        for(AppRoom item:roomList){
            item.setFilePath(serviceConfigSet.getHttpServerPath()+item.getFilePath());
        }
        for(AppUser item:userList){
            item.setFilePath(serviceConfigSet.getHttpServerPath()+item.getFilePath());
        }*/

       // requestData.fillResponseMapData("token",tokenCode).fillClose();

    }

    private void updateUserLoginInfo(AppUser token){
        AppUser appUser = new AppUser();
        appUser.setUserId(token.getUserId());
        if(token.getLoginNumber()==null){
            appUser.setLoginNumber(1);
        }else{
            appUser.setLoginNumber(token.getLoginNumber()+1);
        }
        appUser.setLastUpdateTime(new Date());
        appUser.setLastUpdatePlace(IpUtil.getIp(SpringContexUtil.getServletRequest()));
        appUserMapper.updateById(appUser);
    }

    private boolean realLoginParamValidate(AppUser appUser, HttpRequestData requestData){

        boolean hasAccountParam = !(StringUtils.isEmpty(appUser.getUserName())
                &&StringUtils.isEmpty(appUser.getEmail())
                &&StringUtils.isEmpty(appUser.getPhone()));
        boolean passVerifyCode = false;

        if(!hasAccountParam||StringUtils.isEmpty(appUser.getPassword())){
            requestData.createErrorResponse("请输入账号密码");
            return false;
        }
        //手机登陆
        if(!StringUtils.isEmpty(appUser.getPhone())){
            if(!PhoneUtils.checkPhoneNumber(appUser.getPhone(),null)){
                requestData.createErrorResponse("手机格式有误");
                return false;
            }
            passVerifyCode = VerifyCodeCacheUtil.hasObj(appUser.getPhone());
            appUser.setAccount(appUser.getPhone());
        }
        //邮箱登陆
        if(!StringUtils.isEmpty(appUser.getEmail())){
            if(!RegexValidateUtil.checkEmail(appUser.getEmail())){
                requestData.createErrorResponse("邮箱格式有误");
                return false;
            }
            passVerifyCode = VerifyCodeCacheUtil.hasObj(appUser.getEmail());
            appUser.setAccount(appUser.getEmail());
        }
        //TODO 校验验证码
       /* if(!StringUtils.isEmpty(appUser.getUserName())){
            passVerifyCode = VerifyCodeCacheUtil.hasObj(appUser.getUserName());
            appUser.setAccount(appUser.getUserName());
        }
        if(!passVerifyCode){
            requestData.createErrorResponse("验证码有误");
            return false;
        }*/
        return true;
    }



    @Override
    public void anonymousLogin(Map param,HttpRequestData requestData) {
        String roomId = (String) param.get("roomId");
        String nickname = (String) param.get("nickname");
        String userId = (String) param.get("tokenKey");
        String tokenCode = (String) param.get("tokenCode");



        if(StringUtils.isEmpty(roomId)||StringUtils.isEmpty(nickname)){
            requestData.createErrorResponse(SysTips.PARAM_ERROR);
            return;
        }

        //TODO 防止服务器重启匿名用户丢失 重新登录
        if(!StringUtils.isEmpty(userId)&&!StringUtils.isEmpty(tokenCode)){
            int count = appRoomRoleMapper.selectCount(MybatisUtil.conditionT().eq("room_id",roomId).and().eq("user_id",userId));
            if(count!=0){
                AppUser anomy =  appUserMapper.selectById(new Long(userId));
                AppUserToken token = AppUserToken.create(anomy,tokenCode);
                AppTokenCacheUtil.pushExpire(token);
                requestData.fillResponseData(token);
                return;
            }

        }
//增加匿名用户
        if(nickname.length()>15){
            nickname = nickname.substring(0,15);
        }
        AppUser appUser = new AppUser();
        super.insertDefaultValForApp(appUser,false);
        appUser.setUserId(UUIDUtils.generateNumberUuid());
        appUser.setUserType(AppChatConstant.USER_TYPE_0);
        appUser.setNickName(nickname);
        appUserMapper.insert(appUser);

        AppUserToken token = AppUserToken.create(appUser);
        AppTokenCacheUtil.pushExpire(token);
        this.joinRoomByRoomId(appUser,roomId);
        requestData.fillResponseData(token);
    }


    @Override
    public void joinRoomByRoomId(AppUser appUser,String roomId){
        int count =appRoomRoleMapper.selectCount(MybatisUtil.conditionT().eq("room_id",roomId).and().eq("user_id",appUser.getUserId()));
        if(count>0){
            return;
        }
        AppRoomRole appRoomRole = new AppRoomRole();
        appRoomRole.setRoomId(new Long(roomId));
        appRoomRole.setUserId(appUser.getUserId());
        appRoomRole.setCreationTime(new Date());
        appRoomRole.setUserNickname(appUser.getNickName());
        appRoomRole.setLastUpdateTime(appRoomRole.getCreationTime());
        if(AppChatConstant.USER_TYPE_0.equals(appUser.getUserType())){
            appRoomRole.setRoleType(AppRoomRole.TYPE_ANONYMOUS);
        }else if(AppChatConstant.USER_TYPE_1.equals(appUser.getUserType())){
            appRoomRole.setRoleType(AppRoomRole.TYPE_PEOPLE);
        }
        appRoomRoleMapper.insert(appRoomRole);
    }



    @Override
    public void getVerificationCode(HttpServletResponse response, Map params) {
        try {

            int width = 200;
            int height = 69;

            BufferedImage verifyImg = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

            //生成对应宽高的初始图片
            String randomText = GeneratePictureVerifyCodeUtil.drawRandomText(width, height, verifyImg);

            String verifyCodeUUID = (String) params.get("uuid");
            if(StringUtils.isEmpty(verifyCodeUUID)){
                return;
            }
            if(VerifyCodeCacheUtil.get(verifyCodeUUID)!=null){
                VerifyCodeCacheUtil.remove(verifyCodeUUID);
            }
            VerifyCodeCacheUtil.push(new PictureVerifyCode(verifyCodeUUID,randomText,serviceConfigSet.getAppRegisterCodeExpired()));
            //单独的一个类方法，出于代码复用考虑，进行了封装。

            //功能是生成验证码字符并加上噪点，干扰线，返回值为验证码字符
            response.setContentType("image/png");//必须设置响应内容类型为图片，否则前台不识别

            OutputStream os = response.getOutputStream(); //获取文件输出流

            ImageIO.write(verifyImg, "png", os);//输出图片流

            os.flush();
            os.close();//关闭流

        } catch (IOException e) {
             throw new SystemException(e.getMessage());
        }
    }

  private static   double []giveawayIntegrals  = {18,18,18,18,18,18,18,18,18,28,18,18,18,28,18,18,18,28,38,18,18,18,18,18,48,18,18,18,18,88,18,18,18};
    @Override
    public void register(AppUser appUser, HttpRequestData requestData) {
        this.insertDefaultValForApp(appUser,true);
        String uuid = requestData.getString("uuid");
        String verifyCode =requestData.getString("verifyCode");
        String roomId =requestData.getString("roomId");

        PictureVerifyCode  vfcode= VerifyCodeCacheUtil.get(uuid);
        if(uuid==null||verifyCode==null||vfcode == null){
            requestData.createErrorResponse("邀请码不存在");
            return;
        }
        if(vfcode.isTimeout()){
            requestData.createErrorResponse("邀请码过期");
            return;
        }

        String newPwd = Md5Util.getMD5(appUser.getPassword() + serviceConfigSet.getUserSalt());
        double integralVal = giveawayIntegrals[random.nextInt(giveawayIntegrals.length)];
        appUser.setPassword(newPwd);
        appUser.setUserType(AppChatConstant.USER_TYPE_1);
        appUser.setBirthday(new Date());
        appUser.setLoginNumber(1);
        appUser.setIntegral(integralVal);
        insertDefaultValForApp(appUser,false);
        appUserMapper.insert(appUser);
        //记录积分充值
        appActivityIntegralService.addIntegralRecord(appUser.getUserId(),integralVal,AppChatConstant.ACTIVITY_INTEGRAL_RECHARGE);

        VerifyCodeCacheUtil.remove(uuid);
        appUser.setAccount(getAccout(appUser));
        if(!StringUtils.isEmpty(roomId)){
            this.joinRoomByRoomId(appUser,roomId);
        }
        AppUserToken token = AppUserToken.create(appUser);
        AppTokenCacheUtil.pushExpire(token);
        requestData.fillResponseData(token);
       // if(requestData.get)
    }

    @Override
    public boolean registerParamValidate(AppUser appUser, HttpRequestData requestData) {
        int hasOneAcount=0;
        if(!StringUtils.isEmpty(appUser.getPhone())){
            if(!PhoneUtils.checkPhoneNumber(appUser.getPhone(),null)){
                requestData.createErrorResponse("手机格式错误");
                return false;
            }
            appUser.setAccount(appUser.getPhone());
            hasOneAcount++;
        }
        if(!StringUtils.isEmpty(appUser.getEmail())){
            if(!RegexValidateUtil.checkEmail(appUser.getEmail())){
                requestData.createErrorResponse("邮箱格式错误");
                return false;
            }
            appUser.setAccount(appUser.getEmail());
            hasOneAcount++;
        }
        if(!StringUtils.isEmpty(appUser.getUserName())){
            appUser.setAccount(appUser.getUserName());
            hasOneAcount++;
        }
        if(hasOneAcount!=1||StringUtils.isEmpty(appUser.getPassword())){
            requestData.createErrorResponse("缺少必填参数");
            return false;
        }
        int rows = appUserMapper.selectCount(MybatisUtil.condition()
                .eq("phone",appUser.getAccount())
                .or()
                .eq("email",appUser.getAccount())
                .or()
                .eq("user_name",appUser.getAccount()));
        if(rows>0){
            requestData.createErrorResponse("该账号存在或被绑定");
            return false;
        }
        return true;
    }

    @Autowired
    private RoomChatHelper roomChatHelper;
    @Autowired
    private IAppChatRecordService appChatRecordService;
    private Random random = new Random();


    @Override
    public void searchUserGroupChat(HttpRequestData requestData) {
        String roomId = requestData.getString("roomId");
        AppUserToken token = AppTokenCacheUtil.currentUser();
        if(StringUtils.isEmpty(roomId)){
            requestData.createErrorResponse(SysTips.PARAM_ERROR);
            return;
        }
        AppRoom ar = appRoomMapper.selectById(new Long(roomId));
        if(ar ==null){
            requestData.createErrorResponse(SysTips.APP_ROOM_NOT_EXIST);
            return;
        }
        ar.setRoomType(AppChatConstant.SOCKET_TYPE_GROUP);
        if(!roomChatHelper.isJoinGroupRule(ar,token,requestData)){
            return;
        }



       /* if(!roomChatHelper.isJoinGroupRule(ar,token)){
            requestData.createErrorResponse(SysTips.APP_ROOM_NOT_JOIN_ANONYMOUS);
            return;
        }*/

        if(AppChatConstant.USER_TYPE_1.equals(token.getUserType())){
            //注册用户进入之后 添加到通讯录里
            int rows = appCallListMapper.selectCount(MybatisUtil.conditionT().eq("creator",token.getUserId())
                    .and().eq("room_id",roomId));
            if(rows==0){
                AppCallList call = new AppCallList();
                call.setRemark(ar.getRoomName());
                call.setCreator(token.getUserId());
                call.setRoomId(new Long(roomId));
                appCallListMapper.insert(call);
            }
        }
        //加入群组
        this.joinRoomByRoomId(token,roomId);

        Map condition = new HashMap();
        Map result = new HashMap();
        condition.put("roomId",ar.getRoomId());
        condition.put("httpPath",serviceConfigSet.getHttpServerPath());
        List<AppUser> appUsers = appUserMapper.selectUserByRoom(condition);
        ChatRoomCacheModel chatModel = AppChatCacheUtil.getRoom(ar.getRoomId().toString());
        if(chatModel ==null){

            chatModel = roomChatHelper.joinAppChatRoom(ar, null);
        }
       // List<ChatData> historyRecord =  chatModel.getHistoryChatRecords(ar.getFindChatRecordNumber());

        List<ChatData>  historyRecord = appChatRecordService.searchChatDataByRoom(chatModel.getAppRoom());
        for(AppUser au:appUsers){

            if(AppChatConstant.USER_TYPE_1.equals(au.getUserType())){
                au.setAccount(getAccout(au));
            }
            au.setAge(BirthdayUtils.getAgeByBirthday(au.getBirthday()));
        }
        AppRoom room  = chatModel.getAppRoom();
        AppRoomRole self = appRoomRoleMapper.selectOne(new AppRoomRole(token.getUserId(),new Long(roomId)));
       // int roomPeopleCount = appRoomRoleMapper.selectCount(MybatisUtil.conditionT().eq("room_id",roomId));
        if(AppRoomRole.TYPE_MASTER.equals(self.getRoleType())||AppRoomRole.TYPE_MANAGER.equals(self.getRoleType())){
            room.setBasePeople(appUsers.size());
            room.setOnlineBasePeople(chatModel.getSockets().size());
        }else{
            room.setBasePeople(room.getBasePeople()+appUsers.size());
            room.setOnlineBasePeople(random.nextInt(room.getOnlineBasePeople())+1+chatModel.getSockets().size());
        }


        result.put("users",appUsers);
        result.put("room",room);
        result.put("records",historyRecord);
        result.put("httpPath",serviceConfigSet.getHttpServerPath());
        result.put("serverPort",serviceConfigSet.getWebsocketServerPort());
        requestData.fillResponseData(result);
    }

    @Override
    public AppUser searchAnonymous(Long id) {
        AppUser condi = new AppUser();
        condi.setUserId(id);
        condi.setUserType(AppChatConstant.USER_TYPE_0);
        return appUserMapper.selectOne(condi);
    }

    @Override
    public void searchUserCallList(HttpRequestData requestData) {
        AppUserToken token = AppTokenCacheUtil.currentUser();

        int count = appCallListMapper.selectCount(MybatisUtil.conditionT().eq("creator",token.getUserId()));
        List<CallListDto> callListDtos = new ArrayList<>();
        if(count>0){
            List<AppRoom> groupSet;
            groupSet = appCallListMapper.selectRoomById(token.getUserId());
            for(AppRoom item:groupSet){
                item.setRoomType(AppChatConstant.SOCKET_TYPE_GROUP);
                ChatRoomCacheModel chatModel =  roomChatHelper.joinAppChatRoom(item,null);
               /* if(item.getFindChatRecordNumber()>100){
                    item.setFindChatRecordNumber(100);
                }*/
               //防止加载数据过多
                item.setFindChatRecordNumber(1);
                List<ChatData> records = appChatRecordService.searchChatDataByRoom(item);
               /* if(records!=null&&records.size()>1){
                    for(int i =1;i<records.size();i++){
                        records.get(i).setContent("");
                    }
                }*/

                CallListDto group =new CallListDto(
                        item.getRemark()
                        ,item.getRoomName()
                        ,serviceConfigSet.getHttpServerPath()+item.getFilePath()
                        ,item.getRoomId().toString()
                        ,item.getRoomId().toString()
                        ,records);
                group.setType("group");
                callListDtos.add(group);
            }
            List<AppUser> userSet = appCallListMapper.selectUserById(token.getUserId());
            for(AppUser item:userSet){
                item.setAccount(getAccout(item));
                String roomId = roomChatHelper.generateFinalContactRoomId(token.getUserId(),item.getUserId());
                AppRoom temp = new AppRoom();
                temp.setRoomCode(roomId);
                temp.setFindChatRecordNumber(serviceConfigSet.getAppUserFindRecord());
                temp.setRoomType(AppChatConstant.SOCKET_TYPE_CONTACT);
                ChatRoomCacheModel chatModel =  roomChatHelper.joinAppChatRoom(temp,null);
                chatModel.getAppRoom().setFindChatRecordNumber(1);
                CallListDto call = new CallListDto(
                        item.getSignature()
                        ,item.getNickName()
                        ,serviceConfigSet.getHttpServerPath()+item.getFilePath()
                        ,roomId
                        ,item.getUserId().toString()
                        ,appChatRecordService.searchChatDataByRoom(chatModel.getAppRoom()));
                call.setType("user");
                call.setAccount(item.getAccount());
                call.setAge(BirthdayUtils.getAgeByBirthday(item.getBirthday()));
                call.setSex(item.getSex());
                callListDtos.add(call);
            }
        }

        Map result = new HashMap();
        Map condition = new HashMap();
        condition.put("httpPath",serviceConfigSet.getHttpServerPath());
        condition.put("userId",token.getUserId());
        AppUser newInfo = appUserMapper.selectByIdJoinFile(condition);
        newInfo.setAccount(getAccout(newInfo));
        result.put("callList",callListDtos);
        result.put("httpPath",serviceConfigSet.getHttpServerPath());
        result.put("serverPort",serviceConfigSet.getWebsocketServerPort());
        result.put("user",newInfo);
        result.put("notifys",AppNotifyCacheUtil.getNotify(token.getUserId().toString()));
        requestData.fillResponseData(result);
    }

    private String getAccout(AppUser appUser){
        if(!StringUtils.isEmpty(appUser.getUserName())){
            return appUser.getUserName();
        }
        return "";
       /* if(!StringUtils.isEmpty(appUser.getEmail())){
            return appUser.getEmail();
        }
        if(!StringUtils.isEmpty(appUser.getPhone())){
            return appUser.getPhone();
        }*/
    }

    /**
     * 检查是不是群发
     * @return
     */
    public boolean checkGroupTempContact(HttpRequestData requestData, AppUserToken token, String group, AppCallList friendInfo){
            String receiver =requestData.getString("receiver");
            String meiqia =requestData.getString("meiqia");
            AppRoomRole appRoomRole =roomChatHelper.getDBRoomHasUser(group,token.getUserId());
            if(appRoomRole==null){
                return false;
            }
            //验证是不是群主发起的对话 验证是不是群员发起的联系客服
            if(AppRoomRole.TYPE_MASTER.equals(appRoomRole.getRoleType())||AppChatConstant.CONSTANT_YES.equals(meiqia)){
                Map content =  new HashMap();
                AppUser appUser = new AppUser();
                BeanUtils.copyProperties(token,appUser);
                content.put("user",appUser);
                content.put("group",group);
                if(AppChatConstant.CONSTANT_YES.equals(meiqia)){
                    content.put("meiqia",AppChatConstant.CONSTANT_YES);
                }
                ChatData chatData = new ChatData(token.getUserId().toString(),receiver,content);
                AppUser firendInfo = null;

                if(friendInfo==null){
                    Map condi =  new HashMap();
                    condi.put("userId",receiver);
                    condi.put("httpPath",serviceConfigSet.getHttpServerPath());

                     firendInfo = appUserMapper.selectByIdJoinFile(condi);
                }
                //向好友发送通知
                appChatMessageService.notifyTempContact(chatData,requestData,firendInfo);
            }else{

                AppNotifyCacheUtil.syncRemoveNotify(token.getUserId().toString(),receiver,AppChatConstant.TYPE_TEMP_CONTACT);
            }
            return true;
    }
    @Override
    public void searchUserContact(HttpRequestData requestData) {

        AppUserToken token = AppTokenCacheUtil.currentUser();
        Long receiver = requestData.getLong("receiver");
            String group = requestData.getString("group");
        if(!roomChatHelper.isJoinContactRule(token,requestData)||StringUtils.isEmpty(receiver)){
            requestData.createErrorResponse(SysTips.PARAM_ERROR);
            return;
        }
        AppCallList friendInfo = appCallListMapper.selectOne(new AppCallList(receiver,token.getUserId()));
        //验证 是不是群组里发起的临时对话
        if(StringUtils.isEmpty(group)){
            if(friendInfo==null){
                requestData.createErrorResponse("您不是对方的好友");
                return;
            }
        }else{
            if(!checkGroupTempContact(requestData,token,group,friendInfo)){
                requestData.createErrorResponse(SysTips.PARAM_ERROR);
                return;
            }
        }

        AppRoom tempRoom = new AppRoom();
        String  roomId = roomChatHelper.generateFinalContactRoomId(receiver,token.getUserId());
        tempRoom.setRoomCode(roomId);
        tempRoom.setFindChatRecordNumber(serviceConfigSet.getAppUserFindRecord());
        tempRoom.setRoomType(AppChatConstant.SOCKET_TYPE_CONTACT);
        ChatRoomCacheModel chatRoomCacheModel = roomChatHelper.joinAppChatRoom(tempRoom,null);

        Map map = new HashMap();
        map.put("userId",receiver);
        map.put("httpPath",serviceConfigSet.getHttpServerPath());
        AppUser appUser = appUserMapper.selectByIdJoinFile(map);
        if(friendInfo!=null){
            appUser.setNickName(friendInfo.getRemark());
        }
        map.put("friend",appUser);
        map.put("self",token);
        map.put("roomId",roomId);
        map.put("serverPort",serviceConfigSet.getWebsocketServerPort());
        //map.put("records",chatRoomCacheModel.getHistoryChatRecords(50));
        List<ChatData>  historyRecord = appChatRecordService.searchChatDataByRoom(chatRoomCacheModel.getAppRoom());
        map.put("records",historyRecord);
        requestData.fillResponseData(map);

    }

    @Override
    public void info(HttpRequestData requestData) {
        Long targetUserId = requestData.getLong("target");
        AppUserToken token =  AppTokenCacheUtil.currentUser();
        if(targetUserId==null||token==null){
            requestData.createErrorResponse(SysTips.PARAM_ERROR);
            return;
        }
        Map condition = new HashMap();
        condition.put("httpPath",serviceConfigSet.getHttpServerPath());
        condition.put("userId",targetUserId);
        AppUser target = appUserMapper.selectByIdJoinFile(condition);

        requestData.fillResponseData(target);
       // appUserMapper.selectById();
    }

    @Override
    public void searchFirend(HttpRequestData requestData) {
        String account = requestData.getString("account");
        AppUserToken token =  AppTokenCacheUtil.currentUser();
        if(account==null||token.getAccount().equals(account)){
            requestData.createErrorResponse(SysTips.PARAM_ERROR);
            return;
        }
        Map condition = new HashMap();
        condition.put("httpPath",serviceConfigSet.getHttpServerPath());
        condition.put("account",account);
        AppUser target = appUserMapper.selectByAccountJoinFile(condition);
        if(target.getUserId().toString().equals(token.getUserId().toString())){
            return;
        }

        requestData.fillResponseData(target);

    }

    @Transactional
    @Override
    public void addFirend(HttpRequestData requestData) {
        Long userId = requestData.getLong("userId");
        AppUserToken token =  AppTokenCacheUtil.currentUser();
        if(userId==null){
            requestData.createErrorResponse(SysTips.PARAM_ERROR);
            return;
        }
        int rows = appCallListMapper.selectCount(MybatisUtil.conditionT()
                .eq("creator",token.getUserId())
                .and()
                .eq("user_id",userId));
        if(rows>0){
            requestData.createErrorResponse(SysTips.APP_CALL_LIST_REPEATE);
            return;
        }
        Map condition = new HashMap();
        condition.put("httpPath",serviceConfigSet.getHttpServerPath());
        condition.put("userId",userId);
        AppUser au= new AppUser();
        BeanUtils.copyProperties(token,au);
        //AppUser ap =  appUserMapper.selectByIdJoinFile(condition);
        ChatData chatData = new ChatData(token.getUserId().toString(),userId.toString(),au);
        appChatMessageService.notifyFriendRequest(chatData,requestData);

    }


    @Transactional
    @Override
    public void agreeFriend(HttpRequestData requestData) {
        Long userId = requestData.getLong("userId");
        String nickname = requestData.getString("nickname");
        AppUserToken token =  AppTokenCacheUtil.currentUser();
        if(userId==null){
            requestData.createErrorResponse(SysTips.PARAM_ERROR);
            return;
        }
        int rows = appCallListMapper.selectCount(MybatisUtil.conditionT()
                .eq("creator",token.getUserId())
                .and()
                .eq("user_id",userId));
        if(rows>0){
            //requestData.createErrorResponse(SysTips.APP_CALL_LIST_REPEATE);
            agreeDeleteFriend(userId,requestData);
            return;
        }
        AppCallList appCallList = new AppCallList();
        appCallList.setCreator(token.getUserId());
        appCallList.setRemark(nickname);
        appCallList.setUserId(userId);
        super.insertDefaultValForApp(appCallList,false);
        //把好友添加到自己的通讯录
        appCallListMapper.insert(appCallList);
        //把自己添加到好友的通讯录
        rows = appCallListMapper.selectCount(MybatisUtil.conditionT()
                .eq("creator",userId)
                .and()
                .eq("user_id",token.getUserId()));
        if(rows == 0){
            appCallList = new AppCallList();
            appCallList.setCreator(userId);
            appCallList.setRemark(token.getNickName());
            appCallList.setUserId(token.getUserId());
            super.insertDefaultValForApp(appCallList,false);
            appCallListMapper.insert(appCallList);
            //通知好友添加成功

            CallListDto cd = new CallListDto();
            BeanUtils.copyProperties(token,cd);
            cd.setReceiver(userId.toString());
            cd.setRemark(token.getSignature());
            cd.setType("user");
            cd.setAge(BirthdayUtils.getAgeByBirthday(token.getBirthday()));
            cd.setNameLetter(Cn2Spell.converterToSpell(cd.getNickName()));
            ChatData chatData = new ChatData(token.getUserId().toString(),userId.toString(),cd);
            appChatMessageService.notifyFriendAgreeRequest(chatData,requestData);
        }
            //好友信息返送回去
            Map map = new HashMap();
            map.put("userId",userId);
            map.put("httpPath",serviceConfigSet.getHttpServerPath());
            AppUser appUser = appUserMapper.selectByIdJoinFile(map);
            CallListDto call = new CallListDto();
            BeanUtils.copyProperties(appUser,call);
            call.setAccount(getAccout(appUser));
            call.setRemark(appUser.getSignature());
            call.setNameLetter(Cn2Spell.converterToSpell(call.getNickName()));
            call.setReceiver(userId.toString());
            call.setAge(BirthdayUtils.getAgeByBirthday(appUser.getBirthday()));
            call.setType("user");
            requestData.fillResponseData(call);

        //创建一个聊天Room
    }


    public void agreeDeleteFriend(Long deleteFriend,HttpRequestData requestData){
        AppUserToken token = AppTokenCacheUtil.currentUser();
       int rows = appCallListMapper.selectCount(MybatisUtil.conditionT()
                .eq("creator",deleteFriend)
                .and()
                .eq("user_id",token.getUserId()));
        if(rows == 0){
            AppCallList   appCallList = new AppCallList();
            appCallList.setCreator(deleteFriend);
            appCallList.setRemark(token.getNickName());
            appCallList.setUserId(token.getUserId());
            super.insertDefaultValForApp(appCallList,false);
            appCallListMapper.insert(appCallList);
            //通知好友添加成功

            CallListDto cd = new CallListDto();
            BeanUtils.copyProperties(token,cd);
            cd.setReceiver(deleteFriend.toString());
            cd.setRemark(token.getSignature());
            cd.setType("user");
            cd.setAge(BirthdayUtils.getAgeByBirthday(token.getBirthday()));
            cd.setNameLetter(Cn2Spell.converterToSpell(cd.getNickName()));
            ChatData chatData = new ChatData(token.getUserId().toString(),deleteFriend.toString(),cd);
            appChatMessageService.notifyFriendAgreeRequest(chatData,requestData);
        }
    }

    @Transactional
    @Override
    public void removeFriend(HttpRequestData requestData) {
        Long userId = requestData.getLong("receiver");
        AppUserToken token =  AppTokenCacheUtil.currentUser();
        if(userId==null){
            requestData.createErrorResponse(SysTips.PARAM_ERROR);
            return;
        }
        appCallListMapper.delete(MybatisUtil.conditionT().eq("creator",token.getUserId()).and().eq("user_id",userId));
        EntityWrapper firend = (EntityWrapper) MybatisUtil.conditionT().eq("creator",userId).and().eq("user_id",token.getUserId());
        if(appCallListMapper.selectCount(firend)==0){
            //如果对方也把你删除，则删除彼此的聊天房间
            AppChatCacheUtil.removeRoom(roomChatHelper.generateFinalContactRoomId(userId,token.getUserId()));
        }
      //  appCallListMapper.delete(firend);

        //appChatMessageService.notifyFriendRemove()
    }
    @Transactional
    @Override
    public void removeGroup(HttpRequestData requestData) {
        Long roomId = requestData.getLong("receiver");
        AppUserToken token =  AppTokenCacheUtil.currentUser();
        if(roomId==null){
            requestData.createErrorResponse(SysTips.PARAM_ERROR);
            return;
        }
        appCallListMapper.delete(MybatisUtil.conditionT().eq("creator",token.getUserId()).and().eq("room_id",roomId));
    }

    @Override
    public void modifyDetail(AppUser appUser, HttpRequestData requestData) {
        appUser.setUserId(AppTokenCacheUtil.currentUser().getUserId());
        String userName = appUser.getUserName();
        AppUserToken token = AppTokenCacheUtil.currentUser();
        //username 分支
        if (!StringUtils.isEmpty(userName)){
            if(userName.length()<6||userName.length()>20){
                requestData.createErrorResponse(SysTips.PARAM_ERROR);
                return;
            }
            AppUser self =  appUserMapper.selectById(token.getUserId());
            if(self!=null&&!StringUtils.isEmpty(self.getUserName())){
                requestData.createErrorResponse(SysTips.PARAM_ERROR);
                return;
            }
            int row =appUserMapper.selectCount(MybatisUtil.conditionT().eq("user_name",userName));
            if(row>0){
                requestData.createErrorResponse(SysTips.APP_USERNAME_REPEAT);
                return;
            }
        }
        if(!StringUtils.isEmpty(appUser.getNickName())){
            AppRoomRole roomRole = new AppRoomRole();
            roomRole.setUserNickname(appUser.getNickName());
            token.setNickName(appUser.getNickName());
            AppTokenCacheUtil.pushExpire(token);
            appRoomRoleMapper.update(roomRole,MybatisUtil.conditionT().eq("user_id",token.getUserId()));
        }



        //头像 分支
        Long fileId = appUser.getFileId();
        if(fileId!=null){
            String filePath = appUser.getFilePath();
            String module = requestData.getString("module");
            token.setFilePath(serviceConfigSet.getHttpServerPath()+ module+RemoteFileSFTPImpl.LINUX_FILE_SEPARATOR+filePath);
            AppTokenCacheUtil.pushExpire(token);

        }


        appUserMapper.updateColumById(appUser);

    }

    @Autowired
    private AppChatRecordMapper appChatRecordMapper;
    @Override
    public void removeChatMsg(HttpRequestData requestData) {
        AppUserToken token = AppTokenCacheUtil.currentUser();
        String receiver = requestData.getString("id");
        if(StringUtils.isEmpty(receiver)){
            requestData.createErrorResponse(SysTips.PARAM_ERROR);
            return;
        }

        AppNotifyCacheUtil.syncRemoveNotify2(token.getUserId().toString(),receiver,AppChatConstant.TYPE_TEMP_CONTACT);
        String roomId = roomChatHelper.generateFinalContactRoomId(token.getUserId(),new Long(receiver));
        AppChatCacheUtil.removeRoom(roomId);

    }



    @Override
    public void changeIntegral(Long userId, Double val) {
        Map map = new HashMap();
        map.put("userId",userId);
        map.put("val",val);
        appUserMapper.updateIntegralById(map);
    }


    @Override
    public void searchChatUserByPage(HttpRequestData requestData) {
        Wrapper<AppUser> condition = MybatisUtil.conditionT();
        condition.eq("enable", SysConstantSet.COLUMN_ENABLE_Y);
        condition.eq("user_type", AppChatConstant.USER_TYPE_1);
        String searchText = requestData.getString("searchText");
        if(!StringUtils.isEmpty(searchText)){
            condition.andNew().like("user_name",searchText);
            condition.or().like("phone",searchText);
            condition.or().like("email",searchText);
        }
        condition.orderBy("creation_time", false);
        condition.orderBy("integral", false);

        Page<AppUser> page = Pages.create(requestData.getInteger("page"));
        int count = appUserMapper.selectCount(condition);
        List<AppUser> data = appUserMapper.selectPage(page, condition);
        requestData.generatePageData(data, count);
    }

    @Override
    public void searchChatUserNoneAccountByPage(HttpRequestData requestData) {
        Wrapper<AppUser> condition = MybatisUtil.conditionT();
        condition.eq("enable", SysConstantSet.COLUMN_ENABLE_Y);
        condition.eq("user_type", AppChatConstant.USER_TYPE_1);
        String searchText = requestData.getString("searchText");
        if(!StringUtils.isEmpty(searchText)){
            condition.andNew().like("user_name",searchText);
            condition.or().like("phone",searchText);
            condition.or().like("email",searchText);
        }
        condition.orderBy("creation_time", false);
        condition.orderBy("integral", false);

        Page<AppUser> page = Pages.create(requestData.getInteger("page"));
        int count = appUserMapper.selectCount(condition);
        List<AppUser> data = appUserMapper.selectPage(page, condition);
        for(AppUser ap :data){
            ap.setPhone(null);
            ap.setUserName(null);
        }
        requestData.generatePageData(data, count);
    }

    @Autowired
    private IAppActivityIntegralService appActivityIntegralService;


    @Transactional
    @Override
    public void rechargeIntegral(HttpRequestData requestData) {
        try {
            Long userId =requestData.getLong("userId");
            String val = requestData.getString("val");
            Map map = new HashMap();
            map.put("userId",userId);
            map.put("val",new Double(val).doubleValue());
            int rows = appUserMapper.updateIntegralById(map);
            if(rows==0){
                requestData.createErrorResponse(SysTips.PARAM_ERROR);
                return;
            }
            appActivityIntegralService.addIntegralRecord(userId,new Double(val),AppChatConstant.ACTIVITY_INTEGRAL_RECHARGE);
        }catch (Exception e){
            requestData.createErrorResponse(SysTips.PARAM_ERROR);
        }
    }

    @Override
    public void modifyPwd(HttpRequestData requestData) {
        String pwd1 = requestData.getString("pwd1");//原始密码
        String pwd2 = requestData.getString("pwd2");//新密码
        String pwd3 = requestData.getString("pwd3");//重复密码
        AppUser self = appUserMapper.selectById(AppTokenCacheUtil.currentUser().getUserId());
        if(!self.getPassword().equals(Md5Util.getMD5(pwd1+serviceConfigSet.getUserSalt()))){
            requestData.createErrorResponse("原始密码错误");
            return;
        }
        if(!pwd2.equals(pwd3)){
            requestData.createErrorResponse("两次密码输入不一致");
            return;
        }
        AppUser appUser = new AppUser();
        appUser.setUserId(self.getUserId());
        appUser.setPassword(Md5Util.getMD5(pwd2+serviceConfigSet.getUserSalt()));
        appUserMapper.updateById(appUser);
    }

    @Override
    public void modifyRemark(HttpRequestData requestData) {
        String targetId = requestData.getString("targetId");
        String remark = requestData.getString("remark");
        if(StringUtils.isEmpty(targetId)||StringUtils.isEmpty(targetId)){
            requestData.createErrorResponse(SysTips.PARAM_ERROR);
            return;
        }
        AppUserToken token = AppTokenCacheUtil.currentUser();

        int rows = appCallListMapper.selectCount(MybatisUtil.conditionT().eq("creator",token.getUserId()).and().eq("user_id",targetId));
        if(rows==0){
            requestData.createErrorResponse("黑接口有意思？");
            return;
        }
        AppCallList up = new AppCallList();
        up.setRemark(remark);
        appCallListMapper.update(up,MybatisUtil.conditionT().eq("creator",token.getUserId()).and().eq("user_id",targetId));
    }

    @Override
    public void redirectLogic(HttpRequestData requestData) {
        String group = requestData.getString("group");


        int rows = this.selectCount(MybatisUtil.conditionT());
        rows += serviceConfigSet.getAppUserBaseCount();
        Map map = new HashMap();
        map.put("numbers",rows);

        if(!StringUtils.isEmpty(group)){
            boolean open = true;
            if(!AppGroupQRCodeCacheUtil.existQRCode(group)){
                open = false;
            }else{
                Map qrc = AppGroupQRCodeCacheUtil.getQRCode(group);
                long  effictiveTime = (Long) qrc.get("effictiveTime");
                long  creationTime = (Long) qrc.get("creationTime");
                if(effictiveTime<(System.currentTimeMillis()-creationTime)){
                    open = false;
                }
            }
            if(!open){
                requestData.createErrorResponse("该页面已过期失效");
                return;
            }
            map.put("room","resources/appchat/page/chat/room.jsp?roomId="+group);
        }
        requestData.fillResponseData(map);
    }

    @Override
    public void searchGroupRecordByPage(HttpRequestData requestData) {
        String roomId = requestData.getString("roomId");
        String msguuid = requestData.getString("msguuid");
        if(StringUtils.isEmpty(roomId)||StringUtils.isEmpty(msguuid)){
            requestData.createErrorResponse(SysTips.PARAM_ERROR);
            return;
        }
        AppUserToken token = AppTokenCacheUtil.currentUser();
        ChatRoomCacheModel cacheModel = AppChatCacheUtil.getRoom(roomId);
        if(cacheModel.getUser(token.getUserId().toString()) ==null){
            requestData.createErrorResponse(SysTips.PARAM_ERROR);
            return;
        }
        AppChatRecord targetRecord  = appChatRecordMapper.selectOne(new AppChatRecord(msguuid,roomId));
        if(targetRecord==null){
            requestData.createErrorResponse("服务正忙，请重试");
            return;
        }
        AppRoom appRoom = cacheModel.getAppRoom();
       List<ChatData> chatDatas =  appChatRecordService.searchChatRoomDataByPage(appRoom,targetRecord.getChatRecordId());
       requestData.fillResponseData(chatDatas);

    }
}