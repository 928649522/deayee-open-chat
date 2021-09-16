package com.superhao.base.cache.core;

import com.superhao.base.log.util.SysLogRecordUtil;
import com.superhao.base.util.SpringContexUtil;
import jdk.nashorn.internal.objects.annotations.Where;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.io.Serializable;
import java.util.Objects;

/**
 * @Auther: super
 * @Date: 2019/11/6 17:19
 * @email:
 */
@Component
public class RedisLock {

    public static final String LOCK_PREFIX = "redis_lock";
    public static  int LOCK_EXPIRE = 30000; // ms
    public static  int LOCK_END_SPEED = 25000; // ms
    public static  int LOCK_END_SLEEP_TIME = 100; // ms

    public static  int LOOP_BEGIN_TIME = 10;//ms
    public static final int LOOP_TIME_ADD = 10;//ms  休眠递增的时间
    //public static final int LOOP_TIME = 20;
    //public static final int LOOP_COUNT = LOCK_EXPIRE/LOOP_TIME;//ms
    @Resource(name = "redisObjTemplate")
    private  RedisTemplate redisTemplate ;



/**
     * 最终加强分布式锁
     *
     * @param key key值
     * @return 是否获取到
     */

    public boolean lock(String key) {
         boolean ok = setExprireLock(key,LOCK_EXPIRE);
        while(!ok&&LOOP_BEGIN_TIME<=LOCK_EXPIRE){
            try {
                if(LOOP_BEGIN_TIME>LOCK_END_SPEED){
                    Thread.sleep(LOCK_END_SLEEP_TIME);
                }else{
                    Thread.sleep(LOOP_BEGIN_TIME);
                }
                ok = setExprireLock(key,LOCK_EXPIRE);
                LOOP_BEGIN_TIME+=LOOP_TIME_ADD;
            } catch (InterruptedException e) {
                SysLogRecordUtil.record("redis加锁锁休眠出现问题",e);
            }
        }
        return ok;
    }

    /**
     *
     * @param key
     * @param lockTime  ms -1永久  需要手动解除
     * @return
     */
    public boolean setExprireLock(String key,int lockTime){
        String lock =  LOCK_PREFIX+key;
        // 利用lambda表达式
        return (Boolean) redisTemplate.execute((RedisCallback) connection -> {
            long expireAt = System.currentTimeMillis() + lockTime + 1;
            Boolean acquire = false;
            if(-1==lockTime){
                return connection.setNX(lock.getBytes(), String.valueOf(expireAt).getBytes());
            }else{
                acquire= connection.setNX(lock.getBytes(), String.valueOf(expireAt).getBytes());
            }

            if (acquire) {
                return true;
            } else {
                byte[] value = connection.get(lock.getBytes());
                if (Objects.nonNull(value) && value.length > 0) {
                    long expireTime = Long.parseLong(new String(value));
                    if (expireTime < System.currentTimeMillis()) {
                        // 如果锁已经过期
                        byte[] oldValue = connection.getSet(lock.getBytes(), String.valueOf(System.currentTimeMillis() + lockTime + 1).getBytes());
                        // 防止死锁
                        return Long.parseLong(new String(oldValue)) < System.currentTimeMillis();
                    }
                }
            }
            return false;
        });
    }


/**
     * 删除锁
     *
     * @param key
     *//*
*/
    public void delete(String key) {
        String lock = LOCK_PREFIX+key;
        redisTemplate.delete(lock.getBytes());
          redisTemplate.execute((RedisCallback) connection -> {
              connection.del(lock.getBytes());
            return null;
        });
    }





}

