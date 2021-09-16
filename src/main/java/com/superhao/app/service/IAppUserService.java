package com.superhao.app.service;

import com.superhao.app.entity.AppUser;
import com.superhao.base.common.service.IBaseService;
import com.superhao.base.entity.HttpRequestData;


import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * H5聊天app用户表
 *
 * @author
 * @email
 * @date 2019-10-16 13:57:42
 */
public interface IAppUserService extends IBaseService<AppUser> {


    void realLogin(AppUser appUser, HttpRequestData requestData);

    void anonymousLogin(Map param,HttpRequestData requestData);

    void getVerificationCode(HttpServletResponse response, Map request);

    void register(AppUser appUser, HttpRequestData requestData);

    boolean registerParamValidate(AppUser appUser, HttpRequestData requestData);

    void searchUserGroupChat(HttpRequestData requestData);

    void joinRoomByRoomId(AppUser appUser,String roomId);
    AppUser searchAnonymous(Long id);

    void searchUserCallList(HttpRequestData requestData);

    void searchUserContact(HttpRequestData requestData);

    void info(HttpRequestData requestData);

    void searchFirend(HttpRequestData requestData);

    void addFirend(HttpRequestData requestData);

    void agreeFriend(HttpRequestData requestData);

    void removeFriend(HttpRequestData requestData);

    void modifyDetail(AppUser appUser,HttpRequestData requestData);

    void removeGroup(HttpRequestData requestData);

    void removeChatMsg(HttpRequestData requestData);


    void changeIntegral(Long userId, Double rpcount);


    /**
     * 分页查询 app用户
     * @param requestData
     */
    void searchChatUserByPage(HttpRequestData requestData);

    /**
     * 充值积分
     * @param requestData
     */
    void rechargeIntegral(HttpRequestData requestData);

    /**
     * 修改密码
     * @param requestData
     */
    void modifyPwd(HttpRequestData requestData);

    void modifyRemark(HttpRequestData requestData);

    void searchChatUserNoneAccountByPage(HttpRequestData requestData);

    void redirectLogic(HttpRequestData requestData);

    void searchGroupRecordByPage(HttpRequestData requestData);
}

