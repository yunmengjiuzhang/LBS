package com.dalimao.didi.account.ui;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.dalimao.corelibrary.VerificationCodeInput;
import com.dalimao.didi.R;
import com.dalimao.didi.account.model.AccountManagerImpl;
import com.dalimao.didi.account.presenter.ISmsCodePresenter;
import com.dalimao.didi.account.presenter.SmsCodePresenterImpl;
import com.dalimao.didi.common.http.OkHttpClientImpl;

/**
 * Created by liuguangli on 17/3/5.
 */

public class SmsCodeDialog extends Dialog implements ISmsCodeView {
    private String mPhone;
    private Button mResentBtn;
    private VerificationCodeInput mVerificationInput;
    private View mLoading;
    private View mErrorView;
    private TextView mPhoneTv;
    private ISmsCodePresenter mPresenter;
    private CountDownTimer mCountDownTimer = new CountDownTimer(10000,1000) {
        @Override
        public void onTick(long millisUntilFinished) {

            mResentBtn.setEnabled(false);
            mResentBtn.setText(String.format(getContext().getString(R.string.after_time_resend, millisUntilFinished/1000)));
        }

        @Override
        public void onFinish() {

            mResentBtn.setEnabled(true);
            mResentBtn.setText(getContext().getString(R.string.resend));
            cancel();
        }
    };
    public SmsCodeDialog(Context context, String phone) {

        this(context, R.style.Dialog);
        this.mPhone = phone;
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LayoutInflater inflater = (LayoutInflater) getContext()
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View root = inflater.inflate(R.layout.dialog_smscode_input, null);
        setContentView(root);
        mPhoneTv = (TextView) findViewById(R.id.phone);
        String template = getContext().getString(R.string.sending);
        mPhoneTv.setText(String.format(template, mPhone));
        mResentBtn = (Button) findViewById(R.id.btn_resend);
        mVerificationInput = (VerificationCodeInput) findViewById(R.id.verificationCodeInput);
        mLoading = findViewById(R.id.loading);
        mErrorView = findViewById(R.id.error);
        mErrorView.setVisibility(View.GONE);
        initListeners();
        mPresenter = new SmsCodePresenterImpl(this, new AccountManagerImpl(OkHttpClientImpl.getInstance()));
        mPresenter.subscribe();
        mPresenter.getSmsCode(mPhone);
    }
    @Override
    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        mCountDownTimer.cancel();
        mPresenter.unSubscribe();
    }
    @Override
    public void dismiss() {
        super.dismiss();
        mPresenter.unSubscribe();
    }


    public SmsCodeDialog(Context context, int themeResId) {
        super(context, themeResId);
    }

    protected SmsCodeDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }
    private void initListeners() {

        findViewById(R.id.close).setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        mResentBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resend();
            }
        });
        mVerificationInput.setOnCompleteListener(new VerificationCodeInput.Listener() {
            @Override
            public void onComplete(String code) {
                commit(code);
            }
        });
    }

    private void commit(String code) {
        showLoading();
        mPresenter.verifySmsCode(mPhone, code);
    }

    private void resend() {

        String template = getContext().getString(R.string.sending);
        mPhoneTv.setText(String.format(template, mPhone));
        mPresenter.getSmsCode(mPhone);
    }



    @Override
    public void showLoading() {
        mLoading.setVisibility(View.VISIBLE);

    }


    @Override
    public void showError() {

        mLoading.setVisibility(View.GONE);

    }

    @Override
    public void showSendState(boolean suc) {

        if (suc) {
            mPhoneTv.setText(String.format(getContext().getString(R.string.sms_code_send_phone), mPhone));
            mCountDownTimer.start();
        } else {
            mResentBtn.setEnabled(true);
            mResentBtn.setText(getContext().getString(R.string.resend));
            cancel();
        }

    }



    @Override
    public void showVerifyState(boolean suc) {
        if (!suc) {

            //提示验证码错误
            mErrorView.setVisibility(View.VISIBLE);
            mVerificationInput.setEnabled(true);
            mLoading.setVisibility(View.GONE);
        } else {
            //检查用户是否存在
            mPresenter.isUserExist(mPhone);
        }
    }

    @Override
    public void showUserExist(boolean exist) {
        mLoading.setVisibility(View.GONE);
        mErrorView.setVisibility(View.GONE);
        dismiss();
        if (!exist) {
            //用户不存在,进入注册


            new CreatePasswordDialog(getContext(), mPhone).show();
        } else {
            // 用户存在 ，进入登录
             new LoginDialog(getContext(), mPhone).show();
        }
    }
}
