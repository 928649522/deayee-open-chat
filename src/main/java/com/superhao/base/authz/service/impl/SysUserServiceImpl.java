package com.superhao.base.authz.service.impl;

import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.google.common.base.Splitter;
import com.superhao.base.authz.entity.SysPermission;
import com.superhao.base.authz.entity.SysUser;
import com.superhao.base.authz.handler.AuthCoreHandler;
import com.superhao.base.authz.mapper.SysUserMapper;
import com.superhao.base.authz.service.ISysPermissionService;
import com.superhao.base.authz.service.ISysUserRoleService;
import com.superhao.base.authz.service.ISysUserService;
import com.superhao.base.cache.util.SysAuthzUtil;
import com.superhao.base.common.constant.SysConstantSet;
import com.superhao.base.common.constant.SysServiceConfigSet;
import com.superhao.base.common.dto.SysTips;
import com.superhao.base.common.service.impl.BaseServiceImpl;
import com.superhao.base.entity.HttpRequestData;
import com.superhao.base.entity.Pages;
import com.superhao.base.entity.token.SysUserToken;
import com.superhao.base.util.IpUtil;
import com.superhao.base.util.Md5Util;
import com.superhao.base.util.MybatisUtil;
import com.superhao.base.util.SpringContexUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.*;


@Service("sysUserService")
public class SysUserServiceImpl extends BaseServiceImpl<SysUserMapper, SysUser> implements ISysUserService {


    @Autowired
    private SysServiceConfigSet serviceConfigSet;
    @Autowired
    private SysUserMapper sysUserMapper;
    @Autowired
    private ISysUserRoleService sysUserRoleService;

    @Autowired
    private AuthCoreHandler authCoreHandler;
    @Autowired
    private SysPermissionServiceImpl sysPermissionService;


    @Override
    public boolean login(SysUser sysUser, HttpRequestData requestData) {

        SysUser cd = new SysUser();
        cd.setAccountName(sysUser.getAccountName());

        SysUser user = sysUserMapper.selectJoinFile(cd);
        String tempPassword = Md5Util.getMD5(sysUser.getPassword() + serviceConfigSet.getUserSalt());


        //验证密码
        if (user == null || !user.getPassword().equals(tempPassword)) {
            requestData.createErrorResponse("账号或密码有误");
            return false;
        }
        if(SysConstantSet.COLUMN_ENABLE_N.equals(user.getEnable())){
            requestData.createErrorResponse("此账号已被禁用，请联系管理员");
            return false;
        }

        user.setFilePath(serviceConfigSet.getHttpServerPath()+user.getFilePath());
        //找出对应的权限，并加入缓存当中
        SysUserToken token;
        //开启单人登录
        if(new Boolean(serviceConfigSet.getOpenSingleLogin()).booleanValue()){
            token = authCoreHandler.singleLogin(user);
        }else{
             token = new SysUserToken(new Integer(serviceConfigSet.getSysSessionTimeout()),user.getUserId().toString());
        }
        BeanUtils.copyProperties(user, token);
        this.loadUserPermission(token, user);//获取权限

      //  SpringContexUtil.getSession().setAttribute(SysConstantSet.SYSTEM_AUTHZ_TOKEN_KEY, token.getTokenCode());
       // SpringContexUtil.getSession().setAttribute(SysConstantSet.SESSION_USER_KEY, user);
        token.setLoginAddress(IpUtil.getIp(SpringContexUtil.getServletRequest()));
        SysAuthzUtil.addSysUserToken(token);
        Map tokenMap = new HashMap();
        tokenMap.put("Authorization",token.getTokenCode());
        tokenMap.put("username",token.getUsername());
        tokenMap.put("filePath",token.getFilePath());
        sysUserMapper.updateLoginNumber(user.getUserId());
        requestData.fillResponseData(tokenMap);
        return true;
    }

    private void loadUserPermission(SysUserToken token, SysUser user) {
        Map map = new HashMap();
        map.put("userId", user.getUserId());
        List<SysPermission> userPermission = sysUserRoleService.searchUserRolePermission(map);

        Set<String> set = new HashSet<>();

        if (userPermission.size() > 0) {
            for (SysPermission item : userPermission) {
                String url = item.getUrl();
                if (StringUtils.isEmpty(url)) {
                    continue;
                }
                if (!SysConstantSet.PERMISSION_TYPE_3.equals(item.getType())) {
                    set.add(url);
                    continue;
                }
                if (!url.contains(";")) {//单个url
                    set.add(url);
                    continue;
                }
                //多个url
                List<String> target = Splitter.on(";").trimResults().omitEmptyStrings().splitToList(url);
                set.addAll(target);
            }
        }
        token.setPermissionSet(set);
    }


