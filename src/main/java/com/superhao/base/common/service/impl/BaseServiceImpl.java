package com.superhao.base.common.service.impl;

import com.baomidou.mybatisplus.entity.TableInfo;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.baomidou.mybatisplus.toolkit.ReflectionKit;
import com.baomidou.mybatisplus.toolkit.TableInfoHelper;
import com.superhao.app.entity.token.AppUserToken;
import com.superhao.base.cache.util.AppTokenCacheUtil;
import com.superhao.base.cache.util.SysAuthzUtil;
import com.superhao.base.common.constant.SysConstantSet;
import com.superhao.base.common.service.IBaseService;
import com.superhao.base.entity.token.SysUserToken;
import com.superhao.base.exception.HttpParamException;
import com.superhao.base.exception.SystemException;
import com.superhao.base.util.IpUtil;
import com.superhao.base.util.ReflectionUtils;
import com.superhao.base.util.SpringContexUtil;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.Date;
import java.util.UUID;

/**
 * @Auther: zehao
 * @Date: 2019/4/25 12:51
 * @email: 928649522@qq.com
 * @Description:
 */
@Slf4j
public class BaseServiceImpl<M extends BaseMapper<T>, T> extends ServiceImpl<M, T> implements IBaseService<T> {


    /**
     * 更新系统规定的默认值
     * @param entity
     */
    public void updateDefaultVal(Object entity) {


        SysUserToken sysUser = SysAuthzUtil.currentSysUser();
        try {
            //设置创建时间  修改时间默认值
            this.setDefaultValue(entity, "lastUpdateTime", new Date());

            if(sysUser!=null){
                //设置创建人 修改人
                this.setDefaultValue(entity, "modifier", sysUser.getUserId());

            }

            //设置 IP
            this.setDefaultValue(entity, "lastUpdatePlace", IpUtil.getIp(SpringContexUtil.getServletRequest()));
            this.updateVersionNumber(entity);
        } catch (Exception e) {
            log.error("error:{}   tips:{}","perform service error ","更新字段默认值出错");
            throw new SystemException("设置默认值时,没有找到对应的字段");
        }

    }

    public void insertDefaultVal(Object entity) {

        SysUserToken sysUser = SysAuthzUtil.currentSysUser();
        try {
            //设置创建时间  修改时间默认值
            this.setDefaultValue(entity, "creationTime", new Date());
            this.setDefaultValue(entity, "lastUpdateTime", new Date());

            this.setDefaultValue(entity, "enable", SysConstantSet.COLUMN_ENABLE_Y);


            //设置创建人 修改人
            if(sysUser!=null){
                this.setDefaultValue(entity, "creator", sysUser.getUserId());
                this.setDefaultValue(entity, "modifier", sysUser.getUserId());
            }


            //设置 IP
            this.setDefaultValue(entity, "creationPlace", IpUtil.getIp(SpringContexUtil.getServletRequest()));
            this.setDefaultValue(entity, "lastUpdatePlace", IpUtil.getIp(SpringContexUtil.getServletRequest()));

        } catch (Exception e) {
            throw new SystemException("设置默认值时,没有找到对应的字段");
        }
    }
    public void insertDefaultValForApp(Object entity,boolean setUser) {
        try {
            //设置创建时间  修改时间默认值
            this.setDefaultValue(entity, "creationTime", new Date());
            this.setDefaultValue(entity, "lastUpdateTime", new Date());

            this.setDefaultValue(entity, "enable", SysConstantSet.COLUMN_ENABLE_Y);

            if(setUser){
                AppUserToken token = AppTokenCacheUtil.currentUser();
                if(token!=null){
                    this.setDefaultValue(entity, "creator", token.getUserId());
                    this.setDefaultValue(entity, "modifier", token.getUserId());
                }

            }
            //设置 IP
            this.setDefaultValue(entity, "creationPlace", IpUtil.getIp(SpringContexUtil.getServletRequest()));
            this.setDefaultValue(entity, "lastUpdatePlace", IpUtil.getIp(SpringContexUtil.getServletRequest()));

        } catch (Exception e) {
            throw new SystemException("设置默认值时,没有找到对应的字段");
        }
    }
 /*   public void insertDefaultValForApp(Object entity,boolean setUser) {
        AppUserToken token = AppTokenCacheUtil.currentUser();
        try {
            //设置创建时间  修改时间默认值
            this.setDefaultValue(entity, "creationTime", new Date());
            this.setDefaultValue(entity, "lastUpdateTime", new Date());

            this.setDefaultValue(entity, "enable", SysConstantSet.COLUMN_ENABLE_Y);
            if(token!=null){
                this.setDefaultValue(entity, "creator", sysUser.getUserId());
                this.setDefaultValue(entity, "modifier", sysUser.getUserId());
            }

            //设置 IP
            this.setDefaultValue(entity, "creationPlace", IpUtil.getIp(SpringContexUtil.getServletRequest()));
            this.setDefaultValue(entity, "lastUpdatePlace", IpUtil.getIp(SpringContexUtil.getServletRequest()));

        } catch (Exception e) {
            throw new SystemException("设置默认值时,没有找到对应的字段");
        }
    }*/

