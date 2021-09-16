package com.superhao.app.handle.listener;

import com.superhao.app.constant.AppChatConstant;
import com.superhao.app.entity.AppActivityIntegral;
import com.superhao.app.entity.AppRedPoint;
import com.superhao.app.entity.AppUser;
import com.superhao.app.mapper.AppRedPointMapper;
import com.superhao.app.mapper.AppUserMapper;
import com.superhao.app.service.IAppActivityIntegralService;
import com.superhao.app.service.IAppUserService;
import com.superhao.base.cache.core.RedisLock;
import com.superhao.base.cache.util.AppRedPointCacheUtil;
import com.superhao.base.log.util.SysLogRecordUtil;
import com.superhao.base.util.MybatisUtil;
import com.superhao.base.util.SerializeUtil;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.listener.KeyExpirationEventMessageListener;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

/**
 * 红包过期返还给用户
 * @Auther: super
 * @Date: 2019/11/23 11:56
 * @email:
 */
@Component
    public class RedPointExpirationListener  extends KeyExpirationEventMessageListener {
    public RedPointExpirationListener(RedisMessageListenerContainer listenerContainer) {
        super(listenerContainer);
    }

    @Autowired
    private IAppUserService appUserService;

    @Autowired
    private IAppActivityIntegralService appActivityIntegralService;
    @Autowired
    private AppRedPointMapper appRedPointMapper;



    private final static String  MAYIKT_REDIS_KEY="mayikt_member";
    private final int LOCK_EXPIRE = 30*1000;







    @Transactional
    @Override
    public void onMessage(Message message, byte[] pattern) {
        String expiredKey = message.toString();
        if(!expiredKey.startsWith(AppRedPointCacheUtil.RED_POINT_KEY_PREFIX)){
            return;
        }
        try {
            //message.toString()获取失效的key
            if(AppRedPointCacheUtil.opsRedPointLock(expiredKey,LOCK_EXPIRE)){
               this.redPointBackService(expiredKey);
                AppRedPointCacheUtil.opsRedPointUnLock(expiredKey);
            }else{
                //防止得锁的服务器宕机
                Thread.sleep(LOCK_EXPIRE+10);
                if(AppRedPointCacheUtil.opsRedPointLock(expiredKey,LOCK_EXPIRE)){
                    this.redPointBackService(expiredKey);
                    AppRedPointCacheUtil.opsRedPointUnLock(expiredKey);
                }
            }


        }catch (Exception e){
            AppRedPointCacheUtil.opsRedPointUnLock(expiredKey);
            SysLogRecordUtil.record("红包退回出错",e);
        }
    }



    private void redPointBackService(String expiredKey){
        AppRedPoint  appRedPoint = appRedPointMapper.selectById(expiredKey.replace(AppRedPointCacheUtil.RED_POINT_KEY_PREFIX,""));
        //剩余的积分
        double isGetIntegralCount = appActivityIntegralService.searchRedPointCount(appRedPoint.getRedPointId());
        appRedPoint.setRpcount(appRedPoint.getRpcount()-isGetIntegralCount);
        if(appRedPoint.getRpcount()>0){
            int rows = appActivityIntegralService.selectCount(MybatisUtil.conditionT()
                    .eq("activity_id",appRedPoint.getRedPointId())
                    .and()
                    .eq("status", AppChatConstant.REDPOINT_STATUS_BACK));
            if(rows==0){

                //增加一条红包回退记录
                AppActivityIntegral backRedPoint = new AppActivityIntegral();
                backRedPoint.setCreationTime(new Date());
                backRedPoint.setType(AppChatConstant.ACTIVITY_REDPOINT);
                backRedPoint.setIntegralNumber(appRedPoint.getRpcount());
                backRedPoint.setCreator(appRedPoint.getCreator());
                backRedPoint.setCreationTime(new Date());
                backRedPoint.setLastUpdateTime(backRedPoint.getLastUpdateTime());
                backRedPoint.setActivityId(appRedPoint.getRedPointId().toString());
                backRedPoint.setStatus(AppChatConstant.REDPOINT_STATUS_BACK);
                appActivityIntegralService.insert(backRedPoint);
                appUserService.changeIntegral(appRedPoint.getCreator(),appRedPoint.getRpcount());
            }

        }
    }



}
