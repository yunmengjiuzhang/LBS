package com.dalimao.didi.account.presenter;

import com.dalimao.didi.account.model.IAccountManager;
import com.dalimao.didi.account.model.bean.AccountInfo;
import com.dalimao.didi.account.model.bean.RegisterState;
import com.dalimao.didi.account.ui.IRegisterView;
import com.dalimao.didi.common.databus.RegisterBus;
import com.dalimao.didi.common.databus.RxBus;
import com.dalimao.didi.common.http.CommonResponse;

/**
 * Created by liuguangli on 17/3/12.
 */

public class RegisterPresenterImpl implements IRegisterPresenter{

    private IAccountManager manager;
    private IRegisterView view;

    public RegisterPresenterImpl(IAccountManager manager, IRegisterView view) {
        this.manager = manager;
        this.view = view;
    }

    @Override
    public void register(String phone, String password) {
        manager.register(phone, password);
    }

    @Override
    public void checkUserExist(String phone) {
        manager.checkUserExist(phone);
    }

    @Override
    public void login(String phone, String password) {
        manager.auth(phone, password);
    }

    @RegisterBus
    @Override
    public void onRegisterState(RegisterState state) {

        if (state.getCode() == CommonResponse.STATE_USER_EXIST) {

            view.showUserExist(true);
        } else if (state.getCode() == CommonResponse.STATE_NO_REGISTER){
            view.showUserExist(false);
        } else if (state.getCode() == CommonResponse.STATE_ERROR){

            view.showServerError();

        } else {
            view.showRegisterSuc();
        }
    }
    @RegisterBus
    @Override
    public void onAccountInfo(AccountInfo accountInfo) {
        if (accountInfo.getCode() == CommonResponse.STATE_OK) {
            view.showLoginSuc();
        } else {
            view.showServerError();
        }
    }

    @Override
    public void subscribe() {
        RxBus.getInstance().register(this);

    }

    @Override
    public void unSubscribe() {
        RxBus.getInstance().unRegister(this);
    }
}
