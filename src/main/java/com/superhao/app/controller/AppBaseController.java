package com.superhao.app.controller;


import com.alibaba.fastjson.JSONObject;
import com.superhao.base.util.Base64Utils;

/**
 * @Auther: super
 * @Date: 2019/10/24 14:04
 * @email:
 */
public class AppBaseController {

    public String requestBase64Decode(String source){
        return Base64Utils.decodeBase64(source);
    }

    public <T> T encodeJsonToObj(String source,Class<T> cls){
        source = requestBase64Decode(source);
        return JSONObject.parseObject(source,  cls);
    }



}
