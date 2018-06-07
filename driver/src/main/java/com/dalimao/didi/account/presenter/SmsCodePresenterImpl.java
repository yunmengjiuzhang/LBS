package com.dalimao.didi.account.presenter;

import com.dalimao.didi.account.model.bean.RegisterState;
import com.dalimao.didi.account.model.bean.SmsCodeSendState;
import com.dalimao.didi.account.model.bean.SmsCodeVerifyState;
import com.dalimao.didi.common.databus.RegisterBus;
import com.dalimao.didi.common.http.CommonResponse;
import com.dalimao.didi.common.databus.RxBus;
import com.dalimao.didi.account.model.IAccountManager;
import com.dalimao.didi.account.ui.ISmsCodeView;

/**
 * 短信验证码
 * Created by liuguangli on 17/3/5.
 */

public class SmsCodePresenterImpl implements ISmsCodePresenter {

    private ISmsCodeView view;
    private IAccountManager manager;

    public SmsCodePresenterImpl(ISmsCodeView view, IAccountManager manager) {
        this.view = view;
        this.manager = manager;
    }

    @RegisterBus
    @Override
    public void onSmsSendState(SmsCodeSendState smsCodeSendState) {
        if (smsCodeSendState.getCode() == CommonResponse.STATE_OK) {
            view.showSendState(true);
        } else {
            view.showSendState(false);
        }
    }

    @RegisterBus
    @Override
    public void onSmsCodeVerifyState(SmsCodeVerifyState smsCodeVerifyState) {
        view.showVerifyState(smsCodeVerifyState.getCode() == CommonResponse.STATE_OK);
    }
    @RegisterBus
    public void onRegisterState(RegisterState registerState) {
        view.showUserExist(registerState.getCode() == CommonResponse.STATE_USER_EXIST);
    }

    @Override
    public void getSmsCode(String phone) {


        manager.getSmsCode(phone);

    }

    @Override
    public void verifySmsCode(String phone, String code) {
        manager.verifySmsCode(phone, code);
    }

    @Override
    public void isUserExist(String phone) {
        manager.checkUserExist(phone);
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
