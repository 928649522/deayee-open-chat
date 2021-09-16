package com.superhao.base.cache.impl;

import com.superhao.base.cache.CacheService;
import com.superhao.base.cache.core.GuavaCacheUtil;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * @Auther: zehao
 * @Date: 2019/4/22 21:52
 * @email: 928649522@qq.com
 * @Description:
 */
@Service("guavaCache")
public  class GuavaCacheServiceImpl implements CacheService {

    @Override
    public <T> T get(String key, Class<T> elementType) {
        return (T) GuavaCacheUtil.get(key);
    }

    @Override
    public void put(String key, Object val) {
        GuavaCacheUtil.put(key,val);
    }

    @Override
    public void remove(String key) {

    }

    @Override
    public <T> Set getSetByPrefix(String key, Class<T> elementType) {
        return null;
    }

    @Override
    public boolean existsKey(String key) {
        return false;
    }

    @Override
    public void pushExpire(String key, Object obj, int i, TimeUnit ut) {

    }


}
