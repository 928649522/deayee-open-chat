package com.superhao.base.config.fastjosn;

import com.alibaba.fastjson.serializer.SerializeConfig;
import com.alibaba.fastjson.serializer.ToStringSerializer;
import com.alibaba.fastjson.support.config.FastJsonConfig;

import java.math.BigInteger;

/**
 * @Auther: zehao
 * @Date: 2019/5/6 16:02
 * @email: 928649522@qq.com
 */
public class ConvertConfig extends FastJsonConfig {

    public  ConvertConfig(){
        SerializeConfig serializeConfig = SerializeConfig.globalInstance;
        serializeConfig.put(BigInteger.class, ToStringSerializer.instance);
        serializeConfig.put(Long.class, ToStringSerializer.instance);
        serializeConfig.put(Long.TYPE, ToStringSerializer.instance);
        this.setSerializeConfig(serializeConfig);
    }
}
