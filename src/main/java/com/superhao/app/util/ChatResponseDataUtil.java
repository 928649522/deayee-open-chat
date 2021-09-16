package com.superhao.app.util;

import com.alibaba.fastjson.JSONObject;
import com.superhao.app.entity.dto.ChatData;
import com.superhao.app.entity.token.AppUserToken;
import com.superhao.base.common.dto.BaseJsonResult;
import com.superhao.base.common.dto.SysTips;

import java.util.HashMap;
import java.util.Map;

/**
 * @Auther: super
 * @Date: 2019/10/24 11:52
 * @email:
 */
public class ChatResponseDataUtil {




    public static String createChatText(Object chatData,String status){
        BaseJsonResult jsonResult = new BaseJsonResult(true,status,SysTips.REQUEST_SUCCESS,chatData);
        jsonResult.setData(chatData);
        return JSONObject.toJSON(jsonResult).toString();
    }
    public static String toJsonText(Object target, String handleType,boolean sendSelf,boolean massInfo){
        Map data = new HashMap();
        data.put("data",target);
        data.put("id",handleType);
        data.put("self",sendSelf);
        data.put("mass",massInfo);

        return JSONObject.toJSON(data).toString();
    }
    public static String toJsonTextNoSendSelf(Object target, String handleType){
        Map data = new HashMap();
        data.put("data",target);
        data.put("id",handleType);
        data.put("self",false);
        return JSONObject.toJSON(data).toString();
    }
    public static String fail( AppUserToken sender,Long reciever){
        BaseJsonResult jsonResult = new BaseJsonResult(false,SysTips.REQUEST_FAIL);
      //  ChatData cd = new ChatData(item.getCreator(), item.getRoomId(), sender.getUserId().toString(), item.getCreationTime(), reciever.toString());
       // jsonResult.setData(cd);
        return JSONObject.toJSON(jsonResult).toString();
    }

    public static String timeout( AppUserToken sender,Long reciever){
        BaseJsonResult jsonResult = new BaseJsonResult(false,SysTips.REQUEST_TIMEOUT);
      //  ChatData cd = new ChatData(sender.getCreator(), sender.getRoomId(), sender.getUserId().toString(), item.getCreationTime(), reciever.toString());
      //  jsonResult.setData(cd);
        return JSONObject.toJSON(jsonResult).toString();
    }

    public static void main(String[] args) {
      //  System.out.println(success(123L,123L,"dsa"));
    }


}
