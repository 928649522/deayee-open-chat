package com.superhao.base.authz.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.superhao.base.authz.entity.*;
import com.superhao.base.authz.entity.SysRole;
import com.superhao.base.authz.mapper.*;
import com.superhao.base.authz.mapper.SysRoleMapper;
import com.superhao.base.authz.service.ISysRolePermissionService;
import com.superhao.base.authz.service.ISysRoleService;
import com.superhao.base.authz.service.ISysRoleService;
import com.superhao.base.common.constant.SysConstantSet;
import com.superhao.base.common.dto.SysTips;
import com.superhao.base.common.service.impl.BaseServiceImpl;
import com.superhao.base.entity.HttpRequestData;
import com.superhao.base.entity.Pages;
import com.superhao.base.util.Md5Util;
import com.superhao.base.util.MybatisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.List;


@Service("sysRoleService")
public class SysRoleServiceImpl extends BaseServiceImpl<SysRoleMapper, SysRole> implements ISysRoleService {

    @Autowired
    private SysRoleMapper sysRoleMapper;

    @Autowired
    private SysPermissionMapper sysPermissionMapper;

    @Resource(name = "sysRolePermissionService")
    private ISysRolePermissionService rolePermissionService;

    @Autowired
    private SysUserRoleMapper sysUserRoleMapper;



    @Override
    public boolean sysRoleUpdateValidate(SysRole sysRole, HttpRequestData requestData, boolean isUpdate) {
        Wrapper<SysRole> w = MybatisUtil.conditionT();

        if (isUpdate && sysRole.getRoleId() == null) {
            requestData.createErrorResponse("缺失必要参数", SysTips.PARAM_ERROR);
            return false;
        }
        w.eq("role_name", sysRole.getRoleName());
        if (isUpdate) {
            w.ne("role_id", sysRole.getRoleId());
        }
        Integer row = sysRoleMapper.selectCount(w);
        if (row != null && row.intValue() > 0) {
            requestData.createErrorResponse("角色名已经存在", SysTips.PARAM_ERROR);
            return false;
        }
        return true;
    }

    @Transactional
    @Override
    public void updateSysRole(SysRole sysRole) {
        this.updateDefaultVal(sysRole);
        rolePermissionService.modifyRolePermissionRelation(sysRole);
        sysRoleMapper.updateById(sysRole);
    }

    @Transactional
    @Override
    public void addSysRole(SysRole sysRole, HttpRequestData requestData) {
        this.insertDefaultVal(sysRole);
        sysRoleMapper.insert(sysRole);
        rolePermissionService.modifyRolePermissionRelation(sysRole);
        requestData.fillResponseData(sysRole);
    }


    @Override
    public void searchSysRoleByPage(HttpRequestData requestData) {
        Wrapper<SysRole> condition = MybatisUtil.conditionT();
        condition.eq("enable", SysConstantSet.COLUMN_ENABLE_Y);
        condition.orderBy("creation_time", false);
        Page<SysRole> page = Pages.create(requestData.getInteger("page"));
        List<SysRole> data = sysRoleMapper.selectPage(page, condition);
        int count = sysRoleMapper.selectCount(condition);
        requestData.generatePageData(data, count);
    }

    @Override
    public void searchSysRole(SysRole sysRole, HttpRequestData requestData) {
        sysRole.setEnable(SysConstantSet.COLUMN_ENABLE_Y);
        SysRole res = sysRoleMapper.selectOne(sysRole);
        requestData.fillResponseData(res);
    }

    @Transactional
    @Override
    public void logicRemoveSysRole(HttpRequestData requestData) {
        SysRole target = new SysRole();
        Long roleId = requestData.getLong("roleId");
        String removeType = requestData.getString("removeType");
        target.setRoleId(roleId);
        if(super.removeOneRow(target,removeType)){
            if(SysConstantSet.SYSTEM_DELETE_TYPE_PHYSICAL.equals(removeType)){
                rolePermissionService.delete(MybatisUtil.conditionT().eq("role_id",roleId));
            }else{
                SysRolePermission rolePermission = new SysRolePermission(SysConstantSet.COLUMN_ENABLE_N);
                SysUserRole sysUserRole = new SysUserRole(SysConstantSet.COLUMN_ENABLE_N);
                rolePermissionService.update(rolePermission,MybatisUtil.conditionT().eq("role_id",roleId));
                sysUserRoleMapper.update(sysUserRole,MybatisUtil.conditionT().eq("role_id",roleId));
            }
        }
    }

    @Override
    public void searchSysRolePermission(HttpRequestData requestData) {
        Long roleId = requestData.getLong("roleId");
        EntityWrapper<SysPermission> condition = new EntityWrapper();
        condition.eq("enable", SysConstantSet.COLUMN_ENABLE_Y);
        List<SysPermission> protoTreeData = sysPermissionMapper.selectList(condition);
        if (roleId != null) {
            List<SysRolePermission> checkList = rolePermissionService.searchPermissionByRole(roleId);
            for (SysRolePermission _item : checkList) {
                for (SysPermission item : protoTreeData) {
                    if (item.getPermissionId().equals(_item.getPermissionId())) {
                        item.setChecked(true);
                    }
                }
            }
        }
        requestData.fillResponseData(protoTreeData);
    }
}