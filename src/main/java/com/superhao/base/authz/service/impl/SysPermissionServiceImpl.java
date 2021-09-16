package com.superhao.base.authz.service.impl;

import com.baomidou.mybatisplus.enums.SqlLike;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.superhao.base.authz.entity.SysPermission;
import com.superhao.base.authz.mapper.SysPermissionMapper;
import com.superhao.base.authz.service.ISysPermissionService;
import com.superhao.base.authz.service.ISysUserRoleService;
import com.superhao.base.cache.util.SysAuthzUtil;
import com.superhao.base.common.constant.SysConstantSet;
import com.superhao.base.common.constant.SysServiceConfigSet;
import com.superhao.base.common.dto.SysTips;
import com.superhao.base.common.service.impl.BaseServiceImpl;
import com.superhao.base.entity.HttpRequestData;
import com.superhao.base.entity.Pages;
import com.superhao.base.entity.token.SysUserToken;
import com.superhao.base.util.MybatisUtil;
import com.superhao.base.util.TreeHelpUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Service("sysPermissionService")
public class SysPermissionServiceImpl extends BaseServiceImpl<SysPermissionMapper, SysPermission> implements ISysPermissionService {

    @Autowired
    private SysPermissionMapper sysPermissionMapper;

    @Autowired
    private ISysUserRoleService sysUserRoleService;

    @Autowired
    private SysServiceConfigSet configSet;

    @Override
    public void searchList(HttpRequestData requestData) {
        Wrapper condition =  new EntityWrapper<SysPermission>().eq("enable",SysConstantSet.COLUMN_ENABLE_Y).orderBy("order_number");
        List<SysPermission> resObj = null;
        if(requestData.hasParam("position")){
            condition.eq("position",requestData.getString("position"));
        }
        if(requestData.hasParam("status")){
            condition.eq("status",requestData.getString("status"));
        }
        if(requestData.hasPageParam()){
            Pages<SysPermission> page = Pages.create(requestData.getInteger("page"));
            resObj = sysPermissionMapper.selectPage(page,condition);
            int count = sysPermissionMapper.selectCount(condition);
            requestData.generatePageData(resObj, count);
        }else{
            resObj = sysPermissionMapper.selectList(condition);
            requestData.generatePageData(resObj, 0);
        }
    }


    public boolean isRootUser(){
       return configSet.getRoot().equals(SysAuthzUtil.currentSysUser().getAccountName());
    }


    @Override
    public void searchButtons(HttpRequestData requestData) {

        List<SysPermission> res;
        if(isRootUser()){
            res = sysPermissionMapper.selectList(MybatisUtil.conditionT()
                    .eq("type",SysConstantSet.PERMISSION_TYPE_2)
            .eq("parent_id",requestData.getLong("permissionId")));
        }else{
            Map cd = new HashMap();
            cd.put("type",SysConstantSet.PERMISSION_TYPE_2);
            cd.put("parentId",requestData.getLong("permissionId"));
            cd.put("userId",SysAuthzUtil.currentSysUser().getUserId());
            res = sysUserRoleService.searchUserRolePermission(cd);
        }
        requestData.fillResponseData(res);
    }

    @Override
    public void searchPermissionItem(HttpRequestData requestData) {
        EntityWrapper<SysPermission> condition = new EntityWrapper();
        if(!requestData.hasParam("type")){
            condition.eq("type",SysConstantSet.PERMISSION_TYPE_0)
                    .or().eq("type",SysConstantSet.PERMISSION_TYPE_1);

        }else{
            condition.eq("type",requestData.getString("type"));
        }
        requestData.fillResponseData(sysPermissionMapper.selectList(condition));
    }

    @Override
    public void searchPermission(HttpRequestData requestData) {
        EntityWrapper<SysPermission> condition = new EntityWrapper();
        condition.eq("enable",SysConstantSet.COLUMN_ENABLE_Y);
        if(requestData.hasParam("permissionId")){
             condition.eq("permission_id",requestData.getInteger("permissionId"));
        }
        requestData.fillResponseData(sysPermissionMapper.selectList(condition));
    }

