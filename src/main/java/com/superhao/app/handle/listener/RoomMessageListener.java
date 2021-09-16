package com.superhao.app.handle.listener;

import com.alibaba.fastjson.JSONObject;
import com.superhao.app.constant.AppChatConstant;
import com.superhao.app.entity.dto.ChatData;
import com.superhao.app.entity.token.AppUserToken;
import com.superhao.app.handle.RoomChatHandle;
import com.superhao.app.service.IAppRobotRoomService;
import com.superhao.app.service.impl.AppRobotRoomServiceImpl;
import com.superhao.base.log.util.SysLogRecordUtil;
import com.superhao.base.util.SerializeUtil;
import com.superhao.base.util.SpringContexUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;

import javax.websocket.Session;
import java.util.HashMap;
import java.util.Map;

/**
 * @Auther: super
 * @Date: 2019/11/7 11:09
 * @email:
 */
@Slf4j
public class RoomMessageListener implements MessageListener {

    private RoomChatHandle handle;
    public RoomMessageListener(RoomChatHandle handle){
        this.handle = handle;
    }
    public RoomMessageListener(){

    }

    public static void main(String[] args) {
        Map map = new HashMap();
        map.put("i",1);
        map.put("o",new ChatData("1","1","2"));
        String js = JSONObject.toJSONString(map);
        Map re = (Map) ((Map)JSONObject.parse(js)).get("o");
        System.out.println(re.get("sender"));
    }

    @Override
    public void onMessage(Message message, byte[] bytes) {
        String msg = (String) SerializeUtil.unserialize(message.getBody());
        Map redisData = (Map) JSONObject.parse(msg);



        AppUserToken receiverToken = handle.getUserToken();
        String sender = (String) ((JSONObject)redisData.get("data")).get("sender");
        boolean self= (boolean) redisData.get("self");
        boolean mass= (boolean) redisData.get("mass");

        //不发给自己
        if(!self&&sender.equals(receiverToken.getUserId().toString())){
            return;
        }
        //是不是群发
        if(!mass){
           Map chatData = (Map) redisData.get("data");
           String rec = (String) chatData.get("reciever");
           if(!rec.equals(receiverToken.getUserId().toString())){
               return;
           }
        }
        //聊天机器人处理
        if(AppChatConstant.USER_TYPE_3.equals(receiverToken.getUserType())){
            IAppRobotRoomService robotRoomService = SpringContexUtil.getBean(AppRobotRoomServiceImpl.class);
            robotRoomService.handleChat(handle.getAttribute(),receiverToken,msg);
            return;
        }
        Session wsSession = handle.getChatSession().getSession();
        sendMsg(wsSession,msg);

       /* String loseKey = AppChatMsgLoseCacheUtil.generateMsgLoseKey(handle);
        if(AppChatMsgLoseCacheUtil.existMsgLose(loseKey)){
            List<String> loseMsgList = AppChatMsgLoseCacheUtil.getMsgLose(loseKey);
            for(String loseMsg:loseMsgList){
                sendMsg(wsSession,loseMsg);
            }
        }else{
            sendMsg(wsSession,msg);
        }*/
    }

    private void sendMsg(Session wsSession,String data){
        synchronized (wsSession){
            if(wsSession.isOpen()){
                try {
                    wsSession.getAsyncRemote()
                            .sendText(data);
                    log.info(handle.getUserToken().getUserName()+":订阅了=======");
                   // System.out.println(handle.getUserToken().getUserName()+":订阅了=======");
                  //  System.out.println(data);
                } catch (Exception e) {
                    SysLogRecordUtil.record("RoomMessageListener 出错",e);
                }
            }
           /* else{
                AppChatMsgLoseCacheUtil.syncPush(handle,data);
                System.out.println(handle.getUserToken().getUserName()+":丢掉了=============================");
                System.out.println(data);
            }*/
        }
    }
}
