package com.superhao.base.config.filter;

import com.superhao.base.authz.handler.AuthCoreHandler;
import com.superhao.base.cache.util.SysAuthzUtil;
import com.superhao.base.entity.token.SysUserToken;
import com.superhao.base.util.SpringContexUtil;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @Auther: zehao
 * @Date: 2019/5/15 23:40
 * @email: 928649522@qq.com
 */
public class SwaggerFilter  implements Filter {

    private static  final String SWAGGER_AUTH_URL = "/sys/swagger";

    private static  final String DRUID_AUTH_URL = "/druid/index.html";

    private AuthCoreHandler authCoreHandler;
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        if(authCoreHandler ==null){
            authCoreHandler = SpringContexUtil.getBean(AuthCoreHandler.class);
        }
        String servletPath = request.getServletPath().trim();
        SysUserToken token = SysAuthzUtil.currentSysUser(request);
        if(token==null){
            authCoreHandler.responesLogin(request,response);
            return;
        }
        boolean hasAuth = false;

        if(servletPath.contains("druid")){
            hasAuth = authCoreHandler.authenticate(DRUID_AUTH_URL,token);
        }else{
            hasAuth = authCoreHandler.authenticate(SWAGGER_AUTH_URL,token);
        }
        if(!hasAuth){
            authCoreHandler.responesNoAuth(request,response);
            return;
        }
        filterChain.doFilter(servletRequest, servletResponse);
    }



    @Override
    public void destroy() {

    }
}
