package com.superhao.base.entity;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * @Auther: super
 * @Date: 2019/10/17 17:14
 * @email:
 */
@Getter
@Setter
public class PictureVerifyCode implements Serializable{

    private static final long serialVersionUID = 549735746150787045L;
    public  String uuid;
    public  int timeoutSeconds; //超时时间:单位秒
    private String verifyCode;
    private long createTime;

    public PictureVerifyCode() {

    }
    public PictureVerifyCode(String uuid, String verifyCode,int minute) {
        this.uuid = uuid;
        this.verifyCode = verifyCode;
        this.timeoutSeconds = minute*60*1000;
        this.createTime = System.currentTimeMillis();
    }

    public boolean isTimeout(){
        return (System.currentTimeMillis() - createTime)/1000>this.timeoutSeconds;
    }
}
