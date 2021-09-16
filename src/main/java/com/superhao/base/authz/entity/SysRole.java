package com.superhao.base.authz.entity;

import java.io.Serializable;
import java.util.ArrayList;
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
import java.util.List;

import lombok.Data;

/**
 * 
 * 
 * @author superhao
 * @email
 * @date 2019-05-07 11:27:34
 */
@Data
@TableName("sys_role_t")
@ToString
@Getter
@Setter
public class SysRole implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 角色id
	 */
	@TableId(value = "role_id", type = IdType.ID_WORKER)
	@JsonFormat(shape = JsonFormat.Shape.STRING)
private Long roleId;
	/**
	 * 
	 */
	@TableField(value = "role_name")
private String roleName;
	/**
	 * 
	 */
	@TableField(value = "description")
private String description;
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
	private String permissionIds;


	@TableField(exist = false)
	private  boolean selected=false;


/**
	SysRole.roleId
	SysRole.roleName
	SysRole.description
	SysRole.creationTime
	SysRole.lastUpdateTime
	SysRole.creator
	SysRole.modifier
	SysRole.creationPlace
	SysRole.lastUpdatePlace
	SysRole.status
	SysRole.enable
	SysRole.versionNumber
 */
}
