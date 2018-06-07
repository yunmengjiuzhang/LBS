package com.dalimao.didi.account.ui;

/**
 * Created by liuguangli on 17/3/13.
 */
public interface ILoginView {
    /**
     * 登录成功
     */
    void showLoginSuc();

    /**
     * 显示／隐藏加载框
     * @param show
     */
    void showOrHideLoading(boolean show);

    /**
     * 显示服务器错误
     */
    void showServerError();
    /**
     *  显示密码错误
     */
    void showPasswordError();
}
