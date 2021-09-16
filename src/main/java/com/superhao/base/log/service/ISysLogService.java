package com.superhao.base.log.service;

import com.baomidou.mybatisplus.service.IService;
import com.superhao.base.common.service.IBaseService;
import com.superhao.base.log.entity.SysLog;


import java.util.Map;

/**
 * 
 *
 * @author superhao
 * @email 928649522@qq.com
 * @date 2019-10-23 10:17:12
 */
public interface ISysLogService extends IBaseService<SysLog> {

    void recordSimpleLog(SysLog log);
}

