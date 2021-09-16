package com.superhao.base.config.filter;

import com.google.common.base.Splitter;
import com.google.common.collect.Sets;
import com.superhao.base.authz.handler.AuthCoreHandler;
import com.superhao.base.cache.util.SysAuthzUtil;
import com.superhao.base.entity.token.SysUserToken;
import com.superhao.base.util.SpringContexUtil;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Set;

/**
 * @Auther: zehao
 * @Date: 2019/5/13 11:17
 * @email: 928649522@qq.com
 */
public class AuthzFilter implements Filter{

    private static Set<String> exclusionUrlSet = Sets.newConcurrentHashSet();

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        String exclusionUrls = filterConfig.getInitParameter("exclusionUrls");
        List<String> exclusionUrlList = Splitter.on(",").trimResults().omitEmptyStrings().splitToList(exclusionUrls);
        exclusionUrlSet = Sets.newConcurrentHashSet(exclusionUrlList);
    }
//
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        String servletPath = request.getServletPath();

        AuthCoreHandler  authCoreHandler = SpringContexUtil.getBean(AuthCoreHandler.class);

        //放开排除页面
        if (exclusionUrlSet.contains(servletPath)) {
            filterChain.doFilter(servletRequest, servletResponse);
            return;
        }



        //验证权限 /sys/sysPermission/searchList  /sys/sysPermission/searchButtons
        SysUserToken token =SysAuthzUtil.currentSysUser(request);
        if(!authCoreHandler.authenticate(servletPath,token)){
            authCoreHandler.responesNoAuth(request,response);
            return;
        }
        filterChain.doFilter(servletRequest, servletResponse);

    }

    @Override
    public void destroy() {

    }








}
