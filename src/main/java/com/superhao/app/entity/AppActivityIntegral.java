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
 * 活动积分
 * 
 * @author
 * @email
 * @date 2019-11-21 21:43:33
 */
@Data
@TableName("app_activity_integral_t")
@ToString
@Getter
@Setter
public class AppActivityIntegral implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 */
	@TableId(value = "integral_id", type = IdType.ID_WORKER)
	@JsonFormat(shape = JsonFormat.Shape.STRING)
private Long integralId;
	/**
	 * 
	 */
	@TableField(value = "activity_id")
private String activityId;
	/**
	 * 
	 */
	@TableField(value = "integral_number")
	@JsonFormat(shape = JsonFormat.Shape.STRING)
private Double integralNumber;
	/**
	 * 
	 */
	@TableField(value = "type")
private String type;
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
	@JsonFormat(shape = JsonFormat.Shape.STRING)
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
	private String userName;
	@TableField(exist = false)
	private String phone;
	@TableField(exist = false)
	private String email;

/**
	AppActivityIntegral.integralId
	AppActivityIntegral.userId
	AppActivityIntegral.integralNumber
	AppActivityIntegral.type
	AppActivityIntegral.remark
	AppActivityIntegral.creationTime
	AppActivityIntegral.lastUpdateTime
	AppActivityIntegral.creator
	AppActivityIntegral.modifier
	AppActivityIntegral.creationPlace
	AppActivityIntegral.lastUpdatePlace
	AppActivityIntegral.status
	AppActivityIntegral.enable
	AppActivityIntegral.versionNumber
 */
}
