package com.superhao.app.service.impl;

import com.superhao.app.constant.AppChatConstant;
import com.superhao.app.entity.AppActivityIntegral;
import com.superhao.app.entity.AppUser;
import com.superhao.app.entity.token.AppUserToken;
import com.superhao.app.mapper.AppActivityIntegralMapper;
import com.superhao.app.mapper.AppUserMapper;
import com.superhao.app.service.IAppActivityIntegralService;
import com.superhao.base.cache.core.RedisLock;
import com.superhao.base.cache.util.AppIntegralRedeemSecretCacheUtil;
import com.superhao.base.cache.util.AppTokenCacheUtil;
import com.superhao.base.cache.util.SysAuthzUtil;
import com.superhao.base.cache.util.VerifyCodeCacheUtil;
import com.superhao.base.common.constant.SysServiceConfigSet;
import com.superhao.base.common.dto.SysTips;
import com.superhao.base.common.service.impl.BaseServiceImpl;
import com.superhao.base.entity.HttpRequestData;
import com.superhao.base.entity.Pages;
import com.superhao.base.entity.PictureVerifyCode;
import com.superhao.base.log.util.SysLogRecordUtil;
import com.superhao.base.util.Md5Util;
import com.superhao.base.util.MybatisUtil;
import com.superhao.base.util.UUIDUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.*;


@Service("appActivityIntegralService")
public class AppActivityIntegralServiceImpl extends BaseServiceImpl<AppActivityIntegralMapper, AppActivityIntegral> implements IAppActivityIntegralService {

    @Autowired
    private AppActivityIntegralMapper appActivityIntegralMapper;

    @Autowired
    private AppUserMapper appUserMapper;


    @Override
    public void addRedPointIntegralRecord(double val, Long activityId,String status) {
        AppActivityIntegral appActivityIntegral = new AppActivityIntegral();
        appActivityIntegral.setActivityId(activityId.toString());
        appActivityIntegral.setIntegralNumber(val);
        appActivityIntegral.setType(AppChatConstant.ACTIVITY_REDPOINT);
        appActivityIntegral.setStatus(status);
        super.insertDefaultValForApp(appActivityIntegral,true);
        appActivityIntegralMapper.insert(appActivityIntegral);

    }

    @Override
    public double searchRedPointCount(Long redPointId) {
        AppActivityIntegral appActivityIntegral = new AppActivityIntegral();
        appActivityIntegral.setType(AppChatConstant.ACTIVITY_REDPOINT);
        appActivityIntegral.setActivityId(redPointId.toString());
        appActivityIntegral.setStatus(AppChatConstant.INTEGRAL_ADD);
        Double res = appActivityIntegralMapper.searchActivityIntegralCount(appActivityIntegral);
        return res==null?0:res.doubleValue();
    }

    @Override
    public void integralHistory(HttpRequestData requestData) {
        AppUserToken token = AppTokenCacheUtil.currentUser();
        List<AppActivityIntegral> res =
                appActivityIntegralMapper.selectList(MybatisUtil
                        .conditionT()
                        .eq("creator",token.getUserId())
                .orderBy("creation_time",false));
        requestData.fillResponseData(res);
    }

    @Transactional
    @Override
    public void redeemIntegral(HttpRequestData requestData) {
        String integralText = requestData.getString("val");
        if(StringUtils.isEmpty(integralText)){
            requestData.createErrorResponse(SysTips.PARAM_ERROR);
            return;
        }
        AppUserToken token = AppTokenCacheUtil.currentUser();
        try {

            if(!AppIntegralRedeemSecretCacheUtil.opsRedeemSecretLock(token.getUserId().toString(),600*1000)){
                requestData.createErrorResponse(SysTips.SYS_SERVER_BUSY);
                return;
            }
            Double integral = new Double(integralText);
            if(integral.doubleValue()<AppChatConstant.INTEGRAL_REDEEM_MIN_VAL){
                requestData.createErrorResponse(SysTips.PARAM_ERROR);
                return;
            }

            //验证是否有积分
           AppUser appUser =  appUserMapper.selectById(token.getUserId());
           if(Math.abs(integral.doubleValue())>appUser.getIntegral().doubleValue()){
               requestData.createErrorResponse(SysTips.APP_INTEGRAL_INSUFFICIENT);
               return;
           }
           //生成一个兑换ID
            String redeemID =UUID.randomUUID().toString();

            Map condition = new HashMap();
            condition.put("val",-Math.abs(integral));
            condition.put("userId",token.getUserId());
            int rows = appUserMapper.updateIntegralById(condition);
            if(rows==0){
                requestData.createErrorResponse(SysTips.PARAM_ERROR);
                return;
            }
            //创建一个未处理的活动积分记录  类型：积分兑换
            AppActivityIntegral appActivityIntegral  = new AppActivityIntegral();
            this.insertDefaultValForApp(appActivityIntegral,true);
            appActivityIntegral.setType(AppChatConstant.ACTIVITY_INTEGRAL_REDEEM);
            appActivityIntegral.setActivityId(redeemID);
            appActivityIntegral.setIntegralNumber(-Math.abs(integral));
            appActivityIntegral.setStatus(AppChatConstant.INTEGRAL_REDEEM_STATUS_UN_HANDLE);
            appActivityIntegralMapper.insert(appActivityIntegral);
            requestData.fillResponseData(appActivityIntegral);

        }catch (Exception e){
            requestData.createErrorResponse(SysTips.PARAM_ERROR);
            SysLogRecordUtil.record(token,"兑换积分出错",e);
        }finally {
            AppIntegralRedeemSecretCacheUtil.opsRedeemSecretUnLock(token.getUserId().toString());
        }
    }


