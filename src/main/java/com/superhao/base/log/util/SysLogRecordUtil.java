package com.superhao.base.log.util;

import com.superhao.app.entity.token.AppUserToken;
import com.superhao.base.log.entity.SysLog;
import com.superhao.base.log.service.ISysLogService;
import com.superhao.base.log.service.SysLogServiceImpl;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @Auther: super
 * @Date: 2019/10/23 10:23
 * @email:
 */
@Log
@Component
public class SysLogRecordUtil {

    private static ISysLogService sysLogService;

    @Autowired
    public SysLogRecordUtil(SysLogServiceImpl sysLogService){
        SysLogRecordUtil.sysLogService = sysLogService;
    }

    public static void record(String msg,Throwable  ex){
        ex.printStackTrace();;
        SysLog log = new SysLog();
        StackTraceElement[] stackElements = ex.getStackTrace();
        String exMsg = getStackTrace(ex);
        if(exMsg.length()>2000){
            exMsg = exMsg.substring(0,2000);
        }
        log.setDescription(msg+"/n"+exMsg);
        sysLogService.recordSimpleLog(log);
    }


    public static void record(String Msg){
        SysLog log = new SysLog();
        if(Msg.length()>2000){
            Msg = Msg.substring(0,2000);
        }
        log.setDescription(Msg);
        sysLogService.recordSimpleLog(log);
    }


    public static String getStackTrace(Throwable  ex){
        StackTraceElement[] stackElements = ex.getStackTrace();
        String exMsg = "";
        if (stackElements != null&&stackElements.length>0) {
            for (StackTraceElement item:stackElements) {
                exMsg+=item.getClassName()+"/t";
                exMsg+=item.getFileName()+"/t";
                exMsg+=item.getLineNumber()+"/t";
                exMsg+=item.getMethodName();
            }
        }
        return exMsg;
    }


    public static void record(AppUserToken token, String msg, Exception ex) {
        ex.printStackTrace();;
        SysLog log = new SysLog();
        StackTraceElement[] stackElements = ex.getStackTrace();
        String exMsg = getStackTrace(ex);
        if(exMsg.length()>2000){
            exMsg = exMsg.substring(0,2000);
        }
        msg = "APP用户 account{"+token.getAccount()+"}ID:{"+token.getUserId()+"}操作异常==>"+msg;
        log.setDescription(msg+"/n"+exMsg);
        sysLogService.recordSimpleLog(log);

    }
}
