package com.dalimao.didi.account.model;

/**
 * Created by liuguangli on 17/2/25.
 */

public interface IAccountManager {

    /**
     * 用户登录验证
     * @param account
     * @param pw
     */
    void auth(String account, String pw);

    /**
     *  token 认证
     *
     */
    void authToken();
    /**
     * 用户注册
     * @param account
     * @param pw
     */
    void register(String account, String pw);

    /**
     * 判断登录态
     */
    void isLogin();

    /**
     * 获取本地用户信息
     * @return
     */
    void getLocalAccountInfo();

    /**
     *  获取短信验证码
     */

    void getSmsCode(String phone);
    /**
     *  验证短信验证码
     */
    void verifySmsCode(String phone, String code);
    /**
     *  判断用户是否存在
     */

    void checkUserExist(String phone);
}
