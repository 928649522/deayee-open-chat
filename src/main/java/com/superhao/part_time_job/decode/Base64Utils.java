package com.superhao.part_time_job.decode;


import com.alibaba.fastjson.JSONObject;
import com.sun.org.apache.xerces.internal.util.URI;
import com.sun.xml.internal.rngom.util.Uri;
import jdk.nashorn.internal.runtime.regexp.joni.Regex;
import org.apache.http.entity.ContentType;

import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

/**
 * @Auther: zehao
 * @Date: 2019/10/2 17:01
 * @email: 928649522@qq.com
 */
public class Base64Utils {
    public static String decodeBase64(String target){
        return  new String(Base64.getDecoder().decode(target));
    }
    public static String encodeBase64(String target){
        return  new String(Base64.getEncoder().encode(target.getBytes()));
    }


}
