package com.superhao.app.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.superhao.app.entity.AppActivityIntegral;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

/**
 * 活动积分
 * 
 * @author
 * @email
 * @date 2019-11-21 21:43:33
 */
@Mapper
public interface AppActivityIntegralMapper extends BaseMapper<AppActivityIntegral> {

    Double searchActivityIntegralCount(AppActivityIntegral appActivityIntegral);

    List<AppActivityIntegral> selectPageByUserId(Map condition);

    int selectCountByUserId(Map condition);
}
