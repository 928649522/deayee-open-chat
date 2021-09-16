package com.superhao.app.entity.dto;

import com.baomidou.mybatisplus.annotations.TableField;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

/**
 * @Auther: super
 * @Date: 2019/10/23 12:10
 * @email:
 */
@Setter
@Getter
public class ChatData implements Serializable{

    public transient static String TYPE_TEXT = "text";
    public transient static String TYPE_SHARE = "share";


    private static final long serialVersionUID = 3588636942721679033L;
    private String uuid;
    private String type;
    private String sender ;
    private String reciever;
    private Object content;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date creationTime;


    public ChatData(String type) {
        this.type = type;
        this.creationTime = new Date();
    }

    public ChatData(Long sender, Long reciever, String chatData, Date creationTime, String type){
        this.sender = sender.toString();
        this.reciever = reciever.toString();
        this.creationTime = creationTime;
        this.content =chatData;
        this.type = type;
    }

    public ChatData(String uuid, String type, String sender, String reciever, Object content, Date creationTime) {
        this.uuid = uuid;
        this.type = type;
        this.sender = sender;
        this.reciever = reciever;
        this.content = content;
        this.creationTime = creationTime;
    }


    public static ChatData create(String uuid, String type, String sender, String reciever, Object content, Date creationTime){
        return  new ChatData(uuid,type,sender,reciever,content,creationTime);
    }

    public ChatData(String sender, String reciever, Object content) {
        this.sender = sender;
        this.reciever = reciever;
        this.content = content;
        this.creationTime = new Date();
    }
    public ChatData() {
        this.creationTime = new Date();
    }


    /**
     *
     "id": "short", // 消息类型，
     "sender": "long", // 发送用户唯一id
     "reciever": "long", // 接受用户唯一id
     "data": "string" // 消息内容，如果是文本协议则为文本内容；如果是图片协议则为图片地址；如果是文件协议则为文件地址

     *
     *
     * */

    @Override
    public String toString() {
        return "ChatData{" +
                "uuid='" + uuid + '\'' +
                ", type='" + type + '\'' +
                ", sender='" + sender + '\'' +
                ", reciever='" + reciever + '\'' +
                ", content=" + content +
                ", creationTime=" + creationTime +
                '}';
    }
}
