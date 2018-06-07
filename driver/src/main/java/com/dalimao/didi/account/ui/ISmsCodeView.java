package com.dalimao.didi.account.ui;

/**
 * Created by liuguangli on 17/3/5.
 */

public interface ISmsCodeView {
    void showLoading();
    void showError();
    void showSendState(boolean suc);

    void showVerifyState(boolean suc);
    void showUserExist(boolean exist);
}
