package com.superhao.app.entity;

import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import com.baomidou.mybatisplus.enums.IdType;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableField;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 
 * 
 * @author
 * @email
 * @date 2019-10-24 00:38:53
 */
@Data
@TableName("app_chat_record_t")
@ToString
@Getter
@Setter
public class AppChatRecord implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 *
	 */
	@JsonFormat(shape = JsonFormat.Shape.STRING)
	@TableId(value = "chat_record_id", type = IdType.AUTO)
private Long chatRecordId;


	@TableField(value = "uuid")
	private String uuid;
	/**
	 * 聊天内容
	 */
	@TableField(value = "content")
private String content;
	/**
	 *
	 */
	@TableField(value = "user_id")
private Long userId;
	/**
	 *
	 */
	@TableField(value = "room_id")
private String roomId;
	/**
	 * 创建时间
	 */
	@TableField(value = "creation_time")
private Date creationTime;
	/**
	 * 创建人
	 */
	@TableField(value = "creator")
private Long creator;

	@TableField(value = "type")
	private String type;


	public AppChatRecord() {

	}

	public AppChatRecord(String uuid, String roomId) {
		this.uuid = uuid;
		this.roomId = roomId;
	}

	public AppChatRecord(String content, String roomId, Long creator) {

		this.content = content;
		this.roomId = roomId;
		this.creator = creator;
	}

	public static AppChatRecord createRoomRecord(String content, String type, String uuid, Date creationTime, String roomId, Long creator) {
		return  new AppChatRecord(uuid,creationTime,content,roomId,creator,type);
	}


	public AppChatRecord( String uuid,Date creationTime, String content,  String roomId, Long creator, String type) {
		this.uuid = uuid;
		this.content = content;
		this.roomId = roomId;
		this.creator = creator;
		this.type = type;
		this.creationTime = creationTime;
	}



	public  static AppChatRecord create(String content, String roomId, Long creator){

		return new AppChatRecord(content,roomId,creator);
	}


/**
	AppChatRecord.chatRecordId
	AppChatRecord.content
	AppChatRecord.userId
	AppChatRecord.roomId
	AppChatRecord.createtime
	AppChatRecord.creator
 */
}
