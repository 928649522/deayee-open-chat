package com.superhao.base.cache.core;

import com.superhao.base.util.SSerializeUtil;
import com.superhao.base.util.SerializeUtil;
import com.superhao.base.util.SpringContexUtil;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.TimeoutUtils;
import redis.clients.jedis.Jedis;

import java.io.Serializable;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import static java.util.concurrent.TimeUnit.MILLISECONDS;

/**
 * @Auther: super
 * @Date: 2019/11/6 12:53
 * @email:
 */
public class RedisObjUtil {


    @SuppressWarnings("unchecked")
    private static RedisTemplate<Serializable, Serializable> redisTemplate =
            (RedisTemplate<Serializable, Serializable>) SpringContexUtil
                    .getBean("redisObjTemplate");

    public static void save(final String key, Object value) {
        try{
            final  byte[]     vbytes = SerializeUtil.serialize(value);

            redisTemplate.execute(new RedisCallback<Object>() {
                @Override
                public Object doInRedis(RedisConnection connection)
                        throws DataAccessException {
                    //connection.del(redisTemplate.getStringSerializer().serialize(key));
                    connection.set(redisTemplate.getStringSerializer().serialize(key), vbytes);
                    return null;
                }
            });
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public static void saveExpire(final String key, Object value,long timeout,TimeUnit timeUnit) {
        try{
            final  byte[]     vbytes = SerializeUtil.serialize(value);
            redisTemplate.execute(new RedisCallback<Object>() {
                @Override
                public Object doInRedis(RedisConnection connection)
                        throws DataAccessException {
                    connection.set(redisTemplate.getStringSerializer().serialize(key), vbytes);
                    long rawTimeout = TimeoutUtils.toMillis(timeout, timeUnit);
                    connection.pExpire(redisTemplate.getStringSerializer().serialize(key),rawTimeout);
                    return null;
                }
            });
            //redisTemplate.expire(redisTemplate.getStringSerializer().serialize(key),timeout,timeUnit);
        }catch (Exception e){
            e.printStackTrace();
        }
    }



    public static <T> T get(final String key, Class<T> elementType) {
        return redisTemplate.execute(new RedisCallback<T>() {
            @Override
            public T doInRedis(RedisConnection connection)
                    throws DataAccessException {
                byte[] keybytes = redisTemplate.getStringSerializer().serialize(key);
                if (connection.exists(keybytes)) {
                    byte[] valuebytes = connection.get(keybytes);
                    @SuppressWarnings("unchecked")
                        //    SSerializeUtil.deserialize()
                    T value = (T) SerializeUtil.unserialize(valuebytes);
                    return value;
                }
                return null;
            }
        });
    }

    public static void remove(String key) {
        byte[] keybytes = redisTemplate.getStringSerializer().serialize(key);
        redisTemplate.execute(new RedisCallback<Object>() {
            @Override
            public Object doInRedis(RedisConnection connection)
                    throws DataAccessException {
                connection.del(keybytes);
                return null;
            }
        });
     //  redisTemplate.delete(keybytes);
    }

    public static <T> Set<T> getSetByPrefix(String key, Class<T> elementType) {



        return redisTemplate.execute(new RedisCallback<Set<T>>() {
            @Override
            public Set<T> doInRedis(RedisConnection connection)
                    throws DataAccessException {
                byte[] keybytes = redisTemplate.getStringSerializer().serialize(key);
                    Set<T> res =null;
                    Set<byte[]> valbys =  connection.keys(keybytes);
                if(valbys!=null&&valbys.size()>0){
                    res = new HashSet<>();
                        for(byte[] item:valbys){
                            res.add((T) SerializeUtil.unserialize(connection.get(item)));
                        }
                        return res;
                }
                return res;
            }
        });

    }

    public static boolean existsKey(String key) {
        return redisTemplate.execute(new RedisCallback<Boolean>() {
            @Override
            public Boolean doInRedis(RedisConnection connection)
                    throws DataAccessException {
                byte[] keybytes = redisTemplate.getStringSerializer().serialize(key);
                return connection.exists(keybytes);
            }
        });
    }
    public boolean getLock(String lockId, long millisecond) {
        Boolean success = redisTemplate.opsForValue().setIfAbsent(lockId, "lock");
        return success != null && success;
    }

}

