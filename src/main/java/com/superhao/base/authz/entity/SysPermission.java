package com.superhao.base.authz.entity;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;

import java.io.Serializable;
import java.util.Date;

import com.baomidou.mybatisplus.enums.IdType;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.NotNull;

/**
 * 权限实体类
 *
 * @author superhao
 * @email 928649522@qq.com
 * @date 2019-04-25 11:37:14
 */
@Data
@TableName("sys_permission_t")
@ToString
@Getter
@Setter
public class SysPermission implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @TableId(value = "permission_id", type = IdType.AUTO)
    private Long permissionId;
    /**
     * 权限类型（注 0:菜单集；1:菜单；2:按钮）
     */
    @TableField(value = "type")
    @NotNull(message = "{SysPermission.type}")
    private String type;
    /**
     * 权限位置（注 right：主页面右边；left：主页面左边）
     */
    @TableField(value = "position")
    private String position;

    @TableField(value = "param")
    private String param;

    /**
     * 父权限
     */
    @TableField(value = "parent_id")
    private Long parentId;
    /**
     * 权限名称
     */
    @TableField(value = "name")
    @NotEmpty(message = "{SysPermission.name}")
    private String name;
    /**
     * 权限URL
     */
    @TableField(value = "url")
    private String url;
    /**
     * 图标位置
     */
    @TableField(value = "icon")
    private String icon;
    /**
     * 树节点深度（注：从0开始1,2,3,4）
     */
    @TableField(value = "tree_deep")
    private Integer treeDeep;
    /**
     * 树节点深度编码（注：快速查找）
     */
    @TableField(value = "deep_code")
    private String deepCode;
    /**
     * 排序用（注：越大前）
     */
    @TableField(value = "order_number")
    private Integer orderNumber;
    /**
     * 描述
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
    @TableField(value = "last_update_time",update = "now()")
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
    private boolean checked =false;
}
