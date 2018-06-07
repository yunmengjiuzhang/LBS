package com.dalimao.didi.account.presenter;

import com.dalimao.didi.account.model.bean.AccountInfo;
import com.dalimao.didi.account.model.bean.RegisterState;
import com.dalimao.didi.common.IbasePresenter;

/**
 * 注册页面表现层
 * Created by liuguangli on 17/3/12.
 */

public interface IRegisterPresenter extends IbasePresenter {
    /**
     *  注册
     */
    void register(String phone, String password);
    /**
     *  用户是否存在
     */
    void checkUserExist(String phone);

    /**
     *  登录
     */

    void login(String phone, String password);
    /**
     *  注册状态
     */
    void onRegisterState(RegisterState state);
    /**
     * 登录状态
     */
    void onAccountInfo(AccountInfo accountInfo);
}
