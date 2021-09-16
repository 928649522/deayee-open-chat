package com.superhao.base.entity;

import com.baomidou.mybatisplus.annotations.TableName;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.util.Date;

/**
 * @Auther: zehao
 * @Date: 2019/4/20 17:11
 * @email: 928649522@qq.com
 * @Description:
 */
@Getter
@Setter
@ToString
@TableName("sys_log_t")
public class SysLog implements Serializable {
    private Integer id;

    private String accountName;

    private String creationPlace;

    private String type;

    private String description;

    private String param;

    private Date creationTime;

    private static final long serialVersionUID = 1L;
}