package com.dalimao.didi.account.ui;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.dalimao.didi.R;
import com.dalimao.didi.account.model.AccountManagerImpl;
import com.dalimao.didi.account.presenter.ILoginPresenter;
import com.dalimao.didi.account.presenter.IRegisterPresenter;
import com.dalimao.didi.account.presenter.LoginPresenterImpl;
import com.dalimao.didi.account.presenter.RegisterPresenterImpl;
import com.dalimao.didi.common.http.OkHttpClientImpl;
import com.dalimao.didi.common.utils.ToastUtil;

/**
 * 密码创建/修改
 * Created by liuguangli on 17/2/26.
 */

public class LoginDialog extends Dialog implements ILoginView{

    private TextView mTitle;
    private TextView mPhone;
    private EditText mPw;
    private Button mBtnConfirm;
    private View mLoading;
    private TextView mTips;
    private ILoginPresenter presenter;
    private String mPhoneStr;

    public LoginDialog(Context context, String phone) {
        this(context, R.style.Dialog);
        mPhoneStr = phone;
    }

    public LoginDialog(Context context, int theme) {
        super(context, theme);
        presenter = new LoginPresenterImpl(new AccountManagerImpl(OkHttpClientImpl.getInstance()), this);
        presenter.subscribe();
    }

    @Override
    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        presenter.unSubscribe();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LayoutInflater inflater = (LayoutInflater) getContext()
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View root = inflater.inflate(R.layout.dialog_login_input, null);
        setContentView(root);
        initViews();

    }

    @Override
    public void dismiss() {
        presenter.unSubscribe();
        super.dismiss();

    }

    private void initViews() {


        mPhone = (TextView) findViewById(R.id.phone);
        mPw = (EditText) findViewById(R.id.password);
        mBtnConfirm = (Button) findViewById(R.id.btn_confirm);
        mLoading = findViewById(R.id.loading);
        mTips = (TextView) findViewById(R.id.tips);
        mTitle = (TextView) findViewById(R.id.dialog_title);
        findViewById(R.id.close).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        mBtnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submit();
            }
        });

        mPhone.setText(mPhoneStr);

    }

    /**
     * 提交注册
     */
    private void submit() {

        String password = mPw.getText().toString();
        presenter.login(mPhoneStr, password);

    }




    @Override
    public void showOrHideLoading(boolean show) {
        if (show) {
            mLoading.setVisibility(View.VISIBLE);
            mBtnConfirm.setVisibility(View.GONE);
        } else {
            mLoading.setVisibility(View.GONE);
            mBtnConfirm.setVisibility(View.VISIBLE);
        }

    }
    @Override
    public void showLoginSuc() {
        mLoading.setVisibility(View.GONE);
        mBtnConfirm.setVisibility(View.GONE);
        mTips.setVisibility(View.VISIBLE);
        mTips.setTextColor(getContext().getResources().getColor(R.color.color_text_normal));
        mTips.setText(getContext().getString(R.string.login_suc));
        ToastUtil.show(getContext(), getContext().getString(R.string.login_suc));
        dismiss();

    }

    @Override
    public void showServerError() {
        showOrHideLoading(false);
        mTips.setVisibility(View.VISIBLE);
        mTips.setTextColor(getContext().getResources().getColor(R.color.error_red));
        mTips.setText(getContext().getString(R.string.error_server));
    }

    @Override
    public void showPasswordError() {
        showOrHideLoading(false);
        mTips.setVisibility(View.VISIBLE);
        mTips.setTextColor(getContext().getResources().getColor(R.color.error_red));
        mTips.setText(getContext().getString(R.string.password_error));
    }

}
