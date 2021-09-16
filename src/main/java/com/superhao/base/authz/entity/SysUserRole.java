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
 * 用户角色关联表
 * 
 * @author
 * @email
 * @date 2019-05-11 12:31:16
 */
@Data
@TableName("sys_user_role_t")
@ToString
@Getter
@Setter
public class SysUserRole implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 */
	@TableId(value = "user_role_id", type = IdType.ID_WORKER)
	@JsonFormat(shape = JsonFormat.Shape.STRING)
private Long userRoleId;
	/**
	 * 
	 */
	@TableField(value = "user_id")
private Long userId;
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
	/**
	 * 版本号 默认0
	 */
	@TableField(value = "version_number")
private Integer versionNumber;

    public SysUserRole(Long userId, Long roleId) {
    	this.userId = userId;
    	this.roleId = roleId;
    }

	public SysUserRole(String enable) {
		this.enable = enable;
	}

	public SysUserRole() {

	}


/**
	SysUserRole.userRoleId
	SysUserRole.userId
	SysUserRole.roleId
	SysUserRole.creationTime
	SysUserRole.lastUpdateTime
	SysUserRole.creator
	SysUserRole.modifier
	SysUserRole.creationPlace
	SysUserRole.lastUpdatePlace
	SysUserRole.status
	SysUserRole.enable
	SysUserRole.versionNumber
 */
}
