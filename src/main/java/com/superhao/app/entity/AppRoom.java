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
 * @author
 * @email
 * @date 2019-10-22 14:52:29
 */
@Data
@TableName("app_room_t")
@ToString
@Getter
@Setter
public class AppRoom implements Serializable {

    private static final long serialVersionUID = 3912129681912622874L;
    /**
     *
     */
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    @TableId(value = "room_id", type = IdType.ID_WORKER)
    private Long roomId;
    /**
     *
     */
    @TableField(value = "room_code")
    private String roomCode;
    /**
     *
     */
    @TableField(value = "room_name")
    private String roomName;
    /**
     * 允许查看聊天记录条数
     */
    @TableField(value = "find_chat_record_number")
    private Integer findChatRecordNumber;
    /**
     * 匿名发言时间限制
     */
    @TableField(value = "anonymous_say_time")
    private Integer anonymousSayTime;
    /**
     * 匿名发言数量限制
     */
    @TableField(value = "anonymous_say_number")
    private Integer anonymousSayNumber;
    /**
     *是否允许匿名进入
     */
    @TableField(value = "is_anonymous")
    private String isAnonymous;
    /**
     * 是否开启图片上传
     */
    @TableField(value = "is_picture")
    private String isPicture;
    /**
     * 群头像
     */
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    @TableField(value = "file_id")
    private Long fileId;
    /**
     * 备注 此处为群公告
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
     * 行记录状态 0用户创建
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
     * 2019-12-8 update
     */
    /**
     * 展示：在线人数基数
     */
    @TableField(value = "online_base_people")
    private Integer onlineBasePeople;
    /**
     * 展示：人数基数
     */
    @TableField(value = "base_people")
    private Integer basePeople;
    /**
     * 展示：被拉黑踢除的人
     */
    @TableField(value = "disable_people")
    private String disablePeople;

    /**
     * 广告
     */
    @TableField(value = "ad")
    private String ad;


    @TableField(exist = false)
    private String filePath;

    @TableField(exist = false)
    private String roomType;
    @TableField(exist = false)
    boolean disableSay = false;//房间是否禁言

/**
 AppRoom.roomId
 AppRoom.roomCode
 AppRoom.roomName
 AppRoom.findChatRecordNumber
 AppRoom.anonymousSayTime
 AppRoom.anonymousSayNumber
 AppRoom.isAnonymous
 AppRoom.isPicture
 AppRoom.fileId
 AppRoom.remark
 AppRoom.creationTime
 AppRoom.lastUpdateTime
 AppRoom.creator
 AppRoom.modifier
 AppRoom.creationPlace
 AppRoom.lastUpdatePlace
 AppRoom.status
 AppRoom.enable
 AppRoom.versionNumber
 */
}
