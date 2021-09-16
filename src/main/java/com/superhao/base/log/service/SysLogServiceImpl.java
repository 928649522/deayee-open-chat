package com.superhao.base.log.service;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.superhao.base.authz.entity.SysUser;
import com.superhao.base.authz.mapper.SysUserMapper;
import com.superhao.base.authz.service.ISysUserService;
import com.superhao.base.common.service.impl.BaseServiceImpl;
import com.superhao.base.log.entity.SysLog;
import com.superhao.base.log.mapper.SysLogMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;


@Service("sysLogService")
public class SysLogServiceImpl extends BaseServiceImpl<SysLogMapper, SysLog> implements ISysLogService {

    @Autowired
    private SysLogMapper sysLogMapper;

    @Override
    public void recordSimpleLog(SysLog log) {
        super.insertDefaultVal(log);
        sysLogMapper.insert(log);
    }
}