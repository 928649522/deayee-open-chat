package com.superhao.app.entity;

import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import jdk.nashorn.internal.objects.annotations.Constructor;
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
 * @date 2019-10-25 12:58:11
 */
@Data
@TableName("app_room_role_t")
@ToString
@Getter
@Setter
public class AppRoomRole implements Serializable {
	private static final long serialVersionUID = 1L;

	public static final String TYPE_MASTER = "1";//群主
	public static final String TYPE_MANAGER = "2";//管理员
	public static final String TYPE_PEOPLE = "3";//普通成员
	public static final String TYPE_ANONYMOUS = "4";//匿名

	/**
	 * 
	 */
	@TableId(value = "room_role_id", type = IdType.AUTO)
	@JsonFormat(shape = JsonFormat.Shape.STRING)
private Long roomRoleId;
	/**
	 * 成员类型：（1群主；2管理员；3普通成员）
	 */
	@TableField(value = "role_type")
private String roleType;

	/**
	 * 成员类型：（1群主；2管理员；3普通成员）
	 */
	@TableField(value = "user_nickname")
	private String userNickname;
	/**
	 * 聊天室Id
	 */
	@TableField(value = "room_id")
	@JsonFormat(shape = JsonFormat.Shape.STRING)
private Long roomId;
	/**
	 * 成员
	 */
	@TableField(value = "user_id")
	@JsonFormat(shape = JsonFormat.Shape.STRING)
private Long userId;
	/**
	 * 创建时间
	 */
	@TableField(value = "creation_time")
private Date creationTime;
	/**
	 * 修改时间
	 */
	@TableField(value = "last_update_time")
private Date lastUpdateTime;
	/**
	 * 创建人
	 */
	@TableField(value = "creator")
	@JsonFormat(shape = JsonFormat.Shape.STRING)
private Long creator;
	/**
	 * 修改人
	 */
	@TableField(value = "modifier")
	@JsonFormat(shape = JsonFormat.Shape.STRING)
private Long modifier;

	public AppRoomRole() {
	}
	public AppRoomRole(Long userId) {
		this.userId = userId;
	}
	public AppRoomRole(Long userId,Long roomId) {
		this.userId = userId;
		this.roomId = roomId;
	}

	public AppRoomRole(String roleType, Long roomId) {
		this.roleType = roleType;
		this.roomId = roomId;
	}


	/**
	AppRoomRole.roomRoleId
	AppRoomRole.roleType
	AppRoomRole.roomId
	AppRoomRole.userId
	AppRoomRole.creationTime
	AppRoomRole.lastUpdateTime
	AppRoomRole.creator
	AppRoomRole.modifier
 */
}
