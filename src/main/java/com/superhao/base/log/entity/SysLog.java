package com.superhao.base.log.entity;

import java.io.Serializable;
import java.util.Date;
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
 * @date 2019-10-23 10:17:12
 */
@Data
@TableName("sys_log_t")
@ToString
@Getter
@Setter
public class SysLog implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 */
	@TableId(value = "log_id", type = IdType.AUTO)
	@TableField(value = "log_id")
private Long logId;
	/**
	 * 模块
	 */
	@TableField(value = "module")
private String module;
	/**
	 * 日志类型
	 */
	@TableField(value = "type")
private String type;
	/**
	 * 
	 */
	@TableField(value = "param")
private String param;
	/**
	 * 日志详细
	 */
	@TableField(value = "description")
private String description;
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
	/**
	 * 创建记录的地点
	 */
	@TableField(value = "creation_place")
private String creationPlace;

/**
	SysLog.logId
	SysLog.module
	SysLog.type
	SysLog.param
	SysLog.description
	SysLog.creationTime
	SysLog.creator
	SysLog.creationPlace
 */
}
