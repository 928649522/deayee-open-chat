package com.superhao.app.entity;

import java.io.Serializable;
import java.util.*;

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
 * @date 2019-11-21 19:57:33
 */
@Data
@TableName("app_red_point_t")
@ToString
@Getter
@Setter
public class AppRedPoint implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 */
	@JsonFormat(shape = JsonFormat.Shape.STRING)
	@TableId(value = "red_point_id", type = IdType.ID_WORKER)
private Long redPointId;
	/**
	 * 
	 */
	@TableField(value = "room_id")
private String roomId;
	/**
	 * 总积分
	 */
	@TableField(value = "rpcount")
	@JsonFormat(shape = JsonFormat.Shape.STRING)
private Double rpcount;

	/**
	 * 红包个数
	 */
	@TableField(value = "rpsize")
private Integer rpsize;
	/**
	 * 1:普通 0:拼手气
	 */
	@TableField(value = "rptype")
private String rptype;
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
	@JsonFormat(shape = JsonFormat.Shape.STRING)
	private Double beforeRpcount;

	@TableField(exist = false)
	private List<Map<String,Object>> userIds;


	public static boolean contains(AppRedPoint appRedPoint,String userId){
		if(appRedPoint!=null&&appRedPoint.userIds.size()>0){
			for(Map<String,Object> item:appRedPoint.getUserIds()){
				if(userId.equals(item.get("userId").toString())){
					return true;
				}
			}
		}
		return false;
	}

/**
	AppRedPoint.redPointId
	AppRedPoint.roomId
	AppRedPoint.rpcount
	AppRedPoint.rpsize
	AppRedPoint.rptype
	AppRedPoint.remark
	AppRedPoint.creationTime
	AppRedPoint.lastUpdateTime
	AppRedPoint.creator
	AppRedPoint.modifier
	AppRedPoint.creationPlace
	AppRedPoint.lastUpdatePlace
	AppRedPoint.status
	AppRedPoint.enable
	AppRedPoint.versionNumber
 */
}
