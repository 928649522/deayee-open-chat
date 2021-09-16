package com.superhao.base.cache.impl;

import com.superhao.app.entity.token.AppUserToken;
import com.superhao.base.cache.CacheService;
import com.superhao.base.cache.core.RedisCacheUtil;
import com.superhao.base.cache.core.RedisObjUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * @Auther: super
 * @Date: 2019/11/5 22:22
 * @email:
 */
@Service("redisCache")
public class RedisCacheServiceImpl implements CacheService {

    @Override
    public <T> T  get(String key,Class<T> elementType) {

        return RedisObjUtil.get(key,elementType);
    }

    @Override
    public void put(String key, Object val) {
        RedisObjUtil.save(key,val);
    }



    @Override
    public void remove(String key) {
        RedisObjUtil.remove(key);
    }

    @Override
    public <T> Set getSetByPrefix(String key, Class<T> elementType) {
        key+='*';
        return RedisObjUtil.getSetByPrefix(key,elementType);
    }

    @Override
    public boolean existsKey(String key) {
        return RedisObjUtil.existsKey(key);
    }

    @Override
    public void pushExpire(String key, Object obj, int i, TimeUnit ut) {
        RedisObjUtil.saveExpire(key,obj,i,ut);
    }


}
