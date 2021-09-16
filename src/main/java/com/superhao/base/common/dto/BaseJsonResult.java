package com.superhao.base.common.dto;

import java.util.HashMap;

/**
 * @Auther: zehao
 * @Date: 2019/4/20 13:27
 * @Description:
 */
public class BaseJsonResult extends HashMap<String,Object> {


    public BaseJsonResult(SysTips requestFail) {

    }
    public BaseJsonResult() {

    }

    public enum FIELD {
        success, //是否成功
        status,//状态
        code,//状态码
        msg,//消息
        data,//返回数据

    }

    public enum STATUS{

        /**
         * 服务器渲染数据有误
         */
        E500,
        /**
         * 服务器渲染数据成功
         */
        S200,
        /**
         * 无权限
         */
        L403
    };

    public BaseJsonResult(boolean success, SysTips sysTips) {
        this.setSuccess(success);
        this.setMsg(sysTips.getText());
        this.setCode(sysTips.getCode());
    }
    public BaseJsonResult(boolean success, SysTips sysTips,Object data) {
        this.setSuccess(success);
        this.setMsg(sysTips.getText());
        this.setCode(sysTips.getCode());
        this.setData(data);
    }
    public BaseJsonResult(boolean success,String status, SysTips sysTips,Object data) {
        this.setSuccess(success);
        this.setMsg(sysTips.getText());
        this.setCode(sysTips.getCode());
        this.setData(data);
        this.setStatus(status);
    }


    public BaseJsonResult(boolean success,String msg,SysTips sysTips) {
        this.setSuccess(success);
        this.setMsg(sysTips.getText()+"->"+msg);
        this.setCode(sysTips.getCode());

    }
    public BaseJsonResult(boolean success,String msg) {
        this.setSuccess(success);
        this.setStatus(STATUS.S200.toString());
        this.setMsg(msg);
        this.setCode(SysTips.REQUEST_SUCCESS.getCode());
    }
    public BaseJsonResult(boolean success) {
        this.setSuccess(success);
        this.setCode(200);
        this.setStatus(STATUS.S200.toString());
    }


    public boolean isSuccess() {
        return (Boolean) this.get(FIELD.success);
    }
    public int getCode() {
        return (int) this.get(FIELD.code);
    }

    public void setCode(int code) {
        this.put(FIELD.code.toString(),code);
    }
    public void setSuccess(boolean success) {
        this.put(FIELD.success.toString(),success);
    }

    public String getStatus() {
        return (String) this.get(FIELD.status);
    }

    public void setStatus(String status) {
        this.put(FIELD.status.toString(),status);
    }

    public String getMsg() {
        return (String) this.get(FIELD.msg);
    }

    public void setMsg(String msg) {
        this.put(FIELD.msg.toString(),msg);
    }

    public Object getData() {

        return this.get(FIELD.data);
    }

    public void setData(Object data) {
        this.put(FIELD.data.toString(),data);
    }




}
