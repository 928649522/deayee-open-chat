package com.superhao.base.authz.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.superhao.base.authz.entity.SysRole;
import com.superhao.base.authz.entity.SysRolePermission;
import com.superhao.base.authz.mapper.SysRolePermissionMapper;
import com.superhao.base.authz.service.ISysRolePermissionService;
import com.superhao.base.common.service.impl.BaseServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;


@Service("sysRolePermissionService")
public class SysRolePermissionServiceImpl extends BaseServiceImpl<SysRolePermissionMapper, SysRolePermission> implements ISysRolePermissionService {

    @Autowired
    private SysRolePermissionMapper sysRolePermissionMapper;

    public void modifyRolePermissionRelation(SysRole role) {
        Long []permissions = this.strToLongArray(role.getPermissionIds());
        Wrapper<SysRolePermission> wrapper =new EntityWrapper();
        if(permissions==null ||permissions.length==0){
            wrapper.eq("role_id",role.getRoleId());
            sysRolePermissionMapper.delete(wrapper);
            return;
        }

        wrapper.eq("role_id",role.getRoleId());
        List<SysRolePermission> existsCount=sysRolePermissionMapper.selectList(wrapper);
        //判断是添加还是修改 关系
        //添加
        if(existsCount!=null&&existsCount.size()>0){//添加部分权限角色关系

            Set<Long> compareSet= new HashSet(Arrays.asList(permissions));
            int beforeSize = compareSet.size();
            List<SysRolePermission> insertSet = new ArrayList<>();
            List<Long> removeSet = new ArrayList<>();

            for(int i=0;i<existsCount.size();i++){//取交集
                compareSet.add(existsCount.get(i).getPermissionId());
            }
            if(existsCount.size()==beforeSize&&beforeSize == compareSet.size()){  //大小相等说明没有修改角色权限关系
                return;
            }
            Iterator<Long> items = compareSet.iterator();

            boolean isDelete = true;
            boolean isInsert = true;
            while(items.hasNext()){
                Long permissionId = items.next();
                //找出需要被删除的权限item
                for(int i =0;i<permissions.length;i++){ //不存在permissions中需要删除
                    if(permissionId.equals(permissions[i])){
                        isDelete = false;
                    }
                }

                if(isDelete){
                    removeSet.add(permissionId);
                }
                for(int i =0;i<existsCount.size();i++){  //找出新增的权限item
                    if(permissionId.equals( existsCount.get(i).getPermissionId())){
                        isInsert = false;
                    }
                }
                if(isInsert){
                    SysRolePermission relation = new SysRolePermission();
                    this.insertDefaultVal(relation);
                    relation.setPermissionId(permissionId);
                    relation.setRoleId(role.getRoleId());
                    insertSet.add(relation);
                }
                //找出新增的item
                isDelete = true;
                isInsert = true;
            }
            if(insertSet.size()>0){
                this.insertBatch(insertSet);
            }
            if(removeSet.size()>0){
                wrapper.in("permission_id",removeSet);
                this.delete(wrapper);
            }
        }else{//添加所有权限角色关系
            List<SysRolePermission> insetTarget = new ArrayList<>();
            for(int i=0;i<permissions.length;i++){
                SysRolePermission target = new SysRolePermission();
                this.insertDefaultVal(target);
                target.setPermissionId(permissions[i]);
                target.setRoleId(role.getRoleId());
                insetTarget.add(target);
            }
            this.insertBatch(insetTarget);
        }

        //修改
    }

    @Override
    public List<SysRolePermission> searchPermissionByRole(Long roleId) {
        return sysRolePermissionMapper.selectList(new EntityWrapper<SysRolePermission>()
                .eq("role_id",roleId));
    }


}