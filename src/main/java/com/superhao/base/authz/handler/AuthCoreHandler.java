package com.superhao.base.authz.handler;

import com.superhao.base.authz.entity.SysPermission;
import com.superhao.base.authz.entity.SysUser;
import com.superhao.base.authz.mapper.SysPermissionMapper;
import com.superhao.base.cache.util.SysAuthzUtil;
import com.superhao.base.common.constant.SysServiceConfigSet;
import com.superhao.base.entity.token.SysUserToken;
import com.superhao.base.log.util.SysLogRecordUtil;
import com.superhao.base.util.MybatisUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @Auther: zehao
 * @Date: 2019/5/13 12:12
 * @email: 928649522@qq.com
 */
@Slf4j
@Component
public class AuthCoreHandler {

    private final static String NO_AUTH_URL = "/common/noAuth.page";
    private final static String LOGIN_URL = "/login.page";
    @Autowired
    private SysPermissionMapper sysPermissionMapper;


    @Autowired
    private SysServiceConfigSet configSet;


    
    public boolean authenticate(String servletPath,SysUserToken token){
        //root验证
        if(configSet.getRoot().equals(token.getAccountName())){
            log.info("ROOT超级用户:{} 访问 URL:{}",token.getAccountName(),servletPath);
            return true;
        }
        //验证数据库是否存在此url
        int count =  sysPermissionMapper.selectCount(MybatisUtil.<SysPermission>condition().like("url",servletPath));
        //验证该用户是否存拥有此url  联系列表 昵称 通信列表
        if(count==0||!userExistsPermission(servletPath,token)){
            return false;
        }
        return  true;
    }

    public boolean tokenTimeout(SysUserToken token){
        long current = System.currentTimeMillis();
        if((current-token.getRefreshTime())>token.getTimeoutSeconds()*1000){//超时
            return true;
        }
        token.setRefreshTime(current);
        return false;
    }

    /**
     * 用户是否存在该 url servletPath
     * @param servletPath
     * @param token
     * @return
     */
    private boolean userExistsPermission(String servletPath,SysUserToken token){
        if(token.getPermissionSet()==null||token.getPermissionSet().size()==0){
            log.info("@用户:{} 无权限 URL:{}",token.getAccountName(),servletPath);
            return false;
        }
        log.debug("@用户:{} 通过权限校验 URL:{}",token.getAccountName(),servletPath);
        return token.getPermissionSet().contains(servletPath);
    }

    public SysUserToken singleLogin(SysUser user){
      SysUserToken token =   SysAuthzUtil.getByUserId(user.getUserId().toString());
      if(token!=null){
          SysAuthzUtil.removeSysUserToken(token);
      }
      return new SysUserToken(new Integer(configSet.getSysSessionTimeout()),user.getUserId().toString());
    }


    public void responesLogin(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath();
        if(isAjax(request)){
            response.setHeader("url", LOGIN_URL );
        }else{
            try {
                response.sendRedirect(basePath+LOGIN_URL);
            } catch (Exception e) {
                SysLogRecordUtil.record("AuthCoreHandler responesLogin 出错",e);
            }
        }

    }
    public void responesNoAuth(HttpServletRequest request,HttpServletResponse response) throws IOException {
        String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath();
        if(isAjax(request)){
            response.setHeader("url", NO_AUTH_URL );
        }else{
            response.sendRedirect(basePath+NO_AUTH_URL);
        }
    }
    public  boolean isAjax(HttpServletRequest httpRequest) {
        String xRequestedWith = httpRequest.getHeader("X-Requested-With");
        return (xRequestedWith != null && "XMLHttpRequest".equals(xRequestedWith));
    }

}
