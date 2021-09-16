package com.superhao.base.cache;

import com.superhao.app.entity.token.AppUserToken;

import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * @Auther:
 * @Date: 2019/4/22 21:37
 * @email:
 * @Description:
 */
public interface CacheService {
    /**
     * 向缓存中取出key的取值
     * @param key
     * @return
     */
     <T> T  get(String key,Class<T> elementType);

    /**
     * 向缓存中存入一个名为key的val
     * @param key
     * @param val
     */
    void put(String key,Object val);


    void remove(String key);

    <T> Set getSetByPrefix(String key, Class<T> elementType);

    boolean existsKey(String key);

    void pushExpire(String key, Object obj, int i, TimeUnit ut);
}
