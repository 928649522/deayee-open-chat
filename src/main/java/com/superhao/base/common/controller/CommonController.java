package com.superhao.base.common.controller;

import com.superhao.app.entity.AppUser;
import com.superhao.app.entity.token.AppUserToken;
import com.superhao.base.authz.entity.SysUser;
import com.superhao.base.cache.util.SysAuthzUtil;
import com.superhao.base.config.email.EmailDto;
import com.superhao.base.config.email.MailSenderService;
import com.superhao.base.entity.token.SysUserToken;
import com.superhao.base.log.util.SysLogRecordUtil;
import org.springframework.beans.factory.BeanFactoryUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerMapping;
import org.springframework.web.servlet.mvc.condition.PatternsRequestCondition;
import org.springframework.web.servlet.mvc.condition.RequestMethodsRequestCondition;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Auther: zehao
 * @Date: 2019/4/20 13:38
 * @email: 928649522@qq.com
 * @Description:
 */
@Controller
public class CommonController {

    @Autowired
    WebApplicationContext applicationContext;

    @RequestMapping("/administrator")
    public String goLoginPage() {
        return "login";
    }

    @RequestMapping("/login.page")
    public String goLoginPages() {
        return "login";
    }


    @RequestMapping("/")
    public void app(HttpServletRequest request, HttpServletResponse response) {
        try {
            request.getRequestDispatcher("/resources/appchat/page/common/redirect.jsp").forward(request,response);
        } catch (Exception e) {
            SysLogRecordUtil.record("app主页面出错",e);
        }
    }



    @Autowired
    MailSenderService senderService ;







    @RequestMapping("/sys/index")
    public String goIndexPage(  HttpServletRequest request) {
        SysUser user  = SysAuthzUtil.currentSysUser(request);
        request.setAttribute("user",user);
        //判断是否存在此用户
        return "main";
    }
    @RequestMapping("/sys/authz/sysUserDetail.page")
    public String goSysUserDetail( HttpServletRequest request) {
        SysUser user  = SysAuthzUtil.currentSysUser(request);
        request.setAttribute("user",user);
        //判断是否存在此用户
        return "/base/authz/sysUserDetail";
    }

    /**
     * 无权公示
     * @return
     */
    @RequestMapping("/common/noAuth.page")
    public String goNoAuth() {
        return "common/noAuth";
    }

    /**
     * 错误公示
     * @param request
     * @return
     */
    @RequestMapping("/common/error.page")
    public String goError(HttpServletRequest request) {
        return "common/error";
    }

    /**
     * 敬请期待
     * @param request
     * @return
     */
    @RequestMapping("/common/makeFuture.page")
    public String goMakeFuture(HttpServletRequest request) {
        return "common/makeFuture";
    }





}
