package com.superhao.base.aspect;

import com.alibaba.fastjson.JSON;
import com.superhao.base.annotation.Log;
import com.superhao.base.entity.SysLog;
import com.superhao.base.util.IpUtil;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.Date;

/**
 * @Auther: zehao
 * @Date: 2019/4/20 16:42
 * @email: 928649522@qq.com
 * @Description:
 */
@Component
@Aspect
public class LogAspect {

   /* @Autowired
    private SysLogMapper logMapper;*/

    @Pointcut("@annotation(com.superhao.base.annotation.Log)")
    private void pointcut() {
    }
    public LogAspect() {
        System.out.println("");
    }

    @After("pointcut()")
    public void insertLogSuccess(JoinPoint jp) {
        addLog(jp, getDesc(jp));
    }

    private void addLog(JoinPoint jp, String text) {
        Log.LOG_TYPE type = getType(jp);
        SysLog log = new SysLog();
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        //一些系统监控
        if (requestAttributes != null) {
            HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
            String ip = IpUtil.getIp(request);
            log.setCreationPlace(ip);
        }
        log.setCreationTime(new Date());
        log.setType(type.toString());
        log.setDescription(text);

        Object[] obj = jp.getArgs();
        StringBuffer buffer = new StringBuffer();
        if (obj != null) {
            for (int i = 0; i < obj.length; i++) {
                buffer.append("[参数" + (i + 1) + ":");
                //buffer.append(JSON.toJSONString(obj[i]));
                buffer.append("]");
            }
        }
        log.setParam(buffer.toString());
       /* try {
            CurrentUser currentUser = ShiroUtil.getCurrentUse();
            log.setUserName(currentUser.getUsername());
        } catch (UnavailableSecurityManagerException e) {

        }
        logMapper.insert(log);*/
    }

    /**
     * 记录异常
     *
     * @param joinPoint
     * @param e
     */
    @AfterThrowing(value = "pointcut()", throwing = "e")
    public void afterException(JoinPoint joinPoint, Exception e) {
        System.out.print("-----------afterException:" + e.getMessage());
        addLog(joinPoint, getDesc(joinPoint) + e.getMessage());
    }


    private String getDesc(JoinPoint joinPoint) {
        MethodSignature methodName = (MethodSignature) joinPoint.getSignature();
        Method method = methodName.getMethod();
        return method.getAnnotation(Log.class).text();
    }

    private Log.LOG_TYPE getType(JoinPoint joinPoint) {
        MethodSignature methodName = (MethodSignature) joinPoint.getSignature();
        Method method = methodName.getMethod();
        return method.getAnnotation(Log.class).type();
    }
}
