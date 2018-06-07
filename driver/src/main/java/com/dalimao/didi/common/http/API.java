package com.dalimao.didi.common.http;

/**
 * 接口常量
 * Created by liuguangli on 17/3/7.
 */

public class API {
    /**
     * 获取验证码
     */
    public static final String GET_SMS_CODE = "/f34e28da5816433d/getMsgCode";

    /**
     *  校验验证码
     */
    public static final String VERIFY_SMS_CODE = "/f34e28da5816433d/checkMsgCode?phone=${phone}&code=${code}";
    /**
     *  注册用户
     */
    public static final String REGISTER = "/f34e28da5816433d/register";
    /**
     *  询问用户是否已经存在
     */
    public static final String IS_USER_EXIST = "/f34e28da5816433d/isUserExist?phone=${phone}";
    /**
     * 密码登录认证
     */
    public static final String AUTH = "/f34e28da5816433d/auth";
    /**
     * token登录认证
     */
    public static final String AUTH_TOKEN = "/f34e28da5816433d/login";

    /**
     *  更新自己的位置
     */

    public static final String UPDATE_MY_LOCATION = "/f34e28da5816433d/updateUserLocation";
    /**
     * 获取附近的司机
     */
    public static final String GET_NEAR_DRIVERS= "/f34e28da5816433d/getNearDrivers?latitude=${latitude}&longitude=${longitude}";

    /**
     *  呼叫司机
     */
    public static  final String CALL_DRIVERS = "/f34e28da5816433d/callDriver";
    /**
     * 取消呼叫
     */
    public static final String CANCEL_ORDER = "/f34e28da5816433d/cancelOrder";
    /**
     * 获取进行中的订单
     */
    public static final String GET_PROCESSING_ORDERS= "/f34e28da5816433d/getProcessingOrder?uid=${uid}";

}
