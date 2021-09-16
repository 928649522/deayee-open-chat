package com.superhao.base.authz.entity;

import com.alibaba.fastjson.annotation.JSONField;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;

import java.io.Serializable;
import java.util.Date;

import com.baomidou.mybatisplus.enums.IdType;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.Size;

/**
 *
 *
 * @author
 * @email
 * @date 2019-04-22 19:24:23
 */
@Data
@TableName("sys_user_t")
@ToString
@Getter
@Setter
public class SysUser implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 *用户
	 */
	@JsonFormat(shape = JsonFormat.Shape.STRING)
	@TableId(value = "user_id", type = IdType.ID_WORKER)
	private Long userId;
	/**
	 *头像Id
	 */
	@JsonFormat(shape = JsonFormat.Shape.STRING)
	@TableField(value = "file_id")
	private Long fileId;
	/**
	 * 账号
	 */
	@Size(max = 50,message = "{SysUser.accountName.max}")
	@NotEmpty(message = "{SysUser.accountName}")
	@TableField(value = "account_name")
	private String accountName;
	/**
	 * 密码
	 */
	@Size(max = 33,message = "{SysUser.password.max}")
	@TableField(value = "password")
	private String password;
	/**
	 * 称呼
	 */
	@TableField(value = "username")
	private String username;
	/**
	 * 性别（0-女；1-男）
	 */
	@TableField(value = "sex")
	private String sex;
	/**
	 * 生日
	 */
	@TableField(value = "birthday")
	@JsonFormat(pattern = "yyyy-MM-dd",timezone="GMT+8")
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date birthday;
	/**
	 * 邮箱
	 */
	@TableField(value = "email")
	private String email;
	/**
	 * 手机号码
	 */
	@TableField(value = "phone")
	private String phone;
	/**
	 * 登陆次数
	 */
	@TableField(value = "login_number")
	private Integer loginNumber;
	/**
	 * 住址
	 */
	@TableField(value = "address")
	private String address;
	/**
	 * 个性签名
	 */
	@TableField(value = "signature")
	private String signature;


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
	private String roleIds;
}
