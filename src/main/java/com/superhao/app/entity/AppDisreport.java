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
 * @author superhao
 * @email 928649522@qq.com
 * @date 2019-11-17 13:47:27
 */
@Data
@TableName("app_disreport_t")
@ToString
@Getter
@Setter
public class AppDisreport implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 */
	@JsonFormat(shape = JsonFormat.Shape.STRING)
	@TableId(value = "report_id", type = IdType.ID_WORKER)
private Long reportId;
	/**
	 * 
	 */
	@JsonFormat(shape = JsonFormat.Shape.STRING)
	@TableField(value = "target_id")
private Long targetId;
	/**
	 * 
	 */
	@TableField(value = "report_info")
private String reportInfo;
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

/**
	AppDisreport.reportId
	AppDisreport.targetId
	AppDisreport.reportInfo
	AppDisreport.roomId
	AppDisreport.creationTime
	AppDisreport.lastUpdateTime
	AppDisreport.creator
	AppDisreport.modifier
	AppDisreport.creationPlace
	AppDisreport.lastUpdatePlace
	AppDisreport.status
	AppDisreport.enable
	AppDisreport.versionNumber
 */
}