    @Override
    public void addIntegralRecord(Long userId, Double val, String type) {
        AppActivityIntegral appActivityIntegral = new AppActivityIntegral();
        this.insertDefaultValForApp(appActivityIntegral,false);
        appActivityIntegral.setType(type);
        appActivityIntegral.setIntegralNumber(val);
        appActivityIntegral.setStatus(AppChatConstant.INTEGRAL_ADD);
        appActivityIntegral.setCreator(userId);
        if(SysAuthzUtil.currentSysUser()==null){
            appActivityIntegral.setActivityId("-1");
        }else{
            appActivityIntegral.setActivityId(SysAuthzUtil.currentSysUser().getUserId().toString());
        }

        appActivityIntegralMapper.insert(appActivityIntegral);
    }

    @Override
    public void searchChatUserIntegralByPage(HttpRequestData requestData) {

        Map condition = new HashMap();
        condition.put("page",Pages.create(requestData.getInteger("page")).getOffset());
        condition.put("size",Pages.ROW_SIZE);

        String searchText = requestData.getString("searchText");
        if(!StringUtils.isEmpty(searchText)){
            condition.put("searchText",searchText);
        }

        List<AppActivityIntegral> data = appActivityIntegralMapper.selectPageByUserId(condition);
        int count = appActivityIntegralMapper.selectCountByUserId(condition);
        for(AppActivityIntegral integral:data){
            integral.setPhone(null);
        }
        requestData.generatePageData(data, count);
    }


    @Override
    public void handleSecret(HttpRequestData requestData) {
        String secret = requestData.getString("secret");
        Long integralId = requestData.getLong("integralId");

        if(StringUtils.isEmpty(secret)|| StringUtils.isEmpty(integralId)){
            requestData.createErrorResponse(SysTips.PARAM_ERROR);
            return;
        }
        AppActivityIntegral up = new AppActivityIntegral();
        up.setStatus(AppChatConstant.INTEGRAL_REDEEM_STATUS_HANDLE);
        int rows = appActivityIntegralMapper.update(up,MybatisUtil.conditionT().eq("integral_id",integralId)
                .and()
                .eq("activity_id",secret)
                .eq("type",AppChatConstant.ACTIVITY_INTEGRAL_REDEEM));

        if(rows==0){
            requestData.createErrorResponse(SysTips.PARAM_ERROR);
            return;
        }
    }

    @Autowired
   private  RedisLock redisLock;
    @Transactional
    @Override
    public void withdrawRecharge(HttpRequestData requestData) {
        String activityId = requestData.getString("activityId");
        Long integralId = requestData.getLong("integralId");
        if(StringUtils.isEmpty(activityId)|| StringUtils.isEmpty(integralId)){
            requestData.createErrorResponse(SysTips.PARAM_ERROR);
            return;
        }

        AppActivityIntegral condi = new AppActivityIntegral();
        condi.setIntegralId(integralId);
        condi.setActivityId(activityId);
        AppActivityIntegral target =appActivityIntegralMapper.selectOne(condi);

        if(target==null){
            return;
        }
        String LOCK_KEY = integralId+activityId;
        try {
            if(redisLock.setExprireLock(LOCK_KEY,60*1000)){
                Map map = new HashMap();
                map.put("val",-Math.abs(target.getIntegralNumber().doubleValue()));
                map.put("userId",target.getCreator());
                appUserMapper.updateIntegralById(map);
                appActivityIntegralMapper.deleteById(integralId);
                redisLock.delete(LOCK_KEY);
            }
        }catch (Exception e){
            redisLock.delete(LOCK_KEY);
        }

    }

    @Autowired
    private SysServiceConfigSet sysServiceConfigSet;
    @Override
    public void generateReigisterCode(HttpRequestData requestData) {

        List<PictureVerifyCode> list  = new ArrayList<>();

        for(int i=0;i<10;i++){
            int number = 0;
           String code =  UUIDUtils.generateSixNumberUuid();
           while(VerifyCodeCacheUtil.hasObj(code)){
               code =  UUIDUtils.generateSixNumberUuid();
               if(number++>200){
                   continue;
               }
           }
            PictureVerifyCode vc = new PictureVerifyCode(code,code,sysServiceConfigSet.getAppRegisterCodeExpired());
            VerifyCodeCacheUtil.pushExpire(vc,sysServiceConfigSet.getAppRegisterCodeExpired());
           // System.out.println(VerifyCodeCacheUtil.get(code));
            list.add(vc);
        }
        requestData.generatePageData(list,10);
    }

    public static void main(String[] args) {
        System.out.println();
        Set<String> set = new HashSet<>();
        int number = 0;
        for(int i =0;i<1000000;i++){
            set.add(UUIDUtils.generateSixNumberUuid());

        }
        System.out.println(set.size());
    }

}