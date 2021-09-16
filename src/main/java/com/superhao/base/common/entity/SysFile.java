package com.superhao.base.common.entity;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.util.Date;

/**
 *
 *
 * @author superhao
 * @email 928649522@qq.com
 * @date 2019-05-05 12:58:07
 */
@Data
@TableName("sys_file_t")
@ToString
@Getter
@Setter
public class SysFile implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 *
	 */
	@JsonFormat(shape = JsonFormat.Shape.STRING)
	@TableId(value = "file_id", type = IdType.ID_WORKER)
	private Long fileId;
	/**
	 * 文件名
	 */
	@TableField(value = "file_name")
	private String fileName;
	/**
	 * 文件唯一标识
	 */
	@TableField(value = "file_md5")
	private String fileMd5;
	/**
	 *文件路径
	 */
	@TableField(value = "file_path")
	private String filePath;
	/**
	 *
	 */
	@TableField(value = "file_size")
	private Integer fileSize;
	/**
	 * 文件类型（注：1：）
	 */
	@TableField(value = "file_type")
	private String fileType;

	/**
	 * 所属模块文件（注：1：）
	 */
	@TableField(value = "module")
	private String module;

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


/**
 SysFile.fileId
 SysFile.fileName
 SysFile.fileMd5
 SysFile.filePath
 SysFile.fileSize
 SysFile.fileType
 SysFile.creationTime
 SysFile.lastUpdateTime
 SysFile.creator
 SysFile.modifier
 SysFile.creationPlace
 SysFile.lastUpdatePlace
 SysFile.status
 SysFile.enable
 SysFile.versionNumber
 */
}
