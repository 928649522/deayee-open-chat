package com.superhao.app.entity;

import com.alibaba.fastjson.JSONObject;
import com.superhao.app.entity.dto.ChatData;
import com.superhao.app.entity.seri.PayloadDecoder;
import com.superhao.app.entity.seri.PayloadEncoder;
import com.superhao.app.entity.token.AppUserToken;
import com.superhao.app.handle.RoomChatHandle;
import lombok.Getter;
import lombok.Setter;

import java.io.*;
import java.util.*;

/**
 * @Auther: super
 * @Date: 2019/10/27 12:56
 * @email:
 */
@Getter
@Setter
public class ChatRoomCacheModel implements Serializable {
    Set<RoomChatHandle> sockets;
    List<ChatData> chatRecord;
    AppRoom appRoom; //聊天房间的属性
    Map<String, Long[]> bannedTalks;//被禁言的人


    public ChatRoomCacheModel(AppRoom appRoom){
        this.sockets = Collections.synchronizedSet(new HashSet<>());
        this.chatRecord = Collections.synchronizedList(new ArrayList<>());
        this.bannedTalks = Collections.synchronizedMap(new HashMap<String,Long[]>());
        this.appRoom = appRoom;
    }


    /**
     * 将聊天记录以JSONString形式返回
     * @param rows
     * @return
     */
    public  List<ChatData>  getHistoryChatRecords(int rows) {

        try{
        List<ChatData> tempList = new ArrayList<>(chatRecord);
        if(tempList.size()>0&&rows>0){
            List<ChatData> target =new ArrayList<>();
            if(rows>tempList.size()){
                rows = tempList.size();
            }
            for (int i =(tempList.size()-rows);i<tempList.size();i++){
                target.add(tempList.get(i));
            }
            return target;
        }
    }catch (Exception e){
        System.out.println(e);
    }
            return null;
    }
    public void addChatRecord(ChatData chatData) {
        if(this.chatRecord.size()>=this.appRoom.getFindChatRecordNumber()){
            this.chatRecord.remove(0);
        }
        this.chatRecord.add(chatData);

    }

    /**
     * 验证该房间是否存在此用户
     * @param tokenKey == userId
     * @return
     */
    public boolean existsUser(String tokenKey) {
        for(RoomChatHandle rc:this.sockets){
            if(rc.getUserToken().getUserId().equals(tokenKey)){
                return true;
            }
        }
        return false;

    }

    /**
     * 获取房间用户
     * @param tokenKey == userId
     * @return
     */
    public AppUserToken getUser(String tokenKey) {
        for(RoomChatHandle rc:this.sockets){
            if(rc.getUserToken().getUserId().toString().equals(tokenKey)){
                return rc.getUserToken();
            }
        }
        return null;

    }



}
