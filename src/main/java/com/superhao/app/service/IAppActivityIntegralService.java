package com.superhao.app.service;

import com.baomidou.mybatisplus.service.IService;
import com.superhao.app.entity.AppActivityIntegral;
import com.superhao.base.common.service.IBaseService;
import com.superhao.base.entity.HttpRequestData;


import java.util.List;
import java.util.Map;

/**
 * 活动积分
 *
 * @author superhao
 * @email 928649522@qq.com
 * @date 2019-11-21 21:43:33
 */
public interface IAppActivityIntegralService extends IBaseService<AppActivityIntegral> {

    void addRedPointIntegralRecord(double val, Long redPointId,String status);

    double searchRedPointCount(Long redPointId);

    void integralHistory(HttpRequestData requestData);

    void redeemIntegral(HttpRequestData requestData);

    void addIntegralRecord(Long userId, Double aDouble, String activityRedpoint);

    void searchChatUserIntegralByPage(HttpRequestData requestData);

    void handleSecret(HttpRequestData requestData);

    void withdrawRecharge(HttpRequestData requestData);

    void generateReigisterCode(HttpRequestData requestData);
}

