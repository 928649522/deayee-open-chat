package com.superhao.base.common.controller;


import com.superhao.base.common.dto.BaseJsonResult;
import com.superhao.base.common.dto.SysTips;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.Map;

/**
 * @Auther: zehao
 * @Date: 2019/4/20 13:23
 * @Description:
 */
public class BaseController {




    /**
     * 渲染失败数据
     *
     * @return result
     */
    protected BaseJsonResult renderError() {
        BaseJsonResult result = new BaseJsonResult(true, SysTips.REQUEST_FAIL);
        result.setSuccess(false);
        result.setStatus(BaseJsonResult.STATUS.E500.toString());
        return result;
    }

    public static void main(String[] args) {
        System.out.println(BaseJsonResult.STATUS.E500.toString());
    }

    /**
     * 渲染失败数据（带消息）
     *
     * @param msg 需要返回的消息
     * @return result
     */
    protected BaseJsonResult renderError(String msg) {
        BaseJsonResult result = renderError();
        result.setMsg(msg);
        return result;
    }

    /**
     * 渲染成功数据
     *
     * @return result
     */
    protected BaseJsonResult renderSuccess() {
        BaseJsonResult result = new BaseJsonResult(true, SysTips.REQUEST_SUCCESS);
        result.setSuccess(true);
        result.setStatus(BaseJsonResult.STATUS.S200.toString());
        return result;
    }

    /**
     * 渲染成功数据（带信息）
     *
     * @param msg 需要返回的信息
     * @return result
     */
    protected BaseJsonResult renderSuccess(String msg) {
        BaseJsonResult result = renderSuccess();
        result.setMsg(msg);
        return result;
    }

    /**
     * 渲染成功数据（带数据）
     *
     * @param obj 需要返回的对象
     * @return result
     */
    protected BaseJsonResult renderSuccess(Object obj) {
        BaseJsonResult result = renderSuccess();
        result.setData(obj);
        return result;
    }
}
