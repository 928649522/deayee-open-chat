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
import org.springframework.format.annotation.DateTimeFormat;

/**
 * 
 * 
 * @author superhao
 * @email 928649522@qq.com
 * @date 2019-11-26 15:03:27
 */
@Data
@TableName("app_robot_room_t")
@ToString
@Getter
@Setter
public class AppRobotRoom implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 */
	@JsonFormat(shape = JsonFormat.Shape.STRING)
	@TableId(value = "robot_room_id", type = IdType.ID_WORKER)
private Long robotRoomId;
	/**
	 * 
	 */
	@JsonFormat(shape = JsonFormat.Shape.STRING)
	@TableField(value = "robot_id")
private Long robotId;
	/**
	 * 
	 */
	@JsonFormat(shape = JsonFormat.Shape.STRING)
	@TableField(value = "room_id")
private Long roomId;
	/**
	 * 
	 */
	@TableField(value = "say_rule")
private String sayRule;
	/**
	 * 
	 */
	@JsonFormat(pattern = "HH:mm:ss",timezone="GMT+8")
	@DateTimeFormat(pattern = "HH:mm:ss")
	@TableField(value = "begin_worktime")
private Date beginWorktime;
	/**
	 * 
	 */
	@JsonFormat(pattern = "HH:mm:ss",timezone="GMT+8")
	@DateTimeFormat(pattern = "HH:mm:ss")
	@TableField(value = "end_worktime")
private Date endWorktime;
	/**
	 * 
	 */
	@TableField(value = "dictionary_type")
private String dictionaryType;
	/**
	 * 
	 */
	@TableField(value = "sleep_time")
private Long sleepTime;
	/**
	 * 
	 */
	@TableField(value = "auto_say")
private String autoSay;
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
	private String nickName;
	@TableField(exist = false)
	private String sex;

	@JsonFormat(pattern = "yyyy-MM-dd",timezone="GMT+8")
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	@TableField(exist = false)
	private Date birthday;
	@TableField(exist = false)
	private String signature;
	@JsonFormat(shape = JsonFormat.Shape.STRING)
	@TableField(exist = false)
	private Long fileId;
	@TableField(exist = false)
	private String filePath;
/**
	AppRobotRoom.robotRoomId
	AppRobotRoom.robotId
	AppRobotRoom.roomId
	AppRobotRoom.sayRule
	AppRobotRoom.beginWorktime
	AppRobotRoom.endWorktime
	AppRobotRoom.dictionaryType
	AppRobotRoom.sleepTime
	AppRobotRoom.autoSay
	AppRobotRoom.remark
	AppRobotRoom.creationTime
	AppRobotRoom.lastUpdateTime
	AppRobotRoom.creator
	AppRobotRoom.modifier
	AppRobotRoom.creationPlace
	AppRobotRoom.lastUpdatePlace
	AppRobotRoom.status
	AppRobotRoom.enable
	AppRobotRoom.versionNumber
 */
}
