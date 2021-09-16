package com.superhao.app.handle;

import com.superhao.app.constant.AppChatConstant;
import com.superhao.app.entity.*;
import com.superhao.app.entity.token.AppUserToken;
import com.superhao.app.handle.listener.RoomMessageListener;
import com.superhao.app.mapper.AppCallListMapper;
import com.superhao.app.service.IAppChatRecordService;
import com.superhao.app.service.IAppRoomService;
import com.superhao.app.service.impl.AppChatRecordServiceImpl;
import com.superhao.app.service.impl.AppRoomServiceImpl;
import com.superhao.base.cache.util.AppTokenCacheUtil;
import com.superhao.base.common.constant.SysConstantSet;
import com.superhao.base.log.util.SysLogRecordUtil;
import com.superhao.base.util.MybatisUtil;
import com.superhao.base.util.SpringContexUtil;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.java.Log;
import org.springframework.util.StringUtils;
import org.springframework.web.socket.server.standard.SpringConfigurator;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.io.Serializable;
import java.util.List;

/**
 * @Auther: super
 * @Date: 2019/10/22 16:35
 * @email:
 */
@Log
@ServerEndpoint(value="/app/chat/{roomId}/{roomType}",configurator = SpringConfigurator.class)
@Getter
@Setter
public class RoomChatHandle  implements Serializable{
    private static final long serialVersionUID = 2566709427368791235L;
    /**
     * 服务器消息类型说明：
     * 1.历史聊天通过http方式发送
     * 2.消息、用户状态和其他的通过websocket
     */

    /**
     * 聊天室属性
     */
    private AppRoom attribute;

    /**
     * 会话对象
     */
    private ChatSession chatSession;

    /**
     * 用户信息
     */
    private AppUserToken userToken;




    private transient IAppRoomService appRoomService;

    private transient IAppChatRecordService appChatRecordService;

    private transient RoomChatHelper roomChatHelper;

    private transient RoomMessageListener messageListener;

    private transient AppCallListMapper appCallListMapper;


    //private transient List<RoomMessageListener> topicListenerList;

    //AppChatConstant 根据房间类型进行  topic订阅
    //group直接根据roomId
    //contact根据两者id md5生成 roomId
    //额外订阅的消息有  临时会话temp
    //系统提示公共消息

    public  boolean initObjParam(String roomId, String roomType, Session session) {

        this.appChatRecordService = SpringContexUtil.getBean(AppChatRecordServiceImpl.class);
        this.roomChatHelper = SpringContexUtil.getBean(RoomChatHelper.class);

        this.chatSession = new ChatSession( session);

        if (!AppTokenCacheUtil.auth(chatSession.getParam("tokenKey"), chatSession.getParam("tokenCode"))) {
            return false;
        }
        this.userToken = AppTokenCacheUtil.get(chatSession.getParam("tokenKey"));
        if(this.userToken == null|| SysConstantSet.COLUMN_ENABLE_N.equals(this.userToken.getEnable())){  //用户未登录 或被禁用
            return false;
        }

        //roomType分类
        /**
         * 创建一个群聊websocket
         */
        if(AppChatConstant.SOCKET_TYPE_GROUP.equals(roomType)){
            this.appRoomService = SpringContexUtil.getBean(AppRoomServiceImpl.class);
            this.attribute = appRoomService.selectById(roomId);
            return this.createGroupHandle(roomId);

        }
        /**
         * 创建一个主页websocket
         */
        if(AppChatConstant.SOCKET_TYPE_CALL_LIST.equals(roomType)){
            appCallListMapper = SpringContexUtil.getBean(AppCallListMapper.class);
            return this.createCallListHandle();

        }
        /**
         * 创建一个联系人websocket
         */
        if(AppChatConstant.SOCKET_TYPE_CONTACT.equals(roomType)){
            appCallListMapper = SpringContexUtil.getBean(AppCallListMapper.class);
            return this.createContactHandle(roomId);

        }
        return false;
    }

