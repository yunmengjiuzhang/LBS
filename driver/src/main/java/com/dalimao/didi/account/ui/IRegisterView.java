package com.dalimao.didi.account.ui;

/**
 * 注册页面
 * Created by liuguangli on 17/3/12.
 */

public interface IRegisterView {
    /**
     * 显示／隐藏加载图表
     * @param show
     */
    void showOrHideLoading(boolean show);

    /**
     *  显示注册成功
     */
    void showRegisterSuc();
    /**
     * 显示自动登录成功
     */
    void showLoginSuc();
    /**
     *  显示用户已经存在
     */
    void showUserExist(boolean exist);
    /**
     * 显示服务器繁忙
     */
    void showServerError();

}
