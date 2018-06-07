package com.dalimao.didi.account.presenter;

import com.dalimao.didi.account.model.bean.SmsCodeSendState;
import com.dalimao.didi.account.model.bean.SmsCodeVerifyState;
import com.dalimao.didi.common.IbasePresenter;

/**
 * 验证码输入界面表现逻辑层
 * Created by liuguangli on 17/3/5.
 */

public interface ISmsCodePresenter extends IbasePresenter{
    void getSmsCode(String phone);
    void verifySmsCode(String phone, String code);
    void isUserExist(String phone);
    void onSmsSendState(SmsCodeSendState smsCodeSendState);
    void onSmsCodeVerifyState(SmsCodeVerifyState smsCodeVerifyState);

}