    @Override
    public boolean sysUserUpdateValidate(SysUser sysUser, HttpRequestData requestData, boolean isUpdate) {
        Wrapper<SysUser> w = MybatisUtil.conditionT();

        if (isUpdate && sysUser.getUserId() == null) {
            requestData.createErrorResponse("缺失必要参数", SysTips.PARAM_ERROR);
            return false;
        }

        w.eq("account_name", sysUser.getAccountName());
        if (isUpdate) {
            w.ne("user_id", sysUser.getUserId());
        }
        Integer row = sysUserMapper.selectCount(w);
        if (row != null && row.intValue() > 0) {
            requestData.createErrorResponse("账号名已经存在", SysTips.PARAM_ERROR);
            return false;
        }
        return true;
    }

    @Transactional
    @Override
    public void updateSysUser(SysUser sysUser) {
        this.updateDefaultVal(sysUser);
        if (!StringUtils.isEmpty(sysUser.getPassword())) {
            String newPwd = Md5Util.getMD5(sysUser.getPassword() + serviceConfigSet.getUserSalt());
            sysUser.setPassword(newPwd);
        }
        sysUserMapper.updateById(sysUser);
        sysUserRoleService.updateSysUserRole(sysUser);

    }

    @Transactional
    @Override
    public void addSysUser(SysUser sysUser, HttpRequestData requestData) {
        this.insertDefaultVal(sysUser);
        String newPwd = Md5Util.getMD5(sysUser.getPassword() + serviceConfigSet.getUserSalt());
        sysUser.setPassword(newPwd);
        sysUser.setLoginNumber(0);
        sysUserMapper.insert(sysUser);
        sysUserRoleService.updateSysUserRole(sysUser);
        requestData.fillResponseData(sysUser);
    }

    @Override
    public void searchSysUserByPage(HttpRequestData requestData) {
        Wrapper<SysUser> condition = MybatisUtil.conditionT();
        condition.eq("enable", SysConstantSet.COLUMN_ENABLE_Y);
        if(!sysPermissionService.isRootUser()){
            condition.and().eq("creator", SysAuthzUtil.currentSysUser().getUserId());
        }
        condition.orderBy("creation_time", false);

        Page<SysUser> page = Pages.create(requestData.getInteger("page"));
        List<SysUser> data = sysUserMapper.selectPage(page, condition);
        int count = sysUserMapper.selectCount(condition);
        requestData.generatePageData(data, count);
    }

    @Override
    public void searchSysUser(SysUser sysUser, HttpRequestData requestData) {
        sysUser.setEnable(SysConstantSet.COLUMN_ENABLE_Y);
        SysUser res = sysUserMapper.selectJoinFile(sysUser);
        res.setFilePath(serviceConfigSet.getHttpServerPath() + res.getFilePath());
        requestData.fillResponseData(res);
    }

    @Override
    public void logicRemoveSysUser(HttpRequestData requestData) {
        if(sysPermissionService.isRootUser()){
            requestData.createErrorResponse("无法删除ROOT用户");
            return;
        }
        SysUser target = new SysUser();
        String removeType = requestData.getString("removeType");
        Long userId = requestData.getLong("userId");
        target.setUserId(userId);
        if(super.removeOneRow(target,removeType)&&SysConstantSet.SYSTEM_DELETE_TYPE_PHYSICAL.equals(removeType)){
            sysUserRoleService.delete(MybatisUtil.conditionT().eq("user_id",userId));
        }
    }

    @Override
    public void updateSysUserDetail(SysUser sysUser, HttpRequestData requestData) {
        this.updateDefaultVal(sysUser);
        sysUser.setAccountName(null);
        Long currentUserId = SysAuthzUtil.currentSysUser().getUserId();
        sysUser.setUserId(currentUserId);
        if (!StringUtils.isEmpty(sysUser.getPassword())) {
            String newPwd = Md5Util.getMD5(sysUser.getPassword() + serviceConfigSet.getUserSalt());
            sysUser.setPassword(newPwd);
        }
        //更新Session
        if(sysUserMapper.updateById(sysUser)>0){
            SysUser target =sysUserMapper.selectJoinFile(sysUser);
            target.setFilePath(serviceConfigSet.getHttpServerPath()+target.getFilePath());
            HttpServletRequest request = SpringContexUtil.getServletRequest();

            SysUserToken token = SysAuthzUtil.get(request.getHeader("Authorization"));
            BeanUtils.copyProperties(target,token);
            SysAuthzUtil.addSysUserToken(token);
            requestData.fillResponseData(target);
        }


    }


    public static void main(String[] args) {
        System.out.println(Md5Util.getMD5("21ff37c85e8d0c90cd3a480216ed3701ilovechina"));
    }



}
