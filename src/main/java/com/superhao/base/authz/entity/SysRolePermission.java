package com.superhao.base.authz.entity;

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
 * @date 2019-05-07 19:54:26
 */
@Data
@TableName("sys_role_permission_t")
@ToString
@Getter
@Setter
public class SysRolePermission implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 角色-
	 */
	@TableId(value = "role_permission_id", type = IdType.ID_WORKER)
	@JsonFormat(shape = JsonFormat.Shape.STRING)
private Long rolePermissionId;
	/**
	 * 
	 */
	@TableField(value = "permission_id")
private Long permissionId;
	/**
	 * 
	 */
	@TableField(value = "role_id")
private Long roleId;
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

	public SysRolePermission(String enable) {
		this.enable = enable;
	}
	public SysRolePermission() {
	}


	/**
	SysRolePermission.rolePermissionId
	SysRolePermission.permissionId
	SysRolePermission.roleId
	SysRolePermission.creationTime
	SysRolePermission.lastUpdateTime
	SysRolePermission.creator
	SysRolePermission.modifier
	SysRolePermission.creationPlace
	SysRolePermission.lastUpdatePlace
	SysRolePermission.status
	SysRolePermission.enable
 */
}
