package com.dalimao.didi.account.presenter;

import com.dalimao.didi.account.model.AccountManagerImpl;
import com.dalimao.didi.account.model.IAccountManager;
import com.dalimao.didi.account.model.bean.AccountInfo;
import com.dalimao.didi.account.ui.ILoginView;
import com.dalimao.didi.account.ui.LoginDialog;
import com.dalimao.didi.common.databus.RegisterBus;
import com.dalimao.didi.common.databus.RxBus;
import com.dalimao.didi.common.http.CommonResponse;

/**
 * Created by liuguangli on 17/3/13.
 */
public class LoginPresenterImpl implements ILoginPresenter {
    private IAccountManager manager;
    private ILoginView loginView;
    public LoginPresenterImpl(IAccountManager manager, ILoginView loginView) {
        this.manager = manager;
        this.loginView = loginView;
    }

    @Override
    public void subscribe() {

        RxBus.getInstance().register(this);
    }

    @Override
    public void unSubscribe() {

        RxBus.getInstance().unRegister(this);
    }

    @Override
    public void login(String mPhoneStr, String password) {
        manager.auth(mPhoneStr, password);
    }

    @RegisterBus
    public void onAccountInfo(AccountInfo info){
        if (info.getCode() == CommonResponse.STATE_OK) {
            loginView.showLoginSuc();
        } else if (info.getCode() == CommonResponse.STATE_PW_ERROR) {
            loginView.showPasswordError();
        } else {
            loginView.showServerError();
        }
    }
}
