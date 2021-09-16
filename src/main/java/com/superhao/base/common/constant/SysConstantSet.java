package com.superhao.base.common.constant;

import java.security.Permission;

/**
 * @Auther: zehao
 * @Date: 2019/4/26 12:32
 * @email: 928649522@qq.com
 * @Description:
 */
public interface SysConstantSet {
    /**
     * 权限根节点ID
     */
    long PERMISSION_PARENTID_ROOT=-1L;

    /**
     * 系统用户token身份超时时间  （单位：分钟）
     */
    int SYSTEM_AUTHZ_TOKEN_TIMEOUT=10;

    String SYSTEM_AUTHZ_TOKEN_KEY = "TokenCode";


    /**
     * 数据删除操作的类型
     * 修改数据库enable状态为“逻辑删除”
     * 删除数据库记录为“物理删除”
     */
    String SYSTEM_DELETE_TYPE_LOGIC = "logic";
    String SYSTEM_DELETE_TYPE_PHYSICAL ="physical";



    /**
     * 权限类型（注 0:菜单集；1:菜单；2:按钮；3:其它）
     */
    String PERMISSION_TYPE_0="0";
    String PERMISSION_TYPE_1="1";
    String PERMISSION_TYPE_2="2";
    String PERMISSION_TYPE_3="3";

    String COLUMN_ENABLE_Y="Y";
    String COLUMN_ENABLE_N="N";


    String SESSION_USER_KEY="user";
}
