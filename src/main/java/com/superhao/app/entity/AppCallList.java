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
 * @date 2019-10-22 15:48:21
 */
@Data
@TableName("app_call_list_t")
@ToString
@Getter
@Setter
public class AppCallList implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 */
	@TableId(value = "call_list_id", type = IdType.ID_WORKER)
	@JsonFormat(shape = JsonFormat.Shape.STRING)
private Long callListId;
	/**
	 * 联系人ID
	 */
	@JsonFormat(shape = JsonFormat.Shape.STRING)
	@TableField(value = "user_id")
private Long userId;
	/**
	 * 群聊ID
	 */
	@JsonFormat(shape = JsonFormat.Shape.STRING)
	@TableField(value = "room_id")
private Long roomId;
	/**
	 * 备注
	 */
	@TableField(value = "remark")
private String remark;
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
private Long creator;
	/**
	 * 修改人
	 */
	@TableField(value = "modifier")
private Long modifier;
	/**
	 * 创建记录的地点
	 */
	@TableField(value = "creation_place")
private String creationPlace;
	/**
	 * 修改记录的地点
	 */
	@TableField(value = "last_update_place")
private String lastUpdatePlace;
	/**
	 * 行记录状态
	 */
	@TableField(value = "status")
private String status;
	/**
	 * Y:启用；N:禁用
	 */
	@TableField(value = "enable")
private String enable;
	/**
	 * 版本号 默认0
	 */
	@TableField(value = "version_number")
private Integer versionNumber;

@TableField(exist = false)
private String filePath;

@TableField(exist = false)
private String tempRoomId;

	public AppCallList(Long callListId) {
		this.callListId = callListId;
	}
	public AppCallList() {
	}

	public AppCallList(Long userId, Long creator) {
		this.userId = userId;
		this.creator = creator;
	}

	/**
	AppCallList.callListId
	AppCallList.userId
	AppCallList.roomId
	AppCallList.remark
	AppCallList.creationTime
	AppCallList.lastUpdateTime
	AppCallList.creator
	AppCallList.modifier
	AppCallList.creationPlace
	AppCallList.lastUpdatePlace
	AppCallList.status
	AppCallList.enable
	AppCallList.versionNumber
 */
}
