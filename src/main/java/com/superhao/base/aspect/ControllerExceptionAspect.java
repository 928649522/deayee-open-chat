package com.superhao.base.aspect;


import com.superhao.base.common.dto.BaseJsonResult;
import com.superhao.base.common.dto.SysTips;
import com.superhao.base.exception.HttpParamException;
import com.superhao.base.log.util.SysLogRecordUtil;
import lombok.extern.log4j.Log4j;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @Auther: zehao
 * @Date: 2019/4/30 21:34
 * @email: 928649522@qq.com
 */
@Slf4j
@RestControllerAdvice
public class ControllerExceptionAspect {

    public static final String ERROR_VIEW = "common/error";

    /**
     * 全局异常的处理
     * @param request
     * @param response
     * @param e
     * @return
     * @throws Exception
     */
    @ExceptionHandler(value = {Exception.class})  //声明捕获的异常
    public Object globalErrorHandler(HttpServletRequest request, HttpServletResponse response, Exception e) throws Exception {
        if (isAjax(request)) {
            //BaseJsonResult res = new BaseJsonResult(false,e.getMessage(), SysTips.SYS_UNKNOWN_ERROR);
            BaseJsonResult res = new BaseJsonResult(false, SysTips.SYS_UNKNOWN_ERROR);
            //写日志
            log.warn("错误:====>"+ SysLogRecordUtil.getStackTrace(e));
            SysLogRecordUtil.record("全局捕获异常:====>",e);
            return res;
        } else {
            return  createErrorPageModel(request,e,SysTips.SYS_UNKNOWN_ERROR);
        }
    }

    /**
     * 参数异常的处理
     * @param request
     * @param response
     * @param e
     * @return
     * @throws Exception
     */
    @ExceptionHandler(value = {HttpParamException.class})  //声明捕获的异常
    public Object paramErrorHandler(HttpServletRequest request, HttpServletResponse response, HttpParamException e) throws Exception {
        //写日志
        log.warn("错误:====>"+e.getMessage());
        if (isAjax(request)) {
            return e.getErrorResponse();
        } else {
           return  createErrorPageModel(request,e,SysTips.PARAM_ERROR);
        }
    }

    private ModelAndView createErrorPageModel(HttpServletRequest request,Exception e,SysTips tips){
        ModelAndView p = new ModelAndView();
       // p.addObject("exception", e);
        p.addObject("tips",tips.getText());
        p.addObject("url", request.getRequestURL());
        p.setViewName(ERROR_VIEW);
        return p;
    }



    // 判断是否是ajax请求
    public  boolean isAjax(HttpServletRequest httpRequest) {
        String xRequestedWith = httpRequest.getHeader("X-Requested-With");
        return (xRequestedWith != null && "XMLHttpRequest".equals(xRequestedWith));
    }
}
