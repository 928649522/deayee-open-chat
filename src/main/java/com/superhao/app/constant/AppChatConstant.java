package com.superhao.app.constant;

import org.springframework.util.FileCopyUtils;

/**
 * @Auther: super
 * @Date: 2019/10/27 14:44
 * @email:
 */
public interface AppChatConstant {

    /**
     * 房间类型
     */
    String ROOM_TYPE_GROUP="group";
    String ROOM_TYPE_USER="user";


    /**
     * 处理聊天数据
     */
    String DATA_HANDLE_TYPE_1 = "1";

    int DATA_TEXT_MAX_LENGTH = 300; //文本信息最大长度

    /**
     * 用户类型
     * 0：匿名用户
     * 1：注册用户
     * 3：机器人
     */
    String USER_TYPE_0 = "0";
    String USER_TYPE_1 = "1";
    String USER_TYPE_3 = "3";

    /**
     * 登陆类型
     * NM：匿名用户登陆
     * SM：注册用户登陆
     */
  String LOGIN_TYPE_NM="NM";
  String LOGIN_TYPE_SM="SM";


    /**
     * webSocket消息处理类型
     */
    String HANDLE_TYPE_TEXT = "text"; //普通消息
    String HANDLE_TYPE_ONLINE = "online"; //通知上线
    String HANDLE_TYPE_REMOVE_ROOM = "rmroom"; //删除房间
    String HANDLE_TYPE_CALL_NOTIFY = "contact";//联系人通知
    String HANDLE_TYPE_UPDATE_ROOM = "uproom"; //更新房间信息

    /**
     * ChatData type 消息类型常数
     */
     String TYPE_ONLINE = "online"; //上线通知
    String TYPE_REMOVE_ROOM = "rmroom";
    String TYPE_ADD_FRIEND = "addfriend";
    String TYPE_AGREE_FRIEND = "agreefriend";
    String TYPE_TEMP_CONTACT = "tempcontact";//临时会话
    String TYPE_CREATE_NEW_GROUP = "newgroup";//被拉入新的群

    String TYPE_GROUP_ANNOUNCEMENT = "announcement"; //公告
    String TYPE_GROUP_MESSAGE_REMOVE = "msgremove"; //消息删除
    String TYPE_GROUP_MESSAGE_REMOVE_ALL = "msgremoveall"; //删除所有消息
    String TYPE_GROUP_DISABLE_PEOPLE = "dispeople"; //踢除成员
    String TYPE_GROUP_EXIT_PEOPLE = "exitpeople";
    /**
     * 房间类型
     */
    String SOCKET_TYPE_CONTACT = "contact"; // 联系人页面
    String SOCKET_TYPE_GROUP = "group"; //群聊页面
    String SOCKET_TYPE_CALL_LIST = "calllist"; //联系薄页面

    /**
     * 是或否常量表示
     */
    String CONSTANT_YES="1";
    String CONSTANT_NO="0";



    /**
     * 公共的 app topic
     */
    String TOPIC_SYS_NOTIFY="sysccommon";//


    /**
     * 红包部分
     */
    double RP_RED_POING_COUNT_MAX = 500;
    String RP_RED_POING_CONSTAN="1";
    String RP_RED_POING_RANDOM="0";

    /**
     * 活动部分  module
     */
    String ACTIVITY_REDPOINT="RedPoint"; //红包
    String ACTIVITY_INTEGRAL_REDEEM="IntegralRedeem"; //积分兑换
    String ACTIVITY_INTEGRAL_RECHARGE="SysRecharge";//系统充值
    /**
     * 红包活动
     */
    String REDPOINT_STATUS_BACK="B";//红包返还

    /**
     * 兑换积分活动
     */
    String INTEGRAL_REDEEM_STATUS_UN_HANDLE="0";//管理员未处理
    String INTEGRAL_REDEEM_STATUS_HANDLE="1";//管理员已经处理
    double INTEGRAL_REDEEM_MIN_VAL =18.00;//最小兑换值

    /**
     * 积分
     */
    String INTEGRAL_ADD="+";
    String INTEGRAL_SUBTRACT="-";


    int GROUP_PAGE_SIZE = 18;
}
