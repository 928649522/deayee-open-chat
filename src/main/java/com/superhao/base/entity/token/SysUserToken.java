package com.superhao.base.entity.token;

import com.superhao.base.authz.entity.SysPermission;
import com.superhao.base.authz.entity.SysUser;
import com.superhao.base.util.UUIDUtils;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;

/**
 * 系统用户的身份令牌实体类
 * @Auther: zehao
 * @Date: 2019/4/26 13:35
 * @email: 928649522@qq.com
 */
@Getter
@Setter
public class SysUserToken extends SysUser implements Serializable{


    private static final long serialVersionUID = -1380520689327944162L;
    public  int timeoutSeconds; //超时时间:单位秒
    private String tokenCode;
    private long createTime;
    private long refreshTime;
    private String loginAddress;
    private Set<String> permissionSet;

    public SysUserToken() {
        this.tokenCode = UUID.randomUUID().toString().replace("-","");
        this.createTime = System.currentTimeMillis();
        this.refreshTime = this.createTime;
        this.timeoutSeconds = 600;
    }
    public SysUserToken(int timeoutSeconds,String userId) {
        this.tokenCode = userId+UUIDUtils.generateShortUuid();
        this.createTime = System.currentTimeMillis();
        this.refreshTime = this.createTime;
        this.timeoutSeconds = timeoutSeconds;
    }

}
