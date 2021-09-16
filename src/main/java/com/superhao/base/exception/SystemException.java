package com.superhao.base.exception;

/**
 * @Auther: zehao
 * @Date: 2019/4/26 13:40
 * @email: 928649522@qq.com
 * @Description:
 */
public class SystemException  extends RuntimeException{

    public SystemException(String message,Exception e) {
        super(message,e);
    }
    public SystemException(String message) {
        super(message);
    }

    public static SystemException createService(String message){
        return new SystemException(message);
    }
}
