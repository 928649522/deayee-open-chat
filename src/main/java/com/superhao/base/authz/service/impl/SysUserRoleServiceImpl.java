package com.superhao.base.authz.service.impl;

import com.baomidou.mybatisplus.mapper.Wrapper;
import com.superhao.base.authz.entity.SysPermission;
import com.superhao.base.authz.entity.SysRole;
import com.superhao.base.authz.entity.SysUser;
import com.superhao.base.authz.entity.SysUserRole;
import com.superhao.base.authz.mapper.SysRoleMapper;
import com.superhao.base.authz.mapper.SysUserRoleMapper;
import com.superhao.base.authz.service.ISysUserRoleService;
import com.superhao.base.common.constant.SysConstantSet;
import com.superhao.base.common.service.impl.BaseServiceImpl;
import com.superhao.base.entity.HttpRequestData;
import com.superhao.base.util.MybatisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


@Service("sysUserRoleService")
public class SysUserRoleServiceImpl extends BaseServiceImpl<SysUserRoleMapper, SysUserRole> implements ISysUserRoleService {

    @Autowired
    private SysUserRoleMapper sysUserRoleMapper;

    @Autowired
    private SysRoleMapper roleMapper;


    @Override
    public void searchSysUserRole(HttpRequestData requestData) {
        List<SysRole> roleList =  roleMapper.selectList(MybatisUtil.<SysRole>condition().eq("enable", SysConstantSet.COLUMN_ENABLE_Y));
        List<SysRole> selecedList =  roleMapper.selectRoleByUserId(requestData.getLong("userId"));

        for(SysRole item:selecedList){
            for (SysRole _item:roleList){
                if(item.getRoleId().equals(_item.getRoleId())){
                    _item.setSelected(true);
                }
            }
        }
        requestData.fillResponseData(roleList);

    }

    @Transactional
    @Override
    public void updateSysUserRole(SysUser sysUser) {
        Wrapper<SysUserRole> condtion  = MybatisUtil.conditionT();
        condtion.eq("user_id", sysUser.getUserId());
        sysUserRoleMapper.delete(condtion);
        Long []target = this.strToLongArray(sysUser.getRoleIds());
        if(target!=null&&target.length>0){
            List<SysUserRole> paramList  = new ArrayList<>();
            for(Long roleId:target){
                paramList.add(new SysUserRole(sysUser.getUserId(),roleId));
            }
            this.insertBatch(paramList);
        }
    }

    @Override
    public List<SysPermission> searchUserRolePermission(Map condition) {

        return sysUserRoleMapper.selectRolePermissionByUser(condition);
    }
}