    private boolean createContactHandle(String roomId) {
        if(!roomChatHelper.isJoinContactRule(this.userToken, null)){
            return false;
        }
        String receiver = this.chatSession.getParam("receiver");
        String group = this.chatSession.getParam("group");
        int row = this.appCallListMapper.selectCount(MybatisUtil.conditionT().eq("user_id",receiver).and().eq("creator",this.userToken.getUserId()));
        AppRoomRole appRoomRole = null;
        if(!StringUtils.isEmpty(group)) { //临时会话
            appRoomRole = roomChatHelper.getDBRoomHasUser(group, this.userToken.getUserId());
        }
        if(appRoomRole==null&&row==0){
            return false;
        }
        AppRoom tempRoom = new AppRoom();
        tempRoom.setRoomCode(roomId);
        tempRoom.setFindChatRecordNumber(roomChatHelper.getServiceConfigSet().getAppUserFindRecord());
        tempRoom.setRoomType(AppChatConstant.SOCKET_TYPE_CONTACT);
        this.attribute = tempRoom;
       roomChatHelper.joinAppChatRoom(tempRoom,this);
        messageListener =  new RoomMessageListener(this);
        roomChatHelper.addUserMsgListenner(messageListener,roomId);
        return true;
    }
    private boolean createCallListHandle() {
        if(!roomChatHelper.isJoinCallListRule(this.userToken)){
            return false;
        }

        messageListener =  new RoomMessageListener(this);
        int count = appCallListMapper.selectCount(MybatisUtil.conditionT().eq("creator",this.userToken.getUserId()));
        if(count>0){
            List<AppRoom> groupSet = appCallListMapper.selectRoomById(this.userToken.getUserId());
            for(AppRoom item:groupSet){
             //   roomChatHelper.joinAppChatRoom(item,this);
                roomChatHelper.addUserMsgListenner(messageListener,item.getRoomId().toString());
            }
            List<AppUser> userSet = appCallListMapper.selectUserById(this.userToken.getUserId());
            for(AppUser item:userSet){
                String roomId = roomChatHelper.generateFinalContactRoomId(this.userToken.getUserId(),item.getUserId());
                AppRoom temp = new AppRoom();
                temp.setRoomCode(roomId);
                temp.setFindChatRecordNumber(50);
                temp.setRoomType(AppChatConstant.SOCKET_TYPE_CALL_LIST);
              //  roomChatHelper.joinAppChatRoom(temp,this);
                roomChatHelper.addUserMsgListenner(messageListener,roomId);
            }
        }
        //加入系统话题
        roomChatHelper.addUserMsgListenner(messageListener,AppChatConstant.TOPIC_SYS_NOTIFY);

        return true;
    }

    private boolean createGroupHandle(String roomId) {

        if (this.attribute == null||!roomChatHelper.isJoinGroupRule(this.attribute,this.userToken, null)) {
            return false;
        }
        this.attribute.setRoomType(AppChatConstant.SOCKET_TYPE_GROUP);
        roomChatHelper.joinAppChatRoom(this.attribute,this);

        log.info("连接成功");
        messageListener =  new RoomMessageListener(this);
        roomChatHelper.addUserMsgListenner(messageListener,roomId);
        //通知群员，该用户上线
        roomChatHelper.noticeThisUserOnline(this);
        return true;

    }


    /**
     * @param @param  userId 用户id
     * @param @param  session websocket连接的session属性
     * @param @throws IOException
     * @Title: onOpen
     * @Description: websocekt连接建立时的操作
     */
    @OnOpen
    public void onOpen(@PathParam("roomId") String roomId,@PathParam("roomType")String roomType, Session session) throws IOException {
        //验证请求参数
        //初始化参数
        //判断房间是否存在缓存中 不存在新增  存在连接对象拉进聊天室

        if (StringUtils.isEmpty(roomId)
                || StringUtils.isEmpty(roomType)) {
            session.close();
            log.info("非法连接");
            return;
        }

        if (!this.initObjParam(roomId,roomType,session)){
            session.close();
            log.info("非法连接");
            return;
        }



    }


    @OnMessage
    public void onMessage(String message, Session session) {
        //session.setMaxIdleTimeout();
        if (session == null) {
            log.warning("session 为空");
        }
        // sendChatMessageToUser(message);
        //判断是否为匿名用户
    }

    /**
     * @param @param session 该连接的session
     * @param @param error 发生的错误
     * @Title: onError
     * @Description: 连接发生错误时候的操作
     */
    @OnError
    public void onError(Session session, Throwable error) {
        log.warning(SysLogRecordUtil.getStackTrace(error));
    }

    /**
     * 关闭连接
     */
    @OnClose
    public void close() {
        try {
            try {

                this.getRoomChatHelper().removeRoomUser(this);
                this.getChatSession().getSession().close();
            } catch (IllegalStateException e) {
                log.warning("WebSocket前端断开连接");
            } catch (Exception e) {
                log.warning(""+e.getMessage());
            }
        } catch (Exception ex) {
            SysLogRecordUtil.record("WebSocket关闭连接出错",ex);
            log.warning("WebSocket关闭连接出错："+SysLogRecordUtil.getStackTrace(ex));
        }
    }






}
