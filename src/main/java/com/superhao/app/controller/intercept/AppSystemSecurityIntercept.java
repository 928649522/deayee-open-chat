package com.superhao.app.controller.intercept;

import com.alibaba.fastjson.JSONObject;
import com.superhao.app.constant.AppChatConstant;
import com.superhao.app.entity.AppRoom;
import com.superhao.app.entity.AppUser;
import com.superhao.app.entity.ChatRoomCacheModel;
import com.superhao.app.entity.token.AppUserToken;
import com.superhao.app.mapper.AppRoomMapper;
import com.superhao.app.mapper.AppRoomRoleMapper;
import com.superhao.app.service.IAppUserService;
import com.superhao.app.service.impl.AppUserServiceImpl;
import com.superhao.base.cache.util.AppChatCacheUtil;
import com.superhao.base.cache.util.AppTokenCacheUtil;
import com.superhao.base.common.constant.SysConstantSet;
import com.superhao.base.common.constant.SysServiceConfigSet;
import com.superhao.base.common.dto.SysTips;
import com.superhao.base.entity.HttpRequestData;
import com.superhao.base.util.SpringContexUtil;
import javassist.compiler.TokenId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.support.DefaultMultipartHttpServletRequest;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * @Auther: super
 * @Date: 2019/10/21 21:56
 * @email:
 */
public class AppSystemSecurityIntercept implements HandlerInterceptor {

    private SysServiceConfigSet serviceConfigSet;
    private static String LOGIN_PAGE = "/resources/appchat/page/login/login.jsp";
    private static Set<String> anonymousUrlSet= Collections.synchronizedSet(new HashSet<>());
    private static Set<String> excludeUrlSet= Collections.synchronizedSet(new HashSet<>());
    private IAppUserService appUserService;
    private AppRoomMapper appRoomMapper;
    public AppSystemSecurityIntercept(){
        excludeUrlSet.add("/app/user/login");
        excludeUrlSet.add("/app/user/register");
        excludeUrlSet.add("/app/user/getVerifyCode");
        excludeUrlSet.add("/app/user/count");
        excludeUrlSet.add("/app/room/groupad");


    }
    private void initAnonymousUrlSet(){
        if(serviceConfigSet==null){
            serviceConfigSet = SpringContexUtil.getBean(SysServiceConfigSet.class);
            String urls = serviceConfigSet.getAnonymousUrls();
            if(!StringUtils.isEmpty(urls)){
                anonymousUrlSet.addAll(Arrays.asList(urls.split(";")));
            }
        }

    }
    @Override
    public boolean preHandle(HttpServletRequest request,
                             HttpServletResponse response, Object handler) throws Exception {
        HttpRequestData requestData = null;
        if(request instanceof DefaultMultipartHttpServletRequest){
            requestData = new HttpRequestData(request);
        }else{
            requestData = HttpRequestData.create();
        }
        if(appRoomMapper==null){
            appRoomMapper = SpringContexUtil.getBean(AppRoomMapper.class);
        }
        //验证访问次数

        //验证该用户是否存在是否存在

        //普通和匿名用户登陆

        initAnonymousUrlSet();

        String servletPath = request.getServletPath();
        String roomId = requestData.getString("roomId");

        ChatRoomCacheModel chatRoom = null;




        //TODO 删除记得 chatRoom==null||
        if(excludeUrlSet.contains(servletPath)){
            return true;
        }

        AppUserToken appUser = AppTokenCacheUtil.currentUser();
        if(appUser==null|| SysConstantSet.COLUMN_ENABLE_N.equals(appUser.getEnable())){
            noLoginHandle(requestData,request,response);
            return false;
        }
       /* if(!StringUtils.isEmpty(roomId)){
            chatRoom = AppChatCacheUtil.getRoom(roomId);
        }*/
        //匿名设置
        if(AppChatConstant.USER_TYPE_0.equals(appUser.getUserType())){
            ChatRoomCacheModel roomModel = AppChatCacheUtil.getRoom(roomId);
            if(roomModel!=null&&AppChatConstant.CONSTANT_NO.equals(roomModel.getAppRoom().getIsAnonymous())){ //禁止匿名用户登录
                requestData.createErrorResponse(SysTips.APP_ROOM_NOT_JOIN_ANONYMOUS);
                requestData.responseToJson(response);
                return false;
            }
        }
        if(AppChatConstant.USER_TYPE_1.equals(appUser.getUserType())){
            //普通用户
        }



        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request,
                           HttpServletResponse response, Object handler,
                           ModelAndView modelAndView) throws Exception {
        // TODO Auto-generated method stub

    }

    private void noLoginHandle(HttpRequestData requestData, HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath();

        String roomId = requestData.getString("roomId");
        String queryString = request.getQueryString();
        String location = LOGIN_PAGE;

        if(!StringUtils.isEmpty(queryString)){
            location+=queryString;
        }else if(!StringUtils.isEmpty(roomId)){
            location+=("?roomId="+roomId);
        }
        if (request.getHeader("x-requested-with") != null) {
            response.setHeader("url", location);
        } else {
            request.getRequestDispatcher(location).forward(request, response);
        }
    }
    @Override
    public void afterCompletion(HttpServletRequest request,
                                HttpServletResponse response, Object handler, Exception ex)
            throws Exception {
        // TODO Auto-generated method stub

    }


}