    /**
     * 更新entity 版本号
     *
     * @param entity
     */
    private void updateVersionNumber(Object entity) {
        Field targetField = ReflectionUtils.getDeclaredField(entity, "versionNumber");

        if (targetField != null) {
            try {
                Integer versionNumber = (Integer) targetField.get(entity);
                if (versionNumber != null) {
                    targetField.set(entity, versionNumber + 1);
                }
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }

    private void setDefaultValue(Object entity, String fieldName, Object defaultValue) {
        Field targetField = ReflectionUtils.getDeclaredField(entity, fieldName);
        if (targetField != null) {
            try {
                targetField.set(entity, defaultValue);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 单条的逻辑和物理删除封装
     * @param entity
     * @param removeType
     */
    public boolean removeOneRow(T entity, String removeType) {
        try {
            if (SysConstantSet.SYSTEM_DELETE_TYPE_LOGIC.equals(removeType)) { //逻辑删除
                Field  targetField = ReflectionUtils.getDeclaredField(entity, "enable");
                targetField.set(entity, SysConstantSet.COLUMN_ENABLE_N);
                return this.updateById(entity);
            } else if (SysConstantSet.SYSTEM_DELETE_TYPE_PHYSICAL.equals(removeType)) {//物理删除
                Class<?> cls = entity.getClass();
                TableInfo tableInfo = TableInfoHelper.getTableInfo(cls);
                Object idVal = ReflectionKit.getMethodValue(cls, entity, tableInfo.getKeyProperty());
                return this.deleteById(new Long(idVal.toString()));
                //sysPermissionMapper.delete()
            }
        } catch (IllegalAccessException e) {
            throw new SystemException("删除反射操作出错：" + e.getMessage());
        }
        return false;
    }

    /**
     * "one,two,three“格式的数据转Long数组
     * @param str
     * @return
     */
    public Long[] strToLongArray(String str){
        try {
            Long []res = null;
            if(!org.springframework.util.StringUtils.isEmpty(str)){
                String strArr[] = str.split(",");
                res = new Long[strArr.length];
                for(int i=0;i< strArr.length ;i++){
                    res[i]=new Long(strArr[i].trim());
                }
            }
            return res;
        }catch (Exception e){
            throw new HttpParamException("参数类型错误");
        }

    }

    public String createUUID(){
        return UUID.randomUUID().toString().replace("-","");
    }

    public File createFilePath(String path, String fileName){
        File file =new File(path);

        // 判断文件夹不存在就创建
        if (!file.exists()) {
            file.mkdirs();
            file.setExecutable(true);
            file.setReadable(true);//设置可读权限
            file.setWritable(true);//设置可写权限
        }
        File saveFile =new File(path+File.separator+fileName);
        if (saveFile.exists()) {
            saveFile.delete();
        }
        return saveFile;
    }



}
