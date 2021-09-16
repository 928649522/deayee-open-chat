package com.superhao.app.entity;

import com.superhao.app.entity.dto.ChatData;
import com.superhao.app.entity.seri.*;
import com.superhao.app.handle.RoomChatHandle;
import com.superhao.base.util.ReflectionUtils;
import com.superhao.part_time_job.serialize.CityState;
import lombok.Getter;
import lombok.Setter;

import org.apache.tomcat.websocket.WsRemoteEndpointImplBase;
import org.apache.tomcat.websocket.WsSession;
import org.apache.tomcat.websocket.WsWebSocketContainer;

import javax.websocket.DeploymentException;
import javax.websocket.Endpoint;
import javax.websocket.Session;
import java.io.*;
import java.net.URI;
import java.security.PrivateKey;
import java.util.*;


/**
 * @Auther: super
 * @Date: 2019/10/23 16:25
 * @email:
 */
@Getter
@Setter
public class ChatSession   implements Serializable {
    private static final long serialVersionUID = -8905243847014820398L;
    private transient Session session;
    private Map<String,String> param;
    public ChatSession(){
    }
    public ChatSession(Session session){
        this.session = session;
        initRequestParam();
    }

    public ChatSession initRequestParam(){
        param = new HashMap();
        param.putAll(session.getPathParameters());
        if(session.getRequestParameterMap().size()>0){
            for(Map.Entry<String, List<String>> item:session.getRequestParameterMap().entrySet()){
                //只取第一个值
                param.put(item.getKey(),item.getValue().get(0));
            }
        }
        return this;
    }
    public String getParam(String key){
        return param.get(key);
    }

}
