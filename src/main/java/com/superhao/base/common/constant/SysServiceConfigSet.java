package com.superhao.base.common.constant;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * @Auther: zehao
 * @Date: 2019/5/5 15:56
 * @email: 928649522@qq.com
 */
@Component
@Getter
public class SysServiceConfigSet {

    /**
     * 邮箱配置
     */
    @Value("${email_username}")
    private String emailUsername;
    @Value("${email_password}")
    private String emailPassword;
    @Value("${email_port}")
    private String emailPort;
    @Value("${email_host}")
    private String emailHost;


    /**
     * 登陆模式
     */
    @Value("${open_single_login}")
    private String openSingleLogin;

    /**
     * root账户名
     */
    @Value("${root}")
    private String root;

    @Value( "${user_salt}" )
    public String userSalt;

    @Value("${file_base_path}")
    public String fileBasePath;

    @Value("${http_server_path}")
    private String httpServerPath;


    @Value("${sys_session_timeout}")
    private String sysSessionTimeout;
    /**
     * 匿名用户的权限URL
     */
    @Value("${anonymous_permission}")
    private String anonymousUrls;

    /**
     * 用户查看的历史记录数
     */
    @Value("${app_user_find_record}")
    private int appUserFindRecord;

    /**
     * app房间页面地址 二维码地址
     */
    @Value("${app_room_page_path}")
    private String appRoomPagePath;
    @Value("${app_user_base_count}")
    private int appUserBaseCount;


    /**
     * 开启文件共享模式
     */
    @Value("${open_remote_file}")
    private String openRemoteFile;
    /**
     * SFTP 共享文件路径
     */
    @Value("${remote_file_base_path}")
    private String remoteFileBasePath;
    @Value("${share_file_host}")
    private String shareFileHost;
    @Value("${share_file_port}")
    private int shareFilePort;
    @Value("${share_file_user}")
    private String shareFileUser;
    @Value("${share_file_pwd}")
    private String shareFilePwd;
    @Value("${websocket_server_port}")
    private String websocketServerPort;

    @Value("${app_user_create_group_max}")
    private int appUserCreateGroupMax;

    @Value("${app_register_code_expired}")
    private int appRegisterCodeExpired;


}
