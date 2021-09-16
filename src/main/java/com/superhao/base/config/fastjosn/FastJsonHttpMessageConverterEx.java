package com.superhao.base.config.fastjosn;

import com.alibaba.fastjson.support.spring.FastJsonHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import springfox.documentation.spring.web.json.Json;

/**
 * @Auther: zehao
 * @Date: 2019/4/22 12:01
 * @email: 928649522@qq.com
 * @Description:
 */
class FastJsonHttpMessageConverterExextends extends FastJsonHttpMessageConverter {

    public FastJsonHttpMessageConverterExextends() {
        super();
        this.getFastJsonConfig().getSerializeConfig().put(Json.class, SwaggerJsonSerializer.instance);
    }
}