    @Override
    public boolean permissionUpdateValidate(SysPermission sysPermission, HttpRequestData requestData) {
        EntityWrapper<SysPermission> condition = new EntityWrapper();
        condition.eq("name",sysPermission.getName())
                .ne("type",SysConstantSet.PERMISSION_TYPE_2)
                .ne("type",SysConstantSet.PERMISSION_TYPE_3);

        if(sysPermission.getPermissionId()!=null){
            condition.ne("permission_id",sysPermission.getPermissionId());
        }
        if(sysPermissionMapper.selectCount(condition).intValue()>0){
            requestData.createErrorResponse("权限名称不能重复", SysTips.PARAM_ERROR);
            return false;
        }


        if(sysPermission.getParentId()!=SysConstantSet.PERMISSION_PARENTID_ROOT){
            condition = new EntityWrapper();
            condition.eq("permission_id",sysPermission.getParentId());
            SysPermission param = new SysPermission();
            param.setPermissionId(sysPermission.getParentId());
            SysPermission parent = sysPermissionMapper.selectOne(param);
            if(SysConstantSet.PERMISSION_TYPE_1.equals(parent.getType())
                    &&SysConstantSet.PERMISSION_TYPE_0.equals(sysPermission.getType())){
                requestData.createErrorResponse("“菜单”下不能添加“菜单集合”", SysTips.PARAM_ERROR);
                return false;
            }
            if(SysConstantSet.PERMISSION_TYPE_1.equals(parent.getType())
                    &&SysConstantSet.PERMISSION_TYPE_1.equals(sysPermission.getType())){
                requestData.createErrorResponse("“菜单”下不能添加“菜单”", SysTips.PARAM_ERROR);
                return false;
            }
            if(SysConstantSet.PERMISSION_TYPE_0.equals(parent.getType())
                    &&SysConstantSet.PERMISSION_TYPE_2.equals(sysPermission.getType())){
                requestData.createErrorResponse("“菜单集合”下不能添加“按钮”", SysTips.PARAM_ERROR);
                return false;
            }
        }
        return true;
    }
    @Override
    public void addSysPermission(SysPermission sysPermission) {
        //设置默认值
        this.insertDefaultVal(sysPermission);
        //获取父权限deepCode
        if (sysPermission.getParentId() != SysConstantSet.PERMISSION_PARENTID_ROOT) {//添加子节点

            SysPermission parentPermission = this.selectOne(
                    new EntityWrapper<SysPermission>()
                            .eq("permission_id", sysPermission.getParentId()));
            //获取新增的权限id 生成deepCode后更新
            sysPermission.setTreeDeep(parentPermission.getTreeDeep() + 1);
            this.insert(sysPermission);//TODO 注意有没有获取id
            String tempDeepCode = TreeHelpUtil.createDeepCode(sysPermission.getTreeDeep() , sysPermission.getPermissionId());
            tempDeepCode = parentPermission.getDeepCode()+tempDeepCode;
            sysPermission.setDeepCode(tempDeepCode);
            this.updateById(sysPermission);
        } else {//添加根节点
            sysPermission.setTreeDeep(0);
            sysPermission.setParentId(-1L);
            this.insert(sysPermission);//TODO 注意有没有获取id
            String tempDeepCode = TreeHelpUtil.createDeepCode(0, sysPermission.getPermissionId());
            sysPermission.setDeepCode(tempDeepCode);
            this.updateById(sysPermission);
        }
    }


    @Override
    public void updateSysPermission(SysPermission sysPermission) {

        this.updateDefaultVal(sysPermission);
        //非根节点重新计算深度  编码
        if(sysPermission.getPermissionId()!=SysConstantSet.PERMISSION_PARENTID_ROOT){
            SysPermission parentPermission = new SysPermission();
            if(SysConstantSet.PERMISSION_PARENTID_ROOT ==sysPermission.getParentId()){
                //设置根节点默认值
                sysPermission.setTreeDeep(0);
                String tempDeepCode = TreeHelpUtil.createDeepCode(0, sysPermission.getPermissionId());
                sysPermission.setDeepCode(tempDeepCode);
            }else{
                parentPermission = sysPermissionMapper.selectById(sysPermission.getParentId());
                sysPermission.setTreeDeep(parentPermission.getTreeDeep() + 1);
                String tempDeepCode = TreeHelpUtil.createDeepCode(sysPermission.getTreeDeep(), sysPermission.getPermissionId());
                tempDeepCode = parentPermission.getDeepCode()+tempDeepCode;
                sysPermission.setDeepCode(tempDeepCode);
            }

        }
        sysPermissionMapper.updateById(sysPermission);
    }

    @Transactional
    @Override
    public void removeSysPermission(HttpRequestData requestData) {

        String operationType = requestData.getString("removeType");
        SysPermission entity = new SysPermission();
        entity.setPermissionId(requestData.getLong("permissionId"));
        SysPermission target = sysPermissionMapper.selectById(entity);
        int count = sysPermissionMapper.selectCount(MybatisUtil.<SysPermission>condition().like("deep_code",target.getDeepCode(), SqlLike.RIGHT));
        if(count>1){
          requestData.createErrorResponse("不可以删除存在子节点的记录",SysTips.PARAM_ERROR);
          return;
        }
        this.removeOneRow(entity,operationType);

    }



    @Override
    public void searchUserPermission(HttpRequestData requestData) {
        SysUserToken user = SysAuthzUtil.currentSysUser();
        Map cd = new HashMap<>();
        cd.put("userId",user.getUserId());
        List<SysPermission> res ;
        Wrapper<SysPermission> war= MybatisUtil.conditionT();

        if(requestData.hasParam("position")){
            cd.put("position",requestData.getString("position"));
            war.eq("position",requestData.getString("position"));
        }
        if(requestData.hasParam("status")){
            cd.put("status",requestData.getString("status"));
            war.eq("status",requestData.getString("status"));
        }

        if(isRootUser()){
            res =  sysPermissionMapper.selectList(war);
        }else{
            res =  sysUserRoleService.searchUserRolePermission(cd);

        }
        requestData.fillResponseData(res);
    }




}