package com.superhao.app.entity;

import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.superhao.app.constant.AppChatConstant;
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

import javax.validation.constraints.Size;

/**
 * H5聊天app用户表
 * 
 * @author
 * @email
 * @date 2019-10-16 13:57:42
 */
@Data
@TableName("app_user_t")
@ToString
@Getter
@Setter
public class AppUser implements Serializable {
	private static final long serialVersionUID = 1L;


	/**
	 * 
	 */
	@TableId(value = "user_id", type = IdType.ID_WORKER)
	@JsonFormat(shape = JsonFormat.Shape.STRING)
private Long userId;
	/**
	 * 可用作账号
	 */
	@Size(max = 20,message = "{SysUser.accountName.max}")
	@TableField(value = "user_name")
private String userName;
	/**
	 * 昵称
	 */
	@TableField(value = "nick_name")
private String nickName;
	/**
	 * 
	 */
	@Size(max = 33,message = "{SysUser.password.max}")
	@TableField(value = "password")
private String password;
	/**
	 * 用户类型
	 */
	@TableField(value = "user_type")
private String userType;



	@JsonFormat(shape = JsonFormat.Shape.STRING)
	@TableField(value = "file_id")
	private Long fileId;
	/**
	 *
	 */
	@Size(max = 20,message = "{SysUser.accountName.max}")
	@TableField(value = "email")
private String email;
	/**
	 * 电话
	 */
	@Size(max = 20,message = "{SysUser.accountName.max}")
	@TableField(value = "phone")
private String phone;
	/**
	 * 性别
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
	 * 地址
	 */
	@TableField(value = "address")
private String address;
	/**
	 * 签名
	 */
	@TableField(value = "signature")
private String signature;
	/**
	 * 登陆次数
	 */
	@TableField(value = "login_number")
private Integer loginNumber;
	/**
	 * 备注
	 */
	@TableField(value = "remark")
private String remark;
	/**
	 * 创建时间
	 */
	@TableField(value = "creation_time")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
private Date creationTime;
	/**
	 * 修改时间
	 */
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
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
	/**
	 * 积分
	 */
	@TableField(value = "integral")
	private Double integral;

	@TableField(exist = false)
	private String account;

	@TableField(exist = false)
	private String filePath;
	@TableField(exist = false)
	private String roleType;

	@TableField(exist = false)
	private int age;



	@TableField(exist = false)
	@JsonFormat(shape = JsonFormat.Shape.STRING)
	private Long roomRoleId;


	public AppUser(){
		this.userType = AppChatConstant.USER_TYPE_1;
	}
	public AppUser(String nickName,String userType){
		this.nickName = nickName;
		this.userType = userType;
	}

/**
	AppUser.userId
	AppUser.userName
	AppUser.nickName
	AppUser.password
	AppUser.userType
	AppUser.email
	AppUser.phone
	AppUser.sex
	AppUser.birthday
	AppUser.address
	AppUser.signature
	AppUser.loginNumber
	AppUser.remark
	AppUser.creationTime
	AppUser.lastUpdateTime
	AppUser.creator
	AppUser.modifier
	AppUser.creationPlace
	AppUser.lastUpdatePlace
	AppUser.status
	AppUser.enable
	AppUser.versionNumber
 */
}
