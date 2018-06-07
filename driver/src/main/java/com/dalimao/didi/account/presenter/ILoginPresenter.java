package com.dalimao.didi.account.presenter;

import com.dalimao.didi.common.IbasePresenter;

/**
 * Created by liuguangli on 17/3/13.
 */
public interface ILoginPresenter extends IbasePresenter {
    /**
     * 登录
     * @param mPhoneStr
     * @param password
     */
    void login(String mPhoneStr, String password);
}
